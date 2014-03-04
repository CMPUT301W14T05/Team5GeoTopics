package ca.ualberta.cs.team5geotopics.test;

import java.util.ArrayList;
import java.util.List;

import android.location.Location;
import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.cs.team5geotopics.BrowseActivity;
import ca.ualberta.cs.team5geotopics.CommentModel;
import ca.ualberta.cs.team5geotopics.SortComments;

public class SortCommentsTests extends ActivityInstrumentationTestCase2<BrowseActivity> {

	public SortCommentsTests() {
		super(BrowseActivity.class);
	}
	
	public void testSortCommentByProximityToMe() {
		List<CommentModel> lc = prepCommentList(0.001, 0.005, 0.01);
		Location myLoc = new Location("myLoc");
		myLoc.setLatitude(0);
		myLoc.setLongitude(0);
		
		//lc = SortComments.SortCommentsByProximityToLoc(lc, myLoc);
		
		assertTrue("array is not empty", lc.size() > 0);
		//assertTrue(lc.get(0).getmAuthor().equals("C"));
	}
	
	/*
	 * This tests the functionality of getCommentsWithinRegion()
	 */
	public void testGetCommentsWithinRegion() {
		List<CommentModel> lc = prepCommentList(0.001, 0.01, 0.02);
		Location myLoc = new Location("myLoc");
		myLoc.setLatitude(0);
		myLoc.setLongitude(0);
		
		lc = SortComments.getCommentsWithinRegion(lc, myLoc);
		
		assertTrue("List is not empty", lc.size() > 0);
		assertTrue("The first item ins the is has title 'C'", lc.get(0).getmBody().equals("C"));
		
	}
	
	public List<CommentModel> prepCommentList(double latC, double latA, double latB) {
		Location locA = new Location("A");
		Location locB = new Location("B");
		Location locC = new Location("C");
		
		locC.setLatitude(latC);
		locC.setLongitude(0);
		
		locB.setLatitude(latB);
		locB.setLongitude(0);
		
		locA.setLatitude(latA);
		locA.setLongitude(0);
		
		CommentModel cA = new CommentModel(locA, "A", "A", null, "A");
		CommentModel cB = new CommentModel(locB, "B", "B", null, "B");
		CommentModel cC = new CommentModel(locC, "C", "C", null, "C");
		
		List<CommentModel> lc = new ArrayList<CommentModel>();
		lc.add(cA);
		lc.add(cB);
		lc.add(cC);
		return lc;	
	}

}
