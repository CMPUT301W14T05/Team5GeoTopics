package ca.ualberta.cs.team5geotopics;

import android.app.Application;

//https://github.com/abramhindle/FillerCreepForAndroid/blob/master/src/es/softwareprocess/fillercreep/FillerCreepApplication.java
//josh said not to extend Application

/*
 * this is a collection of singletons
 */
public class GeoTopicsApplication extends Application{
	// with this true then the a list of test comments is loaded
	// into the QueueController.mTopLevel.mIn list.
	// that is we can test to see if the comments pushed into this
	// list are loaded onto the ListView for the BrowseTopLevelView
	transient private static final boolean BROWSE_TOP_LEVEL_TEST = true; 
	static boolean BrowseTopLevelNoInternetTest(){
		return BROWSE_TOP_LEVEL_TEST;
	}
	
	
	transient private static Cache mCache = null;
	static Cache getCache(){
		if (mCache == null) {
			mCache = new Cache();
        }
        return mCache;
	}		
}
