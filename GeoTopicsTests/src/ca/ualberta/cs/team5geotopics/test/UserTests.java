//package ca.ualberta.cs.team5geotopics.test;
//
//import android.test.ActivityInstrumentationTestCase2;
//import ca.ualberta.cs.team5geotopics.CommentModel;
//import ca.ualberta.cs.team5geotopics.TopLevelActivity;
//import ca.ualberta.cs.team5geotopics.User;
//
//public class UserTests extends
//		ActivityInstrumentationTestCase2<TopLevelActivity> {
//
//	public UserTests() {
//		super(TopLevelActivity.class);
//	}
//
//	public void testAddMyComment() {
//
//		try {
//			runTestOnUiThread(new Runnable() {
//				@Override
//				public void run() {
//
//					User myUser = User.getInstance();
//					// Initialises a blank user such that we can test it without
//					// IO.
//					myUser.testSetup();
//
//					CommentModel comment = new CommentModel("1", "1", "Body",
//							"Author", null, "Title");
//					comment.setmEsID("1234");
//
//					assertTrue("User's my comments list should not be empty",
//							!myUser.getMyComments().isEmpty());
//					assertEquals(
//							"The first element should hav ethe right title",
//							myUser.getMyComments().get(0).getmAuthor(),
//							"Author");
//				}
//			});
//		} catch (Throwable e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	public void testUpdateComment() {
//
//		try {
//			runTestOnUiThread(new Runnable() {
//				@Override
//				public void run() {
//
//					User myUser = User.getInstance();
//					// Initialises a blank user such that we can test it without
//					// IO.
//					myUser.testSetup();
//					CommentModel oldComment = new CommentModel("1", "1",
//							"Body.old", "Author.old", null, "Title.old");
//					oldComment.setmEsID("1234");
//					CommentModel newComment = new CommentModel("1", "1",
//							"Body.new", "Author.new", null, "Title.new");
//					newComment.setmEsID("1234");
//					myUser.addToMyComments(oldComment);
//
//					assertEquals(
//							"My comments should contain the old comment title",
//							myUser.getMyComments().get(0).getmAuthor(),
//							"Title.old");
//					// update the comment
//					myUser.updateMyComment(newComment);
//
//					assertEquals(
//							"My comments should contain the new comment title",
//							myUser.getMyComments().get(0).getmAuthor(),
//							"Title.new");
//
//				}
//			});
//		} catch (Throwable e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//}
