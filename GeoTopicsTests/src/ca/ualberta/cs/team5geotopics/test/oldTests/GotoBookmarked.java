package ca.ualberta.cs.team5geotopics.test.oldTests;

import java.util.ArrayList;
import main.GeoTopicsActivity;
import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.cs.team5geotopics.Comment;

/*
	** This test is bound to change substantially as we may not use this format for bookmarks **

	This will test a basic bookmark concept where each bookmark is a reference
	to the ID of the post (0 -> infinity). testSuccess attempts to goto the 
	first bookmarked comment which would have ID = 0 and the testFailure will
	try to go to an invalid ID = -1.
	
	Exceptions:

		1.) Comment was deleted therefore there is no content at ID = 0
	
	last updated: Feb 9/2014 2:50pm brycecartman
*/


public class GotoBookmarked extends ActivityInstrumentationTestCase2<GeoTopicsActivity> {

	public GotoBookmarked() {
		super(GeoTopicsActivity.class);
	}

	public void testSuccess(){
		WebServiceController wsc = new WebServiceController();

		ArrayList<Comment> bookmarkList = new ArrayList<Comment>();
		
		int ID = 0; // First ID to be posted
		bookmarkList.get(ID);	
		
		try{
		wsc.gotoBookmarkedComment(bookmarkList);
		}catch(Exception 1){ // Comment was deleted
		
		}
		assertTrue(bookmarkList.contains(ID));
	}
	
	public void testFailure(){
		WebServiceController wsc = new WebServiceController();

		ArrayList<Comment> bookmarkList = new ArrayList<Comment>();
		
		int ID = -1; // Invalid ID
		bookmarkList.get(ID);	
		
		wsc.gotoBookmarkedComment(bookmarkList);
		
		assertTrue(bookmarkList.contains(ID));
	}
	
	
}
