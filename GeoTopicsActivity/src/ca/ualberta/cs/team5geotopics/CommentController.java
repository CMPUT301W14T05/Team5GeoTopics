package ca.ualberta.cs.team5geotopics;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;

public class CommentController {
	private Cache mCache;
	private User myUser;

	public CommentController() {
		this.mCache = Cache.getInstance();
		this.myUser = User.getInstance();
	}

	public void newTopLevel(CommentModel newComment, Context context) {
		mCache.addToHistory(newComment, context);
		myUser.addToMyComments(newComment, context);
		PutIndexService.pushComment(context, "TopLevel", newComment);
	}

	public void newReply(CommentModel newComment, CommentModel parentComment,
			Context context) {
		parentComment.addReply(newComment);
		myUser.addToMyComments(newComment, context);
	}

	public void updateComment(CommentModel comment, String title,
			String author, String body, Bitmap picture, Location mGeolocation) {
		comment.setmTitle(title);
		comment.setmAuthor(author);
		comment.setmBody(body);
		comment.setmPicture(picture);
		comment.setmGeolocation(mGeolocation);
	}
}
