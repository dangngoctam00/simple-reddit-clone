import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from '../shared/auth.service';
import { SignupRequestPayload } from './signup-request.payload';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {

  signupForm: FormGroup;
  signupRequestPayload: SignupRequestPayload;

  constructor(private authService : AuthService, private router : Router,
    private toastr: ToastrService) {
    this.signupRequestPayload = {
      username: '',
      password: '',
      email: ''
    }
  }

  ngOnInit(): void {
    this.signupForm = new FormGroup({
      username: new FormControl('', Validators.required),
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', Validators.required)
    })
  }

  signup() {
    this.signupRequestPayload.username = this.signupForm.get('username').value;
    this.signupRequestPayload.password = this.signupForm.get('password').value;
    this.signupRequestPayload.email = this.signupForm.get('email').value;
    if (this.signupForm.get('email').valid && this.signupForm.get('username').valid) {
      this.authService.signup(this.signupRequestPayload).subscribe(
        result => {
          console.log(result);
          this.router.navigate(['/login'], {queryParams: {registerd: 'true'}});
        }, () => {
          this.toastr.error('Registration failed! Please try again.');
        }
      )
    } 
  }
}
