package tests;

import java.util.ArrayList;
import main.GeoTopicsActivity;
import android.test.ActivityInstrumentationTestCase2;
import java.lang.Object.objectdraw.Location;

/*
 	2. 
	User Story: As a user, I want to sort comments by proximity to another location.
	Participating Actors: User
	Goal: Sort the comments by proximity to a specific location (Geo Location)
	Trigger: User is viewing comments and selects sort by proximity to location.
	Pre condition: User is in a location and wants to view comments sorted on proxmity to another location.
	Post condition: Comments sorted by proximity to a location.

	Basic Flow:
	    User selects a sort type
	    Comments re-display sorted on selected sort type

	Exceptions:
	    2.There are no comments, system displays an empty list.
 */

public class SortCommentsByProximityToLocation extends ActivityInstrumentationTestCase2<GeoTopicsActivity> {

	public SortCommentsByProximityToLocation() {
		super(GeoTopicsActivity.class);
	}

	public void testSuccess() {

		SortController sC = new SortController();		

		/*
		 * This location is just a dummy location supposed to represent
		 * a location taken from a specific comment.
		 */
		Location arbitraryLocation = new Location(Comment.get(3).geoLocation);
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
		We can create sortCommentsByProximityToLocation by comparing my current 
		location (which will be taked as a prexisting location) to all other
		comments.
		 */

		sC.sortCommentsByProximityToLocation(arbitraryLocation, unsortedComments);

		assertTrue(unsortedComments.equals(sortedComments);
	}
}
