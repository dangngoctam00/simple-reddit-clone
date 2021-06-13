export class LoginResponsePayload {
    jwtToken: string;
    username: string;
    refreshToken: string;
    expiredAt: Date;
}
