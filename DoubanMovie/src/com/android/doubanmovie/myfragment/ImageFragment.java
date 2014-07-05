package com.android.doubanmovie.myfragment;

import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.android.doubanmovie.R;
import com.android.doubanmovie.activity.BigImageActivity;
import com.android.doubanmovie.cache.MyCache;
import com.android.doubanmovie.datasrc.DataSrc;
import com.android.doubanmovie.datasrc.ImageData;
import com.android.doubanmovie.myadapter.MyImageAdapter;
import com.android.doubanmovie.utils.JsonUtils;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

//这个界面用Volley来做
public class ImageFragment extends Fragment {
	GridView imageGridView;
	List<ImageData> datas;
	MyImageAdapter adapter;
	RequestQueue queue;
	ImageCache imgCache;

	public static ImageFragment newInstance(Bundle bundle) {
		ImageFragment fr = new ImageFragment();
		fr.setHasOptionsMenu(true);
		fr.setArguments(bundle);
		return fr;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		imgCache = new ImageCache() {

			@Override
			public void putBitmap(String arg0, Bitmap arg1) {
				MyCache.saveData(arg0, arg1);

			}

			@Override
			public Bitmap getBitmap(String arg0) {
				return MyCache.getData(arg0);
			}
		};
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.imagefragment, container, false);
		initImageInterface(view);
		initData();
		return view;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	private void initImageInterface(View view) {
		imageGridView = (GridView) view.findViewById(R.id.iImageGrid);
	}

	private void initData() {
		queue = Volley.newRequestQueue(getActivity());
		queue.add(new JsonObjectRequest(Method.GET, DataSrc.IMAGEPATHBEFORE
				+ getActivity().getIntent().getStringExtra("id")
				+ DataSrc.IMAGEPATHAFTER, null, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject arg0) {
				datas = JsonUtils.parseImageJSON(arg0);
				Log.v("test", datas.toString());
				adapter = new MyImageAdapter(datas, getActivity());
				adapter.setCache(imgCache, queue);
				imageGridView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				imageGridView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent(getActivity(),
								BigImageActivity.class);
						intent.putExtra("image", datas.get(position).image);
						intent.putExtra("name", datas.get(position).desc);
						intent.putExtra("position", position);
						Bundle bundle = new Bundle();
						for (int i = 0; i < datas.size(); i++) {
							String[] tempData = new String[2];
							tempData[0] = datas.get(i).image;
							tempData[1] = datas.get(i).desc;
							bundle.putStringArray(i + "", tempData);
						}
						intent.putExtra("data", bundle);
						startActivity(intent);
					}
				});

			}
		}, null));
		queue.start();
	}
}
