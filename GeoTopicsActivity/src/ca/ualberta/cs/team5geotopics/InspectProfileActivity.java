package ca.ualberta.cs.team5geotopics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.example.team5geotopics.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;

public class InspectProfileActivity extends Activity {

	public static String imageFilePath;
	public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	public static final int SELECT_LOCATION_REQUEST_CODE = 200;
	public static final int GET_PHOTO = 105;
	public static Uri imageFileUri;
	protected GeoTopicsApplication application;
	protected User myUser;
	protected UserController uController;

	ImageView profileImage;
	
	//Viewing variables
	protected Bitmap mPicture;
	protected ImageButton photoBtn;
	protected ImageButton cancelBtn;
	protected ImageButton postBtn;
	protected String mAuthor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Remove the top back button, not going to use it.
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setHomeButtonEnabled(false);
		getActionBar().setDisplayShowHomeEnabled(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.inspect_comment, menu);
		return true;
	}
	
	@Override
	public void onResume() {
		invalidateOptionsMenu();
		super.onResume();
	}
	
	// Ensures the proper action bar items are shown
		public boolean onPrepareOptionsMenu(Menu menu) {
			MenuItem item;
			item = menu.findItem(R.id.action_favourite);
			item.setVisible(false);
			item = menu.findItem(R.id.action_bookmark);
			item.setVisible(false);
			item = menu.findItem(R.id.new_top_level_comment);
			item.setVisible(false);
			item = menu.findItem(R.id.action_sort);
			item.setVisible(false);
			item = menu.findItem(R.id.action_refresh);
			item.setVisible(false);
			item = menu.findItem(R.id.action_profile);
			item.setVisible(false);
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
				profileImage.setImageBitmap(mPicture);

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
				profileImage.setImageBitmap(mPicture);
			}
		}
	}

	/**
	 * Method gets photo from gallery.
	 */
	public void getPhoto() {
		Intent getPhotoFromGallery = new Intent(Intent.ACTION_PICK);
		getPhotoFromGallery.setType("image/*");
		startActivityForResult(getPhotoFromGallery, GET_PHOTO);
	}

	/**
	 * Returns the image as a Bitmap (resized to 200x200px)
	 * 
	 * @param image
	 *            The image to be resized.
	 * @return image The scaled bitmap from the parameter image.
	 */
	public Bitmap returnBitmapImage(Bitmap image) {
		return image = Bitmap.createScaledBitmap(image, 200, 200, false);
	}

}
