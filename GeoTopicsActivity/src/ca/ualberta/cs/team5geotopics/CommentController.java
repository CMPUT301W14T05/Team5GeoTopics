package ca.ualberta.cs.team5geotopics;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * The CommentController class is what helps distinguish between a new reply or a new top level
 * comment. As well as it updates the data to and from the comment as it is modified and pushes
 * it back to the server.
 */

public class CommentController {
	
	private User myUser;
	
	private Gson mGson;
	
	private CommentManager manager;
	
	/**
	 * Constructor
	 *
	 * @param context	The context of an activity.
	 * @return      A comment controller
	 */
	public CommentController(Context context) {
		this.myUser = User.getInstance();
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Bitmap.class, new BitmapJsonConverter());
		this.mGson = builder.create();
		manager = CommentManager.getInstance();
		
	}

	
	
	/**
	 * Creates a new top level comment. The comment is pushed to the web
	 * and added to the local my comments list.
	 *
	 * @param  newComment	The new top level comment.
	 */
	public void newTopLevel(CommentModel newComment) {
		manager.newTopLevel(newComment);
	}

	/**
	 * Creates a new top level comment. The comment is pushed to the web
	 * and added to the local my comments list.
	 *
	 * @param  newComment  an absolute URL giving the base location of the image
	 * @param	context	An activity context
	 */
	public void newReply(CommentModel newComment, Context context) {
		manager.newReply(newComment, context, myUser);
	}

	/**
	 * Replaces the contents of a comment with new ones and pushes the new comment
	 * online.
	 *
	 * @param  comment The comment with its old attributes.
	 * @param	title	New title
	 * @param	author	New author name
	 * @param	body	New body
	 * @param	picture	New picture
	 * @param	mGeolocation	New comment location
	 */
	public void updateComment(CommentModel comment, String title,
			String author, String body, Bitmap picture, Location mGeolocation) {
		comment.setmTitle(title);
		comment.setmAuthor(author);
		comment.setmBody(body);
		comment.setmPicture(picture);
		comment.setmGeolocation(mGeolocation);
		manager.updateComment(comment);
	}
}
