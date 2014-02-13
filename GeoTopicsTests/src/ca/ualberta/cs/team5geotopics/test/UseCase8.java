package ca.ualberta.cs.team5geotopics.test;

import java.util.ArrayList;


import ca.ualberta.cs.team5geotopics.GeoTopicsActivity;

import android.test.ActivityInstrumentationTestCase2;
/*
ReplyToComment

User Story: As a user, I want to reply to comments.

Participating Actors: User

Goal: Allow user to reply to a comment

Trigger: User selects reply from inside a comment

Pre condition: User is viewing a comment and selects reply

Post condition: Users reply is added to the comments reply list

Basic Flow:

    User selects a reply while viewing a comment
    User fills in the body
    User submits the reply

Exceptions:

2.)User does not fill in the body

2.1)Display warning message to user about body being empty. Do not submit till body has contents
*/

import android.widget.ListView;

public class UseCase8 extends ActivityInstrumentationTestCase2<GeoTopicsActivity>
{

	public UseCase8(){
		super(null);
	}

	public void testSuccess(){
		Comment Reply = new comment("This is a reply");
		
		ListView lw = (ListView) findViewById(ca.ualberta.cs.team5geotopics.R.id.commentList);
		ArrayAdapter<comments> aa = (ArrayAdapter<comments>) lw.getAdapter();
		Comment parent = aa.getItem(0);//grabs the first comment (probably have to check that this exists)
		replyToComment(reply, parent); //assuming that this will require 2 comments as arguments
		
		assertEquals(reply, aa.getItem(0).getChild()); /*I also make the 
		assumption that the comment had no other replies (getChild should 
		return a list and the index of the new comment should be determined) */
	}
}
