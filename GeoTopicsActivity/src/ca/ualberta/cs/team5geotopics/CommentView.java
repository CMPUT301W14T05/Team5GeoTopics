package ca.ualberta.cs.team5geotopics;


import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.team5geotopics.R;

public class CommentView extends InspectCommentView {
	private TextView mTitle;
	private TextView mBody;
	private TextView mAuthor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.comment_view);
		Bundle extras = getIntent().getBundleExtra("comment");
		setmComment((CommentModel) extras.getSerializable("comment"));
		setUpTextViews();
	}
	
	private void setUpTextViews() {
		CommentModel comment = getmComment();
		mTitle = (TextView)findViewById(R.id.comment_view_title);
		mTitle.setText(comment.getmTitle());
		
		mBody = (TextView)findViewById(R.id.comment_view_body);
		mBody.setText(comment.getmBody());
		
		mAuthor = (TextView)findViewById(R.id.comment_view_author);
		mAuthor.setText(comment.getmAuthor());
		
	}

	@Override
	public void update(QueueModel model) {
		//void
		//don't implement
	}

}
