package com.android.joocola.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.joocola.R;

/**
 * 附近界面
 * 
 * @author bb
 * 
 */
public class Nearbyfragment extends Fragment {
	private RelativeLayout issue_rl, person_rl;
	private View issue_line, person_line;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.fragment_nearby, container, false);
		initComponets(view);
		return view;
	}

	private void initComponets(View view) {
		issue_rl = (RelativeLayout) view.findViewById(R.id.nearby_issue);
		person_rl = (RelativeLayout) view.findViewById(R.id.nearby_person);
		issue_line = (View) view.findViewById(R.id.issue_line);
		person_line = (View) view.findViewById(R.id.person_line);
		issue_rl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FragmentManager fm = getChildFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				NearbyIssueFragment nearbyIssueFragment = new NearbyIssueFragment();
				ft.replace(R.id.fl_content, nearbyIssueFragment);
				ft.commit();
				issue_line.setVisibility(View.VISIBLE);
				person_line.setVisibility(View.INVISIBLE);
			}
		});
		person_rl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FragmentManager fm = getChildFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				NearbyPersonFragment nearbyIssueFragment = new NearbyPersonFragment();
				ft.replace(R.id.fl_content, nearbyIssueFragment);
				ft.commit();
				issue_line.setVisibility(View.INVISIBLE);
				person_line.setVisibility(View.VISIBLE);
			}
		});
		issue_rl.performClick();
	}
}
