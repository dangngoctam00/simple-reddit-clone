import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { VotePayload } from './vote-button/vote-payload';

@Injectable({
  providedIn: 'root'
})
export class VoteService {

  url = 'http://localhost:8080/api/votes'

  constructor(private httpClient: HttpClient) { }

  getUserVoted(postId: number): Observable<any> {
    return this.httpClient.get(this.url + '/current-vote/' + postId);
  }

  vote(votePayload: VotePayload): Observable<any> {
    return this.httpClient.post(this.url, votePayload);
  }
}
