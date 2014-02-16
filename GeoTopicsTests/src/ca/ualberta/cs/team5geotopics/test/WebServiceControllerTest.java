package ca.ualberta.cs.team5geotopics.test;

import java.util.ArrayList;
import ca.ualberta.cs.team5geotopics.Comment;

import android.test.ActivityInstrumentationTestCase2;


public class WebServiceControllerTest extends
		ActivityInstrumentationTestCase2<WebServiceController> {

	public WebServiceControllerTest() {
		super(WebServiceController.class);
	}
	
	
	/*
	 * Use Case 15: PushCommentsOnInternetConnectivity
	 * tests to see if comments are pushed to web service
	 */
	public void testPushCommentsOnInternetCon(){
		/*
		 * To view textual test case go to the used case 15 in the wiki
		 * and click 'view textual test case'
		 */
		
		WebServiceController wsc = new WebServiceController();
		Comment c = new Comment("Test");
		ArrayList<Comment> commentList = new ArrayList<Comment>();
		/* if (internet is available) {*/
			wsc.pushCommentsOnInternetConnectivity();
		/*}*/
		try{
			wsc.getCommentsOnConnectivity(commentList);
		}catch(Exception 3){ 
			// reference to exception above
		}catch(Exception 3.1){ // reference to excetpion above

		}
		assertTrue(commentList.contains(c));
	}

	
	
	
	/* 	Use Case 20:	GetCommentsOnInternetReConnect
		tests to see if comments are pulled from web service
		
		updated: Feb 8/2014 11:00am slmyers
		last update: Feb 12/2014 8:48pm twendlan
	*/
	public void testGetCommentsOnInternetReconnect(){
		WebServiceController wsc = new WebServiceController();
		Comment c = new Comment("Test");
		// ensure connectivity via sometype of code here
		wsc.pushCommentOnConnectivity(c);
		ArrayList<Comment> commentList = new ArrayList<Comment>();
		// ensure that internet connectiviy is disabled via some type of code here
		// then turn on internet connectivity
		try{
			wsc.getCommentsOnConnectivity(commentList);
		}catch(Exception 3){ // reference to exception above

		}catch(Exception 3.1){ // reference to excetpion above

		}
		assertTrue(commentList.contains(c));
	}

}
