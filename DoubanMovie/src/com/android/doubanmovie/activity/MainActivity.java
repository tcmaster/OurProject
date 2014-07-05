package com.android.doubanmovie.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.android.doubanmovie.R;
import com.android.doubanmovie.myfragment.CinemaFragment;
import com.android.doubanmovie.myfragment.ISeeFragment;
import com.android.doubanmovie.myfragment.ShowFragment;
import com.android.doubanmovie.myfragment.ShowFragment.CityCallBack;

public class MainActivity extends FragmentActivity implements OnClickListener,
		CityCallBack {
	ViewPager pager;
	FragmentManager manager;
	FragmentTransaction transaction;
	FragmentPagerAdapter adapter;
	private static final int SHOWFM = 0;
	private static final int CIMAFM = 1;
	private static final int ISEEFM = 2;
	int positionOne, positionTwo, positionThree;
	int currentPage;
	public String cityStr;
	ImageView view;
	Button showText, iSeeText, cinemaText;
	ActionBar actionBar;
	ShowFragment showFragment;
	CinemaFragment cinemaFragment;
	ISeeFragment iSeeFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		showText = (Button) findViewById(R.id.show);
		cinemaText = (Button) findViewById(R.id.cinema);
		iSeeText = (Button) findViewById(R.id.isee);
		showText.setOnClickListener(this);
		cinemaText.setOnClickListener(this);
		iSeeText.setOnClickListener(this);
		showFragment = ShowFragment.newInstance(savedInstanceState);
		cinemaFragment = CinemaFragment.newInstance(savedInstanceState);
		iSeeFragment = ISeeFragment.newInstance(savedInstanceState);
		cityStr = "��ȡ��";
		initWidth();
		initPager();
		initActionBar();
	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
				.setTitle("�����Ӱ")
				.setMessage("ȷ��Ҫ�˳�Ӧ����")
				.setPositiveButton("ȷ��",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								System.exit(1);

							}
						}).setNegativeButton("�ٹ�һ���", null).create().show();
	}

	private void initWidth() {

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		positionOne = 0;
		positionTwo = dm.widthPixels / 3;
		positionThree = positionTwo * 2;
		Log.v("test", positionOne + "");
		Log.v("test", positionTwo + "");
		Log.v("test", positionThree + "");
	}

	private Fragment getPageInstance(int index) {
		switch (index) {
		case SHOWFM:
			return showFragment;
		case CIMAFM:
			return cinemaFragment;
		case ISEEFM:
			return iSeeFragment;
		default:
			return null;
		}
	}

	private void initActionBar() {
		actionBar = getActionBar();
		actionBar.setLogo(R.drawable.ic_launcher);
		actionBar.setTitle("�����Ӱ");
		actionBar.show();
	}

	private void initPager() {
		view = (ImageView) findViewById(R.id.cursor);
		pager = (ViewPager) findViewById(R.id.viewpage);
		manager = getSupportFragmentManager();
		adapter = new FragmentPagerAdapter(manager) {
			@Override
			public int getCount() {
				return 3;
			}

			@Override
			public Fragment getItem(int arg0) {
				return getPageInstance(arg0);
			}
		};
		pager.setAdapter(adapter);
		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@SuppressLint("ResourceAsColor")
			@Override
			public void onPageSelected(int arg0) {
				playAnimation(arg0, currentPage);
				currentPage = arg0;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// ����
				// arg0 0,1,2
				// arg1 0 - 1
				// arg2 0 - ��Ļ���
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

	}

	@Override
	public void onClick(View v) {
		Log.v("test", "it 's be clicked");
		Log.v("test", "id is" + v.getId());
		Log.v("test", R.id.show + "");
		Log.v("test", R.id.cinema + "");
		Log.v("test", R.id.isee + "");
		switch (v.getId()) {
		case R.id.show:
			Log.v("test", "1");
			playAnimation(SHOWFM, currentPage);
			currentPage = SHOWFM;
			pager.setCurrentItem(SHOWFM);
			break;
		case R.id.cinema:
			Log.v("test", "2");
			playAnimation(CIMAFM, currentPage);
			currentPage = CIMAFM;
			pager.setCurrentItem(CIMAFM);
			break;
		case R.id.isee:
			Log.v("test", "3");
			playAnimation(ISEEFM, currentPage);
			currentPage = ISEEFM;
			pager.setCurrentItem(ISEEFM);
			break;
		default:
			break;
		}
	}

	private void playAnimation(int pos, int currentPos) {
		Animation animation = null;
		switch (pos) {
		case SHOWFM:
			switch (currentPage) {
			case ISEEFM:
				animation = new TranslateAnimation(positionThree, positionOne,
						0, 0);
				break;
			case CIMAFM:
				animation = new TranslateAnimation(positionTwo, positionOne, 0,
						0);
				break;
			default:
				return;
			}
			break;
		case CIMAFM:
			switch (currentPage) {
			case SHOWFM:
				animation = new TranslateAnimation(positionOne, positionTwo, 0,
						0);
				break;
			case ISEEFM:
				animation = new TranslateAnimation(positionThree, positionTwo,
						0, 0);
				break;
			default:
				return;
			}
			break;
		case ISEEFM:
			switch (currentPage) {
			case SHOWFM:
				animation = new TranslateAnimation(positionOne, positionThree,
						0, 0);
				break;
			case CIMAFM:
				animation = new TranslateAnimation(positionTwo, positionThree,
						0, 0);
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
		view.startAnimation(animation);
	}

	@Override
	public void getCity(String city) {
		Log.v("test", "����ص�");
		if (showFragment != null) {
			Log.v("test", "fragment��Ϊ��");
			if (showFragment.showMenu != null) {
				Log.v("test", "showmenu��Ϊ��");
				MenuItem item = showFragment.showMenu
						.findItem(R.id.action_mylocation);
				if (item != null) {
					cityStr = city;
					item.setTitle(cityStr);

				}
			}
		}
	}

}
