import { Component, Input, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { PostModel } from '../../shared/post-model';
import { PostService } from '../../shared/post.service';
import { faComments } from '@fortawesome/free-solid-svg-icons';
import { Router } from '@angular/router';

@Component({
  selector: 'app-post-title',
  templateUrl: './post-overview.component.html',
  styleUrls: ['./post-overview.component.css']
})
export class PostOverviewComponent implements OnInit {


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

  ngOnInit(): void { }

  goToPost(slug: string): void {
    this.router.navigateByUrl('/view-post/' + slug);
  }

}
