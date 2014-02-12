package tests;

import java.util.ArrayList;
import main.GeoTopicsActivity;
import android.test.ActivityInstrumentationTestCase2;
import java.lang.Object.objectdraw.Location;
/*
 * 1. User Story: As a user, I want to sort comments by proximity to me
 * Participating Actors: User
 * Goal: Sort the comments by proximity to user (Geo Location)
 * Trigger: User is viewing comments and selects sort by proximity to me.
 * Pre condition: User is in a location and wants to view comments.
 * Post condition: Comments sorted by proximity to user.
 * Basic Flow:
 * User selects a sort type
 * Comments re-display sorted on selected sort type
 * Exceptions:
 * 2.There are no comments, system displays an empty list.
 */

public class SortCommentsByProximityToMe extends ActivityInstrumentationTestCase2<GeoTopicsActivity> {

	public SortCommentsByProximityToMe() {
		super(GeoTopicsActivity.class);
	}

	public void testSuccess() {
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
		Location myLocation = new Location(100, 100);
		ArrayList<Comment> unsortedComments = new ArrayList<Comment>();
		ArrayList<Comment> sortedComments = new ArrayList<Comment>();		
		
		Location loc1 = new Location(100, 100);
		Location loc2 = new Location(200, 200);
		Location loc3 = new Location(300, 300);

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

		assertTrue(unsortedComments.equals(sortedComments);
	}
}
