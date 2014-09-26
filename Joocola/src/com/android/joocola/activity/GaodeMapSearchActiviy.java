package com.android.joocola.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.OnMapClickListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.android.joocola.R;
import com.android.joocola.utils.AMapUtil;
import com.android.joocola.utils.Utils;

public class GaodeMapSearchActiviy extends Activity implements OnGeocodeSearchListener, OnMapClickListener {

	private MapView mapView;
	private AMap aMap;
	private ProgressDialog progDialog = null;
	private GeocodeSearch geocoderSearch;
	private String locationCity;
	private Marker geoMarker;
	private EditText editText;
	private Button searchButton;
	private ActionBar mActionBar;
	private double locationX;
	private String mAddress = "";
	private double locationY;
	private String addressName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gaodesearch);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		Intent intent = getIntent();
		locationCity = intent.getStringExtra("locationCity");
		init();
		mActionBar = getActionBar();
		mActionBar.setHomeButtonEnabled(true);
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setDisplayShowHomeEnabled(true);
		mActionBar.setDisplayShowCustomEnabled(true);
		mActionBar.setCustomView(R.layout.gaode_search_actionbar);
		editText = (EditText) mActionBar.getCustomView().findViewById(R.id.search_et);
		searchButton = (Button) mActionBar.getCustomView().findViewById(R.id.search_bt);
		searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String dizhi = editText.getText().toString();
				getLatlon(dizhi);
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case android.R.id.home:
			goBack();
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		goBack();
	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			aMap.setOnMapClickListener(this);
			geoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
		}
		;
		geocoderSearch = new GeocodeSearch(this);
		geocoderSearch.setOnGeocodeSearchListener(this);
		progDialog = new ProgressDialog(this);

	}

	private void goBack() {
		Intent intent = new Intent();
		intent.putExtra("locationX", locationX);
		intent.putExtra("locationY", locationY);
		intent.putExtra("address", mAddress);
		setResult(10, intent);
		finish();
	}

	/**
	 * 响应地理编码
	 */
	public void getLatlon(final String name) {
		showDialog();
		GeocodeQuery query = new GeocodeQuery(name, locationCity);// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
		geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
	}

	@Override
	public void onGeocodeSearched(GeocodeResult result, int rCode) {
		dismissDialog();
		if (rCode == 0) {
			if (result != null && result.getGeocodeAddressList() != null && result.getGeocodeAddressList().size() > 0) {
				GeocodeAddress address = result.getGeocodeAddressList().get(0);
				aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(AMapUtil.convertToLatLng(address.getLatLonPoint()), 15));
				geoMarker.setPosition(AMapUtil.convertToLatLng(address.getLatLonPoint()));
				locationX = address.getLatLonPoint().getLatitude();
				locationY = address.getLatLonPoint().getLongitude();
				mAddress = address.getFormatAddress();
				addressName = "经纬度值:" + address.getLatLonPoint() + "\n位置描述:" + address.getFormatAddress();
				Utils.toast(GaodeMapSearchActiviy.this, addressName);
			} else {
				Utils.toast(GaodeMapSearchActiviy.this, "没找到这个位置");
			}

		} else if (rCode == 27) {
			Utils.toast(GaodeMapSearchActiviy.this, "网络错误");
		} else if (rCode == 32) {
			Utils.toast(GaodeMapSearchActiviy.this, "key有问题");
		} else {
			Utils.toast(GaodeMapSearchActiviy.this, "错误" + rCode);
		}
	}

	@Override
	public void onRegeocodeSearched(RegeocodeResult arg0, int arg1) {
		Log.e("bb", "onRegeocodeSearched");
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
		progDialog.show();
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
	public void onMapClick(LatLng arg0) {

	}
}
