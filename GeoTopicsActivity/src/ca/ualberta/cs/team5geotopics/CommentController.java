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

	public JestResult returnResult(){
		return mResult;
	}
	public void newTopLevel(CommentModel newComment) {
		myUser.addToMyComments(newComment);
		pushComment(newComment, "TopLevel");
	}

	public void newReply(CommentModel newComment, Context context) {
		myUser.addToMyComments(newComment);
		pushComment(newComment, "ReplyLevel");
		Log.w("CommentController", "id: " + newComment.getmEsID() +"\n" 
				+ "type: " + newComment.getmEsType());
	}

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
