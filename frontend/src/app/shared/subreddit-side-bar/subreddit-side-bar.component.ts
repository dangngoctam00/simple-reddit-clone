import { Component, OnInit } from '@angular/core';
import { SubredditModel } from 'src/app/subreddit/subreddit-response';
import { SubredditService } from 'src/app/subreddit/subreddit.service';

@Component({
  selector: 'app-subreddit-side-bar',
  templateUrl: './subreddit-side-bar.component.html',
  styleUrls: ['./subreddit-side-bar.component.css']
})
export class SubredditSideBarComponent implements OnInit {

  subreddits$: Array<SubredditModel> = [];
  displayAll: boolean;

  constructor(private subredditService: SubredditService) {
    // tslint:disable-next-line: deprecation
    subredditService.getAllSubreddits().subscribe(
      subreddits => {
        if (subreddits.length >= 4) {
          this.subreddits$ = subreddits.splice(0, 3);
          this.displayAll = true;
        } else {
          this.subreddits$ = subreddits;
          this.displayAll = false;
        }
      },
      error => console.log('get all subreddits failed')
    );
  }

  ngOnInit(): void {
  }

}
