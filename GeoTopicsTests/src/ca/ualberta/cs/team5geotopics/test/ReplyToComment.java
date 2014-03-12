package ca.ualberta.cs.team5geotopics.test;

import android.location.Location;
import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.cs.team5geotopics.CommentModel;
import ca.ualberta.cs.team5geotopics.InspectCommentActivity;

public class ReplyToComment extends ActivityInstrumentationTestCase2<InspectCommentActivity> {
	public ReplyToComment() {
		super(InspectCommentActivity.class);
	}
	
	public void testReplyToComment() {
		
//		Location mGeolocation = new Location("");
//		mGeolocation.setLatitude(30.6282);
//		mGeolocation.setLongitude(55.3116);
		
		CommentModel parent = new CommentModel ("30.6282", "55.3116", "Parent Body", "Parent Author", null);
		CommentModel reply = new CommentModel ("30.6282", "55.3116", "Reply Body", "Reply Author", null);
		
		reply.setmParentID(parent.getmEsID());
		
		assertTrue("Reply references parent", reply.getmParentID() == parent.getmEsID());
	}
}
