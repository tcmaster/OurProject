package com.android.doubanmovie.myfragment;

import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.doubanmovie.R;
import com.android.doubanmovie.httptask.GetJsonTask;
import com.android.doubanmovie.myadapter.MyIntroListAdp;

public class ShortCommentFragment extends Fragment {
	ListView scList;
	MyIntroListAdp adpter;

	public static ShortCommentFragment newInstance(Bundle bundle) {
		ShortCommentFragment fr = new ShortCommentFragment();
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
		View view = inflater.inflate(R.layout.shortcommentfragment, container,
				false);
		initShortInterface(view);
		initData();
		return view;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	private void initShortInterface(View view) {
		scList = (ListView) view.findViewById(R.id.shortcommentlist);
	}

	private void initData() {
		new GetJsonTask(GetJsonTask.SCTASK,
				new GetJsonTask.JsonCallBackAboutSC() {

					@Override
					public void getData(List<Map<String, String>> data) {
						adpter = new MyIntroListAdp(getActivity(), data);
						scList.setAdapter(adpter);
					}
				}, getActivity().getIntent().getStringExtra("id")).execTask();

	}
}
