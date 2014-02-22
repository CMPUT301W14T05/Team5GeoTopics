package ca.ualberta.cs.team5geotopics;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.location.Location;

public class TopLevelModel extends CommentModel {
	private  ArrayList<ReplyLevelModel> mReplies;
	
	public TopLevelModel(Location mGeolocation, String mBody, String mTitle,
			String mAuthor, Bitmap mPicture) {
		super(mGeolocation, mBody, mTitle, mAuthor, mPicture);
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
}
