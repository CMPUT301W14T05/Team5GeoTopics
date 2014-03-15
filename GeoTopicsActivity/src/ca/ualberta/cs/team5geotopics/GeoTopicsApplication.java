package ca.ualberta.cs.team5geotopics;

import io.searchbox.client.JestClient;
import android.content.Context;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;

//https://github.com/abramhindle/FillerCreepForAndroid/blob/master/src/es/softwareprocess/fillercreep/FillerCreepApplication.java
//josh said not to extend Application

/**
 * This is a collection of singletons.
 */
public class GeoTopicsApplication {
	transient private static final String SEARCHLY_CLUSTER = "http://site:d87a47445dc808449dd78637d9031609@bombur-us-east-1.searchly.com";

	@SuppressWarnings("unused")
	private static final String CMPUT301_CLUSTER = "http://cmput301.softwareprocess.es:8080/testing/";

	private CommentModel currentlyViewingComment = null;
	private static GeoTopicsApplication myself;
	private JestClient mClient = null;
	private String mID;
	private Context context;
	
	private GeoTopicsApplication() {
	}

	/**
	 * Gets a new instance of the application.
	 * @return myself A new instance of GeoTopicsApplication()
	 */
	public static GeoTopicsApplication getInstance() {
		if(myself == null){
			myself = new GeoTopicsApplication();
		}
		return myself;
	}

	/**
	 * Gets the client using the applciation.
	 * @return this.mClient The client(user) of the application.
	 */
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
	
	public void setContext(Context context) {
		this.context = context;
	}
	
	/**
	 * Returns the context of the application.
	 * @return this.context The context of the application.
	 */
	public Context getContext() {
		return this.context;
	}

}