package ca.ualberta.cs.team5geotopics;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.location.Location;

public class TopLevelModel extends CommentModel {
	private  ArrayList<ReplyLevelModel> mReplies;
	private String mTitle;

	public TopLevelModel(Location mGeolocation, String mBody, String mTitle,
			String mAuthor, Bitmap mPicture) {
		super(mGeolocation, mBody, mAuthor, mPicture);
		this.mTitle = mTitle;
		super.putTimeStamp();
		this.mReplies = new ArrayList<ReplyLevelModel>();
	}

	public ArrayList<ReplyLevelModel> getReplies(){
		return this.mReplies;
	}
	
	public boolean hasReply(){
		if(mReplies.size() >= 1){
			return true;
		}
		return false;
	}
	
	public void addReply(ReplyLevelModel reply){
		if(!this.mReplies.contains(reply)){
			this.mReplies.add(reply);
		}
	}
	
	public void removeReply(ReplyLevelModel reply){
		if(this.mReplies.contains(reply)){
			this.mReplies.remove(reply);
		}
	}
	
	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}
	
	public String getmTitle() {
		return mTitle;
	}

}
