import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CommentPayload } from './comment-payload';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  url = 'http://localhost:8080/api/comments';

  constructor(private httpClient: HttpClient) { }

  createComment(commentPayload: CommentPayload): Observable<any> {
    return this.httpClient.post(this.url, commentPayload);
  }

  getCommentsForPost(postId: number): Observable<CommentPayload[]> {
    return this.httpClient.get<CommentPayload[]>(this.url + '/by-post/' + postId);
  }

  getAllCommentsByUser(name: string): Observable<CommentPayload[]> {
    return this.httpClient.get<CommentPayload[]>(this.url + '/by-user/' + name);
  }

  getCommentById(id: number): Observable<CommentPayload[]> {
    return this.httpClient.get<CommentPayload[]>(this.url + '/' + id);
  }
}
