package com.android.doubanmovie.myfragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ComponentName;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.doubanmovie.R;
import com.android.doubanmovie.activity.MainActivity;
import com.android.doubanmovie.activity.MovieContentActivity;
import com.android.doubanmovie.cache.MyCache;
import com.android.doubanmovie.datasrc.DataSrc;
import com.android.doubanmovie.datasrc.ShowData;
import com.android.doubanmovie.layout.MyGridView;
import com.android.doubanmovie.myadapter.FurtureShowAdapter;
import com.android.doubanmovie.myadapter.MyGridAdapter;
import com.android.doubanmovie.provider.ProviderData;
import com.android.doubanmovie.service.BackupService;
import com.android.doubanmovie.utils.JsonUtils;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class ShowFragment extends Fragment implements OnItemClickListener,
		OnClickListener {
	private MyGridView showGrid;
	private MyGridView furtureGrid;
	private ShowData mydata;
	private ServiceConnection connection;
	private LoaderManager loaderManager;
	private MyGridAdapter adapter;
	private Button furtureShow;
	private LinearLayout layout;
	private ShowLoaderCallBack callBack = new ShowLoaderCallBack();
	BackupService myService;
	ActionBar actionBar;
	public Menu showMenu;
	private RequestQueue queue;
	private ImageCache cache;

	public static ShowFragment newInstance(Bundle bundle) {
		ShowFragment fm = new ShowFragment();
		fm.setHasOptionsMenu(true);
		fm.setArguments(bundle);
		return fm;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// 初始化LoaderManager
		loaderManager = getActivity().getLoaderManager();
		loaderManager.initLoader(1, null, callBack);
		connection = new ServiceConnection() {

			@Override
			public void onServiceDisconnected(ComponentName name) {

			}

			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				BackupService.MyBinder binder = (BackupService.MyBinder) service;
				myService = binder.getService();
				Log.v("test", "服务已经绑定");
				myService.backupData(Uri.parse(ProviderData.SHOW_ALL_URL));

			}
		};
		Intent intent = new Intent(getActivity(), BackupService.class);
		getActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE);
		actionBar = getActivity().getActionBar();
		super.onActivityCreated(savedInstanceState);
		cache = new ImageCache() {

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
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		showMenu = menu;
		getActivity().getActionBar().setTitle("豆瓣电影");
		if (this.isResumed()) {
			inflater.inflate(R.menu.main, menu);
			menu.findItem(R.id.action_mylocation).setTitle(
					((MainActivity) getActivity()).cityStr);
		}
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (this.isResumed()) {
			switch (item.getItemId()) {
			case R.id.action_search:
				break;
			case R.id.action_mylocation:
				break;
			case R.id.action_reflush:
				myService.refresh(Uri.parse(ProviderData.SHOW_ALL_URL));
				break;
			default:
				break;
			}
		}
		return true;
	}

	// 在该视图下需要动态添加一个按钮，点击按钮后还需要动态添加一个GridView
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.showfragment, container, false);
		showGrid = (MyGridView) view.findViewById(R.id.showGrid);
		furtureShow = (Button) view.findViewById(R.id.furturnShow);
		layout = (LinearLayout) view.findViewById(R.id.showlayout);
		furtureShow.setOnClickListener(this);
		dataInit();
		return view;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	private void dataInit() {
		if (myService != null) {
			mydata = myService.getShowData();
		}
		if (adapter == null && mydata != null) {
			adapter = new MyGridAdapter(mydata, getActivity());
			showGrid.setAdapter(adapter);
			if (furtureShow != null)
				furtureShow.setVisibility(View.VISIBLE);
			showGrid.setTag(mydata);
		}
		showGrid.setOnItemClickListener(ShowFragment.this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ShowData data = (ShowData) parent.getTag();
		Intent intent = new Intent(getActivity(), MovieContentActivity.class);
		String sId = (String) data.getSubjects().get(position).get("id");
		String sName = (String) data.getSubjects().get(position).get("title");
		Log.v("test", "we get the subject id" + sId);
		intent.putExtra("id", sId);
		intent.putExtra("movieName", sName);
		startActivity(intent);
	}

	@Override
	public void onDetach() {
		getActivity().unbindService(connection);
		super.onDetach();
	}

	private class ShowLoaderCallBack implements LoaderCallbacks<Cursor> {

		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args) {
			Log.v("test", "我的loader被初始化了！");
			CursorLoader loader = new CursorLoader(getActivity());
			// 查找所有的数据
			loader.setUri(Uri.parse(ProviderData.SHOW_ALL_URL));
			return loader;
		}

		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
			Log.v("test", "看来我的loader更新了");
			mydata = new ShowData();
			mydata.setSubjects();
			List<Map<String, Object>> newData = mydata.getSubjects();
			while (data.moveToNext()) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("title", data.getString(data.getColumnIndex("title")));
				Map<String, String> map2 = new HashMap<String, String>();
				map2.put("average",
						data.getString(data.getColumnIndex("average")));
				map2.put("stars", data.getString(data.getColumnIndex("stars")));
				map.put("rating", map2);
				Map<String, String> map3 = new HashMap<String, String>();
				map3.put("small", data.getString(data.getColumnIndex("small")));
				map.put("images", map3);
				map.put("id", data.getString(data.getColumnIndex("id")));
				newData.add(map);
			}
			if (furtureShow != null)
				furtureShow.setVisibility(View.VISIBLE);
			if (adapter == null) {
				adapter = new MyGridAdapter(mydata, getActivity());
				showGrid.setAdapter(adapter);
				showGrid.setTag(mydata);
			} else {
				adapter.bindData(mydata);
				showGrid.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				showGrid.setTag(mydata);
			}
		}

		@Override
		public void onLoaderReset(Loader<Cursor> loader) {

		}
	}

	public interface CityCallBack {
		public void getCity(String city);
	}

	@Override
	public void onClick(View v) {
		layout.removeView(layout.findViewById(R.id.furturnShow));
		layout.addView(LayoutInflater.from(getActivity())
				.inflate(R.layout.showlineitem, null, false)
				.findViewById(R.id.showline));
		furtureGrid = (MyGridView) LayoutInflater.from(getActivity())
				.inflate(R.layout.furturegrid, null, false)
				.findViewById(R.id.furtureShowGrid);
		layout.addView(furtureGrid);
		queue = Volley.newRequestQueue(getActivity());
		queue.add(new JsonObjectRequest(Method.GET, DataSrc.FURTURESHOWPATH,
				null, new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject arg0) {
						final ShowData fdata = JsonUtils.parseShowJSON(arg0
								.toString());
						FurtureShowAdapter adapter = new FurtureShowAdapter(
								fdata, getActivity(), cache, queue);
						furtureGrid.setAdapter(adapter);
						adapter.notifyDataSetChanged();
						furtureGrid
								.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(
											AdapterView<?> parent, View view,
											int position, long id) {
										Intent intent = new Intent(
												getActivity(),
												MovieContentActivity.class);
										String sId = (String) fdata
												.getSubjects().get(position)
												.get("id");
										String sName = (String) fdata
												.getSubjects().get(position)
												.get("title");
										Log.v("test", "we get the subject id"
												+ sId);
										intent.putExtra("id", sId);
										intent.putExtra("movieName", sName);
										startActivity(intent);
									}
								});
					}
				}, null));
		queue.start();
	}
}
