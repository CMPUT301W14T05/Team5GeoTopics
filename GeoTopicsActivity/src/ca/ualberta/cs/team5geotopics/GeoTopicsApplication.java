package ca.ualberta.cs.team5geotopics;

import io.searchbox.client.JestClient;
import android.content.Context;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;



//https://github.com/abramhindle/FillerCreepForAndroid/blob/master/src/es/softwareprocess/fillercreep/FillerCreepApplication.java
//josh said not to extend Application

/*
 * this is a collection of singletons
 */
public class GeoTopicsApplication {
	transient private static final String SEARCHLY_CLUSTER = "http://site:d87a47445dc808449dd78637d9031609@bombur-us-east-1.searchly.com";

	@SuppressWarnings("unused")
	private static final String CMPUT301_CLUSTER = "http://cmput301.softwareprocess.es:8080/testing/";

	private CommentModel currentlyViewingComment = null;
	private static GeoTopicsApplication myself = new GeoTopicsApplication();
	private JestClient mClient = null;
	private String mID;
	private Context context;
	
	private GeoTopicsApplication() {
	}

	public static GeoTopicsApplication getInstance() {
		return myself;
	}

	public JestClient getClient() {
		if (mClient == null) {
			DroidClientConfig clientConfig = new DroidClientConfig.Builder(
					SEARCHLY_CLUSTER).build();

			JestClientFactory jestClientFactory = new JestClientFactory();
			jestClientFactory.setDroidClientConfig(clientConfig);
			this.mClient = jestClientFactory.getObject();
		}
		return this.mClient;
	}

	// This allows us to pass the comment around between activities without
	// putExtra
	// Might need to implement this as a push and pop stack if activities are
	// having
	// Issues with it.
	public CommentModel getCurrentViewingComment() {
		return this.currentlyViewingComment;
	}

	// Sets the current Viewing Comment
	public void setCurrentViewingComment(CommentModel comment) {
		this.currentlyViewingComment = comment;
	}
	
	public void setContext(Context context) {
		this.context = context;
	}
	
	public Context getContext() {
		return this.context;
	}

}