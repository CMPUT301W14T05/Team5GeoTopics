package ca.ualberta.cs.team5geotopics;

import java.lang.reflect.Type;
import java.util.ArrayList;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Search;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class ProfileSearch {
	private JestClient client;
	private Gson gson;
	private JestResult lastResult;
	
	
	/**
	 * CommentSearch creates a new instance of a search given a CommentListModel.
	 * @param listModel The CommentListModel to be searched.
	 */
	public ProfileSearch(){
		this.client = GeoTopicsApplication.getInstance().getProfileClient();
		
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Bitmap.class, new BitmapJsonConverter());
		this.gson = builder.create();
		this.lastResult = new JestResult(this.gson);
	}
	
	private String getReplyFilter(String profileID){
		return "{\n" + 					
				"\"query\": {\n" + 
				"\"term\":{\n" + 
				"\"mID\" : \"" + profileID +  "\"\n"  + 
				"}\n" +
				"}\n" +
				"}";
	}
	
	/**
	 * This will pull a specific profile ID from the Internet and then push it to the activity that requested it.
	 * The method will ensure that the search returns only one unique hit else it will return nothing.
	 * @param profileID The unique profile ID we are searching for
	 * @param ProfileActivity The activity that requested it
	 */
	public void pullProfile(final String profileID, final InspectOtherProfilesActivity ProfileActivity){
		final String query = getReplyFilter(profileID);
		final User profile = null;

		Thread thread = new Thread() {
			@Override
			public void run() {
				Log.w("ProfileSearch", "Profile ID = " + profileID);
				Search search = (Search) new Search.Builder(query).addIndex(
						"profile").build();
				try{
					lastResult = client.execute(search);
					Log.w("ProfileSearch", "result json string = " + lastResult.getJsonString());
				}
				catch (Exception e){
					Log.w("ProfileSearch", e);
				}
				
				client.shutdownClient();
				
				Type elasticSearchSearchResponseType = new TypeToken<ElasticSearchSearchResponse<User>>(){}.getType();
				String lastResultJsonString = lastResult.getJsonString();

				final ElasticSearchSearchResponse<User> esResponse = gson.fromJson(lastResultJsonString, elasticSearchSearchResponseType);
				if(esResponse == null)
					Log.w("ProfileSearch", "Is null");
				// zjullion https://github.com/slmyers/PicPosterComplete/blob/master/src/ca/ualberta/cs/picposter/network/ElasticSearchOperations.java
				
				final ArrayList<User> acm = new ArrayList<User>();
				acm.addAll((ArrayList<User>)esResponse.getSources());
				Log.w("ProfileSearch", "Here");
				if(acm.size() == 1){
					Log.w("ProfileSearch", "Here2");
					Runnable updateModel = new Runnable() {
						@Override
						public void run() {
							User profile;
							profile = acm.get(0);
							Log.w("ProfileSeach", "Profile ID: " + profile.getProfileID());
							ProfileActivity.passProfile(profile);
						}
					};
					ProfileActivity.runOnUiThread(updateModel);
				}
				else{
					Log.w("ProfileSearch", Integer.toString(acm.size()));
				}
			}
		};
		thread.start();
		
	}

}
