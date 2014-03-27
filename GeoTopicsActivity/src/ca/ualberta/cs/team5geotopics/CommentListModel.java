package ca.ualberta.cs.team5geotopics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.location.Location;
import android.util.Log;

/**
 * CommentListModel holds the sorting data and the functions to add a comment
 * within the list. This list can then be cleared as needed and comments can be
 * updated.
 */

public class CommentListModel extends AModel<AView> {
	private ArrayList<CommentModel> mComments;
	private Cache mCache;
	private int sortFlag = 2;
	protected User myUser;

	/**
	 * Constructor
	 * 
	 * @return A comment list model
	 */
	public CommentListModel() {
		this.mComments = new ArrayList<CommentModel>();
		this.mCache = Cache.getInstance();
		this.myUser = User.getInstance();
	}

	/**
	 * Returns The list of comments in the CLM.
	 * 
	 * @return List of comments
	 */
	public ArrayList<CommentModel> getList() {
		return this.mComments;
	}

	/**
	 * Add a new comment to the list. This notifies and views registered with
	 * the model.
	 * 
	 * @param comment	The comment to add to the list.
	 */
	public void add(CommentModel comment) {
		mComments.add(comment);
		sortOnUpdate();
		this.notifyViews();
	}

	/**
	 * Clears the list of comments in the CLM.
	 *
	 */
	public void clearList() {
		this.mComments.clear();
	}

	/**
	 * Sets the sort flag for this CLM. This defines how the CLM will
	 * be sorted by default.
	 *
	 * @param  sortFlag  The sort flag to set
	 */
	public void setSortFlag(int sortFlag) {
		this.sortFlag = sortFlag;
	}

	/**
	 * Uses the sort flag to determine how to sort the list each
	 * time it is updated. 
	 *
	 */
	public void sortOnUpdate() {
		/*
		 * Since we do not have location functionality working right now (its
		 * part of our part 4 release plan) I will set a single static location
		 * that will be used for all the sorts that need a location
		 * SortByProximityToMe SortByProximityToLoc SortByFreshness
		 */
		Location myLoc = myUser.getCurrentLocation();
		Location defLoc = new Location("myLoc");
		defLoc.setLongitude(0);
		defLoc.setLatitude(0);
		switch (sortFlag) {
		case 0:
			sortCommentsByProximityToLoc(myLoc);
			break;
		case 1:
			sortCommentsByProximityToLoc(defLoc);
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

	
	/**
	 * Sort the comments by proximity to a location that we supply.
	 *
	 * @param  myLoc  The location to sort by proximity to.
	 */
	public void sortCommentsByProximityToLoc(Location myLoc) {
		sortCommentsByProximity(mComments, myLoc);
		// this.notifyViews();
	}

	public static ArrayList<CommentModel> sortCommentsByProximity(
			final ArrayList<CommentModel> cList, final Location myLoc) {
		Collections.sort(cList, new Comparator<CommentModel>() {
			public int compare(CommentModel a, CommentModel b) {
				return (int) (a.getGeoLocation().distanceTo(myLoc) - b
						.getGeoLocation().distanceTo(myLoc));
			}
		});
		return cList;
	}

	/**
	 * Sorts comments by freshness. 
	 *
	 * @param  myLoc  Location to use for the sort.
	 */
	public void sortCommentsByFreshness(Location myLoc) {
		/*
		 * This should remove any comment from the list that is further than 1
		 * km away
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
			mComments.get(i).setSortWeight(
					mComments.get(i).getSortWeight() + weightPoint);
			weightPoint -= 1;
		}
		sortCommentsBySortWeight(mComments);
	}

	/**
	 * Sort comments by their internal sort weight. 
	 *
	 * @param  cList	Comment list
	 */
	public void sortCommentsBySortWeight(final ArrayList<CommentModel> cList) {
		Collections.sort(cList, new Comparator<CommentModel>() {
			public int compare(CommentModel a, CommentModel b) {
				return (int) (b.getSortWeight() - a.getSortWeight());
			}
		});
	}

	/*
	 * Handles Used Case 3: SortCommentsByPicture
	 * 
	 * take in the array of current TopLevelComments and splits it up into list
	 * of replies containing photos, and a reply list with no photos. Sorts them
	 * both by proximity and then adds them to the comment list again.
	 * 
	 * NOTE: As of right now does NOT consider location
	 */
	/**
	 * Sorts comments by picture and location. Comments with pictures appear
	 * at the top sorted by proximity and the same with the ones with
	 * not pictures after. 
	 *
	 * @param  loc  Location for proximity sorting
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

		// this.notifyViews();
	}
	
	/**
	 * Sorts all comments in the CLM by their date.
	 *
	 */
	public void sortAllCommentsByDate() {
		mComments = sortCommentsByDate(mComments);
		// this.notifyViews();
	}

	/**
	 * Sorts Comments by date.
	 *
	 * @param  cList  The list of comments to sort
	 * @return     An array of sorted comments
	 */
	public ArrayList<CommentModel> sortCommentsByDate(
			final ArrayList<CommentModel> cList) {
		/*
		 * This should sort the comment list based on date
		 */
		Collections.sort(cList, new Comparator<CommentModel>() {
			public int compare(CommentModel a, CommentModel b) {
				return (int) (b.getDate().getTime() - a.getDate().getTime());
			}
		});
		return cList;
	}

	/**
	 * Sets the comment list model to a specific list of comments 
	 *
	 * @param  mComments A list of comments.
	 */
	public void setList(ArrayList<CommentModel> mComments) {
		this.mComments.clear();
		this.mComments.addAll(mComments);
		//sortOnUpdate();
		this.notifyViews();
	}

	/**
	 * Add new comments to the list. Will do a check to ensure the comment
	 * does not already exist in the list. IF it doest exist it gets replaced by the
	 * version in new comments
	 *
	 * @param  newComments  A list of comments
	 */
	public void addNew(ArrayList<CommentModel> newComments) {
		int i;
		boolean inList;
		try {
			newComments.size();
		} catch (NullPointerException e) {
			return;
		}

		for (CommentModel inComment : newComments) {
			inList = false;
			i = 0;
			for (CommentModel listComment : this.mComments) {
				if (listComment.getmEsID().equals(inComment.getmEsID())) {
					inList = true;
					break;
				}
				i++;
			}

			if (!inList) {
				mComments.add(inComment);
			} else {
				mComments.set(i, inComment);
			}
		}

		Log.w("Cache", Integer.valueOf(mComments.size()).toString());
		//sortOnUpdate();
		this.notifyViews();
	}
	
}
