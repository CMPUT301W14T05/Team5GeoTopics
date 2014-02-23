package ca.ualberta.cs.team5geotopics;

import java.util.ArrayList;

/*
 * This is a model of a queue of CommentModels that are
 * being uploaded to or downloaded from the WebService.
 */
public class QueueModel extends AModel<AView> {
	private ArrayList<CommentModel> mIn;
	private ArrayList<CommentModel> mOut;
	
	public QueueModel(){
		super();
		this.mIn = new ArrayList<CommentModel>();
		this.mOut = new ArrayList<CommentModel>();
	}

	public void addToIn(CommentModel comment){
		if(!mIn.contains(comment)){
			mIn.add(comment);
		}
		// notify the views that we have new comments
		this.notifyViews();
	}
	
	public void addToOut(CommentModel comment){
		if(!mOut.contains(comment)){
			mOut.add(comment);
		}
	}
	
	public void removeFromIn(CommentModel comment){
		if(mIn.contains(comment)){
			mIn.remove(comment);
		}
	}
	
	public void removeFromOut(CommentModel comment){
		if(mOut.contains(comment)){
			mOut.remove(comment);
		}
	}
	
	public ArrayList<CommentModel> getIn(){
		return mIn;
	}
	
	public ArrayList<CommentModel> getOut(){
		return mOut;
	}

	public void testInit() {
		for(int i = 0; i < 3; i++){
			// load up three TopLevelComments with null pictures and null location
			// tests only the text fields title, body, author.
			// TODO: set a constant date and check comment time in the BrowseTopLevelTests
			mIn.add(new TopLevelModel(null, "body" + Integer.valueOf(i + 1).toString(),
												"title" + Integer.valueOf(i + 1).toString(), 
												"author" + Integer.valueOf(i + 1).toString(),
												null));
		
		}
	}
}
