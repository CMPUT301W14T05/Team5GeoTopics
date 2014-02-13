package ca.ualberta.cs.team5geotopics.test;

import java.util.ArrayList;
import main.GeoTopicsActivity;
import android.test.ActivityInstrumentationTestCase2;

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

public class UseCase15 extends ActivityInstrumentationTestCase2<GeoTopicsActivity> {

	public UseCase15(){
		super(GeoTopicsActivity.class);
	}

	public void testFailure(){
		WebServiceController wsc = new WebServiceController();
		// ensure network disconnectivity
		Comment c = new Comment("Test.");
		wsc.pushCommentOnConnectivity(c);
		// ensure network Commentectivity
		ArrayList<Comment> cl = new ArrayList<Comment>();
		wsc.getCommentsOnConnectivity(cl);
		assertTrue(cl.contains(c));

	}

	public void testSuccess(){
		WebServiceController wsc = new WebServiceController();
		// ensure network connectivity
		Comment c = new Comment("Test.");
		wsc.pushCommentOnConnectivity(c);
		ArrayList<Comment> cl = new ArrayList<Comment>();
		wsc.getCommentsOnConnectivity(cl);
		assertTrue(cl.contains(c));
	}

}
