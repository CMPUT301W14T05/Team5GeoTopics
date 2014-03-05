package ca.ualberta.cs.team5geotopics;

import java.util.ArrayList;
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

	/*
	 * Handles Used Cases 1, 2:
	 * SortByProximityToMe
	 * SortByProximityToLoc
	 * 
	 * Simply pass the current comment list and the either the location you want or your location
	 * 
	 * NOTE: this is for TLC sorting
	 */
	public static List<CommentModel> SortTLCommentsByProximityToLoc(List<CommentModel> cList, Location myLoc) {
		cList = SortAllCommentsByProximity(cList, myLoc);
		return cList;
	}
	
	/*
	 * Handles Used Cases 1, 2:
	 * SortByProximityToMe
	 * SortByProximityToLoc
	 * 
	 * Simply pass the current comment list and the either the location you want or your location
	 * 
	 * NOTE: this is for REPLY sorting
	 */
	public static List<CommentModel> SortRepliesByProximityToLoc(List<CommentModel> cList, Location myLoc) {
		List<CommentModel> tempList = new ArrayList<CommentModel>();
		for (int i = cList.size() - 1; i >= 1; i--) {
			tempList.add(cList.get(i));
			cList.remove(i);
		}
		
		tempList = SortAllCommentsByProximity(tempList, myLoc);
		
		for (int i = 0; i < tempList.size(); i++) {
			cList.add(tempList.get(i));
		}
		
		return cList;
	}
	
	public static List<CommentModel> SortAllCommentsByProximity(final List<CommentModel> cList, final Location myLoc) {
	
		Collections.sort(cList, new Comparator<CommentModel>() {
			public int compare(CommentModel a, CommentModel b) {
				return (int) (a.getGeoLocation().distanceTo(myLoc) - 
						b.getGeoLocation().distanceTo(myLoc));
			}
		});
		return cList;
	}
	
	public static List<CommentModel> SortTLCByFreshness(List<CommentModel> cList, Location myLoc) {
		cList = getCommentsWithinRegion(cList, myLoc, 0);
    	cList = sortCommentsByDate(cList);
    	
		return cList;
	}
	
	public static List<CommentModel> SortRepliesByFreshness(List<CommentModel> cList, Location myLoc) {
		cList = getCommentsWithinRegion(cList, myLoc, 1);
		List<CommentModel> tempList = new ArrayList<CommentModel>();
		for (int i = cList.size(); i >= 1; i--) {
			tempList.add(cList.get(i));
			cList.remove(i);
		}
    	
		tempList = sortCommentsByDate(tempList);
    	
		for (int i = 0; i < tempList.size(); i++) {
			cList.add(tempList.get(i));
		}
 
		return cList;
	}
	
	/*
	 * Handles Used Case 3:
	 * SortCommentsByPicture
	 * 
	 * take in the array of current TopLevelComments and splits it up into list of 
	 * replies containing photos, and a reply list with no photos. Sorts them both
	 * by date and then adds them to the comment list again.
	 * 
	 *  NOTE: As of right now does  NOT consider location
	 */
	public static List<CommentModel> SortTLCByPicture(List<CommentModel> cList) {
		List<CommentModel> picList = new ArrayList<CommentModel>();
		List<CommentModel> noPicList = new ArrayList<CommentModel>();
		
		for (int i = cList.size() - 1; i >= 0; i--) {
			if (cList.get(i).hasPicture()) {
				picList.add(cList.get(i));
				cList.remove(i);
			} else {
				noPicList.add(cList.get(i));
				cList.remove(i);
			}
		}
		
		picList = sortCommentsByDate(picList);
		noPicList = sortCommentsByDate(noPicList);
		
		for (int i = 0; i < picList.size(); i++) {
			cList.add(picList.get(i));
		}
		for (int i = 0; i < noPicList.size(); i++) {
			cList.add(noPicList.get(i));
		}
		
		return cList;
	}
	
	/*
	 * Handles Used Case 3:
	 * SortCommentsByPicture
	 * 
	 * take in the array of current replies and splits it up into list of 
	 * replies containing photos, and a reply list with no photos. Sorts them both
	 * by date and then adds them to the comment list again.
	 * 
	 *  NOTE: As of right now does  NOT consider location
	 */
	public static List<CommentModel> SortRepliesByPicture(List<CommentModel> cList) {
		List<CommentModel> picList = new ArrayList<CommentModel>();
		List<CommentModel> noPicList = new ArrayList<CommentModel>();
		
		for (int i = cList.size() - 1; i >= 1; i--) {
			if (cList.get(i).hasPicture()) {
				picList.add(cList.get(i));
				cList.remove(i);
			} else {
				noPicList.add(cList.get(i));
				cList.remove(i);
			}
		}
		
		picList = sortCommentsByDate(picList);
		noPicList = sortCommentsByDate(noPicList);
		
		for (int i = 0; i < picList.size(); i++) {
			cList.add(picList.get(i));
		}
		for (int i = 0; i < noPicList.size(); i++) {
			cList.add(noPicList.get(i));
		}
		
		return cList;
	}
	
//	/*
//	 * Retrieves the comments in the current list that have pictures
//	 */
//	public static List<CommentModel> getCommentsWithPictures(List<CommentModel> cList, int end) {
//		for (int i = cList.size() - 1; i >= end; i++) {
//			if (!cList.get(i).hasPicture()) {
//				cList.remove(i);
//				i -= 1; 
//			}
//		}
//		return cList;
//	}
	
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

	public static List<CommentModel> getCommentsWithinRegion(List<CommentModel> cList, Location myLoc, int end) {

		/*
		 *	This should remove any comment from the list that is further than 1 km away 
		 */
		for (int i = cList.size() - 1; i >= end; i--) {
			if (cList.get(i).getGeoLocation().distanceTo(myLoc) > 1000) {
				cList.remove(i);
			}
		}
		return cList;
	}
}
