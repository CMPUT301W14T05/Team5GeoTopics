package ca.ualberta.cs.team5geotopics.test;

import java.util.ArrayList;

import org.w3c.dom.Comment;

import android.location.Location;
import android.test.ActivityInstrumentationTestCase2;

public class SortControllerTests extends ActivityInstrumentationTestCase2<SortController> {

	public SortControllerTests() {
		super(SortController.class);
	}

	public void testSortByProximityToMe() {
		/* I am not sure yet as to exactly how our class system will be setup so for now
		 * I will make direct calls to the sortController as it is specified in the 
		 * UML. 
   		 * The idea is to create a brief list of comments with random locations and set a 
		 * static location variable (which will simulate the location of 'ME' and then 
		 * call the sort function and compare it to the sorted version of the comment list
		 * which we will already know 
		 */
		SortController sC = new SortController();		

		/*
		 * This location is just a dummy location supposed to represent me
		 */
		Location myLocation = new Location("myLocation");
		myLocation.setLatitude(100);
		myLocation.setLongitude(100);
		ArrayList<Comment> unsortedComments = new ArrayList<Comment>();
		ArrayList<Comment> sortedComments = new ArrayList<Comment>();		
		
		Location loc1 = new Location("loc1");
		Location loc2 = new Location("loc2");
		Location loc3 = new Location("loc3");

		loc1.setLatitude(100);
		loc1.setLongitude(100);
		loc2.setLatitude(200);
		loc2.setLongitude(200);
		loc3.setLatitude(300);
		loc3.setLongitude(300);
		
		Comment com1 = new Comment(loc1);
		Comment com2 = new Comment(loc2);
		Comment com3 = new Comment(loc3);

		unsortedComments.add(com2);
		unsortedComments.add(com3);
		unsortedComments.add(com1);

		sortedComments.add(com1);
		sortedComments.add(com2);
		sortedComments.add(com3);

		/*
		 * Call the sorting function
		 * NOTE: this is likely not the way we will be calling the sort function
		 * so I will likely have to come back to this. But for now I will call it this
		 * way and then you can test if they have the same contents and ordering by doing
		 * a .equals.
		 */
		sC.sortCommentsByProximityToMe(myLocation, unsortedComments);

		assertTrue(unsortedComments.equals(sortedComments));
	}
	
	public void testSortByProximityToLocation() {
		/* I am not sure yet as to exactly how our class system will be setup so for now
		 * I will make direct calls to the sortController as it is specified in the 
		 * UML. 
   		 * The idea is to create a brief list of comments with random locations and set a 
		 * static location variable (which will simulate the location of 'ME' and then 
		 * call the sort function and compare it to the sorted version of the comment list
		 * which we will already know 
		 */
		SortController sC = new SortController();	
		
		/*
		 * This location is just a dummy location supposed to represent
		 * a location taken from a specific comment.
		 */
		/*
		Location arbitraryLocation = new Location(Comment.get(3).geoLocation);
		*/

		/*
		 * This location is just a dummy location supposed to represent me
		 */
		Location specLocation = new Location("specificLocation");
		specLocation.setLatitude(100);
		specLocation.setLongitude(100);
		ArrayList<Comment> unsortedComments = new ArrayList<Comment>();
		ArrayList<Comment> sortedComments = new ArrayList<Comment>();		
		
		Location loc1 = new Location("loc1");
		Location loc2 = new Location("loc2");
		Location loc3 = new Location("loc3");

		loc1.setLatitude(100);
		loc1.setLongitude(100);
		loc2.setLatitude(200);
		loc2.setLongitude(200);
		loc3.setLatitude(300);
		loc3.setLongitude(300);
		
		Comment com1 = new Comment(loc1);
		Comment com2 = new Comment(loc2);
		Comment com3 = new Comment(loc3);

		unsortedComments.add(com2);
		unsortedComments.add(com3);
		unsortedComments.add(com1);

		sortedComments.add(com1);
		sortedComments.add(com2);
		sortedComments.add(com3);

		/*
		 * Call the sorting function
		 * NOTE: this is likely not the way we will be calling the sort function
		 * so I will likely have to come back to this. But for now I will call it this
		 * way and then you can test if they have the same contents and ordering by doing
		 * a .equals.
		 */
		sC.sortCommentsByProximityToLocation(specLocation, unsortedComments);

		assertTrue(unsortedComments.equals(sortedComments));
	}
	
	public void testSortByProximityToPicture() {
		/* I am not sure yet as to exactly how our class system will be setup so for now
		 * I will make direct calls to the sortController as it is specified in the 
		 * UML. 
   		 * The idea is to create a brief list of comments with random locations and set a 
		 * static location variable (which will simulate the location of 'ME' and then 
		 * call the sort function and compare it to the sorted version of the comment list
		 * which we will already know 
		 */
		SortController sC = new SortController();	

		/*
		 * This location is just a dummy location supposed to represent me
		 */
		Location specLocation = new Location("specificLocation");
		specLocation.setLatitude(100);
		specLocation.setLongitude(100);
		ArrayList<Comment> unsortedComments = new ArrayList<Comment>();
		ArrayList<Comment> sortedComments = new ArrayList<Comment>();		
		
		Location loc1 = new Location("loc1");
		Location loc2 = new Location("loc2");
		Location loc3 = new Location("loc3");

		loc1.setLatitude(100);
		loc1.setLongitude(100);
		loc2.setLatitude(200);
		loc2.setLongitude(200);
		loc3.setLatitude(300);
		loc3.setLongitude(300);
		
		Comment com1 = new Comment(loc1);
		Comment com2 = new Comment(loc2);
		Comment com3 = new Comment(loc3);
		
		/*
		 * Not really sure how these are relevant - Tyler
		 * I figured we would need a photo attached to a comment
		 * in order to sort by photos which are near me - Bryce
		 */
		//Picture pic1 = new Picture(cat.bmp);
		//Picture pic2 = new Picture(dog.bmp);

		unsortedComments.add(com2);
		unsortedComments.add(com3);
		unsortedComments.add(com1);

		sortedComments.add(com1);
		sortedComments.add(com2);
		sortedComments.add(com3);

		/*
		 * Call the sorting function
		 * NOTE: this is likely not the way we will be calling the sort function
		 * so I will likely have to come back to this. But for now I will call it this
		 * way and then you can test if they have the same contents and ordering by doing
		 * a .equals.
		 */
		sC.sortCommentsByProximityToLocation(specLocation, unsortedComments);

		assertTrue(unsortedComments.equals(sortedComments));
	}
	
	/*
	 * UseCase 4: SortCommentsByScoringSystem
	 */
	public void testSortCommentsByScoringSystem() {
		/* 
		 * Not exactly sure how we will test this yet, as we have not clearly
		 * laid out how we will be setting up said 'scoring system'. We may need
		 * a few test cases for this as users will have to be able to select different
		 * attributes to sort on.
		 * 
		 * The main flow of this test will be much like the other sorting tests found in 
		 * this class. We will have to create a few dummy comments with different attributes
		 * then we will have to test the different possible scoring systems on the dummy
		 * comments. Since we what attributes make up the comments we will be able to 
		 * predetermine the expected outcomes and will compare them to the actual outcomes
		 * in order to test this.
		 */
	}
	
	/*
	 * missing used case!!!
	 */
	public void testSortCommmentsByTime() {
	
		SortView sV = new SortView();	
		
		// Dates (This is probably highly incorrect but a good visual)
		Date date1 = new Date(1/12/2014);
		Date date2 = new Date(2/12/2014);
		Date date3 = new Date(3/12/2014);
		
		ArrayList<Comment> unsortedComments = new ArrayList<Comment>();
		ArrayList<Comment> sortedComments = new ArrayList<Comment>();	

		Comment com1 = new Comment(date1);
		Comment com2 = new Comment(date2);
		Comment com3 = new Comment(date3);		
		
		// Random sort
		unsortedComments.add(com2);
		unsortedComments.add(com3);
		unsortedComments.add(com1);
		
		// Sort by newest first
		sortedComments.add(com3);
		sortedComments.add(com2);
		sortedComments.add(com1);
		
		
		// It should not be Time but rather Date as that is what we have
		// been using. This function would just check the dates. Won't
		// be difficult to implement.
		sV.sortCommentsByTime(unsortedComments);

		assertTrue(unsortedComments.equals(sortedComments));
	}
	
	/*
	 * UseCase 17: Default Fresh Comments
	 */
	public void testSortCommentsByDefault() {
		/*
		 * This method MAY be removed as most of the group has agreed that 
		 * the default sort is in fact going to be sortByProximityToMe(). and
		 * we have already built the test case for that above.
		 * 
		 * If we do not remove it it will be a similar test as the 
		 * testSortByProximityToMe(). (Literally the same actually).
		 */
	}
	
}
