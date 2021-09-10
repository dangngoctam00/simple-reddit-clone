import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/auth/shared/auth.service';
import { PostModel } from '../post-model';
import { PostService } from '../post.service';
import { VoteService } from '../vote.service';
import { VotePayload } from './vote-payload';

@Component({
  selector: 'app-vote-button',
  templateUrl: './vote-button.component.html',
  styleUrls: ['./vote-button.component.css']
})
export class VoteButtonComponent implements OnInit {

  @Input() post: PostModel;

  voteStatus: number;
  upVoteColor: string;
  downVoteColor: string;
  teal = 'blue';
  red = 'black';
  votePayload: VotePayload;
  constructor(private voteService: VoteService, private postService: PostService,
              private router: Router, private authService: AuthService) {
    this.upVoteColor = '';
    this.downVoteColor = '';
  }

  ngOnInit(): void {
    this.getCurrentVote();
  }

  changeUpVoteColor(): void {
    if (this.upVoteColor === '') {
      this.upVoteColor = 'blue';
    }
    else {
      this.upVoteColor = '';
    }
  }

  changedownVoteColor(): void {
    if (this.downVoteColor === '') {
      this.downVoteColor = 'red';
    }
    else {
      this.downVoteColor = '';
    }
  }

  upvotePost(): void {
    if (!this.authService.isLoggedIn()) {
      this.router.navigateByUrl('/login');
    }
    this.votePayload = {
      voteType: 'UPVOTE',
      postId: this.post.id
    };
    this.changeUpVoteColor();
    if (this.downVoteColor !== '') {
      this.changedownVoteColor();
    }
    // tslint:disable-next-line: deprecation
    this.voteService.vote(this.votePayload).subscribe(data => {
        this.getPostDetails();
      },
      err => console.log('upvote failed'));
  }
  downVotePost(): void {
    if (!this.authService.isLoggedIn()) {
      this.router.navigateByUrl('/login');
    }
    this.votePayload = {
      voteType: 'DOWNVOTE',
      postId: this.post.id
    };
    this.changedownVoteColor();
    if (this.upVoteColor !== '') {
      this.changeUpVoteColor();
    }
    // tslint:disable-next-line: deprecation
    this.voteService.vote(this.votePayload).subscribe(data => {
      this.getPostDetails();
    },
    err => console.log('downVote failed'));
  }

  getCurrentVote(): void {
    this.voteService.getUserVoted(this.post.id)
      // tslint:disable-next-line: deprecation
      .subscribe(data => {
        if (data === 1) {
          this.upVoteColor = 'blue';
          this.downVoteColor = '';
        }
        else if (data === 0) {
          this.upVoteColor = '';
          this.downVoteColor = '';
        }
        else {
          this.upVoteColor = '';
          this.downVoteColor = 'red';
        }
      },
        err => console.log('get current vote error'));
  }

  getPostDetails(): void {
    console.log('abc');
    // tslint:disable-next-line: deprecation
    this.postService.getPostBySlug(this.post.slug).subscribe(data => this.post = data, err => console.log('get post failed'));
  }

}
