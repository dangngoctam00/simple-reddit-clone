import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { throwError } from 'rxjs';
import { CommentPayload } from 'src/app/comment/comment-payload';
import { CommentService } from 'src/app/comment/comment.service';
import { PostModel } from 'src/app/shared/post-model';
import { PostService } from 'src/app/shared/post.service';

@Component({
  selector: 'app-view-post',
  templateUrl: './view-post.component.html',
  styleUrls: ['./view-post.component.css']
})
export class ViewPostComponent implements OnInit {

  postId: number;
  post: PostModel;
  commentForm: FormGroup;
  commentPayload: CommentPayload;
  comments: CommentPayload[];

  constructor(private postService: PostService, private activatedRoute: ActivatedRoute,
              private commentService: CommentService) {
    this.postId = this.activatedRoute.snapshot.params.id;
    this.commentForm = new FormGroup({
      text: new FormControl('', Validators.required)
    });
    this.commentPayload = {
      id: null,
      text: '',
      postId: this.postId,
      parentCommentId: null,
      duration: null,
      username: '',
      comments: null
    };
    this.getPostById();
    this.getCommentsForPost();
  }

  ngOnInit(): void {


  }

  private getPostById(): void {
    console.log('get post');
    // tslint:disable-next-line: deprecation
    this.postService.getPostById(this.postId).subscribe(data => {
      console.log(data);
      this.post = data;
    }, error => {
      console.log('get post failed');
    });
  }

  private getCommentsForPost(): void {
    // tslint:disable-next-line: deprecation
    this.commentService.getCommentsForPost(this.postId).subscribe(
      data => {
        this.comments = data;
      }, error => {
        console.log('get comments for posts failed');
      }
    );
  }

  postComment(): void {
    this.commentPayload.postId = this.postId;
    this.commentPayload.text = this.commentForm.get('text').value;
    this.commentPayload.parentCommentId = null;
    // tslint:disable-next-line: deprecation
    this.commentService.createComment(this.commentPayload).subscribe(
      data => {
        this.commentForm.get('text').setValue('');
        this.getCommentsForPost();
      },
      error => {
        console.log('create post failed');
      }
    );
  }
}
