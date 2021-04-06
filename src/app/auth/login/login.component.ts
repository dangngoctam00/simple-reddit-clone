import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from '../shared/auth.service';
import { LoginRequestPayload } from './login-request.payload';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;
  loginRequestPayload: LoginRequestPayload;
  registerSuccessMessage: string;
  isError: boolean;

  constructor(private authService: AuthService, private activatedRoute: ActivatedRoute,
              private toastr: ToastrService, private router: Router) { }

  ngOnInit(): void {
    this.loginForm = new FormGroup({
      username: new FormControl('', [Validators.required, Validators.minLength(3)]),
      password: new FormControl('', [Validators.required, Validators.minLength(3)])
    });
    this.loginRequestPayload = {
      username: '',
      password: ''
    };
    this.activatedRoute.queryParams.subscribe(
      params => {
        if (params.registerd != undefined && params.registerd == 'true') {
          this.toastr.success('Signup successfull');
          this.registerSuccessMessage = 'Please check your mail for activate your account!';
        }
      }
    );
    this.isError = false;
  }

  login(): void {
    this.loginRequestPayload.username = this.loginForm.get('username').value;
    this.loginRequestPayload.password = this.loginForm.get('password').value;
    if (this.loginForm.get('username').valid && this.loginForm.get('password').valid) {
      // tslint:disable-next-line: deprecation
      this.authService.login(this.loginRequestPayload).subscribe(
        result => {
            console.log('successful' + result);
            this.isError = false;
            this.router.navigateByUrl('/');
            this.toastr.success('Login successful');
        }, err => {
          console.log('error' + err);
          this.isError = true;
        }
      );
    }
  }

}
