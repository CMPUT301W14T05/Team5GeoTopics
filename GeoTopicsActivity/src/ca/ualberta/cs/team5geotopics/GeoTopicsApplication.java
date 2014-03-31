package ca.ualberta.cs.team5geotopics;

import io.searchbox.client.JestClient;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;

//https://github.com/abramhindle/FillerCreepForAndroid/blob/master/src/es/softwareprocess/fillercreep/FillerCreepApplication.java
//josh said not to extend Application

/**
 * This is a collection of singletons.
 */
public class GeoTopicsApplication {
	transient private static final String SEARCHLY_CLUSTER = "http://site:d87a47445dc808449dd78637d9031609@bombur-us-east-1.searchly.com";
	transient private static final String SEARCHLY_PROFILES = "http://site:5bf382d96e42cc0b437f4a412b299975@dwalin-us-east-1.searchly.com";

	@SuppressWarnings("unused")
	private static final String CMPUT301_CLUSTER = "http://cmput301.softwareprocess.es:8080/testing/";

	private static GeoTopicsApplication myself;
	private Context context;

	private GeoTopicsApplication() {
	}

	/**
	 * Lazy implementation of a singleton that initialises when first requested.
	 * 
	 * @return myself A single instance of GeoTopicsApplication.
	 */
	public static GeoTopicsApplication getInstance() {
		if (myself == null) {
			myself = new GeoTopicsApplication();
		}
		return myself;
	}

	/**
	 * Gets the jest client for the profiles storage using the application.
	 * 
	 * @return this.mClient The client(user) of the application.
	 */
	public JestClient getClient() {
		JestClient client;
		DroidClientConfig clientConfig = new DroidClientConfig.Builder(
				SEARCHLY_CLUSTER).build();

		JestClientFactory jestClientFactory = new JestClientFactory();
		jestClientFactory.setDroidClientConfig(clientConfig);
		client = jestClientFactory.getObject();
		return client;
	}

	/**
	 * Gets the jest client using the application.
	 * 
	 * @return this.mClient The client(user) of the application.
	 */
	public JestClient getProfileClient() {
		JestClient client;
		DroidClientConfig clientConfig = new DroidClientConfig.Builder(
				SEARCHLY_PROFILES).build();

		JestClientFactory jestClientFactory = new JestClientFactory();
		jestClientFactory.setDroidClientConfig(clientConfig);
		client = jestClientFactory.getObject();
		return client;
	}

	/**
	 * Stores an application context in the application singleton. This is
	 * useful for save/load code that need a context to find the files to
	 * read/write too but do not need a specific context.
	 * 
	 * @param context
	 */
	public void setContext(Context context) {
		this.context = context;
	}

	/**
	 * Returns the context of the application. This is useful for save/load code
	 * that need a context to find the files to read/write too but do not need a
	 * specific context.
	 * 
	 * @return this.context The context of the application.
	 */
	public Context getContext() {
		return this.context;
	}
	
	/**
	 * Returns status about the network availablilty.
	 * 
	 * @author Alexandre Jasmin Link:
	 *         http://stackoverflow.com/questions/4238921/
	 *         android-detect-whether-there-is-an-internet-connection-available
	 * @param context
	 *            An application context.
	 * @return True is the network is available, false if not.
	 */
	public boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) this.context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return (activeNetworkInfo != null && activeNetworkInfo.isConnected());
	}


}