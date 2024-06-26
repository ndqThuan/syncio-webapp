import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CommentService } from 'src/app/core/services/comment.service';
import { LikeService } from 'src/app/core/services/like.service';
import { TokenService } from 'src/app/core/services/token.service';
import { UserService } from 'src/app/core/services/user.service';
import { UserResponse } from 'src/app/features/authentication/login/user.response';

@Component({
  selector: 'app-like',
  templateUrl: './like.component.html',
  styleUrls: ['./like.component.scss'],
})
export class LikeComponent {
  @Input() postId!: string;
  @Output() visibleChange = new EventEmitter<boolean>();
  @Input() isOnPostDetails: boolean = false;
  likeCount: number = 0;
  commentCount: number = 0;
  userId: number = 0;
  isLiked: boolean = false;
  userResponse?: UserResponse | null =
    this.userService.getUserResponseFromLocalStorage();

  constructor(
    private likeService: LikeService,
    private commentService: CommentService,
    private userService: UserService
  ) {}

  ngOnInit() {
    this.countLikes();
    this.countComments();
  }

  likePost() {
    this.likeService.toggleLikes(this.postId, this.userResponse?.id).subscribe({
      next: () => {
        this.isLiked = !this.isLiked;
        this.countLikes();
      },
      error: (error: any) => {},
    });
  }

  openDialog() {
    this.visibleChange.emit(true);
  }

  countLikes() {
    this.likeService.countLikes(this.postId).subscribe({
      next: (count) => {
        this.likeCount = count;

        if (
          this.userResponse &&
          (this.userResponse.id !== null || this.userResponse.id !== undefined)
        ) {
          this.likeService.hasLiked(this.postId).subscribe({
            next: (liked: boolean) => {
              this.isLiked = liked;
            },
            error: (error: any) => {
              console.error('Error checking if post is liked:', error);
            },
          });
        }
      },
      error: (error) => {
        console.error('Error checking if post is liked:', error);
      },
    });
  }

  countComments() {
    this.commentService.countComments(this.postId).subscribe({
      next: (count) => {
        this.commentCount = count;
      },
      error: (error) => {},
    });
  }
}
