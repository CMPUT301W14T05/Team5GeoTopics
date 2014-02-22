package ca.ualberta.cs.team5geotopics;

public class QueueController {
	private QueueModel mTopLvlQueue;
	private QueueModel mReplyLvlQueue;
	
	public QueueController(){
		super();
		this.mTopLvlQueue = new QueueModel();
		this.mReplyLvlQueue = new QueueModel();
	}
	
	/*
	 * have some thread that pulls comments from web service and
	 * puts them in appropriate list in the appropriate QueueModel.
	 * 
	 * example: BrowseTopLevelView is launched
	 * 			TopLevelComments are pulled from WebService 
	 * 			and then they are added to the mTopLvlQueue.mIn
	 * 
	 * 			When all CommentModels are pulled then sorted, then the Views 
	 * 			should be notified.
	 */
}
