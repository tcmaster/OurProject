package com.android.doubanmovie.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.doubanmovie.R;

public class CommentDetail extends Activity {
	TextView author, usefulCount, createDate, content, title;
	RatingBar ratingBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment_detail);
		author = (TextView) findViewById(R.id.dAuthor);
		usefulCount = (TextView) findViewById(R.id.dUse);
		createDate = (TextView) findViewById(R.id.dData);
		content = (TextView) findViewById(R.id.dConnent);
		title = (TextView) findViewById(R.id.dTitle);
		ratingBar = (RatingBar) findViewById(R.id.dRatingbar);
		author.setText(getIntent().getStringExtra("name"));
		usefulCount.setText(getIntent().getStringExtra("useful_count" + "”–”√"));
		createDate.setText(getIntent().getStringExtra("created_at"));
		content.setText(getIntent().getStringExtra("content"));
		content.setMaxLines(999);
		title.setText(getIntent().getStringExtra("title"));
		ratingBar.setMax(5);
		ratingBar.setProgress(Integer.valueOf(getIntent().getStringExtra(
				"value")));
	}

}
