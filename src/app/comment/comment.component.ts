import { Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
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
  comments: CommentPayload[];
  replyForm: FormGroup;
  replyCommentPayload: CommentPayload;
  constructor(private commentService: CommentService) {
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
        this.commentService.getCommentsForPost(this.post.id).subscribe(
          result => {
            this.comment = result[0];
          }, error => {
            console.log('get comments for posts failed');
          }
        );
      },
      error => {
        console.log('create reply comment failed');
      }
    );
  }

}
