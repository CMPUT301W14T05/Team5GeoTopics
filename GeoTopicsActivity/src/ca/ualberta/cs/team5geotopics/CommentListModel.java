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
	private int sortFlag = 2;
	
	public CommentListModel(){
		this.mComments = new ArrayList<CommentModel>();	
		this.mCache = Cache.getInstance();
	}
	
	public ArrayList<CommentModel> getList() {
		return this.mComments;
	}
	
	public void add(CommentModel comment) {
		mComments.add(comment);
		sortOnUpdate();
		//this.notifyViews();
	}
	
	public void clearList(){
		this.mComments.clear();
	}
	
	public void setSortFlag(int sortFlag) {
		this.sortFlag = sortFlag;
	}
	
	public void sortOnUpdate() {
		/*
		 * Since we do not have location functionality working right now
		 * (its part of our part 4 release plan) I will set a single static
		 * location that will be used for all the sorts that need a location
		 * SortByProximityToMe
		 * SortByProximityToLoc
		 * SortByFreshness
		 */
		Location myLoc = new Location("myLoc");
		myLoc.setLongitude(0);
		myLoc.setLatitude(0);
		
		switch (sortFlag) {
		case 0:
			sortCommentsByProximityToLoc(myLoc);
			break;
		case 1:
			sortCommentsByProximityToLoc(myLoc);
			break;
		case 2:
			sortCommentsByFreshness(myLoc);
			break;
		case 3:
			sortCommentsByPicture(myLoc);
			break;
		case 4:
			// as of right now only sorts by date
			sortCommentsByDate(mComments);
			break;
		default:
			break;
		}
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
		//this.notifyViews();
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
		sortCommentsByProximityToLoc(myLoc);
		int weightPoint = mComments.size();
		for (int i = 0; i < mComments.size(); i++) {
			mComments.get(i).setSortWeight(weightPoint);
			weightPoint -= 1;
		}
		sortCommentsByDate(mComments);
		weightPoint = mComments.size();
		for (int i = 0; i < mComments.size(); i++) {
			mComments.get(i).setSortWeight(mComments.get(i).getSortWeight() + weightPoint);
			weightPoint -= 1;
		}
		sortCommentsBySortWeight(mComments);
//		for (int i = mComments.size() - 1; i >= 0; i--) {
//			if (mComments.get(i).getGeoLocation().distanceTo(myLoc) > 1000) {
//				mComments.remove(i);
//			}
//		}
//    	mComments = sortCommentsByDate(mComments);
    	
		//this.notifyViews();
	}
	
	public void sortCommentsBySortWeight(final ArrayList<CommentModel> cList) {
		Collections.sort(cList, new Comparator<CommentModel>() {
			public int compare(CommentModel a, CommentModel b) {
				return (int) (b.getSortWeight() - 
						a.getSortWeight());
			}
		});
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
		
		//this.notifyViews();
	}
	
	public void sortAllCommentsByDate() {
		mComments = sortCommentsByDate(mComments);
		//this.notifyViews();
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
		sortOnUpdate();
		//this.notifyViews();
	}

	public void addNew(ArrayList<CommentModel> newComments) {
		ArrayList<CommentModel> filteredComments = new ArrayList<CommentModel>();
		boolean inList = false;
		try{
			newComments.size();
		}
		catch (NullPointerException e){
			return;
		}
		
		for(CommentModel inComment : newComments){
			for (CommentModel listComment : this.mComments){
				if (listComment.getmEsID().equals(inComment.getmEsID())){
					inList = true;
					break;
				}
			}
			
			if(inList == false){
				filteredComments.add(inComment);
			}
			else{
				inList = false;
			}
		}
		if( filteredComments.size() > 0){
			this.mComments.addAll(filteredComments);
		}
		
		Log.w("addNew" , Integer.valueOf(mComments.size()).toString());
		sortOnUpdate();

	}
	//Replaces a comment in the list if it exists.
	public void updateComment(CommentModel updatedComment){
		String id =updatedComment.getmEsID();
		int count = 0;
		for(CommentModel comment : mComments){
			if(id.equals(comment.getmEsID())){
				Log.w("CLM", "Found a match");
				mComments.set(count, updatedComment);
				return;
			}
			count++;
		}
	}

}
