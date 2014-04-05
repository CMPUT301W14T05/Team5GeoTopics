package ca.ualberta.cs.team5geotopics;


import java.util.ArrayList;
import android.util.Log;

public class CommentRetriever {
	private User mUser;

	public User getMUser() {
		return mUser;
	}

	public void setMUser(User mUser) {
		this.mUser = mUser;
	}

	/**
	 * Returns a list of comment models representing all the comments the user has bookmarked.
	 * @return  array list of comment models
	 */
	public ArrayList<CommentModel> getMyBookmarks(CommentManager commentManager) {
		ArrayList<String> commentIDs = mUser.getMyBookmarks();
		ArrayList<CommentModel> mComments = new ArrayList<CommentModel>();
		for (String ID : commentIDs) {
			mComments.add(this.getCommentByComboID(ID, commentManager));
		}
		return mComments;
	}

	/**
	 * Returns a list of comment models representing all the comments the user has favourited.
	 * @return  array list of comment models
	 */
	public ArrayList<CommentModel> getMyFavourites(CommentManager commentManager) {
		ArrayList<String> commentIDs = mUser.getMyFavourites();
		ArrayList<CommentModel> mComments = new ArrayList<CommentModel>();
		for (String ID : commentIDs) {
			mComments.add(this.getCommentByComboID(ID, commentManager));
		}
		return mComments;
	}

	/**
	 * Returns a list of comment models representing all the comments the user authored.
	 * @return  array list of comment models
	 */
	public ArrayList<CommentModel> getMyComments(CommentManager commentManager) {
		CommentModel tempComment;
		ArrayList<String> commentIDs = mUser.getMyComments();
		Log.w("MyComments", Integer.toString(commentIDs.size()));
		for (String comment : commentIDs) {
			Log.w("MyComments", comment);
		}
		ArrayList<CommentModel> mComments = new ArrayList<CommentModel>();
		for (String ID : commentIDs) {
			tempComment = this.getCommentByComboID(ID, commentManager);
			if (tempComment != null) {
				mComments.add(tempComment);
			}
		}
		for (CommentModel comment : mComments) {
			Log.w("MyComments", comment.getmBody());
		}
		Log.w("MyComments", Integer.toString(mComments.size()));
		return mComments;
	}

	/**
	 * Used to retrieve a comment from the myComments array in the user class. Assumes that you somehow know the comment already exists in the array. If it doesn't it returns null and you will get null pointer exceptions if you do not account for this.
	 * @param EsID The ID of the comment we want
	 * @return  The comment OR null if not found.
	 */
	public CommentModel getCommentByComboID(String ID,
			CommentManager commentManager) {
		String parentID = mUser.breakParentID(ID);
		String commentID = mUser.breakID(ID);
		return commentManager.getComment(parentID, commentID);
	}
}