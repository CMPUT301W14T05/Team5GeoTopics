package ca.ualberta.cs.team5geotopics.test;

import java.util.ArrayList;
import java.util.List;

import android.location.Location;
import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.cs.team5geotopics.BrowseView;
import ca.ualberta.cs.team5geotopics.CommentModel;
import ca.ualberta.cs.team5geotopics.SortComments;

public class SortCommentsTests extends ActivityInstrumentationTestCase2<BrowseView> {

	public SortCommentsTests() {
		super(BrowseView.class);
	}
	
	public void testSortCommentByProximityToMe() {
		List<CommentModel> lc = prepCommentList();
		Location myLoc = new Location("myLoc");
		myLoc.setLatitude(0);
		myLoc.setLongitude(0);
		
		lc = SortComments.SortCommentsByProximityToLoc(lc, myLoc);
		
		assertTrue(lc.get(0).getmAuthor().equals("C"));
	}
	
	public List<CommentModel> prepCommentList() {
		Location locA = new Location("A");
		Location locB = new Location("B");
		Location locC = new Location("C");
		
		locC.setLatitude(50);
		locC.setLongitude(0);
		
		locB.setLatitude(1000);
		locB.setLongitude(0);
		
		locA.setLatitude(550);
		locA.setLongitude(0);
		
		List<CommentModel> lc = new ArrayList<CommentModel>();
		lc.add(new CommentModel(locA, "A", "A", null));
		lc.add(new CommentModel(locB, "B", "B", null));
		lc.add(new CommentModel(locC, "C", "C", null));
		
		return lc;	
	}

}
