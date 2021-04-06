import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { SubredditModel } from '../subreddit-response';
import { SubredditService } from '../subreddit.service';

@Component({
  selector: 'app-create-subreddit',
  templateUrl: './create-subreddit.component.html',
  styleUrls: ['./create-subreddit.component.css']
})
export class CreateSubredditComponent implements OnInit {

  createSubredditForm: FormGroup;
  subredditModel: SubredditModel;

  constructor(private router: Router, private subredditService: SubredditService) {
    this.createSubredditForm = new FormGroup(
      {
        title: new FormControl('', Validators.required),
        description: new FormControl('', Validators.required)
      }
    );
    this.subredditModel = {
      name: '',
      description: ''
    }
   }

  ngOnInit(): void {
  }

  discard(): void {
    this.router.navigateByUrl('/');
  }

  createSubreddit(): void {
    this.subredditModel.name = this.createSubredditForm.get('title').value;
    this.subredditModel.description = this.createSubredditForm.get('description').value;
    // tslint:disable-next-line: deprecation
    this.subredditService.createSubreddit(this.subredditModel).subscribe(data => {
      this.router.navigateByUrl('/list-subreddits');
    }, error => {
      console.log('Error occurred');
    });
  }

}
