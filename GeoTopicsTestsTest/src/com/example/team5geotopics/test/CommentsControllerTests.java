package com.example.team5geotopics.test;

import java.util.ArrayList;

import android.test.ActivityInstrumentationTestCase2;

/* 
 * contains the test code for all methods in the CommentsController class
 * 
 * NOTE: this is currently skeleton test code - please add to it
 */
public class CommentsControllerTests extends
		ActivityInstrumentationTestCase2<CommentsController> {

	public CommentsControllerTests() {
		super(CommentsController.class);
	}

	/*
	 * Use Case 5: MakeNewTopLevelComment Test
	 */
	public void MakeNewTopLevelCommentTest() {
		/* 
		 * not exactly sure how this should work but I figurer
		 * a draft test can help us in the end - even if it isn't
		 * runnable.
		 */
		CommentsController cC = new CommentsController();
		
		/*
		 * create the new top level comment by calling MakeNewTopLevelComment
		 * and then BrowseTopLevelComments to ensure it's there
		 * 
		 * NOTE (1): picture has been ommitted since user does not always need to specify a pic.
		 * we'll have to have a constructor to handle this
		 * 
		 * NOTE (2): should MakeNewTopLevelComment return an I.D. of some sort so the we can check for it?
		 */
		String text = "This is a top level comment!";
		MakeNewTopLevelComment(text);

		/*
		 * Now retrieve the top level comments
		 */
		ArrayList<TopLevelComment> tlc = BrowseTopLevelComments();
		
		/*
		 * Right now I am checking for same text but once we have an ID method we will have to check
		 * for the id instead as it will be unique
		 */
		boolean success = false;
		for (int i = 0; i < tlc.getSize(); i++) {
			if (tlc.get(i).getText.equals(text)) {
				success = true;
			}
		}
	
		assertTrue(success);
	}
	
	/*
	 * Use Case 6: BrowseTopLevelComments Test
	 */
	public void BrowseTopLevelCommentsTest() {
		
	}
	
	/*
	 * Use Case 7: BrowseCommentReplies Test
	 */
	public void BrowseCommentRepliesTest() {
		
	}
	
	/*
	 * Use Case 8: ReplyToComment Test
	 */
	public void ReplyToCommentTest() {
		
	}
	
	/*
	 * Use Case 9: AddPictureToComment Test
	 */
	public void AddPictureToCommentTest() {
		
	}
	
	/*
	 * Use Case 12: SaveComentAsFavoritees Test
	 */
	public void SaveCommentAsFavoritTest() {
		
	}

	/*
	 * Use Case 14: EditAuthoredComment Test
	 */
	public void EditAuthoredCommentTest() {
		
	}
	
	/*
	 * Use Case 16: GetLatestComments Test
	 */
	public void GetLatestComments() {
		
	}
	
}
