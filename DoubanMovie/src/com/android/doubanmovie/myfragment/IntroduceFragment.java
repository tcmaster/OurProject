package com.android.doubanmovie.myfragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.doubanmovie.R;
import com.android.doubanmovie.cache.MyCache;
import com.android.doubanmovie.datasrc.IntroduceData;
import com.android.doubanmovie.httptask.GetImageTask;
import com.android.doubanmovie.httptask.GetImageTask.ImageCallBack;
import com.android.doubanmovie.httptask.GetJsonTask;
import com.android.doubanmovie.layout.MyGridView;
import com.android.doubanmovie.layout.MyListView;
import com.android.doubanmovie.myadapter.MyIntroGridAdp;
import com.android.doubanmovie.myadapter.MyIntroListAdp;

public class IntroduceFragment extends Fragment {
	// ��ӰͼƬ
	private ImageView videoView;
	// ����
	private RatingBar ratingBar;
	// ����
	private TextView score;
	// ��������?
	private TextView people;
	// ����
	private TextView date;
	// ��Ӱ����
	private TextView movieType;
	// ����
	private TextView movieCountry;
	// ��Ӱ����
	private TextView introduce;
	// Ӱ������Ϣ
	private MyGridView gridView;
	// ��������
	private MyListView listView;
	// ����
	private IntroduceData iData;
	// ���������Ƶ
	private ImageButton playB;
	private Button wantSee, haveSee;
	// �ж���ǰ�����ı�������
	boolean isFull = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public static IntroduceFragment newInstance(Bundle bundle) {
		IntroduceFragment fr = new IntroduceFragment();
		fr.setHasOptionsMenu(true);
		fr.setArguments(bundle);
		return fr;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.introducefragment, container,
				false);
		initIntroduceInterface(view);
		// �����������³�ʼ������
		initData();
		// �����������³�ʼ������(�ж�����)
		initDataByOffline();
		return view;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	private void initIntroduceInterface(View view) {
		videoView = (ImageView) view.findViewById(R.id.videoimg);
		ratingBar = (RatingBar) view.findViewById(R.id.iratingbar);
		score = (TextView) view.findViewById(R.id.iscoretext);
		people = (TextView) view.findViewById(R.id.commentnum);
		date = (TextView) view.findViewById(R.id.showdate);
		movieType = (TextView) view.findViewById(R.id.movietype);
		movieCountry = (TextView) view.findViewById(R.id.moviecountry);
		introduce = (TextView) view.findViewById(R.id.introduceText);
		gridView = (MyGridView) view.findViewById(R.id.peopleGrid);
		listView = (MyListView) view.findViewById(R.id.peopleList);
		playB = (ImageButton) view.findViewById(R.id.playB);
		wantSee = (Button) view.findViewById(R.id.iwantsee);
		haveSee = (Button) view.findViewById(R.id.ihavesee);
	}

	// �����������³�ʼ�������ؼ�������
	private void initData() {
		new GetJsonTask(GetJsonTask.INTRODUCETASK,
				new GetJsonTask.JsonCallBackAboutIntroduce() {

					@Override
					public void getData(IntroduceData data) {
						iData = data;// ��ȡ�ý������������
						Log.v("new", iData.toString());
						ratingBar.setMax(50);
						ratingBar.setProgress(Integer.valueOf(iData.rating
								.get("stars")));
						videoView.setVisibility(View.VISIBLE);
						ratingBar.setVisibility(View.VISIBLE);
						score.setText(iData.rating.get("average"));
						score.setVisibility(View.VISIBLE);
						people.setText("(" + iData.ratings_count + "������)");
						people.setVisibility(View.VISIBLE);
						date.setText(iData.mainland_pubdate);
						date.setVisibility(View.VISIBLE);
						movieType.setText(iData.genres);
						movieType.setVisibility(View.VISIBLE);
						movieCountry.setText(iData.countries);
						movieCountry.setVisibility(View.VISIBLE);
						introduce.setText(iData.summary);
						introduce.setClickable(true);
						introduce.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								isFull = !isFull;
								if (!isFull)
									introduce.setMaxLines(5);
								else
									introduce.setMaxLines(999);
							}
						});
						introduce.setVisibility(View.VISIBLE);
						Bitmap bm = MyCache.getData("iData.images");
						if (bm == null) {
							new GetImageTask(100, 120).execTask(iData.images,
									null, new ImageCallBack() {

										@Override
										public void getData(String path,
												ViewGroup parent, Bitmap bm) {
											videoView.setImageBitmap(bm);
											MyCache.saveData(path, bm);
										}
									}, false);
						} else {
							videoView.setImageBitmap(bm);
						}
						playB.setVisibility(View.VISIBLE);
						wantSee.setVisibility(View.VISIBLE);
						haveSee.setVisibility(View.VISIBLE);
						MyIntroGridAdp gadp = new MyIntroGridAdp(getActivity(),
								iData.writers, iData.casts, iData.directors);
						gridView.setAdapter(gadp);
						MyIntroListAdp adp2 = new MyIntroListAdp(getActivity(),
								iData.popular_comments);
						listView.setAdapter(adp2);
						adp2.notifyDataSetChanged();
						playB.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// ������ò�����Ƶ�����������Ƶ
								Intent intent = new Intent(Intent.ACTION_VIEW);
								intent.setDataAndType(
										Uri.parse(iData.trailer_urls),
										"video/*");
								intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
								startActivity(intent);
							}
						});
						// HeightUtils.setViewGroupHeight(listView);
					}
				}, getActivity().getIntent().getStringExtra("id")).execTask();
	}

	private void initDataByOffline() {

	}
}
