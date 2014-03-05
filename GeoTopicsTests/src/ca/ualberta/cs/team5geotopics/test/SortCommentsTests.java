package ca.ualberta.cs.team5geotopics.test;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.location.Location;
import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.cs.team5geotopics.BrowseActivity;
import ca.ualberta.cs.team5geotopics.CommentModel;
import ca.ualberta.cs.team5geotopics.SortComments;

public class SortCommentsTests extends ActivityInstrumentationTestCase2<BrowseActivity> {

	public SortCommentsTests() {
		super(BrowseActivity.class);
	}
	
	/*
	 * Test SortAllCommentsByProximity
	 * 
	 * test adds Comments to a list in order C -> B -> A
	 * after sort C -> A -> B.
	 */
	public void testSortCommentsByProximity() {
		List<CommentModel> lc = prepCommentList(15, 20, 0.01, false);
		Location myLoc = new Location("My Location");
		myLoc.setLongitude(0);
		myLoc.setLatitude(0);
		
		assertTrue("The list is not null", lc.size() > 0);
		assertTrue("The first element of the list is C", lc.get(0).getmBody().equals("C"));
		assertTrue("The second element of the list is B", lc.get(1).getmBody().equals("B"));
		assertTrue("The third element of the list is A", lc.get(2).getmBody().equals("A"));
		
		lc = SortComments.SortCommentsByProximity(lc, myLoc);
		
		assertTrue("The first element of the list is C", lc.get(0).getmBody().equals("C"));
		assertTrue("The second element of the list is A", lc.get(1).getmBody().equals("A"));
		assertTrue("The first element of the list is B", lc.get(2).getmBody().equals("B"));
		
	}
	
	public void testSortRepliesByProximityToLoc() {
		List<CommentModel> lc = prepCommentList(10, .03, 15, false);
		Location myLoc = new Location("My Location");
		myLoc.setLongitude(0);
		myLoc.setLatitude(0);
		
		assertTrue("The list is not null", lc.size() > 0);
		assertTrue("The first element of the list is C", lc.get(0).getmBody().equals("C"));
		assertTrue("The second element of the list is B", lc.get(1).getmBody().equals("B"));
		assertTrue("The third element of the list is A", lc.get(2).getmBody().equals("A"));
		
		lc = SortComments.SortCommentsByProximity(lc, myLoc);
		
		assertTrue("The first element of the list is B", lc.get(0).getmBody().equals("B"));
		assertTrue("The second element of the list is A", lc.get(1).getmBody().equals("A"));
		assertTrue("The first element of the list is C", lc.get(2).getmBody().equals("C"));
	}
	
	public void testSortTLCByFreshness() {
		List<CommentModel> lc = prepCommentList(0.001, .02, 0.008, false);
		Location myLoc = new Location("My Location");
		myLoc.setLongitude(0);
		myLoc.setLatitude(0);
		
		assertEquals("The size of the array should now be 3", 3, lc.size());
		assertTrue("The first element of the list is C", lc.get(0).getmBody().equals("C"));
		assertTrue("The second element of the list is B", lc.get(1).getmBody().equals("B"));
		assertTrue("The third element of the list is A", lc.get(2).getmBody().equals("A"));
		
		SortComments.SortTLCByFreshness(lc, myLoc);
		
		assertEquals("The size of the array should now be 2", 2, lc.size());
		assertTrue("The first element of the list is A", lc.get(0).getmBody().equals("A"));
		assertTrue("The second element of the list is C", lc.get(1).getmBody().equals("C"));
	}
	
	public void testSortRepliesByFreshness() {
		List<CommentModel> lc = prepCommentList(0.001, .02, 0.008, false);
		Location myLoc = new Location("My Location");
		myLoc.setLongitude(0);
		myLoc.setLatitude(0);
		
		assertEquals("The size of the array should now be 3", 3, lc.size());
		assertTrue("The first element of the list is C", lc.get(0).getmBody().equals("C"));
		assertTrue("The second element of the list is B", lc.get(1).getmBody().equals("B"));
		assertTrue("The third element of the list is A", lc.get(2).getmBody().equals("A"));
		
		SortComments.SortRepliesByFreshness(lc, myLoc);
		
		assertEquals("The size of the array should now be 2", 2, lc.size());
		assertTrue("The first element of the list is C", lc.get(0).getmBody().equals("C"));
		assertTrue("The second element of the list is A", lc.get(1).getmBody().equals("A"));
	}
	
	/*
	 * Test SortTLCByPicture
	 * Handles Used Case 3
	 * 
	 * Initial order of comments C -> B -> A, after sort order should be A -> C -> B
	 * 
	 */
	public void testSortTLCByPicture() {
		List<CommentModel> lc = prepCommentList(10, 0.01, 10, true);
		
		assertTrue("The list is not null", lc.size() > 0);
		assertTrue("The first element of the list is C", lc.get(0).getmBody().equals("C"));
		assertTrue("The second element of the list is B", lc.get(1).getmBody().equals("B"));
		assertTrue("The third element of the list is A", lc.get(2).getmBody().equals("A"));
		SortComments.SortTLCByPicture(lc);
		assertTrue("The first element of the list is A", lc.get(0).getmBody().equals("A"));
		assertTrue("The second element of the list is C", lc.get(1).getmBody().equals("C"));
		assertTrue("The third element of the list is B", lc.get(2).getmBody().equals("B"));
	}
	
	/*
	 * Test SortTLCByPicture
	 * Handles Used Case 3
	 * 
	 * Initial order of comments C -> B -> A, after sort order should be C -> A -> B
	 * as it assumes C is the comment and A and B are the replies
	 */
	public void testSortRepliesByPicture() {
		List<CommentModel> lc = prepCommentList(10, 0.01, 10, true);
		
		assertTrue("The list is not null", lc.size() > 0);
		assertTrue("The first element of the list is C", lc.get(0).getmBody().equals("C"));
		assertTrue("The second element of the list is B", lc.get(1).getmBody().equals("B"));
		assertTrue("The third element of the list is A", lc.get(2).getmBody().equals("A"));
		SortComments.SortRepliesByPicture(lc);
		assertTrue("The first element of the list is C", lc.get(0).getmBody().equals("C"));
		assertTrue("The second element of the list is A", lc.get(1).getmBody().equals("A"));
		assertTrue("The third element of the list is B", lc.get(2).getmBody().equals("B"));
		
	}
	
	/*
	 * This tests the functionality of getCommentsWithinRegion()
	 * 
	 * This adds three Comments to a list, one of which is within 1 kilometer 
	 * of myLoc. getCommentsWithinRegion retrieves all comments within a kilometer 
	 * and therefore the size should only contain the one comment upon return
	 */
	public void testGetCommentsWithinRegion() {
		List<CommentModel> lc = prepCommentList(0.01, 0.02, 0.001, false);
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
		List<CommentModel> lc = prepCommentList(0.01, 0.02, 0.001, false);
		
		assertTrue("The first element is originally C", lc.get(0).getmBody().equals("C"));
		assertTrue("The first element is originally B", lc.get(1).getmBody().equals("B"));
		assertTrue("The first element is originally A", lc.get(2).getmBody().equals("A"));
		
		lc = SortComments.sortCommentsByDate(lc);
		
		assertTrue("List is not empty", lc.size() > 0);
		assertTrue("The first element should be A", lc.get(0).getmBody().equals("A"));
		assertTrue("The first element is originally B", lc.get(1).getmBody().equals("B"));
		assertTrue("The first element is originally C", lc.get(2).getmBody().equals("C"));
	}
	
	public List<CommentModel> prepCommentList(double latA, double latB, double latC,
			boolean hasPhoto) {
		Location locA = new Location("A");
		Location locB = new Location("B");
		Location locC = new Location("C");
		Bitmap bm = null;
		
		if (hasPhoto) {
			bm = Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888);
		} 
		
		locC.setLatitude(latC);
		locC.setLongitude(0);
		
		locB.setLatitude(latB);
		locB.setLongitude(0);
		
		locA.setLatitude(latA);
		locA.setLongitude(0);
		
		CommentModel cA = new CommentModel(locA, "A", "A", bm, "A");
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
		CommentModel cC = new CommentModel(locC, "C", "C", bm, "C");
		
		List<CommentModel> lc = new ArrayList<CommentModel>();
		lc.add(cC);
		lc.add(cB);
		lc.add(cA);
		return lc;	
	}

}
