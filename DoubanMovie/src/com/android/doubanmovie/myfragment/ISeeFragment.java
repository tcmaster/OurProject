package com.android.doubanmovie.myfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.doubanmovie.R;

public class ISeeFragment extends Fragment {
	public static ISeeFragment newInstance(Bundle bundle) {
		ISeeFragment fm = new ISeeFragment();
		fm.setHasOptionsMenu(true);
		fm.setArguments(bundle);
		return fm;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		getActivity().getActionBar().setTitle("Œ“ø¥");
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.iseefragment, container, false);
		initISeeInterface();
		return view;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	private void initISeeInterface() {

	}
}
