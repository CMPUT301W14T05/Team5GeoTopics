package ca.ualberta.cs.team5geotopics;

import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CommentController {
	public final static String TOP_LEVEL = "TopLevelTest";
	private Cache mCache;
	private User myUser;
	private JestClient mClient;
	private Gson mGson;
	
	public CommentController() {
		this.mCache = Cache.getInstance();
		this.myUser = User.getInstance();
		this.mClient = GeoTopicsApplication.getInstance().getClient();
		
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Bitmap.class, new BitmapJsonConverter());
		this.mGson = builder.create();
	}

	public void newTopLevel(CommentModel newComment) {
		mCache.addToHistory(newComment);
		myUser.addToMyComments(newComment);
		
	}

	public void newReply(CommentModel newComment, CommentModel parentComment,
			Context context) {
		newComment.setmParentID(parentComment.getmParentID());
		myUser.addToMyComments(newComment);
	}

	public void updateComment(CommentModel comment, String title,
			String author, String body, Bitmap picture, Location mGeolocation) {
		comment.setmTitle(title);
		comment.setmAuthor(author);
		comment.setmBody(body);
		comment.setmPicture(picture);
		comment.setmGeolocation(mGeolocation);
	}
	
	public void pushComment(final CommentModel comment, final String type){
		Thread thread = new Thread(){
			@Override
			public void run(){
				Exception e = null;
				Index pushIndex = new Index.Builder(mGson.toJson(comment)).index(type)
						.type(comment.getmEsType()).id(comment.getmEsID()).build();
				
				try{
					mClient.execute(pushIndex);
				}
				catch (Exception e1){
					e = e1;
					e1.printStackTrace();
				}
				mClient.shutdownClient();
				
				if (e == null){
					User user = User.getInstance();
					user.updatePostCountFile();
				}
				
				
			}
		};
		thread.start();
	}
}
