package ca.ualberta.cs.team5geotopics;


import io.searchbox.client.JestClient;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;

//https://github.com/abramhindle/FillerCreepForAndroid/blob/master/src/es/softwareprocess/fillercreep/FillerCreepApplication.java
//josh said not to extend Application

/*
 * this is a collection of singletons
 */
public class GeoTopicsApplication{
	transient private static final String SEARCHLY_CLUSTER = 
			"http://site:d87a47445dc808449dd78637d9031609@bombur-us-east-1.searchly.com";

	@SuppressWarnings("unused")
	private static final String CMPUT301_CLUSTER = 
			"http://cmput301.softwareprocess.es:8080/testing/";

	private static CommentModel currentlyViewingComment;
	private static GeoTopicsApplication myself =  null;
	private static JestClient mClient = null;

	public GeoTopicsApplication(){
		if(myself == null){
			myself = this;
			if (mClient == null) {
				DroidClientConfig clientConfig = new DroidClientConfig.Builder(
				SEARCHLY_CLUSTER).build();

				JestClientFactory jestClientFactory = new JestClientFactory();
				jestClientFactory.setDroidClientConfig(clientConfig);
				mClient = jestClientFactory.getObject();
			}
		}
	}
	public JestClient getClient(){
		return mClient;
	}

	//This allows us to pass the comment around between activities without putExtra
	//Might need to implement this as a push and pop stack if activities are having
	//Issues with it.
	public CommentModel getCurrentViewingComment() {
		return currentlyViewingComment;
	}
	//Sets the current Viewing Comment
	public void setCurrentViewingComment(CommentModel comment) {
		GeoTopicsApplication.currentlyViewingComment = comment;
	}


}