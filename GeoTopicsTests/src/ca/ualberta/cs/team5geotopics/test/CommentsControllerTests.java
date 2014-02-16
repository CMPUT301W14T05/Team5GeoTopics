package ca.ualberta.cs.team5geotopics.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.w3c.dom.Comment;

import ca.ualberta.cs.team5geotopics.GeoTopicsActivity;

import android.app.Activity;
import android.app.Instrumentation;
import android.graphics.Picture;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.view.View;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

/* 
 * contains the test code for all methods in the CommentsController class
 * 
 * NOTE: this is currently skeleton test code - please add to it
 */
public class CommentsControllerTests extends
		ActivityInstrumentationTestCase2<GeoTopicsActivity> {

	Instrumentation instrumentation;
	Activity activity;
	
	
	public CommentsControllerTests() {
		super(GeoTopicsActivity.class);
	}

	protected void setUp() throws Exception{
		super.setUp();
		instrumentation = getInstrumentation();
		activity = getActivity();
	}
	
	/*
	 * Use Case 5: MakeNewTopLevelComment Test
	 */
	public void testMakeNewTopLevelComment() {
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
	 * browse list of TopLevelComments
	 */
	public void testBrowseTopLevelComments() throws Throwable{
		runTestOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				createTestComment();
				((Button)activity.findViewById(ca.ualberta.cs.team5geotopics.R.id.browseButton)).performClick();
				ListView lv = ((ListView)activity.findViewById(ca.ualberta.cs.team5geotopics.R.id.browseButton));
				
				ArrayAdapter<Comment> adapter = (ArrayAdapter<Comment>) lv.getAdapter();
				
				
				View view = adapter.getView(0, null, null);
				ViewAsserts.assertOnScreen(lv, view);
				
			}
		});
		
		
	}
	
	/*
	 * Use Case 7: BrowseCommentReplies Test
	 */
	public void testBrowseCommentReplies() {
		/*
		 * Unsure as of right now how we would code this test as it has a lot of dependencies.
		 * We have written up a Textual test case that can be navigated to by looking for used case 7
		 * in the wiki and selecting 'view textual test' link.
		 * 
		 * Basically we will need to create a toplevelcomment using a UI thread and push it to the
		 * CommentController, if there is internet the WebServiceController will push the comment to the web 
		 * and pull all the topLevelComments again. a reply will be generated and we will test load the 
		 * replies into the commentArrayAdapter and test that the generated reply is present.
		 * ---For a more in depth textual test description view the actual textual test (direction located above)
		 * 
		 * assert will be the following:
		 * To Verify the results:
		 * use ViewAsserts.assertOnScreen(ListView, CommentArrayAdapter.getView(replyLevel, null, null))
         * ---review second parameter 
		 */
	}
	
	/*
	 * Use Case 8: ReplyToComment Test
	 */
	public void testReplyToComment() {
		Comment Reply = new comment("This is a reply");
		
		ListView lw = (ListView) findViewById(ca.ualberta.cs.team5geotopics.R.id.commentList);
		ArrayAdapter<comments> aa = (ArrayAdapter<comments>) lw.getAdapter();
		Comment parent = aa.getItem(0);//grabs the first comment (probably have to check that this exists)
		replyToComment(reply, parent); //assuming that this will require 2 comments as arguments
		
		assertEquals(reply, aa.getItem(0).getChild()); /*I also make the 
		assumption that the comment had no other replies (getChild should 
		return a list and the index of the new comment should be determined) */
	}
	
	/*
	 * Use Case 9: AddPictureToComment Test
	 */
	public void testAddPictureToComment() {
		CommentsController cC = new CommentsController();
		ArrayList<Comment> Comments = new ArrayList<Comment>();
		Comment c = new Comment("Test.");
		Picture pic1 = new Picture(cat.bmp);
		
		AddPictureToComment(c, pic1);
		Comments.add(c);
		
		assertEquals("Returns true is comment has"
		+ " a picture", true, Comments.get(1).hasPicture());
		
		/*
		 * -----MATTS VERSION-------
		 * Comment com = new comment("This is a comment");
		 * Picture pic = new picture("testPic.bmp");
		 * com.addPicture(pic);
		 * int id = makeNewTopLevelComment(com); //lets have an Id returned when a comment is made
		 * 
		 * ArrayList<TopLevelComment> tlc = BrowseTopLevelComments();
		 * assertEquals(tlc.getItem(id).getPicture(), pic);
		 */
	}
	
	/*
	 * Use Case 12: SaveComentAsFavoritees Test
	 */
	public void testSaveCommentAsFavorit() {
		/*
		 * Unsure as of right now how we would code this test as it has a lot of dependencies.
		 * We have written up a Textual test case that can be navigated to by looking for used case 12
		 * in the wiki and selecting 'view textual test' link.
		 * 
		 * A basic summary: A comment is created, whether it is a toplevel or a reply is irrelevent
		 * but we will have to test both (the only difference will be the start). The comment is pushed
		 * to the CommentController and depending on internet connectivity, is pushed to the db through the 
		 * WebServiceController then pulled again. the comment is added to the CommentArrayAdapter. Once the 
		 * comment is pressed in the UI it is then the favorite button is toggled. The comment should then be 
		 * saved to the favorites Cache. We will then have to load from the cache to check that the selected 
		 * comment has been saved properly.
		 * 
		 * To Verify the results:
		 * 
 		 * ViewAsserts.assertOnScreen(listView, view); // use this when the ListView is updated 
		 * ---view is adapter.getView(adapter.getPosition(topLevel), null, null);
 		 * ViewAsserts.assertOnScreen(favorite.getRootView(), favorite);
		 * ---where favorite = (Button) activity.findViewById(ca.ualberta.cs.team5geotopics.R.id.toggleFavorite);
 		 * assertTrue(cacheController.getFavorites().contains(adapter.getItem(0)));
 		 * ---this requires that topLevel is the first TopLevelComment loaded
		 */
	}

	/*
	 * Use Case 14: EditAuthoredComment Test
	 */
	public void testEditAuthoredComment() {
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
	public void testGetLatestComments() {
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
	
	public void createTestComment(){
		((Button)activity.findViewById(ca.ualberta.cs.team5geotopics.R.id.createNewTopComment)).performClick();
		EditText comment = (EditText) findViewById(ca.ualberta.cs.team5geotopics.R.id.commentTextInput);
		comment.setText("Test");
		((Button)activity.findViewById(ca.ualberta.cs.team5geotopics.GeoTopicsActivity.R.id.newCommentOk)).performClick();
		
	}
	
}
