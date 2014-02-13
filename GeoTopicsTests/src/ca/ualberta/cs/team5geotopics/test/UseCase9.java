package ca.ualberta.cs.team5geotopics.test;

import java.util.ArrayList;


import ca.ualberta.cs.team5geotopics.GeoTopicsActivity;

import android.test.ActivityInstrumentationTestCase2;
/*
AddPictureToComment

User Story: As a user, I want to attach a picture to my comment.

Participating Actors: User

Goal: Allow user to add a picture to a comment

Trigger: User selects add picture when creating a comment

Pre condition: User has a picture and wants to add it to a new comment

Post condition: Users submits a comment with an attached picture.

Basic Flow:

    User selects add a picture
    User uses file browser to select a file
    User submits the comment

Exceptions:

2.)File is not a picture

2.1)Display a warning message about wrong file format and return to step 1
*/

import android.widget.ListView;

public class UseCase9 extends ActivityInstrumentationTestCase2<GeoTopicsActivity>
{

	public UseCase9(){
		super(null);
	}

	/*this is just testing the model. We need to hash out how to access comments after they're made */
	public void testSuccess(){
		Comment com = new comment("This is a comment");
		Picture pic = new picture("testPic.bmp");
		com.addPicture(pic);
		int id = makeNewTopLevelComment(com); //lets have an Id returned when a comment is made
		
		ArrayList<TopLevelComment> tlc = BrowseTopLevelComments();
		assertEquals(tlc.getItem(id).getPicture(), pic);

	}
}
