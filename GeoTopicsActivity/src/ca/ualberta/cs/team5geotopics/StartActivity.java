package ca.ualberta.cs.team5geotopics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.team5geotopics.R;


public class StartActivity extends Activity {
	private Button mBrowseTopLevel;
	private Button takePhotoButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_geo_topics);
		
		setUpBrowseButton();
		setUpPhotoButton();
	}
	
	private void setUpBrowseButton() {
		mBrowseTopLevel = (Button)findViewById(R.id.start_browse_top_level);
		mBrowseTopLevel.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(StartActivity.this, BrowseTopLevelView.class);
				startActivity(myIntent);
				
			}
		});
	}
	
	private void setUpPhotoButton() {
		takePhotoButton = (Button)findViewById(R.id.takePhotoButton);
		takePhotoButton.setOnClickListener(new View.OnClickListener() 
		
		{
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				showDialog(0);
				}
		});
	}

	@Override
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
	
	public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	public static final int GET_PHOTO = 105;
	public static Uri imageFileUri;
	
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
	
	// Method gets photo from gallery.
	public void getPhoto(){
		Intent getPhotoFromGallery = new Intent(Intent.ACTION_PICK);
		getPhotoFromGallery.setType("image/*");
		startActivityForResult(getPhotoFromGallery, GET_PHOTO);
	}
		

	// After camera takes photo, goto here to deal with it.
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// Took photo
	       if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE){
	           // Photo was taken successfully.
	           if (resultCode == RESULT_OK) {   	
	               ImageButton button = (ImageButton) findViewById(R.id.displayPhotoButton);
	               // Gets image from /GeoTopics path.
	               button.setImageDrawable(Drawable.createFromPath(imageFileUri.getPath()));
	            } else if (resultCode == RESULT_CANCELED) {
	                // Photo was canceled, do nothing.
	            } else {
	                // Unknown error, do nothing.
	            } 
	           
	    }
	       if (requestCode == GET_PHOTO){
        	   ImageButton button = (ImageButton) findViewById(R.id.displayPhotoButton);
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
               button.setImageBitmap(image);
           }
	}
}
