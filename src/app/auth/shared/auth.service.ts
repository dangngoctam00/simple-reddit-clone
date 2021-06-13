import { HttpClient } from '@angular/common/http';
import { EventEmitter, Injectable, Output } from '@angular/core';
import { Router } from '@angular/router';
import { LocalStorageService } from 'ngx-webstorage';
import { Observable, throwError } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { LoginRequestPayload } from '../login/login-request.payload';
import { LoginResponsePayload } from '../login/login-response.payload';
import { SignupRequestPayload} from '../signup/signup-request.payload';
import { RefreshTokenPayload } from './refreshTokenPayload';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  // tslint:disable-next-line: align
  // tslint:disable-next-line: no-unused-expression
  @Output() loggedIn: EventEmitter<boolean> = new EventEmitter();
  // tslint:disable-next-line: no-unused-expression
  @Output() username: EventEmitter<string> = new EventEmitter();


  constructor(private httpClient: HttpClient, private localStorage: LocalStorageService,
              private router: Router) {}


  url = 'http://localhost:8080/api/auth/';
  isLoggedIn(): boolean {
    return this.localStorage.retrieve('jwtToken') != null;
  }

  signup(signupRequestPayload: SignupRequestPayload): Observable<any> {
    return this.httpClient.post(this.url + 'signup', signupRequestPayload, {responseType: 'text' as 'json'});
  }

  login(loginRequestPayload: LoginRequestPayload): Observable<boolean> {
    return this.httpClient.post<LoginResponsePayload>(this.url + 'login', loginRequestPayload)
      .pipe(map(data => {
        this.localStorage.store('jwtToken', data.jwtToken);
        this.localStorage.store('username', data.username);
        this.localStorage.store('refreshToken', data.refreshToken);
        this.localStorage.store('expiredAt', data.expiredAt);
        this.loggedIn.emit(true);
        this.username.emit(data.username);
        return true;
      }));
  }

  logout(): void {
    const refreshTokenPayload = {
      refreshToken: this.getRefreshToken(),
      username: this.getUserName()
    };
    console.log('http://localhost:8080/api/auth/logout');
    this.httpClient.post('http://localhost:8080/api/auth/logout', refreshTokenPayload,
      { responseType: 'text' })
      // tslint:disable-next-line: deprecation
      .subscribe(data => {
        console.log(data);
      }, error => {
        throwError(error);
      });
    this.localStorage.clear('jwtToken');
    this.localStorage.clear('username');
    this.localStorage.clear('refreshToken');
    this.localStorage.clear('expiredAt');
  }

  refreshToken(): Observable<any> {
    const refreshTokenPayload = {
      refreshToken: this.getRefreshToken(),
      username: this.getUserName()
    };
    console.log(this.url + '/refresh/token' + '/refresh token: ' + refreshTokenPayload.refreshToken);
    return this.httpClient.post<LoginResponsePayload>(this.url + 'refresh/token', refreshTokenPayload)
      .pipe(tap(response => {
        this.localStorage.store('jwtToken', response.jwtToken);
        this.localStorage.store('expiredAt', response.expiredAt);
        console.log('refresh token successful');
      }
      ));
  }


  getRefreshToken(): string {
    return this.localStorage.retrieve('refreshToken');
  }

  getUserName(): string {
    return this.localStorage.retrieve('username');
  }

  getExpirationTime(): string {
    return this.localStorage.retrieve('expiredAt');
  }

  getJwtToken(): string {
    return this.localStorage.retrieve('jwtToken');
  }
}
