package ca.ualberta.cs.team5geotopics;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

public class PutIndexService extends IntentService{
	private IndexResponse mResponse;
	public PutIndexService() {
		super("PutIndexService");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle bundle = intent.getExtras();
		//id, type, and index arguments needed for client.prepareIndex
		String[] args = bundle.getStringArray("args");
		String jsonComment = bundle.getString("json");
		
		Client client = GeoTopicsApplication.getClient();
		//for clarity
		String index = args[0];
		String type = args[1];
		String id = args[2];
		mResponse = client.prepareIndex(index, type, id)
					.setSource(jsonComment)
					.execute()
					.actionGet();
		
		if(GeoTopicsApplication.giveFeedback()){
			//http://www.mobiledevguide.com/2013/01/how-to-use-intentservice-in-android.html
			/*create new intent to broadcast our processed data to our activity*/
			Intent resultBroadCastIntent = new Intent();
			/*set action here*/
			resultBroadCastIntent.setAction(TextCapitalizeResultReceiver.ACTION_TEXT_CAPITALIZED);
			/*set intent category as default*/
			resultBroadCastIntent.addCategory(Intent.CATEGORY_DEFAULT);
	 
			/*add data to intent*/
			resultBroadCastIntent.putExtra(OUTPUT_TEXT, data);
			/*send broadcast */
			sendBroadcast(resultBroadCastIntent);
		}
	
	}
	
	
}
