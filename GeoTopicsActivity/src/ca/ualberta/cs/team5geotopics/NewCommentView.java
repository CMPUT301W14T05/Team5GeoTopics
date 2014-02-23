package ca.ualberta.cs.team5geotopics;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.team5geotopics.R;

public class NewCommentView extends InspectCommentView {
	private EditText mTitle; 
	private EditText mBody;
	private EditText mAuthor;
	private Button mOK;
	//just so we can update the model with some ease.
	private NewCommentView mThis;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_comment_view);
		mThis = this;
		super.initmComment();
		
		QueueModel topLevelQ = GeoTopicsApplication.getQueueController().getmTopLvlQueue();
		topLevelQ.addView(this);
		
		setUpEditTexts();
		setUPOKbtn();
	}
	@Override
	public void update(QueueModel topLevelQ) {
		topLevelQ.addToOut(getmComment());
		
	}
	

	private void setUPOKbtn() {
		mOK = (Button)findViewById(R.id.new_comment_ok);
		mOK.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				/*
				 * TODO: error checking, eg, no Author or title etc. 
				 */
				CommentModel comment = getmComment();
				comment.setmAuthor(mAuthor.getText().toString());
				comment.setmBody(mBody.getText().toString());
				comment.setmTitle(mTitle.getText().toString());
				
				update(GeoTopicsApplication.getQueueController().getmTopLvlQueue());
				//when comment is pushed to ElasticSearch then pull it and
				//pass it to this activity.
				Intent next = new Intent(mThis, CommentView.class);
				Bundle b = new Bundle();
				b.putSerializable("comment", comment);
				next.putExtra("comment", b);
				startActivity(next);
			}
		});
		
	}

	private void setUpEditTexts() {
		mTitle = (EditText)findViewById(R.id.new_comment_title);
		mBody = (EditText)findViewById(R.id.new_comment_body);
		mAuthor = (EditText)findViewById(R.id.new_comment_author);
	}

}
