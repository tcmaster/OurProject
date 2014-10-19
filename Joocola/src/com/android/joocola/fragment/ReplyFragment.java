package com.android.joocola.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.joocola.R;

/**
 * 回复系统消息界面
 * 
 * @author:bb
 * @see:
 * @since:
 * @copyright © joocola.com
 * @Date:2014年10月19日
 */
public class ReplyFragment extends Fragment {

	private static final String TAG = "ReplyFragment";

	private TextView mTempTextView;
	private ListView mListView;
	private String mUserID;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragmen_issuedynamic, container, false);
		mListView = (ListView) view.findViewById(R.id.issuedynamic_lv);
		return view;
	}

	public void setmUserID(String mUserID) {
		this.mUserID = mUserID;
	};
}
