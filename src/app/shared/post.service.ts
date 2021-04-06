import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CommentPayload } from '../comment/comment-payload';
import { CommentService } from '../comment/comment.service';
import { CreatePostPayload } from '../post/create-post/create-post.payload';
import { PostModel } from './post-model';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  url = 'http://localhost:8080/api/posts';

  constructor(private httpClient: HttpClient, private commentService: CommentService) { }

  getAllPosts(): Observable<Array<PostModel>> {
    console.log(this.url);
    return this.httpClient.get<Array<PostModel>>(this.url);
  }

  createPost(postPayload: CreatePostPayload): Observable<any> {
    console.log(this.url);
    return this.httpClient.post(this.url, postPayload, {responseType: 'text'});
  }

  getPostById(id: number): Observable<PostModel> {
    console.log(this.url);
    return this.httpClient.get<PostModel>(this.url + '/' + id);
  }

  getAllPostsByUser(name: string): Observable<PostModel[]> {
    console.log(this.url + '/buy-user');
    return this.httpClient.get<PostModel[]>(this.url + '/by-user/' + name);
  }


}
