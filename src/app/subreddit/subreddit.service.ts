import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { SubredditModel } from './subreddit-response';

@Injectable({
  providedIn: 'root'
})
export class SubredditService {

  url = 'http://localhost:8080/api/subreddit';

  constructor(private httpClient: HttpClient) {
  }

  getAllSubreddits(): Observable<Array<SubredditModel>> {
    console.log(this.url + '/all');
    return this.httpClient.get<Array<SubredditModel>>(this.url + '/all');
  }

  createSubreddit(subredditModel: SubredditModel): Observable<any> {
    console.log(this.url);
    return this.httpClient.post(this.url, subredditModel);
  }
}
