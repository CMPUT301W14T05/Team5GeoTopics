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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_geo_topics);
		
		setUpBrowseButton();
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
	
}
