import { AfterViewInit, Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { from } from 'rxjs';
import { PostModel } from '../shared/post-model';
import { CommentPayload } from './comment-payload';
import { CommentService } from './comment.service';

@Component({
  selector: 'app-comment',
  templateUrl: './comment.component.html',
  styleUrls: ['./comment.component.css']
})
export class CommentComponent implements OnInit {

  @Input() post: PostModel;
  @Input() comment: CommentPayload;
  isLoaded: boolean;
  comments: CommentPayload[];
  replyForm: FormGroup;
  replyCommentPayload: CommentPayload;
  postCommentFlag: boolean;
  constructor(private commentService: CommentService, private router: Router) {
    this.isLoaded = true;
    this.replyForm = new FormGroup({
      text: new FormControl('', Validators.required),
      parentCommentId: new FormControl('', Validators.required)
    });
    this.replyCommentPayload = {
      id: null,
      text: '',
      postId: null,
      parentCommentId: null,
      duration: null,
      username: '',
      comments: null
    };
  }


  ngOnInit(): void {
    this.postCommentFlag = false;
  }

  replyComment(): void {
    this.replyCommentPayload.postId = this.post.id;
    this.replyCommentPayload.text = this.replyForm.get('text').value;
    this.replyCommentPayload.parentCommentId = this.comment.id; // need to get parent comment id
    // tslint:disable-next-line: deprecation
    this.commentService.createComment(this.replyCommentPayload).subscribe(
      data => {
        this.replyForm.get('text').setValue('');
        // tslint:disable-next-line: deprecation
        // this.commentService.getCommentsForPost(this.post.id).subscribe(
        //   result => {
        //     this.comment = result[0];
        //     this.isLoaded = true;
        //   }, error => {
        //     console.log('reply comment for posts failed');
        //   }
        // );
        this.postCommentFlag = false;
        window.location.reload();
      },
      error => {
        console.log('create reply comment failed');
      }
    );
  }

  openReplyBox(): void {
    this.postCommentFlag = true;
  }

  discardComment(): void {
    this.postCommentFlag = false;
    this.replyForm = new FormGroup({
      text: new FormControl('', Validators.required),
      parentCommentId: new FormControl('', Validators.required)
    });
  }

}
