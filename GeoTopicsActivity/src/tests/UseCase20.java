package tests;

import java.util.ArrayList;
import main.GeoTopicsActivity;
import android.test.ActivityInstrumentationTestCase2;

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

last updated: Feb 8/2014 11:00am slmyers
*/
public class UseCase20 extends ActivityInstrumentationTestCase2<GeoTopicsActivity> {

	public UseCase20() {
		super(GeoTopicsActivity.class);
	}

	public void testSuccess(){
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
