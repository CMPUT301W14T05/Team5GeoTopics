package ca.ualberta.cs.team5geotopics;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.location.Location;
import ca.ualberta.cs.team5geotopics.CommentModel;

/*
 * This is a simple sort comments class that will be required for local sorting (no internet)
 * it has been seperated from any single model as it will be used across more than one.
 * 
 * Addresses Use Cases: 1, 2, 3, 4
 * 
 * Authored: Tyler Wendlandt
 */
public class SortComments {

	/*
	 * Handles Used Cases 1, 2:
	 * SortByProximityToMe
	 * SortByProximityToLoc
	 * 
	 * Simply pass the current comment list and the either the location you want or your location
	 */
	public static List<CommentModel> SortCommentsByProximityToLoc(List<CommentModel> cList, Location myLoc) {

		cList = getCommentsWithinRegion(cList, myLoc);
    	cList = sortCommentsByDate(cList);
    	
		return cList;

	}
	
	/*
	 * Handles Used Case 3:
	 * SortCommentsByPicture
	 * 
	 * take in the array of current comments and retrieves those with pictures,
	 * then sorts them by date.
	 * 
	 *  NOTE: As of right now does  NOT consider location
	 */
	public static List<CommentModel> SortCommentsByPicture(List<CommentModel> cList) {
		getCommentsWithPictures(cList);
		sortCommentsByDate(cList);
		return cList;
	}
	
	/*
	 * Retrieves the comments in the current list that have pictures
	 */
	public static List<CommentModel> getCommentsWithPictures(List<CommentModel> cList) {
		for (int i = 0; i < cList.size(); i++) {
			if (!cList.get(i).hasPicture()) {
				cList.remove(i);
				i -= 1; 
			}
		}
		return cList;
	}
	
	public static List<CommentModel> sortCommentsByDate(final List<CommentModel> cList) {
		
		/*
		 * This should sort the comment list based on date
		 */
		Collections.sort(cList, new Comparator<CommentModel>() {
			public int compare(CommentModel a, CommentModel b) {
				return (int) (a.getDate().getTime() - 
						b.getDate().getTime());
			}
		});
		return cList;
	}
	
	public static List<CommentModel> getCommentsWithinRegion(List<CommentModel> cList, Location myLoc) {
		
		/*
		 *	This should remove any comment from the list that is further than 500 meters away 
		 */
		for (int i = 0; i < cList.size(); i++) {
			if (500 < cList.get(i).getGeoLocation().distanceTo(myLoc)) {
				cList.remove(i);
				i -= 1;
			}
		}
		return cList;
	}
}
