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
		
	}
	
	public void testSortAllCommentsByProximity() {
		List<CommentModel> lc = prepCommentList(15, 20, 0.01);
		Location myLoc = new Location("My Location");
		myLoc.setLongitude(0);
		myLoc.setLatitude(0);
		
		assertTrue("The list is not null", lc.size() > 0);
		assertTrue("The first element of the list is C", lc.get(0).getmBody().equals("C"));
		assertTrue("The second element of the list is B", lc.get(1).getmBody().equals("B"));
		assertTrue("The third element of the list is A", lc.get(2).getmBody().equals("A"));
		
		lc = SortComments.SortAllCommentsByProximity(lc, myLoc);
		
		assertTrue("The first element of the list is C", lc.get(0).getmBody().equals("C"));
		assertTrue("The second element of the list is A", lc.get(1).getmBody().equals("A"));
		assertTrue("The first element of the list is B", lc.get(2).getmBody().equals("B"));
		
	}
	
	/*
	 * This tests the functionality of getCommentsWithinRegion()
	 * 
	 * This adds three Comments to a list, one of which is within 1 kilometer 
	 * of myLoc. getCommentsWithinRegion retrieves all comments within a kilometer 
	 * and therefore the size should only contain the one comment upon return
	 */
	public void testGetCommentsWithinRegion() {
		List<CommentModel> lc = prepCommentList(0.01, 0.02, 0.001);
		Location myLoc = new Location("myLoc");
		myLoc.setLatitude(0);
		myLoc.setLongitude(0);
		
		lc = SortComments.getCommentsWithinRegion(lc, myLoc, 0);
		
		assertTrue("List is not empty", lc.size() > 0);
		assertEquals("List is expected to be size = 1", 1, lc.size());
		assertTrue("The first item ins the is has title 'C'", lc.get(0).getmBody().equals("C"));
		
	}
	
	/*
	 * This tests the functionality of sortCommentsByDate()
	 * 
	 * This adds three Comments to a list. The three comments are created 
	 * with a delay so that their dates are different. The comments are 
	 * created in the order: A -> B -> C and are originally put into the 
	 * list in order: C -> B -> A. After the sort it should be in order
	 * A -> B -> C
	 */
	public void testSortCommentsByDate() {
		List<CommentModel> lc = prepCommentList(0.01, 0.02, 0.001);
		
		assertTrue("The first element is originally C", lc.get(0).getmBody().equals("C"));
		assertTrue("The first element is originally B", lc.get(1).getmBody().equals("B"));
		assertTrue("The first element is originally A", lc.get(2).getmBody().equals("A"));
		
		lc = SortComments.sortCommentsByDate(lc);
		
		assertTrue("List is not empty", lc.size() > 0);
		assertTrue("The first element should be A", lc.get(0).getmBody().equals("A"));
		assertTrue("The first element is originally B", lc.get(1).getmBody().equals("B"));
		assertTrue("The first element is originally C", lc.get(2).getmBody().equals("C"));
	}
	
	public List<CommentModel> prepCommentList(double latA, double latB, double latC) {
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
		try {
		    Thread.sleep(10);
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
		CommentModel cB = new CommentModel(locB, "B", "B", null, "B");
		try {
		    Thread.sleep(10);
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
		CommentModel cC = new CommentModel(locC, "C", "C", null, "C");
		
		List<CommentModel> lc = new ArrayList<CommentModel>();
		lc.add(cC);
		lc.add(cB);
		lc.add(cA);
		return lc;	
	}

}
