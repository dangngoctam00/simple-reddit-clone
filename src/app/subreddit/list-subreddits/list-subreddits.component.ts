import { Component, OnInit } from '@angular/core';
import { SubredditModel } from '../subreddit-response';
import { SubredditService } from '../subreddit.service';

@Component({
  selector: 'app-list-subreddits',
  templateUrl: './list-subreddits.component.html',
  styleUrls: ['./list-subreddits.component.css']
})
export class ListSubredditsComponent implements OnInit {

  subreddits: Array<SubredditModel> = [];

  constructor(private subredditService: SubredditService) { }

  ngOnInit(): void {
    // tslint:disable-next-line: deprecation
    this.subredditService.getAllSubreddits().subscribe(
      data => {
        this.subreddits = data;
      }, error => {
        console.log('get subreddit error');
      }
    );
  }

}
