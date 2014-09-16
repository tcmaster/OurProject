package com.android.joocola.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.joocola.MainActivity;
import com.android.joocola.R;

/**
 * 引导B页面 也没什么太大用。。
 * 
 * @author bb
 * 
 */
public class GuideCFragment extends Fragment implements OnClickListener {

	private TextView login;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.guidec, container, false);
		login = (TextView) view.findViewById(R.id.guide_login);
		login.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.guide_login:
			Intent login = new Intent(getActivity(), MainActivity.class);
			startActivity(login);
			getActivity().finish();

			break;

		default:
			break;
		}
	}

}
