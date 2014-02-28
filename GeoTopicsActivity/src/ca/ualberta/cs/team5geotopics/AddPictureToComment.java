package ca.ualberta.cs.team5geotopics;

import java.io.File;

import com.example.team5geotopics.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.widget.ImageButton;
import android.widget.TextView;

/* This class will be called when a user selects "Picture"
 * in either a new comment, a reply or an edit of a comment/reply.
 * 
 * A box will prompt the user to either select a photo from his/her
 * photo's image gallery or to take a new photo from the phone's camera.
 * 
 * AndroidManifest.xml was modified to allow use of CAMERA and saving to phone.
 */

public class AddPictureToComment extends Activity{
	
	public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	public static Uri imageFileUri;
	
	public void browsePhotos(){
		Intent getPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(getPhoto, 1);
	}
	
	// Method takes photo from camera and returns image.
	public void takePhoto(){
		
		// Camera control intent
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		
		// Folder to store image in
		String folder = Environment.getExternalStorageDirectory().getAbsolutePath() + "/GeoTopics";
        File folderF = new File(folder);
        
        // Creates folder if it doesn't exist
        if (!folderF.exists()) {
            folderF.mkdir();
        }
        
        String imageFilePath = folder + "/" + String.valueOf(System.currentTimeMillis()) + "jpg";
        File imageFile = new File(imageFilePath);
        imageFileUri = Uri.fromFile(imageFile);
        
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

	
	// After camera takes photo, goto here to deal with it.
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// Took photo
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            // Photo was taken successfully.
            if (resultCode == RESULT_OK) {
            	
            	Intent myIntent = new Intent(AddPictureToComment.this, StartActivity.class);
				
                ImageButton button = (ImageButton) findViewById(R.id.displayPhotoButton);
                // Gets image from /GeoTopics path.
                button.setImageDrawable(Drawable.createFromPath(imageFileUri.getPath()));
                
                startActivity(myIntent);
            } else if (resultCode == RESULT_CANCELED) {
                // Photo was canceled, do nothing.
            } else {
                // Unknown error, do nothing.
            }
            if(requestCode == 1){
            	 Intent myIntent = new Intent(AddPictureToComment.this, StartActivity.class);
            	 ImageButton button = (ImageButton) findViewById(R.id.displayPhotoButton);
                 // Gets image from /GeoTopics path.
            	 Uri myPhoto = data.getData();
                 button.setImageDrawable(Drawable.createFromPath(myPhoto.getPath()));
                 startActivity(myIntent);
            	
            }
        }
    }
}