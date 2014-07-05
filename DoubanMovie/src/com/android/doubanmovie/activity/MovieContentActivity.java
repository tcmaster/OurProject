package com.android.doubanmovie.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.android.doubanmovie.R;
import com.android.doubanmovie.myfragment.CommentFragment;
import com.android.doubanmovie.myfragment.ImageFragment;
import com.android.doubanmovie.myfragment.IntroduceFragment;
import com.android.doubanmovie.myfragment.ShortCommentFragment;

public class MovieContentActivity extends FragmentActivity implements
		OnPageChangeListener, OnClickListener {
	private Button b1, b2, b3, b4;// 该Activity的四个选项
	private ImageView cursor;// 游标动画
	private ViewPager pager;
	private List<Fragment> list;
	private final int INTRODUCE = 0;
	private final int IMAGE = 1;
	private final int SHORTCOMMENT = 2;
	private final int COMMENT = 3;
	private int posOne, posTwo, posThree, posFour;
	private int currentPos = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie_content);
		b1 = (Button) findViewById(R.id.intorduce);
		b2 = (Button) findViewById(R.id.image);
		b3 = (Button) findViewById(R.id.shortcomment);
		b4 = (Button) findViewById(R.id.comment);
		// 计算当前屏幕的位置
		calculatePos();
		cursor = (ImageView) findViewById(R.id.cursor);
		pager = (ViewPager) findViewById(R.id.viewpage2);
		list = new ArrayList<Fragment>();
		list.add(IntroduceFragment.newInstance(null));
		list.add(ImageFragment.newInstance(null));
		list.add(ShortCommentFragment.newInstance(null));
		list.add(CommentFragment.newInstance(null));
		b1.setOnClickListener(this);
		b2.setOnClickListener(this);
		b3.setOnClickListener(this);
		b4.setOnClickListener(this);
		MyAdapter adp = new MyAdapter(getSupportFragmentManager());
		pager.setAdapter(adp);
		adp.notifyDataSetChanged();
		pager.setOnPageChangeListener(this);
		getActionBar().setTitle(getIntent().getStringExtra("movieName"));
	}

	private class MyAdapter extends FragmentPagerAdapter {

		public MyAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			return list.get(arg0);
		}

		@Override
		public int getCount() {
			return list.size();
		}

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		playAnimation(arg0, currentPos);
		currentPos = arg0;
	}

	private void calculatePos() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		posOne = 0;
		posTwo = dm.widthPixels / 4;
		posThree = posTwo * 2;
		posFour = posTwo * 3;
	}

	private void playAnimation(int newPos, int currentPos) {
		Animation animation = null;
		switch (newPos) {
		case INTRODUCE:
			switch (currentPos) {
			case IMAGE:
				animation = new TranslateAnimation(posTwo, posOne, 0, 0);
				break;
			case SHORTCOMMENT:
				animation = new TranslateAnimation(posThree, posOne, 0, 0);
				break;
			case COMMENT:
				animation = new TranslateAnimation(posFour, posOne, 0, 0);
				break;
			default:
				return;
			}
			break;
		case IMAGE:
			switch (currentPos) {
			case INTRODUCE:
				animation = new TranslateAnimation(posOne, posTwo, 0, 0);
				break;
			case SHORTCOMMENT:
				animation = new TranslateAnimation(posThree, posTwo, 0, 0);
				break;
			case COMMENT:
				animation = new TranslateAnimation(posFour, posTwo, 0, 0);
				break;
			default:
				return;
			}
			break;
		case SHORTCOMMENT:
			switch (currentPos) {
			case INTRODUCE:
				animation = new TranslateAnimation(posOne, posThree, 0, 0);
				break;
			case IMAGE:
				animation = new TranslateAnimation(posTwo, posThree, 0, 0);
				break;
			case COMMENT:
				animation = new TranslateAnimation(posFour, posThree, 0, 0);
				break;
			default:
				return;
			}
			break;
		case COMMENT:
			switch (currentPos) {
			case INTRODUCE:
				animation = new TranslateAnimation(posOne, posFour, 0, 0);
				break;
			case IMAGE:
				animation = new TranslateAnimation(posTwo, posFour, 0, 0);
				break;
			case SHORTCOMMENT:
				animation = new TranslateAnimation(posThree, posFour, 0, 0);
				break;
			default:
				return;
			}
			break;
		default:
			return;
		}
		animation.setFillAfter(true);
		animation.setDuration(300);
		cursor.startAnimation(animation);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.intorduce:
			pager.setCurrentItem(INTRODUCE);
			playAnimation(INTRODUCE, currentPos);
			currentPos = INTRODUCE;
			break;
		case R.id.image:
			pager.setCurrentItem(IMAGE);
			playAnimation(IMAGE, currentPos);
			currentPos = IMAGE;
			break;
		case R.id.shortcomment:
			pager.setCurrentItem(SHORTCOMMENT);
			playAnimation(SHORTCOMMENT, currentPos);
			currentPos = SHORTCOMMENT;
			break;
		case R.id.comment:
			pager.setCurrentItem(COMMENT);
			playAnimation(COMMENT, currentPos);
			currentPos = COMMENT;
			break;

		}
	}
}
