package ca.ualberta.cs.team5geotopics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.Context;
import android.location.Location;
import android.util.Log;

public class CommentListModel extends AModel<AView>{
	private ArrayList<CommentModel> mComments;
	private Cache mCache;
	
	public CommentListModel(){
		this.mComments = new ArrayList<CommentModel>();	
		this.mCache = Cache.getInstance();
	}
	
	public ArrayList<CommentModel> getList() {
		return mComments;
	}
	
	public void add(CommentModel comment) {
		mComments.add(comment);
		this.notifyViews();
	}
	
	/*
	 * Handles Used Cases 1, 2:
	 * SortByProximityToMe
	 * SortByProximityToLoc
	 * 
	 * Simply pass the current comment list and the either the location you want or your location
	 * 
	 * NOTE: this is for TLC sorting
	 */
	public void sortCommentsByProximityToLoc(Location myLoc) {
		sortCommentsByProximity(mComments, myLoc);
		this.notifyViews();
	}
	
	public static ArrayList<CommentModel> sortCommentsByProximity(final ArrayList<CommentModel> cList, final Location myLoc) {
	
		Collections.sort(cList, new Comparator<CommentModel>() {
			public int compare(CommentModel a, CommentModel b) {
				return (int) (a.getGeoLocation().distanceTo(myLoc) - 
						b.getGeoLocation().distanceTo(myLoc));
			}
		});
		return cList;
	}
	
	public void sortCommentsByFreshness(Location myLoc) {
		/*
		 *	This should remove any comment from the list that is further than 1 km away 
		 */
		for (int i = mComments.size() - 1; i >= 0; i--) {
			if (mComments.get(i).getGeoLocation().distanceTo(myLoc) > 1000) {
				mComments.remove(i);
			}
		}
    	mComments = sortCommentsByDate(mComments);
    	
		this.notifyViews();
	}
	
	/*
	 * Handles Used Case 3:
	 * SortCommentsByPicture
	 * 
	 * take in the array of current TopLevelComments and splits it up into list of 
	 * replies containing photos, and a reply list with no photos. Sorts them both
	 * by proximity and then adds them to the comment list again.
	 * 
	 *  NOTE: As of right now does  NOT consider location
	 */
	public void sortCommentsByPicture(Location loc) {
		ArrayList<CommentModel> picList = new ArrayList<CommentModel>();
		ArrayList<CommentModel> noPicList = new ArrayList<CommentModel>();
		
		for (int i = mComments.size() - 1; i >= 0; i--) {
			if (mComments.get(i).getPicture() != null) {
				picList.add(mComments.get(i));
				mComments.remove(i);
			} else {
				noPicList.add(mComments.get(i));
				mComments.remove(i);
			}
		}
		
		picList = sortCommentsByProximity(picList, loc);
		noPicList = sortCommentsByProximity(noPicList, loc);
		
		for (int i = 0; i < picList.size(); i++) {
			mComments.add(picList.get(i));
		}
		for (int i = 0; i < noPicList.size(); i++) {
			mComments.add(noPicList.get(i));
		}
		
		this.notifyViews();
	}
	
	public void sortAllCommentsByDate() {
		mComments = sortCommentsByDate(mComments);
		this.notifyViews();
	}
	
	public ArrayList<CommentModel> sortCommentsByDate(final ArrayList<CommentModel> cList) {
		/*
		 * This should sort the comment list based on date
		 */
		Collections.sort(cList, new Comparator<CommentModel>() {
			public int compare(CommentModel a, CommentModel b) {
				return (int) (b.getDate().getTime() - 
						a.getDate().getTime());
			}
		});
		return cList;
	}

	public void setList(ArrayList<CommentModel> mComments) {
		this.mComments = mComments;
		this.notifyViews();
	}

	public void refreshAddAll(ArrayList<CommentModel> newTopLevel) {
		this.mComments.removeAll(mComments);
		this.mComments.addAll(newTopLevel);
		Log.w("refreshAddAll" , Integer.valueOf(mComments.size()).toString());
		this.notifyViews();
		this.mCache.replaceHistory(mComments);
	}
}
