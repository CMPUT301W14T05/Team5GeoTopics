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
		this.mIn = new ArrayList<CommentModel>();
		this.mOut = new ArrayList<CommentModel>();
	}

}
