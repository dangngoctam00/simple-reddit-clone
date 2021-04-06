import { Component, Input, OnInit } from '@angular/core';
import { faArrowDown, faArrowUp } from '@fortawesome/free-solid-svg-icons';
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
  faArrowUp = faArrowUp;
  faArrowDown = faArrowDown;
  upvoteColor: string;
  downvoteColor: string;
  teal = 'blue';
  red = 'black';
  votePayload: VotePayload;
  constructor(private voteService: VoteService, private postService: PostService) {
    this.upvoteColor = '';
    this.downvoteColor = '';
  }

  ngOnInit(): void {
    this.getCurrentVote();
  }

  changeUpVoteColor(): void {
    if (this.upvoteColor === '') {
      this.upvoteColor = 'blue';
    }
    else {
      this.upvoteColor = '';
    }
  }

  changeDownVoteColor(): void {
    if (this.downvoteColor === '') {
      this.downvoteColor = 'red';
    }
    else {
      this.downvoteColor = '';
    }
  }

  upvotePost(): void {
    this.votePayload = {
      voteType: 'UPVOTE',
      postId: this.post.id
    };
    this.changeUpVoteColor();
    if (this.downvoteColor !== '') {
      this.changeDownVoteColor();
    }
    // tslint:disable-next-line: deprecation
    this.voteService.vote(this.votePayload).subscribe(data => {
        this.getPostDetails();
      },
      err => console.log('upvote failed'));
  }
  downvotePost(): void {
    this.votePayload = {
      voteType: 'DOWNVOTE',
      postId: this.post.id
    };
    this.changeDownVoteColor();
    if (this.upvoteColor !== '') {
      this.changeUpVoteColor();
    }
    // tslint:disable-next-line: deprecation
    this.voteService.vote(this.votePayload).subscribe(data => {
      this.getPostDetails();
    },
    err => console.log('downvote failed'));
  }

  getCurrentVote(): void {
    this.voteService.getUserVoted(this.post.id)
      // tslint:disable-next-line: deprecation
      .subscribe(data => {
        if (data === 1) {
          this.upvoteColor = 'blue';
          this.downvoteColor = '';
        }
        else if (data === 0) {
          this.upvoteColor = '';
          this.downvoteColor = '';
        }
        else {
          this.upvoteColor = '';
          this.downvoteColor = 'red';
        }
      },
        err => console.log('get current vote error'));
  }

  getPostDetails(): void {
    // tslint:disable-next-line: deprecation
    this.postService.getPostById(this.post.id).subscribe(data => this.post = data, err => console.log('get post failed'));
  }

}
