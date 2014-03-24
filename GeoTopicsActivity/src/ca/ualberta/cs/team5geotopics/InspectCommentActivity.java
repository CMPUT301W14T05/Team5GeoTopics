package ca.ualberta.cs.team5geotopics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ActionBar.LayoutParams;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.team5geotopics.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

// Code used from Camera Demo on eClass

/**
 * This source file holds the methods for the CreateCommentActivity as well as
 * the EditCommentActivity. These are both called when a user hits the '+' button
 * to either create/edit a comment based on where they are in the software.
 * 
 * This source file can initiate the camera and load a image from the gallery
 * (both tasks shrink the image file to 200x200px without filtering. 
 */

public class InspectCommentActivity extends Activity {

	public static String imageFilePath;
	public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	public static final int GET_PHOTO = 105;
	public static Uri imageFileUri;
	protected GeoTopicsApplication application;
	protected Cache mCache;
	protected CommentModel newComment;
	protected User myUser;
	protected CommentController controller;
	protected String commentType;
	protected String parentID;
	protected Bundle b;
	protected CommentModel viewingComment;
	


	// Variables for comment/edit comment.
	protected Location mGeolocation;
	protected String mBody;
	protected String mAuthor;
	protected Bitmap mPicture;
	protected String mTitle;
	protected GoogleMap googleMap;
	public  LatLng geoPoint;

	// Buttons for the 4 options at the bottom
	protected ImageButton locationBtn;
	protected ImageButton photoBtn;
	protected ImageButton cancelBtn;
	protected ImageButton postBtn;

	// The views to pull data from
	protected EditText title;
	protected EditText author;
	protected EditText body;

	ImageView uploadedImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.application = GeoTopicsApplication.getInstance();
		this.mCache = Cache.getInstance();
		this.application.setContext(getApplicationContext());
		this.myUser = User.getInstance();
		
		this.controller = new CommentController(getApplicationContext());
		
		try{
			this.b = getIntent().getExtras();
			commentType = b.getString("CommentType");
		}
		catch(NullPointerException e){
			commentType = "notApplicable";
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.inspect_comment, menu);
		return true;
	}

	/**
	 * Method takes photo from camera and returns image.
	 */
	public void takePhoto() {

		// Camera control intent
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		// Folder to store image in
		String folder = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/GeoTopics";
		File folderF = new File(folder);

		// Creates folder if it doesn't exist
		if (!folderF.exists()) {
			folderF.mkdir();
		}

		imageFilePath = folder + "/"
				+ String.valueOf(System.currentTimeMillis()) + "jpg";
		File imageFile = new File(imageFilePath);
		imageFileUri = Uri.fromFile(imageFile);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
		startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	}

	protected Dialog onCreateDialog(int i) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		// Add Photo was pressed
		if (i == 0) {
			String options[] = new String[2];

			options[0] = "Take new photo";
			options[1] = "Take from gallery";

			builder.setTitle("Select Option").setItems(options,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// Which is option 0 or 1
							// Take a photo with the phone's camera
							if (which == 0) {
								takePhoto();
							}
							// Get photo from phone's gallery
							if (which == 1) {
								getPhoto();
							}
						}
					});
		}

		// Location was pressed
		if (i == 1) {
			String options[] = new String[2];

			options[0] = "Get current location";
			options[1] = "Set location";

			builder.setTitle("Select Option").setItems(options,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// Which is option 0 or 1
							// Get current location
							if (which == 0) {
								//GET CURRENT LOCATION
							}
							
							// Open up Google maps to select location
							if (which == 1) {
								// SET LOCATION VIA GOOGLE MAP
								Intent myIntent = new Intent(InspectCommentActivity.this, MapsActivity.class);
								startActivity(myIntent);
								
								
							}
						}
					});
		}
		return builder.create();
	}

	/**
	 * After camera takes photo, goto here to deal with it.
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// Took photo, deal with it and get Bitmap
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			// Photo was taken successfully.
			if (resultCode == RESULT_OK) {
				// Gets image from '/GeoTopics' path.
				ImageView uploadedImage = (ImageView) findViewById(R.id.imageViewPicture);
				try {
					Bitmap image = MediaStore.Images.Media.getBitmap(
							this.getContentResolver(), imageFileUri);
					mPicture = returnBitmapImage(image);	
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// Set image as Bitmap
				uploadedImage.setImageBitmap(mPicture);

			} else if (resultCode == RESULT_CANCELED) {
				// Photo was canceled, do nothing.
			} else {
				// Unknown error, do nothing.
			}

		}
		
		// Get photo from internal storage
		if (requestCode == GET_PHOTO) {
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
				uploadedImage.setImageBitmap(mPicture);
			}
		}
	}

	/**
	 *  Method gets photo from gallery.
	 */
	public void getPhoto() {
		Intent getPhotoFromGallery = new Intent(Intent.ACTION_PICK);
		getPhotoFromGallery.setType("image/*");
		startActivityForResult(getPhotoFromGallery, GET_PHOTO);
	}

	/**
	 * Returns the image as a Bitmap (resized to 200x200px)
	 * @param image The image to be resized.
	 * @return image The scaled bitmap from the parameter image.
	 */
	public Bitmap returnBitmapImage(Bitmap image) {
		return image = Bitmap.createScaledBitmap(image, 200, 200, false);
	}
	
	
	
}
