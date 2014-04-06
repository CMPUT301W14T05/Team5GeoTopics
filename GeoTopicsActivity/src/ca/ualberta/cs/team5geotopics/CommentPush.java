package ca.ualberta.cs.team5geotopics;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Index;
import android.graphics.Bitmap;
import android.util.Log;

public class CommentPush {
	
	public final static String TOP_LEVEL = "TopLevelTest";
	private JestClient mClient;
	private Gson mGson;
	private JestResult mResult;
	
	/**
	 * Constructor
	 *
	 * @param context	The context of an activity.
	 * @return      A comment controller
	 */
	public CommentPush() {
		this.mClient = GeoTopicsApplication.getInstance().getClient();
		
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Bitmap.class, new BitmapJsonConverter());
		this.mGson = builder.create();
		this.mResult = new JestResult(mGson);	
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
					Log.w("Connectivity", "Error with the comment push");
					Log.w("Connectivity", e1);
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
		return thread;
	}

	public JestResult returnResult(){
		return this.mResult;
	}
}
