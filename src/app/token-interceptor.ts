import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { catchError, filter, switchMap, take } from 'rxjs/operators';
import { AuthService } from './auth/shared/auth.service';
import { LoginResponsePayload } from './auth/login/login-response.payload';
@Injectable({
    providedIn: 'root'
})

export class TokenInterceptor implements HttpInterceptor {

    behaviorSubject: BehaviorSubject<any> = new BehaviorSubject<any>(null);
    isRefreshTokenInProgress = false;

    constructor(private authService: AuthService) {

    }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        // console.log(req.url);
        if (req.url.indexOf('refresh/token') !== -1 || req.url.indexOf('/home') !== -1) {
            console.log('refresh token request');
            return next.handle(req);
        }
        req = this.addToken(req);

        return next.handle(req).pipe(catchError( error => {
            if (error instanceof HttpErrorResponse && error.status === 403) {
                // console.log('refresh token');
                if (this.isRefreshTokenInProgress === true) {
                    return this.behaviorSubject.pipe(
                        filter(result => result !== null),
                        take(1),
                        switchMap(() => next.handle(this.addToken(req)))
                    );
                }
                else {
                    // console.log('refreshing');
                    this.isRefreshTokenInProgress = true;
                    this.behaviorSubject.next(null);
                    return this.authService.refreshToken().pipe(
                        switchMap((refreshTokenResponse: LoginResponsePayload) => {
                            this.isRefreshTokenInProgress = false;
                            this.behaviorSubject.next(refreshTokenResponse.jwtToken);
                            return next.handle(this.addToken(req));
                        })
                    );
                }
            }
            else {
                return throwError(error);
            }
        }));
    }

    // tslint:disable-next-line: typedef
    addToken(req: HttpRequest<any>) {
        const jwtToken = this.authService.getJwtToken();
        if (jwtToken !== null) {
            // console.log('if token exists: ' + req.url + ' ' + jwtToken);
            return req.clone({
                headers: req.headers.set('Authorization', 'Bearer ' + jwtToken)
            });
        }
        return req;
    }

}
