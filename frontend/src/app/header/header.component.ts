import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { faCaretDown, faSignOutAlt, faUser } from '@fortawesome/free-solid-svg-icons';
import { LocalStorageService } from 'ngx-webstorage';
import { AuthService } from '../auth/shared/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  faUser = faUser;

  isLoggedIn: boolean;
  username: string;
  isDataLoaded = false;
  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
    this.authService.loggedIn.subscribe((data: boolean) => this.isLoggedIn = data);
    this.authService.username.subscribe((data: string) => this.username = data);
    this.isLoggedIn = this.authService.isLoggedIn();
    this.username = this.authService.getUserName();
    this.isDataLoaded = true;
  }

  goToUserProfile(): void {
    this.router.navigateByUrl('/user-profile/' + this.username);
  }

  logout(): void {
    this.authService.logout();
    this.isLoggedIn = false;
    this.router.navigateByUrl('').then(
      () =>  {
        window.location.reload();
      }
    );
  }

  goHome(): void {
    this.router.navigateByUrl('/').then(
      () => window.location.reload()
    );
  }

}
