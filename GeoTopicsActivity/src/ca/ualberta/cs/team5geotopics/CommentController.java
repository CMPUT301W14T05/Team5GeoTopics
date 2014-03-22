package ca.ualberta.cs.team5geotopics;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Index;
import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * The CommentController class is what helps distinguish between a new reply or a new top level
 * comment. As well as it updates the data to and from the comment as it is modified and pushes
 * it back to the server.
 */

public class CommentController {
	public final static String TOP_LEVEL = "TopLevelTest";
	private Cache mCache;
	private User myUser;
	private JestClient mClient;
	private Gson mGson;
	private JestResult mResult;
	
	/**
	 * Constructor
	 *
	 * @param context	The context of an activity.
	 * @return      A comment controller
	 */
	public CommentController(Context context) {
		this.mCache = Cache.getInstance();
		GeoTopicsApplication.getInstance().setContext(context);
		this.myUser = User.getInstance();
		this.mClient = GeoTopicsApplication.getInstance().getClient();
		
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Bitmap.class, new BitmapJsonConverter());
		this.mGson = builder.create();
		this.mResult = new JestResult(mGson);
		
	}

	/**
	 * Returns a Jest result
	 *
	 * @return      The Jest result.
	 */
	public JestResult returnResult(){
		return mResult;
	}
	
	/**
	 * Creates a new top level comment. The comment is pushed to the web
	 * and added to the local my comments list.
	 *
	 * @param  newComment	The new top level comment.
	 */
	public void newTopLevel(CommentModel newComment) {
		myUser.addToMyComments(newComment);
		pushComment(newComment, "TopLevel");
	}

	/**
	 * Creates a new top level comment. The comment is pushed to the web
	 * and added to the local my comments list.
	 *
	 * @param  newComment  an absolute URL giving the base location of the image
	 * @param	context	An activity context
	 */
	public void newReply(CommentModel newComment, Context context) {
		myUser.addToMyComments(newComment);
		pushComment(newComment, "ReplyLevel");
		Log.w("CommentController", "id: " + newComment.getmEsID() +"\n" 
				+ "type: " + newComment.getmEsType());
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
		if(comment.isTopLevel()) {
			pushComment(comment, "TopLevel");
		}else{
			pushComment(comment, "ReplyLevel");
		}
		myUser.updateMyComment(comment);
	}
	
	/**
	 * Pushes a comment to the web. If the web is unavailable will 
	 * stash the comment and push it when Internet is available.
	 *
	 * @param  commentModel  The comment we are pushing
	 * @param	type	The type of comment we are pushing. Valid types are 
	 * "TopLevel" and "ReplyLevel".
	 * @return      The thread the push is running on.
	 */
	public Thread pushComment(final CommentModel comment, final String type){
		Thread thread = new Thread(){
			@Override
			public void run(){
				Exception e = null;
				Index pushIndex = new Index.Builder(mGson.toJson(comment)).index(type)
						.type(comment.getmEsType()).id(comment.getmEsID()).build();
				
				try{
					mResult = mClient.execute(pushIndex);
				}
				catch (Exception e1){
					e = e1;
					e1.printStackTrace();
				}
				mClient.shutdownClient();
				
				if (e == null){
					User user = User.getInstance();
					user.updatePostCountFile();
					Log.w("CommentController", mResult.getJsonString());
				}
				
				
			}
		};
		thread.start();
		return thread;
	}
}
