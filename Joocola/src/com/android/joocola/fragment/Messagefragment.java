package com.android.joocola.fragment;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.joocola.R;
import com.android.joocola.activity.ChatActivity;
import com.android.joocola.adapter.Fg_Chat_List_Adapter;
import com.android.joocola.app.JoocolaApplication;
import com.android.joocola.entity.ChatOfflineInfo;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
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
	private DbUtils db;
	private List<ChatOfflineInfo> infos;
	private Set<String> listInfo;
	private Fg_Chat_List_Adapter adapter;
	private boolean isFirst = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		db = JoocolaApplication.getInstance().getDB();
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
		updateData();
		if (isFirst)
			isFirst = !isFirst;
		return view;
	}

	private void updateData() {
		listInfo = new HashSet<String>();
		if (db == null)
			db = JoocolaApplication.getInstance().getDB();
		try {
			infos = db.findAll(Selector.from(ChatOfflineInfo.class).where(
					"user",
					"=",
					JoocolaApplication.getInstance().getUserInfo()
							.getUserName()));
			if (infos != null) {
				for (int i = 0; i < infos.size(); i++) {
					/**
					 * 过滤重复的数据
					 */
					listInfo.add(infos.get(i).getKey());
				}
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
		if (adapter == null) {
			adapter = new Fg_Chat_List_Adapter(getActivity(),
					listInfo.toArray(new String[] {}));
			lv_message_list.setAdapter(adapter);
		} else {
			adapter.bindData(listInfo.toArray(new String[] {}));
			lv_message_list.setAdapter(adapter);
		}
	}

	@OnItemClick(R.id.chatlist)
	public void onlistItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(getActivity(), ChatActivity.class);
		intent.putExtra("nickName",
				((TextView) view.findViewById(R.id.ml_nickName_tv)).getText());
		startActivity(intent);
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		if (isVisibleToUser && !isFirst) {
			updateData();
		}
	}
}
