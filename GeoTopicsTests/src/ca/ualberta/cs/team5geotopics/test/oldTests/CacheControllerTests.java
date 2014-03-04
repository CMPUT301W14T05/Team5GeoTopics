//package ca.ualberta.cs.team5geotopics.test.oldTests;
//
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//
//import org.w3c.dom.Comment;
//
//import android.app.Activity;
//import android.app.Instrumentation;
//import android.content.Context;
//import android.test.ActivityInstrumentationTestCase2;
//import android.test.ViewAsserts;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ListView;
//import ca.ualberta.cs.team5geotopics.StartActivity;
//
//
//public class CacheControllerTests extends
//		ActivityInstrumentationTestCase2<StartActivity> {
//	
//	Instrumentation instrumentation;
//	Activity activity;
//	
//	public CacheControllerTests() {
//		super(StartActivity.class);
//	}
//	
//	protected void setUp() throws Exception{
//		super.setUp();
//		instrumentation = getInstrumentation();
//		activity = getActivity();
//	}
//	
//	// Test Case 10.
//	// Caches a read comment.
//	public void testCacheReadComment(){
//	FileOutputStream fileOut;
//	Comment Comment1 = new comment("This is a comment");
//	Comment Comment2;
//	Comment.read = 1;
//	
//	// Stores read comment.
//	if(comment.read == 1){
//		try {
//			fileOut = context.openFileOutput("readComments.txt", Context.MODE_PRIVATE);
//			ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
//			objOut.writeObject(Comment);
//			objOut.close();
//		}catch (Exception e){
//			e.printStackTrace();
//		  }		
//		}
//		
//	// Restores read comment.
//	FileInputStream fileIn;
//		try {
//			fileIn = context.openFileInput("readComments.txt");
//			ObjectInputStream objIn = new ObjectInputStream(fileIn);
//			Comment2 = (Comment<CacheController>) objIn.readObject();
//			objIn.close();
//		}catch (FileNotFoundException e){
//			return;
//		}catch (Exception e){
//			e.printStackTrace();
//		}		
//		
//		assertEquals("Testing the two comments", true, Comment1.text, Comment2.text);
//	}
//	
//	// use case 10b alternate version
//	// caches a read comment temporarily 
//	public void testCacheReadComment2() throws Throwable{
//		runTestOnUiThread(new Runnable()
//		{
//			@Override
//			public void run(){
//				//cache controller is a singleton
//				CacheController cacheController = getCacheController();
//				//creates test comment
//				createTestComment();
//				//browse button on main screen
//				((Button)activity.findViewById(ca.ualberta.cs.team5geotopics.GeoTopicsActivity.R.StartActivity.browseButton)).performClick();
//				//after we hit browse we should load TopLevelComments and it should only be the test comment in there
//				//the ListView for the custom adapter
//				ListView listView = (ListView) activity.findViewById(ca.ualberta.cs.team5geotopics.GeoTopicsActivity.R.StartActivity.commentList);
//				//the custom adapter on the physical screen
//				ArrayAdapter<Comment> adapter = (ArrayAdapter<Comment>) listView.getAdapter();
//				
//				//click on comment to view it
//				
//				View view = adapter.getView(adapter.getPosition(comment), null, null);
//				ViewAsserts.assertOnScreen(listView, view);
//				view.performClick();
//
//				assertTrue(cacheController.getHistory().contains(comment));
//
//			}
//		});
//	}
//	
//	
//	//use case 11.
//	//Caches a comment to read offline
//	
//	public void testCacheWntToRead() throws Throwable{
//		runTestOnUiThread(new Runnable()
//		{
//			@Override
//			public void run(){
//				//cache controller is a singleton
//				CacheController cacheController = getCacheController();
//				//creates test comment
//				createTestComment();
//				//browse button on main screen
//				((Button)activity.findViewById(ca.ualberta.cs.team5geotopics.GeoTopicsActivity.R.StartActivity.browseButton)).performClick();
//				//the ListView for the custom adapter
//				ListView listView = (ListView) activity.findViewById(ca.ualberta.cs.team5geotopics.GeoTopicsActivity.R.StartActivity.commentList);
//				ArrayAdapter<Comment> adapter = (ArrayAdapter<Comment>) listView.getAdapter();
//				// click comment to view it
//				View view = adapter.getView(0, null, null);
//				ViewAsserts.assertOnScreen(listView, view);
//				view.performClick();
//				
//				//ok at this time the ListView should be updated to display
//				//only the ConcreteCommentView of comment
//				Button bookMark = (Button) activity.findViewById(ca.ualberta.cs.team5geotopics.R.id.toggleBookMark);
//				ViewAsserts.assertOnScreen(bookMark.getRootView(), bookMark);
//				bookMark.performClick();
//				
//				// is there a way I can get references to the objects
//				// already instantiated in the test thread?
//				
//				assertTrue(cacheController.getBookMarks().contains(adapter.getItem(0)));
//				
//			}
//		});
//	}
//			
//		
//	
//	
//	//use case 13
//	//Loads favorites Cache
//	
//	public void testViewFavouriteComments() throws Throwable{
//		runTestOnUiThread(new Runnable() {
//			
//			@Override
//			public void run() {
//				//cache controller is a singleton
//				CacheController cacheController = getCacheController();
//				//creates test comment
//				createTestComment();
//				//browse button on main screen
//				((Button)activity.findViewById(ca.ualberta.cs.team5geotopics.GeoTopicsActivity.R.StartActivity.browseButton)).performClick();
//				//the ListView for the custom adapter
//				ListView listView = (ListView) activity.findViewById(ca.ualberta.cs.team5geotopics.GeoTopicsActivity.R.StartActivity.commentList);
//				//the custom adapter on the physical screen
//				ArrayAdapter<Comment> adapter = (ArrayAdapter<Comment>) listView.getAdapter();
//				
//				//click comment to view it
//				View view = adapter.getView(0, null, null);
//				ViewAsserts.assertOnScreen(listView, view);
//				view.performClick();
//				
//				//only the ConcreteCommentView of comment
//				//not sure where toggleFavorite is ie which view
//				Button favorite = (Button) activity.findViewById(ca.ualberta.cs.team5geotopics.R.id.toggleFavorite);
//				ViewAsserts.assertOnScreen(favorite.getRootView(), favorite);
//				favorite.performClick();
//				//shutdown app
//				
//			}
//		});
//		
//		runTestOnUiThread(new Runnable(){
//			
//			@Override
//			public void run(){
//				// the favorites button on app startup
//				((Button)activity.findViewById(ca.ualberta.cs.team5geotopics.GeoTopicsActivity.R.StartActivity.favoritesButton))
//				.performClick();
//				ListView listView = (ListView) activity.findViewById(ca.ualberta.cs.team5geotopics.R.id.commentList);
//				
//				ArrayAdapter<Comment> adapter = (ArrayAdapter<Comment>) listView.getAdapter();
//				
//				//assert that the ListView has a view that represents our comment uploaded into favorites
//				View view = adapter.getView(0, null, null);
//				ViewAsserts.assertOnScreen(listView, view);
//				CacheController.emptyCaches();
//			}
//		});
//		
//		
//			
//	}
//	
//	public void createTestComment(){
//		((Button)activity.findViewById(ca.ualberta.cs.team5geotopics.R.id.createNewTopComment)).performClick();
//		EditText comment = (EditText) findViewById(ca.ualberta.cs.team5geotopics.R.id.commentTextInput);
//		comment.setText("Test");
//		((Button)activity.findViewById(ca.ualberta.cs.team5geotopics.GeoTopicsActivity.R.StartActivity.newCommentOk)).performClick();
//		
//	}
//}
