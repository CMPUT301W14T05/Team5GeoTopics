package ca.ualberta.cs.team5geotopics;

import java.util.ArrayList;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class CommentManager extends AModel<AView> {
	private static CommentManager myself = null;
	private Cache mCache;
	private Context mContext;
	private User mUser;

	private CommentManager() {
		this.mCache = Cache.getInstance();
		this.mContext = GeoTopicsApplication.getInstance().getContext();
		this.mUser = User.getInstance();
	}

	public static CommentManager getInstance() {
		if (myself == null) {
			Log.w("Refresh", "Making new COmment manager");
			myself = new CommentManager();
		}
		return myself;
	}

	/**
	 * Returns status about the network availablilty.
	 * 
	 * @author Alexandre Jasmin Link:
	 *         http://stackoverflow.com/questions/4238921/
	 *         android-detect-whether-there-is-an-internet-connection-available
	 * @param context
	 *            An application context.
	 * @return True is the network is available, false if not.
	 */
	public boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) this.mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return (activeNetworkInfo != null && activeNetworkInfo.isConnected());
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
		if (isNetworkAvailable()) {

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
	 * Refreshes a comment list model with a list of the users authored comments.
	 * @param clm The clm to refresh.
	 */
	public void refreshMyComments(CommentListModel clm){
		ArrayList<CommentModel> temp = this.getMyComments();
		clm.addNew(temp);
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
	public CommentModel getMyComment(String ID) {
		String parentID = mUser.breakParentID(ID);
		String commentID = mUser.breakID(ID);
		Log.w("MyComments", parentID);
		Log.w("MyComments", commentID);
		return getComment(parentID, commentID);
	}
	
	/**
	 * Returns a list of comment models representing all the comments the user
	 * authored.
	 * @return array list of comment models
	 */
	public ArrayList<CommentModel> getMyComments(){
		ArrayList<String> commentIDs = mUser.getMyComments();
		ArrayList<CommentModel> mComments = new ArrayList<CommentModel>();
		
		for(String ID : commentIDs){
			mComments.add(this.getMyComment(ID));
		}
		return mComments;
	}
	
	/**
	 * Handles updating a comment. This involves pushing the comment
	 * to the Internet AND updating the cache.
	 * @param comment The updated comment
	 */
	public void updateComment(CommentModel comment){
		CommentPush pusher = new CommentPush();
		if(comment.isTopLevel()) {
			pusher.pushComment(comment, "TopLevel");
		}else{
			pusher.pushComment(comment, "ReplyLevel");
		}
		
		mCache.updateCache(comment);
		mUser.saveMyComments();
	}
	
	/**
	 * Handles adding a new reply. This involves pushing it to the Internet 
	 * and into the cache.
	 * 
	 * @param comment The new replys
	 * @return The thread the push is running on
	 */
	public Thread newReply(CommentModel comment){
		Thread thread;
		CommentPush pusher = new CommentPush();
		
		thread = pusher.pushComment(comment, "ReplyLevel");

		mCache.updateCache(comment);
		
		return thread;
	}
	
	/**
	 * Handles adding a new Top level. This involves pushing it to the Internet 
	 * and into the cache.
	 * 
	 * @param comment The new reply
	 * @return The thread the push is running on
	 */
	public Thread newTopLevel(CommentModel comment){
		Thread thread;
		CommentPush pusher = new CommentPush();
		
		thread = pusher.pushComment(comment, "TopLevel");

		mCache.updateCache(comment);
		
		return thread;
	}
	
}
