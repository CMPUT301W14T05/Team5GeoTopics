package ca.ualberta.cs.team5geotopics.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import ca.ualberta.cs.team5geotopics.BitmapJsonConverter;
import ca.ualberta.cs.team5geotopics.BrowseActivity;
import ca.ualberta.cs.team5geotopics.Cache;
import ca.ualberta.cs.team5geotopics.CommentModel;
import ca.ualberta.cs.team5geotopics.TopLevelActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class CacheTests extends
		ActivityInstrumentationTestCase2<TopLevelActivity> {
	
	BrowseActivity activity;

	public CacheTests() {
		super(TopLevelActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		activity = getActivity();
	}

	public void testFileIO(){
		//this has the "hard to test" smell
		Cache cache = Cache.getInstance();
		String filename = "testFile";
		
		Bitmap pic = Bitmap.createBitmap(10,10 ,Bitmap.Config.ARGB_8888);

		ArrayList<CommentModel> acm = new ArrayList<CommentModel>();
		CommentModel comment1 = new CommentModel("1.2", "1.111", "Body-test body", "Author", null, "Title");
		CommentModel comment2 = new CommentModel("2", "2", "Body2", "Author2", pic, "Title2");
		
 		acm.add(comment1);
 		acm.add(comment2); //make a list of 2 comments
 		
 		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Bitmap.class, new BitmapJsonConverter());
		Gson gson = builder.create();
		
		String jsonString = gson.toJson(acm);
		
		cache.replaceFileHistory(jsonString, filename); //store to file 
		acm.clear();
		
		acm = cache.loadFromCache(filename, activity);
		assertTrue(!acm.isEmpty()); 
		
		assertTrue(acm.get(0).getmBody().equals(comment1.getmBody()));
		assertTrue(acm.get(0).getmAuthor().equals(comment1.getmAuthor()));
		assertTrue(acm.get(0).getmTitle().equals(comment1.getmTitle()));
		assertTrue(acm.get(0).getmPicture() == null);
		assertTrue(acm.get(0).getLat().equals(comment1.getLat()));
		assertTrue(acm.get(0).getLon().equals(comment1.getLon()));
		
		assertTrue(acm.get(1).getmBody().equals(comment2.getmBody()));
		assertTrue(acm.get(1).getmAuthor().equals(comment2.getmAuthor()));
		assertTrue(acm.get(1).getmTitle().equals(comment2.getmTitle()));
		assertTrue(acm.get(1).getLat().equals(comment2.getLat()));
		assertTrue(acm.get(1).getLon().equals(comment2.getLon()));
		
		//Log.w("filterlogstest", acm.get(1).getmPicture().toString());
		//Log.w("filterlogstest", comment2.getmPicture().toString()); //references change but the picture still makes it.
		assertTrue(acm.get(1).getmPicture().describeContents() == comment2.getmPicture().describeContents());
		
		
	}
}
