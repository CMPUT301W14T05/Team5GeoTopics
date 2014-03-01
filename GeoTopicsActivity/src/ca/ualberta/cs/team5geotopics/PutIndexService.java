package ca.ualberta.cs.team5geotopics;


import io.searchbox.client.JestResult;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

public class PutIndexService extends IntentService{
	private String mResponse;
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
		
		mResponse = EsOperations.putComment(args, jsonComment);
		
		if(GeoTopicsApplication.giveFeedback()){
			final String OUTPUT_TEXT = "OUTPUT_TEXT";
			//http://www.mobiledevguide.com/2013/01/how-to-use-intentservice-in-android.html
			/*create new intent to broadcast our processed data to our activity*/
			Intent resultBroadCastIntent = new Intent();
			/*set action here*/
			resultBroadCastIntent.setAction(EsTestActivity.PutIndexReciever.ACTION_TEXT_CAPITALIZED);
			/*set intent category as default*/
			resultBroadCastIntent.addCategory(Intent.CATEGORY_DEFAULT);
	 
			/*add data to intent*/
			resultBroadCastIntent.putExtra(OUTPUT_TEXT, mResponse);
			/*send broadcast */
			sendBroadcast(resultBroadCastIntent);
		}
	
	}
	
	
}
