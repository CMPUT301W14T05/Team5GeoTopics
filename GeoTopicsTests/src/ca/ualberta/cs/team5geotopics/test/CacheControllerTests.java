package ca.ualberta.cs.team5geotopics.test;

import java.util.ArrayList;

import android.test.ActivityInstrumentationTestCase2;


public class CacheControllerTests extends
		ActivityInstrumentationTestCase2<CacheController> {

	public CacheControllerTests() {
		super(CacheController.class);
	}
	
	// Test Case 10.
	// Caches a read comment.
	public void testCacheReadComment(){
	FileOutputStream fileOut;
	Comment Comment1 = new comment("This is a comment");
	Comment Comment2;
	Comment.read = 1;
	
	// Stores read comment.
	if(comment.read == 1){
		try {
			fileOut = context.openFileOutput("readComments.txt", Context.MODE_PRIVATE);
			ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
			objOut.writeObject(Comment);
			objOut.close();
		}catch (Exception e){
			e.printStackTrace();
		  }		
		}
		
	// Restores read comment.
	FileInputStream fileIn;
		try {
			fileIn = context.openFileInput("readComments.txt");
			ObjectInputStream objIn = new ObjectInputStream(fileIn);
			Comment2 = (Comment<CacheController>) objIn.readObject();
			objIn.close();
		}catch (FileNotFoundException e){
			return;
		}catch (Exception e){
			e.printStackTrace();
		}		
		
		assertEquals("Testing the two comments", true, Comment1.text, Comment2.text);
	}

}
