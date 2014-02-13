package ca.ualberta.cs.team5geotopics.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.w3c.dom.Comment;

import android.graphics.Picture;
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
		CommentsController cC = new CommentsController();
		ArrayList<Comment> Comments = new ArrayList<Comment>();
		Comment c = new Comment("Test.");
		Picture pic1 = new Picture(cat.bmp);
		
		AddPictureToComment(c, pic1);
		Comments.add(c);
		
		assertEquals("Returns true is comment has"
		+ " a picture", true, Comments.get(1).hasPicture());
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
		/* 
		 * not exactly sure how this should work but I figurer
		 * a draft test can help us in the end - even if it isn't
		 * runnable.
		 * 
		 * NOTE: this should maybe test last edit date instead of just testing the text
		 * 	although this still checks that the comment was actually edited
		 */
		CommentsController cC = new CommentsController();
		
		/*
		 * create the new top level comment by calling MakeNewTopLevelComment
		 * and then BrowseTopLevelComments to retrieve it and change it
		 * 
		 * NOTE (1): perhaps we need to have some sort of search by id
		 * 
		 * 
		 * NOTE (2): should MakeNewTopLevelComment return an I.D. of some sort so the we can check for it?
		 */
		String text = "This is a top level comment!";
		MakeNewTopLevelComment(text);

		/*
		 * Now retrieve the top level comments
		 */
		ArrayList<TopLevelComment> tlc = BrowseTopLevelComments();
		TopLevelComment myComment;
		TopLevelComment myChangedComment;
		
		/*
		 * Right now I am checking for same text but once we have an ID method we will have to check
		 * for the id instead as it will be unique
		 */
		for (int i = 0; i < tlc.getSize(); i++) {
			if (tlc.get(i).getText.equals(text)) {
				myComment = tlc.get(i);
			}
		}
		
		EditAuthoredComment(myComment, "new text");
		
		for (int i = 0; i < tlc.getSize(); i++) {
			if (tlc.get(i).getText.equals("new text")) {
				myChangedComment = tlc.get(i);
			}
		}
		
		assertFalse("new comment should not equal old comments text", myComment.getText().equals(myChangedComment.getText()));
		assertTrue("new comment should have text - 'new text'", myChangedComment.getText().equals("new text"));
	}
	
	/*
	 * Use Case 16: GetLatestComments Test
	 */
	public void GetLatestComments() {
		/* 
		 * Im assuming getLatestComments() will return some arrayList<Comment>
		 */
		Date date1 = new Date(0);
		Date date2 = new Date(50000);
		Date date3 = new Date(100000);
		
		Comment com1 = new Comment("default1", date1);
		Comment com2 = new Comment("default2", date2);
		Comment com3 = new Comment("default3", date3);
		
		/*
		 * Assumption being made here is that
		 */
		MakeNewTopLevelCommentTest(com1);
		MakeNewTopLevelCommentTest(com2);
		MakeNewTopLevelCommentTest(com3);
		
		/*
		 * set up the unsorted list
		 */
		List<Comment> sortedComList = new ArrayList<Comment>();
		sortedComList.add(com3);
		sortedComList.add(com2);
		sortedComList.add(com1);
		
		/*
		 * get latest Comment returns all of the latest comments, the top three should be our 
		 * 3 new ones ordered
		 */
		ArrayList<Comment> retrievedList = getLatestComments();
		List<Comment> subList = retrievedList.subList(0, 3);
		assertTrue("this sub list should be equal to our defined sorted list", subList.equals(sortedComList));
		
	}
	
}
