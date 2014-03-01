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
		SortComments sc = new SortComments();
		
		List<CommentModel> lc = prepCommentList();
		Location myLoc = new Location("myLoc");
		
		lc = sc.SortCommentsByProximityToMe(lc, myLoc);
		
		assertEquals("C", lc.get(0));
	}
	
	public List<CommentModel> prepCommentList() {
		Location locA = new Location("A");
		Location locB = new Location("B");
		Location locC = new Location("C");
		
		locC.setLatitude(50);
		locC.setLongitude(50);
		
		locB.setLatitude(100);
		locB.setLongitude(100);
		
		locA.setLatitude(150);
		locA.setLongitude(150);
		
		List<CommentModel> lc = new ArrayList<CommentModel>();
		lc.add(new CommentModel(locA, "A", "A", null));
		lc.add(new CommentModel(locB, "B", "B", null));
		lc.add(new CommentModel(locC, "C", "C", null));
		
		return lc;	
	}

}
