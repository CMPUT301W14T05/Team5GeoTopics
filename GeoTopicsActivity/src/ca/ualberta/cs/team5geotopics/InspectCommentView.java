package ca.ualberta.cs.team5geotopics;

import android.app.Activity;

public abstract class InspectCommentView extends Activity implements AView<Cache>{
	CommentModel mComment;

	public CommentModel getmComment() {
		return mComment;
	}

	public void setmComment(CommentModel mComment) {
		this.mComment = mComment;
	}

	public void initmComment(){
		this.mComment = new CommentModel(null, null, null, null);
	}

	
	
}
