package com.android.joocola.activity;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.AMap.OnCameraChangeListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.poisearch.PoiSearch.SearchBound;
import com.android.joocola.R;
import com.android.joocola.utils.AMapUtil;
import com.android.joocola.utils.Constants;
import com.android.joocola.utils.Utils;

public class GaodeMapActivity extends BaseActivity implements OnGeocodeSearchListener, OnClickListener,
		OnPoiSearchListener, InfoWindowAdapter, OnMarkerClickListener, OnCameraChangeListener {

	// OnMapClickListener,
	private MapView mapView;
	private AMap aMap;
	private ProgressDialog progDialog = null;
	private GeocodeSearch geocoderSearch;
	private String addressName;
	private Marker regeoMarker;
	private String address;
	private String cityString;
	private String[] itemDeep = { "餐饮", "景区", "酒店", "影院" };
	private double locationX;
	private double locationY;
	private LatLonPoint latLonPoint;
	private String locationCity;
	private SharedPreferences sharedPreferences;
	private Spinner mSpinner;
	private ListView mListView;
	private PoiSearch poiSearch;
	private PoiSearch.Query mQuery;// Poi查询条件类
	private ArrayList<String> mSearchList = new ArrayList<String>();
	private ArrayAdapter<String> mSearchAdapter;
	private TextView addressTv;
	private boolean isChooseAddress = false;

	private LatLng latLng;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.issue_gaodeditu);
		Intent intent = getIntent();
		isChooseAddress = intent.getBooleanExtra("isChooseAddress", false);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		sharedPreferences = getSharedPreferences(Constants.LOGIN_PREFERENCE, Context.MODE_PRIVATE);
		locationCity = sharedPreferences.getString("LocationCity", "北京");
		getActionBar().setTitle(locationCity);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(true);
		init();
		String LocationXStr = sharedPreferences.getString("LocationX", "");
		String locationYStr = sharedPreferences.getString("LocationY", "");
		if (isChooseAddress) {
			locationX = intent.getDoubleExtra("LocationX", 0.0);
			locationY = intent.getDoubleExtra("LocationY", 0.0);
			locationCity = intent.getStringExtra("LocationCityName");
			latLng = new LatLng(locationX, locationY);
			latLonPoint = AMapUtil.convertToLatLonPoint(latLng);
			getAddress(latLonPoint);
			doSearchQuery(latLonPoint);

			regeoMarker.setPosition(latLng);
		} else {
			if (!TextUtils.isEmpty(LocationXStr) && !TextUtils.isEmpty(locationYStr)) {
				locationX = Double.parseDouble(LocationXStr);
				locationY = Double.parseDouble(locationYStr);
				latLng = new LatLng(locationX, locationY);
				latLonPoint = AMapUtil.convertToLatLonPoint(latLng);
				getAddress(latLonPoint);
				doSearchQuery(latLonPoint);
				regeoMarker.setPosition(latLng);
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.search, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case R.id.action_search:
			Intent intent = new Intent();
			intent.setClass(GaodeMapActivity.this, GaodeMapSearchActiviy.class);
			intent.putExtra("locationCity", locationCity);
			startActivityForResult(intent, 10);
			break;
		case android.R.id.home:
			backToIssue();
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			// aMap.setOnMapClickListener(this);
			aMap.setOnCameraChangeListener(this);
			aMap.setOnMarkerClickListener(this);// 添加点击marker监听事件
			aMap.setInfoWindowAdapter(this);// 添加显示infowindow监听事件
			// geoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f,
			// 0.5f).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
			regeoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
		}
		;
		geocoderSearch = new GeocodeSearch(this);
		geocoderSearch.setOnGeocodeSearchListener(this);
		progDialog = new ProgressDialog(this);
		mListView = (ListView) this.findViewById(R.id.map_listview);
		mSpinner = (Spinner) this.findViewById(R.id.map_spinner);
		ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, itemDeep);
		mSpinner.setAdapter(mAdapter);
		mSearchAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mSearchList);
		mListView.setAdapter(mSearchAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String name = mSearchList.get(position);
				address = address + " - " + name;
				backToIssue();
			}
		});
		addressTv = (TextView) this.findViewById(R.id.gaode_address);

	}

	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 显示进度条对话框
	 */
	public void showDialog() {
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(true);
		progDialog.setMessage("正在获取地址");
		// progDialog.show();
	}

	/**
	 * 响应地理编码
	 */
	public void getLatlon(final String name) {
		showDialog();
		GeocodeQuery query = new GeocodeQuery(name, locationCity);// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode
		// 需要控制第2个参数,第2个参数为城市编码。
		geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
	}

	/**
	 * 响应地理编码
	 */
	public void getLatlon(final String name, final String city) {
		showDialog();
		GeocodeQuery query = new GeocodeQuery(name, city);// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode
		// 需要控制第2个参数,第2个参数为城市编码。
		geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
	}

	/**
	 * 响应逆地理编码
	 */
	public void getAddress(final LatLonPoint latLonPoint) {
		// showDialog();
		RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
		geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
	}

	/**
	 * 地理编码查询回调
	 */
	@Override
	public void onGeocodeSearched(GeocodeResult result, int rCode) {
		// dismissDialog();
		// if (rCode == 0) {
		// if (result != null && result.getGeocodeAddressList() != null &&
		// result.getGeocodeAddressList().size() > 0) {
		// GeocodeAddress address = result.getGeocodeAddressList().get(0);
		// aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(AMapUtil.convertToLatLng(address.getLatLonPoint()),
		// 15));
		// // regeoMarker.setPosition(AMapUtil.convertToLatLng(address.getLatLonPoint()));
		// // locationX = address.getLatLonPoint().getLatitude();
		// // locationY = address.getLatLonPoint().getLongitude();
		// // addressName = "经纬度值:" + address.getLatLonPoint() + "\n位置描述:" + address.getFormatAddress();
		//
		// Utils.toast(GaodeMapActivity.this, addressName);
		// } else {
		// Utils.toast(GaodeMapActivity.this, "没找到这个位置");
		// }
		//
		// } else if (rCode == 27) {
		// Utils.toast(GaodeMapActivity.this, "网络错误");
		// } else if (rCode == 32) {
		// Utils.toast(GaodeMapActivity.this, "key有问题");
		// } else {
		// Utils.toast(GaodeMapActivity.this, "错误" + rCode);
		// }
	}

	/**
	 * 点击地图后走这
	 */
	@Override
	public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
		// dismissDialog();
		if (rCode == 0) {
			if (result != null && result.getRegeocodeAddress() != null && result.getRegeocodeAddress().getFormatAddress() != null) {
				addressName = result.getRegeocodeAddress().getFormatAddress();
				// aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(AMapUtil.convertToLatLng(latLonPoint),
				// 15));
				// regeoMarker.setPosition(AMapUtil.convertToLatLng(latLonPoint));
				address = addressName;
				addressTv.setText(address);
				// doSearchQuery(result.getRegeocodeQuery().getPoint());
				// Toast.makeText(GaodeMapActivity.this, address, Toast.LENGTH_SHORT).show();
			} else {
				Utils.toast(GaodeMapActivity.this, "无此地址");
			}
		} else if (rCode == 27) {
			Utils.toast(GaodeMapActivity.this, "网络有问题");
		} else if (rCode == 32) {
			Utils.toast(GaodeMapActivity.this, "key有问题");
		} else {

			Utils.toast(GaodeMapActivity.this, rCode + "");
		}

	}

	@Override
	public void onBackPressed() {
		backToIssue();
	}

	private void backToIssue() {
		Intent intent = new Intent();
		intent.putExtra("locationX", locationX);
		intent.putExtra("locationY", locationY);
		intent.putExtra("address", address);
		intent.putExtra("LocationCityName", locationCity);
		setResult(40, intent);
		finish();
	}

	/**
	 * 隐藏进度条对话框
	 */
	public void dismissDialog() {
		if (progDialog != null) {
			progDialog.dismiss();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		default:
			break;
		}
	}

	// @Override
	// public void onMapClick(LatLng arg0) {
	// latLonPoint = AMapUtil.convertToLatLonPoint(arg0);
	// locationX = arg0.latitude;
	// locationY = arg0.longitude;
	// getAddress(latLonPoint);
	// }z

	@Override
	public void onPoiItemDetailSearched(PoiItemDetail arg0, int arg1) {

	}

	@Override
	public void onPoiSearched(PoiResult result, int rCode) {
		dismissDialog();
		if (rCode == 0) {
			if (result != null && result.getQuery() != null) {// 搜索poi的结果
				mSearchList.clear();
				for (int i = 0; i < result.getPois().size(); i++) {
					// Log.e("bb", "getTitle" + result.getPois().get(i).getTitle());
					// Log.e("bb", "getLatitude" + result.getPois().get(i).getLatLonPoint().getLongitude());
					// Log.e("bb", "getLongitude" + result.getPois().get(i).getLatLonPoint().getLatitude());
					mSearchList.add(result.getPois().get(i).getTitle());
				}
				mSearchAdapter.notifyDataSetChanged();

			} else {
				Utils.toast(GaodeMapActivity.this, "对不起，没有搜索到相关数据！");
			}
		} else if (rCode == 27) {
			Utils.toast(GaodeMapActivity.this, "搜索失败,请检查网络连接！");
		} else if (rCode == 32) {
			Utils.toast(GaodeMapActivity.this, "key验证无效！");
		} else {
			Utils.toast(GaodeMapActivity.this, getString(R.string.error_other) + rCode);
		}
	}

	@Override
	public View getInfoContents(Marker arg0) {
		return null;
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		return null;
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		return false;
	}

	/**
	 * 开始进行poi搜索
	 */
	protected void doSearchQuery(LatLonPoint lp) {
		mQuery = new PoiSearch.Query("", mSpinner.getSelectedItem().toString(), "北京市");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
		mQuery.setPageSize(30);// 设置每页最多返回多少条poiitem

		if (lp != null) {
			poiSearch = new PoiSearch(this, mQuery);
			poiSearch.setOnPoiSearchListener(this);
			poiSearch.setBound(new SearchBound(lp, 2000, true));//
			poiSearch.searchPOIAsyn();// 异步搜索
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 10) {
			String backAddress = data.getStringExtra("address");
			if (TextUtils.isEmpty(backAddress)) {
				return;
			}
			locationX = data.getDoubleExtra("locationX", 123.123456);
			locationY = data.getDoubleExtra("locationY", 123.123456);
			address = backAddress;
			backToIssue();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onCameraChange(CameraPosition arg0) {

	}

	@Override
	public void onCameraChangeFinish(CameraPosition cameraPosition) {
		locationX = cameraPosition.target.latitude;
		locationY = cameraPosition.target.longitude;
		latLng = new LatLng(locationX, locationY);
		latLonPoint = AMapUtil.convertToLatLonPoint(latLng);
		getAddress(latLonPoint);
		doSearchQuery(latLonPoint);
		regeoMarker.setPosition(latLng);
	}

}