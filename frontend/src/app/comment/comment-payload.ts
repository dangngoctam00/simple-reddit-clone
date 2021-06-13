export class CommentPayload {
  id: number;
  text: string;
  postId: number;
  username: string;
  duration: string;
  parentCommentId: number;
  comments: CommentPayload[];
}
