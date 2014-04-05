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
	protected User myUser;
	protected CommentSort commentSorter;

	/**
	 * Constructor
	 * 
	 * @return A comment list model
	 */
	public CommentListModel() {
		this.mComments = new ArrayList<CommentModel>();
		this.myUser = User.getInstance();
		this.commentSorter = new CommentSort(this.mComments);
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
		this.commentSorter.sortOnUpdate();
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

	/**
	 * Sets the comment list model to a specific list of comments 
	 *
	 * @param  mComments A list of comments.
	 */
	public void setList(ArrayList<CommentModel> mComments) {
		this.mComments.clear();
		this.mComments.addAll(mComments);
		this.commentSorter.sortOnUpdate();
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
		this.commentSorter.sortOnUpdate();
		this.notifyViews();
	}

	public Location getCustomSortLoc() {
		return this.commentSorter.getCustomSortLoc();
	}

	public void setCustomSortLoc(Location customSortLoc) {
		this.commentSorter.setCustomSortLoc(customSortLoc);
	}
	
	public void sortOnUpdate(){
		this.commentSorter.sortOnUpdate();
		this.notifyViews();
	}
	
	public void setSortFlag(int sortFlag) {
		this.commentSorter.setSortFlag(sortFlag);
	}
	
}
