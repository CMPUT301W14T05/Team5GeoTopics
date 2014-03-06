package ca.ualberta.cs.team5geotopics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.example.team5geotopics.R;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.widget.ImageButton;
import android.widget.ImageView;

public class InspectCommentActivity extends Activity {

	public static String imageFilePath;
	public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	public static final int GET_PHOTO = 105;
	public static Uri imageFileUri;
	
	// Variables for comment/edit comment.
	Location mGeolocation; 
	String mBody; 
	String mAuthor;
	Bitmap mPicture;
	String mTitle;
	
	// Buttons for the 4 options at the bottom
	ImageButton locationBtn;
	ImageButton photoBtn;
	ImageButton cancelBtn;
	ImageButton postBtn;
		
	GeoTopicsApplication application;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inspect_comment);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.inspect_comment, menu);
		return true;
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
	       
	   imageFilePath = folder + "/" + String.valueOf(System.currentTimeMillis()) + "jpg";
	   File imageFile = new File(imageFilePath);
	   imageFileUri = Uri.fromFile(imageFile);
	   intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
	   startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	}
	
	protected Dialog onCreateDialog(int i) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		String options[] = new String[2];
		
		options[0] = "Take new photo";
		options[1] = "Take from gallery";
		
		
		builder.setTitle("Select Option")
				.setItems(options, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// Which is option 0 or 1
						if(which == 0){
							takePhoto();
						}
						
						if(which == 1){
			                 getPhoto();
						}
					}
				});
		return builder.create();
	}

	
	// After camera takes photo, goto here to deal with it.
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// Took photo
	       if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE){
	           // Photo was taken successfully.
	           if (resultCode == RESULT_OK) {   	     
	               // Gets image from /GeoTopics path.
			       ImageView uploadedImage = (ImageView)findViewById(R.id.imageViewPicture);
			       try {
					Bitmap image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageFileUri);
					mPicture = returnBitmapImage(image);
		            mPicture = Bitmap.createScaledBitmap(mPicture, 200, 200, true);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			       // Rotates normal baxk camera photo
			       //Matrix m = new Matrix();
			       //m.postRotate(90);
			       //mPicture = Bitmap.createBitmap(mPicture, 0, 0, mPicture.getWidth(), mPicture.getHeight(), m, true);
			       uploadedImage.setImageBitmap(mPicture);
			       
	            } else if (resultCode == RESULT_CANCELED) {
	                // Photo was canceled, do nothing.
	            } else {
	                // Unknown error, do nothing.
	            } 
	           
	    }
	       if (requestCode == GET_PHOTO){
	    	   if (resultCode == RESULT_OK) {   	
	              // Gets image from /GeoTopics path.
	       	   Uri myPhoto = data.getData();
	              InputStream imageStream = null;
			try {
				imageStream = getContentResolver().openInputStream(myPhoto);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	              Bitmap image = BitmapFactory.decodeStream(imageStream);
	              // Set mPicture with Bitmap image.
	              mPicture = returnBitmapImage(image);
	              mPicture = Bitmap.createScaledBitmap(mPicture, 200, 200, true);
	              ImageView uploadedImage = (ImageView)findViewById(R.id.imageViewPicture);
	              uploadedImage.setImageBitmap(mPicture);
	          }
	    }
	}	
	
	// Method gets photo from gallery.
	public void getPhoto(){
		Intent getPhotoFromGallery = new Intent(Intent.ACTION_PICK);
		getPhotoFromGallery.setType("image/*");
		startActivityForResult(getPhotoFromGallery, GET_PHOTO);
	}
	
	// Returns the image as a Bitmap
	public Bitmap returnBitmapImage(Bitmap image){
		return image;
	}

}
