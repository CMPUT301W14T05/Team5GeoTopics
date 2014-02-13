package ca.ualberta.cs.team5geotopics.test;

import java.util.ArrayList;

import android.test.ActivityInstrumentationTestCase2;


public class WebServiceControllerTest extends
		ActivityInstrumentationTestCase2<WebServiceController> {

	public WebServiceControllerTest() {
		super(WebServiceController.class);
	}
	
	/*
		use case name:	PushCommentsOnInternetConnectivity
		user story:		As a comment author, if I have internet, 
						I want my comments pushed and shared with all other users of the app immediately.
		Participating Actors: Comment Author.
	
		Goal: Push all the comments from an author to the rest of the app users immediately if we have internet access.
	
		Trigger: Comment author publishes a new comment.
	
		Pre condition: Comment author has created new comments.
	
		Post condition: The comment is pushed to other app users.
	
		Basic Flow:
	
		Comment author creates a new comment.
		Comment author has internet.
		Comment is pushed to the server.
		Other app users can receive the new comment from the server.
		Exceptions:
	
		2.) No internet.
	
		2.1) Comment is cached locally until it can be pushed at a later time.
		last updated: Feb 8/2014 10:57am slmyers
	 */
	
	/*
	 * Use Case 15: PushCommentsOnInternetConnectivity
	 * this tests for failure
	 */
	public void pushCommentsOnInternetConTestFail(){
		WebServiceController wsc = new WebServiceController();
		// ensure network disconnectivity
		Comment c = new Comment("Test.");
		wsc.pushCommentOnConnectivity(c);
		// ensure network Commentectivity
		ArrayList<Comment> cl = new ArrayList<Comment>();
		wsc.getCommentsOnConnectivity(cl);
		assertTrue(cl.contains(c));

	}

	/*
	 * Use Case 15: PushCommentsOnInternetConnectivity
	 * this tests for success
	 */
	public void pushCommentsOnInternetConTestPass(){
		WebServiceController wsc = new WebServiceController();
		// ensure network connectivity
		Comment c = new Comment("Test.");
		wsc.pushCommentOnConnectivity(c);
		ArrayList<Comment> cl = new ArrayList<Comment>();
		wsc.getCommentsOnConnectivity(cl);
		assertTrue(cl.contains(c));
	}
	
	
	/* 	Use Case Name:	GetCommentsOnInternetReConnect
		User Story:		As a user, if my phone connects to the internet while this app is open it should
						load the latest and greatest and most relevant comments.
	
		Goal: 			Upon connecting to the internet the app should load the freshest and most relevant comments.
		Trigger: 		Phone connects to the internet while the app is running.
	
		Pre condition: 	App is running and not connected to the internet.
		
		Post condition:	The latest and most relevant comments are retrieved.
		
		Basic Flow:
		
						User has the app open but not connected to the internet.
						Phone connects to the internet.
						Newest comments are retrieved from the server.
		Exceptions:
		
						3.) Phone loses internet before all comments are retrieved 
						3.1) Abandon the comments retrieval and try again when we get internet.
		
		updated: Feb 8/2014 11:00am slmyers
		last update: Feb 12/2014 8:48pm twendlan
	*/
	public void GetCommentsOnInternetReconnectTest(){
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
