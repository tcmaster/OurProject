package com.android.joocola.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.joocola.R;

public class NearbyPersonFragment extends Fragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public android.view.View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.nearby_person, container, false);
		return view;
	};
}
