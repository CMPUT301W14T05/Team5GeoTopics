package ca.ualberta.cs.team5geotopics;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

public class CommentManager extends AModel<AView> {
	private CommentRetriever commentRetriever = new CommentRetriever();
	private static CommentManager myself = null;
	private Cache mCache;
	private Context mContext;
	private GeoTopicsApplication mApp;
	private BroadcastReceiver webConnectionReceiver;
	private ArrayList<CommentModel> commentStash;
	private User myUser;

	private CommentManager() {
		this.mCache = Cache.getInstance();
		this.mContext = GeoTopicsApplication.getInstance().getContext();
		commentRetriever.setMUser(User.getInstance());
		this.mApp = GeoTopicsApplication.getInstance();
		this.myUser = User.getInstance();
		this.commentStash = new ArrayList<CommentModel>();
		webConnectionReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (mApp.isNetworkAvailable()) {
					Log.w("Connectivity", "Have network");
					pushStashedComments();
				}
			}
		};
		mApp.getContext().registerReceiver(webConnectionReceiver,
				new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
	}

	public static CommentManager getInstance() {
		if (myself == null) {
			Log.w("Refresh", "Making new Comment manager");
			myself = new CommentManager();
		}
		return myself;
	}

	/**
	 * Handles loading comments for the view into its comment list model.
	 * Attempts to load from the web else it loads from the cache.
	 * 
	 */
	public void refresh(CommentListModel clm, BrowseActivity mActivity,
			CommentModel viewingComment) {
		String EsID;
		if (viewingComment == null) {
			EsID = "-1";
		} else {
			EsID = viewingComment.getmEsID();
		}
		CommentSearch modelController = new CommentSearch(clm);
		if (mApp.isNetworkAvailable()) {
			Log.w("Cache", "Internet before test");
			if (mActivity.getType().equals("TopLevel")) {
				Log.w("Refresh", "Top Level");
				modelController.pullTopLevel(mActivity);
			} else {
				Log.w("Refresh", "Reply");
				modelController.pullReplies(mActivity, EsID);
			}
			Log.w("Cache", "Have Internet");
		} else {
			Log.w("Cache", "No Internet");
			mCache.loadFileList(); // gets record of cache from previous session

			if (mCache.repliesExist(EsID)) {
				mCache.loadFromCache(EsID, clm);
				Log.w("Cache", "load replies from cache");
			} else {
				Toast toast = Toast
						.makeText(
								mActivity,
								"Unable to load from the cache, Please try again with internet",
								5);
				toast.show();
			}
		}
	}

	/**
	 * Refreshes a comment list model with a list of the users authored
	 * comments.
	 * 
	 * @param clm
	 *            The clm to refresh.
	 */
	public void refreshMyComments(CommentListModel clm) {
		ArrayList<CommentModel> temp = commentRetriever.getMyComments(this);
		clm.addNew(temp);
	}

	/**
	 * Refreshes a comment list model with a list of the users bookmarked
	 * comments.
	 * 
	 * @param clm
	 *            The clm to refresh.
	 */
	public void refreshMyBookmarks(CommentListModel clm) {
		ArrayList<CommentModel> temp = commentRetriever.getMyBookmarks(this);
		clm.setList(temp);
	}

	/**
	 * Refreshes a comment list model with a list of the users bookmarked
	 * comments.
	 * 
	 * @param clm
	 *            The clm to refresh.
	 */
	public void refreshMyFavourites(CommentListModel clm) {
		ArrayList<CommentModel> temp = commentRetriever.getMyFavourites(this);
		clm.setList(temp);
	}

	/**
	 * Retrieves a single comment from the cache.
	 * 
	 * @param mParentID
	 *            The parent ID of the comment we want
	 * @param EsID
	 *            The ID of the comment we want
	 * @return The comment if it exists otherwise expect null.
	 */
	public CommentModel getComment(String mParentID, String EsID) {
		return mCache.loadComment(mParentID, EsID);
	}

	/**
	 * Used to retrieve a comment from the myComments array in the user class.
	 * Assumes that you somehow know the comment already exists in the array. If
	 * it doesn't it returns null and you will get null pointer exceptions if
	 * you do not account for this.
	 * 
	 * @param EsID
	 *            The ID of the comment we want
	 * @return The comment OR null if not found.
	 */
	public CommentModel getCommentByComboID(String ID) {
		return commentRetriever.getCommentByComboID(ID, this);
	}

	/**
	 * Handles updating a comment. This involves pushing the comment to the
	 * Internet AND updating the cache.
	 * 
	 * @param comment
	 *            The updated comment
	 */
	public void updateComment(CommentModel comment) {
		CommentPush pusher = new CommentPush();
		if (mApp.isNetworkAvailable()) {
			if (comment.isTopLevel()) {
				pusher.pushComment(comment, "TopLevel");
			} else {
				pusher.pushComment(comment, "ReplyLevel");
			}
		} else {
			Log.w("Connectivity", "Stashed a comment update");
			this.commentStash.add(comment);
		}
		mCache.updateCache(comment);
		// mUser.saveMyComments();
	}

	/**
	 * Handles adding a new reply. This involves pushing it to the Internet and
	 * into the cache.
	 * 
	 * @param comment
	 *            The new replys
	 * @return The thread the push is running on
	 */
	public Thread newReply(CommentModel comment) {
		Thread thread = null;
		CommentPush pusher = new CommentPush();
		if (mApp.isNetworkAvailable()) {
			thread = pusher.pushComment(comment, "ReplyLevel");
		} else {
			Log.w("Connectivity", "Stashed a new reply");
			this.commentStash.add(comment);
		}
		mCache.updateCache(comment);

		return thread;
	}

	/**
	 * Handles adding a new Top level. This involves pushing it to the Internet
	 * and into the cache.
	 * 
	 * @param comment
	 *            The new reply
	 * @return The thread the push is running on
	 */
	public Thread newTopLevel(CommentModel comment) {
		Thread thread = null;
		CommentPush pusher = new CommentPush();
		if (mApp.isNetworkAvailable()) {
			thread = pusher.pushComment(comment, "TopLevel");
		} else {
			Log.w("Connectivity", "Stashed a new top levels");
			this.commentStash.add(comment);
		}
		mCache.updateCache(comment);
		myUser.addToMyComments(comment);

		return thread;
	}

	/**
	 * Attempts to push all the comments we have stashed. Will make sure 
	 * network is available on each push. If it is not then it gets re-stashed
	 * waiting for a later push. Comments will be pushed in the same order
	 * they were put in (fifo). If they have to be re-stashed they will be 
	 * stashed in the same order they were originally put in. 
	 */
	private void pushStashedComments() {
		ArrayList<CommentModel> temp = new ArrayList<CommentModel>();
		Log.w("Connectivity", "Stash Size: " + Integer.toString(commentStash.size()));
		temp.addAll(commentStash);
		commentStash.clear();

		for (CommentModel comment : temp) {
			CommentPush pusher = new CommentPush();
			if (mApp.isNetworkAvailable()) {
				if (comment.isTopLevel()) {
					Log.w("Connectivity", "Pushed Stashed Top Level");
					pusher.pushComment(comment, "TopLevel");
				} else {
					Log.w("Connectivity", "Pushed Stashed Reply Level");
					pusher.pushComment(comment, "ReplyLevel");
				}
			} else {
				Log.w("Connectivity", "Re stashed a comment");
				this.commentStash.add(comment);
			}
		}

	}

	/**
	 * Creates a new reply comment. The comment is pushed to the web and added to the local my comments list.
	 * @param newComment   the new reply level comment
	 * @param context 	An activity context
	 * @param myUser the user profile to add the newly created reply comment to.
	 */
	public void newReply(CommentModel newComment, Context context, User myUser) {
		myUser.addToMyComments(newComment);
		newReply(newComment);
		Log.w("CommentController", "id: " + newComment.getmEsID() + "\n"
				+ "type: " + newComment.getmEsType());
	}

}
