package tests;

import java.util.ArrayList;
import main.GeoTopicsActivity;
import android.test.ActivityInstrumentationTestCase2;
import java.lang.Object.objectdraw.Location;

/*
	3. 
	User Story: As a user, I want to sort comments by pictures.
	Use Case Name: SortCommentsByProximityPicture
	Participating Actors: User
	Goal: Sort the comments by picture
	Trigger: User is viewing comments and selects sort by picture.
	Pre condition: User wants to view comments sorted on picture.
	Post condition: Comments sorted by picture.
	
	Basic Flow:
	    User selects a sort type
	    Comments re-display sorted on selected sort type

	Exceptions:
	2.There are no comments, system displays an empty list.
 */

public class SortCommentsByProximityPicture extends ActivityInstrumentationTestCase2<GeoTopicsActivity> {

	public SortCommentsByProximityPicture() {
		super(GeoTopicsActivity.class);
	}

	public void testSuccess() {

		SortController sC = new SortController();		

		/*
		   This location is just a dummy location represeting me.
		 */
		Location arbitraryLocation = new Location(100, 100);
		ArrayList<Comment> unsortedComments = new ArrayList<Comment>();
		ArrayList<Comment> sortedComments = new ArrayList<Comment>();		
		
		Location loc1 = new Location(100, 100);
		Location loc2 = new Location(200, 200);
		Location loc3 = new Location(300, 300);

		Picture pic1 = new Picture(cat.bmp);
		Picture pic2 = new Picture(dog.bmp);

		Comment com1 = new Comment(loc1, pic1);
		Comment com2 = new Comment(loc2, pic2);
		Comment com3 = new Comment(loc3);

		unsortedComments.add(com2);
		unsortedComments.add(com3);
		unsortedComments.add(com1);

		sortedComments.add(com1);
		sortedComments.add(com2);
		sortedComments.add(com3);

		/*
		sortCommentsByProximityPicture should take in all the unsorted comments
		and firstly find the ones that have pictures. If it has a picture,
		sort it along with the other comments w/ pictures by their location
		with reference to my location.

		The order should be com1, com2, com3.
    		  */

		sC.sortCommentsByProximityPicture(arbitraryLocation, unsortedComments);

		assertTrue(unsortedComments.equals(sortedComments);
	}
}
