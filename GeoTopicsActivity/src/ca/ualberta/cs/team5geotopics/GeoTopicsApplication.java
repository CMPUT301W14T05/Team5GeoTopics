package ca.ualberta.cs.team5geotopics;


import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;

import io.searchbox.client.JestClient;
import android.app.Application;

//https://github.com/abramhindle/FillerCreepForAndroid/blob/master/src/es/softwareprocess/fillercreep/FillerCreepApplication.java
//josh said not to extend Application

/*
 * this is a collection of singletons
 */
public class GeoTopicsApplication extends Application{
	// with this true then the a list of test comments is loaded
	// into the QueueController.mTopLevel.mIn list.
	// that is we can test to see if the comments pushed into this
	// list are loaded onto the ListView for the BrowseTopLevelView
	transient private static final boolean BROWSE_TOP_LEVEL_TEST = true; 
	transient private static final boolean GIVE_FEEDBACK = true;
	
	
	transient private static final String SEARCHLY_CLUSTER = 
			"http://site:d87a47445dc808449dd78637d9031609@bombur-us-east-1.searchly.com";
	
	@SuppressWarnings("unused")
	transient private static final String CMPUT301_CLUSTER = 
			"http://cmput301.softwareprocess.es:8080/testing/";
	
	static boolean BrowseTopLevelNoInternetTest(){
		return BROWSE_TOP_LEVEL_TEST;
	}
	
	static boolean giveFeedback(){
		return GIVE_FEEDBACK;
	}
	
	transient private static JestClient mClient = null;
	static JestClient getClient(){
		if (mClient == null) {
			DroidClientConfig clientConfig = new DroidClientConfig.Builder(
					SEARCHLY_CLUSTER).build();

			JestClientFactory jestClientFactory = new JestClientFactory();
			jestClientFactory.setDroidClientConfig(clientConfig);
			mClient = jestClientFactory.getObject();
        }
        return mClient;
	}
	
	transient private static Cache mCache = null;
	static Cache getCache(){
		if (mCache == null) {
			mCache = new Cache();
        }
        return mCache;
	}		
}
