package ca.ualberta.cs.team5geotopics.test;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.cs.team5geotopics.BitmapJsonConverter;
import ca.ualberta.cs.team5geotopics.BrowseActivity;
import ca.ualberta.cs.team5geotopics.Cache;
import ca.ualberta.cs.team5geotopics.CommentModel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CacheTests extends
		ActivityInstrumentationTestCase2<BrowseActivity> {

	public CacheTests() {
		super(BrowseActivity.class);
	}

	public void testFileIO(){
		Cache cache = Cache.getInstance();
		
		Bitmap pic = Bitmap.createBitmap(10,10 ,Bitmap.Config.ARGB_8888);

		ArrayList<CommentModel> acm = new ArrayList<CommentModel>();
 		acm.add(new CommentModel("1.2", "1.111", "Body", "Author", null, "Title"));
 		acm.add(new CommentModel("2", "2", "Body2", "Author2", pic, "Title2")); //make a list of 2 comments
 		
 		assertTrue("Comment Body is correct", acm.get(0).getmBody().equals("Body"));
 		assertTrue("Comment Author is correct", acm.get(0).getmAuthor().equals("Author"));
 		assertTrue("Comment latitude is correct", acm.get(0).getGeoLocation().getLatitude() == 1.2);
 		assertTrue("Comment Longitude is correct", acm.get(0).getGeoLocation().getLongitude() == 1.111);
 		
 		assertTrue("Comment Title is correct", acm.get(1).getmTitle().equals("Title2"));
 		assertTrue("Comment Picture is correct", acm.get(1).getPicture().equals(pic));
 		
 		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Bitmap.class, new BitmapJsonConverter());
		Gson gson = builder.create();
		
		String jsonString = gson.toJson(acm);
		
	}
}
