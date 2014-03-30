package ca.ualberta.cs.team5geotopics;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Index;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ProfilePush {
	
	public final static String TOP_LEVEL = "TopLevelTest";
	private JestClient mClient;
	private Gson mGson;
	private JestResult mResult;
	private String type = "profile";
	
	/**
	 * Constructor
	 *
	 * @param context	The context of an activity.
	 * @return      A comment controller
	 */
	public ProfilePush() {
		this.mClient = GeoTopicsApplication.getInstance().getProfileClient();
		
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Bitmap.class, new BitmapJsonConverter());
		this.mGson = builder.create();
		this.mResult = new JestResult(mGson);	
	}
	
	/**
	 * Pushes a profile to the web. If unavailable then statsh it and
	 * push when Internet comes up
	 * @param user The users profile
	 * @return The thread the push is happening on. For test purposes mostly.
	 */
	public Thread pushProfile(final User user){
		Thread thread = new Thread(){
			@Override
			public void run(){
				Exception e = null;
				Index pushIndex = new Index.Builder(mGson.toJson(user)).index(type)
						.type("profile").id(user.getProfileID()).build();
				
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
					Log.w("EsUser", mResult.getJsonString());
				}
				
				
			}
		};
		thread.start();
		return thread;
	}

}
