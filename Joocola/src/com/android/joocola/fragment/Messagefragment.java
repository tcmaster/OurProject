package com.android.joocola.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.joocola.R;
import com.android.joocola.activity.ChatActivity;
import com.android.joocola.adapter.Fg_Chat_List_Adapter;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnItemClick;

/**
 * 
 * @author bb，lixiaosong
 * 
 */
public class Messagefragment extends Fragment {
	@ViewInject(R.id.chatlist)
	private ListView lv_message_list;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		ViewUtils.inject(getActivity());
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_message, container,
				false);
		ViewUtils.inject(this, view);
		initData();
		return view;
	}

	private void initData() {
		lv_message_list.setAdapter(new Fg_Chat_List_Adapter(getActivity()));
	}

	@OnItemClick(R.id.chatlist)
	public void onlistItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(getActivity(), ChatActivity.class);
		startActivity(intent);
	}
}
