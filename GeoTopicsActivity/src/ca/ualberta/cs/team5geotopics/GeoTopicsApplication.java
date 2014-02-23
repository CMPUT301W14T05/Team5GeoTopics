package ca.ualberta.cs.team5geotopics;

import java.util.List;

import android.content.Context;

//https://github.com/abramhindle/FillerCreepForAndroid/blob/master/src/es/softwareprocess/fillercreep/FillerCreepApplication.java
//josh said not to extend Application

/*
 * this is a collection of singletons
 */
public class GeoTopicsApplication {
	// with this true then the a list of test comments is loaded
	// into the QueueController.mTopLevel.mIn list.
	// that is we can test to see if the comments pushed into this
	// list are loaded onto the ListView for the BrowseTopLevelView
	transient private static final boolean BROWSE_TOP_LEVEL_TEST = true; 
	static boolean BrowseTopLevelNoInternetTest(){
		return BROWSE_TOP_LEVEL_TEST;
	}
	
	transient private static QueueController mQueueController = null;
	static QueueController getQueueController() {
        if (mQueueController == null) {
        	mQueueController = new QueueController();
        }
        return mQueueController;
    }
	
	transient private static Cache mCache = null;
	static Cache getCache(){
		if (mCache == null) {
			mCache = new Cache();
        }
        return mCache;
	}
	
	transient private static BrowseCommentController  mBrowseTopLevelController = null;
	static BrowseCommentController getTopLevelController(Context context, 
									int layoutResourceId, List<CommentModel> comments){
		if (mBrowseTopLevelController == null) {
			mBrowseTopLevelController = new BrowseCommentController(context, layoutResourceId, comments);
        }
        return mBrowseTopLevelController;
	}
	
	transient private static BrowseCommentController  mBrowseReplyController = null;
	static BrowseCommentController getReplyLevelController(Context context, 
									int layoutResourceId, List<CommentModel> comments){
		if (mBrowseReplyController == null) {
			mBrowseReplyController = new BrowseCommentController(context, layoutResourceId, comments);
        }
        return mBrowseReplyController;
	}
	
	transient private static BrowseCommentController  mBrowseCacheController = null;
	static BrowseCommentController getCacheController(Context context, 
									int layoutResourceId, List<CommentModel> comments){
		if (mBrowseCacheController == null) {
			mBrowseCacheController = new BrowseCommentController(context, layoutResourceId, comments);
        }
        return mBrowseCacheController;
	}
	
	
	
}
