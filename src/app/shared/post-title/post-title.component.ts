import { Component, Input, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { PostModel } from '../post-model';
import { PostService } from '../post.service';
import { faComments } from '@fortawesome/free-solid-svg-icons';
import { Router } from '@angular/router';

@Component({
  selector: 'app-post-title',
  templateUrl: './post-title.component.html',
  styleUrls: ['./post-title.component.css']
})
export class PostTitleComponent implements OnInit {


  faComments = faComments;

  @Input()
  posts: PostModel[];

  constructor(private postService: PostService, private router: Router) {
    // tslint:disable-next-line: deprecation
    this.postService.getAllPosts().subscribe(
      posts => {
        this.posts = posts;
      }
    );
  }

  ngOnInit(): void {
  }

  goToPost(id: number): void {
    this.router.navigateByUrl('/view-post/' + id);
  }

}
