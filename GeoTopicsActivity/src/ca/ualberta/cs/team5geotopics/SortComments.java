package ca.ualberta.cs.team5geotopics;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.location.Location;

/*
 * This is a simple sort comments class that will be required for local sorting (no internet)
 * it has been seperated from any single model as it will be used across more than one.
 * 
 * Addresses Use Cases: 1, 2, 3, 4
 * 
 * Authored: Tyler Wendlandt
 */
public class SortComments {

	public SortComments() {
		super();
	}

	public List<CommentModel> SortCommentsByProximityToMe(final List<CommentModel> cList, final Location myLoc) {

		Collections.sort(cList, new Comparator<CommentModel>() {
    	    public int compare(CommentModel a, CommentModel b) {
    	        return (int) (b.getGeoLocation().distanceTo(myLoc) - 
    	        		a.getGeoLocation().distanceTo(myLoc));
    	    }
    	});
    	
		return cList;

	}
	
	public List<CommentModel> getCommentsWithinRegion(List<CommentModel> cList, Location myLoc) {
		for (int i = 0; i < cList.size(); i++) {
			
		}
		
		return cList;
	}
}
