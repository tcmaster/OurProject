package com.android.doubanmovie.myfragment;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.doubanmovie.R;
import com.android.doubanmovie.activity.CommentDetail;
import com.android.doubanmovie.httptask.GetJsonTask;
import com.android.doubanmovie.myadapter.MyCDAdapter;

public class CommentFragment extends Fragment {
	ListView cSList;
	MyCDAdapter adapter;
	List<Map<String, String>> sdData;

	public static CommentFragment newInstance(Bundle bundle) {
		CommentFragment fr = new CommentFragment();
		fr.setHasOptionsMenu(true);
		fr.setArguments(bundle);
		return fr;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.commentfragment, container, false);
		initCommentInterface(view);
		initData();
		return view;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	private void initCommentInterface(View view) {
		cSList = (ListView) view.findViewById(R.id.commentList);
	}

	private void initData() {
		new GetJsonTask(GetJsonTask.CDTASK,
				new GetJsonTask.JsonCallBackAboutSC() {

					@Override
					public void getData(List<Map<String, String>> data) {
						adapter = new MyCDAdapter(data, getActivity());
						cSList.setAdapter(adapter);
						sdData = data;
					}
				}, getActivity().getIntent().getStringExtra("id")).execTask();
		cSList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(), CommentDetail.class);
				Map<String, String> data = sdData.get(position);
				intent.putExtra("value", data.get("value"));
				intent.putExtra("useful_count", data.get("useful_count"));
				intent.putExtra("title", data.get("title"));
				intent.putExtra("created_at", data.get("created_at"));
				intent.putExtra("name", data.get("name"));
				intent.putExtra("content", data.get("content"));
				startActivity(intent);
			}
		});
	}
}
