package ca.ualberta.cs.team5geotopics;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class CommentManager extends AModel<AView> {
	private static CommentManager myself = null;
	private Cache mCache;
	private Context mContext;
	
	private CommentManager(){
		this.mCache = Cache.getInstance();
		this.mContext = GeoTopicsApplication.getInstance().getContext();
	}
	
	public static CommentManager getInstance(){
		if(myself == null){
			Log.w("Refresh", "Making new COmment manager");
			myself = new CommentManager();
		}
		return myself;
	}
	
	/**
	 * Returns status about the network availablilty.
	 *
	 *@author Alexandre Jasmin
	 *Link: http://stackoverflow.com/questions/4238921/android-detect-whether-there-is-an-internet-connection-available
	 *@param context An application context.
	 * @return  True is the network is available, false if not.
	 */
	public boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) this.mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return (activeNetworkInfo != null && activeNetworkInfo.isConnected());
	}
	
	/**
	 * Handles loading comments for the view into its comment list model. Attempts to load from the web else
	 * it loads from the cache.
	 *
	 */
	public void refresh(CommentListModel clm, BrowseActivity mActivity, CommentModel viewingComment){
		String EsID;
		if(viewingComment == null){
			EsID = null;
		}else{
			EsID = viewingComment.getmEsID();
		}
		CommentSearch modelController = new CommentSearch(clm);
		if (isNetworkAvailable()) {
			
			if(mActivity.getType().equals("TopLevel")){
				Log.w("Refresh", "Top Level");
				modelController.pullTopLevel(mActivity);
			}
			else{
				Log.w("Refresh", "Reply");
				modelController.pullReplies(mActivity, EsID);
			}
			Log.w("Cache", "Have Internet");
		} else {
			Log.w("Cache", "No Internet");
			mCache.loadFileList(); //gets record of cache from previous session
			mCache.registerModel(clm);
			
			if (mActivity.getType().equals("TopLevel")) {
				mCache.loadFromCache("history.sav", mActivity);
			}
			else{
				String filename = EsID;
				if (mCache.repliesExist(filename)){
					mCache.loadFromCache(filename, mActivity);
					Log.w("Cache", "load replies from cache");
				}
				else{
					Toast toast = Toast.makeText(mActivity,"Unable to load from the cache, Please try again with internet",5);
					toast.show();
				}
			}
		}
	}
	
	
	
	
}
