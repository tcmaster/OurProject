package com.android.joocola.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.android.joocola.R;
import com.android.joocola.utils.AMapUtil;
import com.android.joocola.utils.Constans;
import com.android.joocola.utils.Utils;

public class GaodeMapActivity extends BaseActivity implements
		OnGeocodeSearchListener, OnClickListener, OnMapClickListener {
	private MapView mapView;
	private AMap aMap;
	private ProgressDialog progDialog = null;
	private GeocodeSearch geocoderSearch;
	private String addressName;
	private Marker geoMarker;
	private Marker regeoMarker;
	private String address;
	private Button searchBtn;
	private EditText editText, cityEdit;
	private String cityString;
 
	private double locationX;
	private double locationY;
	private LatLonPoint latLonPoint;
	private String locationCity;
	private SharedPreferences sharedPreferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.issue_gaodeditu);
		getActionBar().hide();
		Intent intent = getIntent();
		address = intent.getStringExtra("address");
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		sharedPreferences = getSharedPreferences(Constans.LOGIN_PREFERENCE,
				Context.MODE_PRIVATE);
		locationCity = sharedPreferences.getString("LocationCity", "北京");
		init();
		getLatlon(address);

	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			aMap.setOnMapClickListener(this);
			geoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
			regeoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
		}
		editText = (EditText) this.findViewById(R.id.issue_map_edit);
		searchBtn = (Button) this.findViewById(R.id.issue_map_searchbtn);
		searchBtn.setOnClickListener(this);
		cityEdit = (EditText) this.findViewById(R.id.issue_city_edit);
		cityEdit.setText(locationCity);
		geocoderSearch = new GeocodeSearch(this);
		geocoderSearch.setOnGeocodeSearchListener(this);
		progDialog = new ProgressDialog(this);

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
	 * 响应地理编码
	 */
	public void getLatlon(final String name) {
		showDialog();
		GeocodeQuery query = new GeocodeQuery(name, "010");// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode
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
		showDialog();
		RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
				GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
		geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
	}
	
	/**
	 * 地理编码查询回调
	 */
	@Override
	public void onGeocodeSearched(GeocodeResult result, int rCode) {
		dismissDialog();
		if (rCode == 0) {
			if (result != null && result.getGeocodeAddressList() != null
					&& result.getGeocodeAddressList().size() > 0) {
				GeocodeAddress address = result.getGeocodeAddressList().get(0);
				aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
						AMapUtil.convertToLatLng(address.getLatLonPoint()), 15));
				geoMarker.setPosition(AMapUtil.convertToLatLng(address
						.getLatLonPoint()));
				Log.e("address.getLatLonPoint()", address.getLatLonPoint() + "");
				locationX = address.getLatLonPoint().getLatitude();
				locationY = address.getLatLonPoint().getLongitude();
				addressName = "经纬度值:" + address.getLatLonPoint() + "\n位置描述:"
						+ address.getFormatAddress();
				Utils.toast(GaodeMapActivity.this, addressName);
			} else {
				Utils.toast(GaodeMapActivity.this, "没找到这个位置");
			}

		} else if (rCode == 27) {
			Utils.toast(GaodeMapActivity.this, "网络错误");
		} else if (rCode == 32) {
			Utils.toast(GaodeMapActivity.this, "key有问题");
		} else {
			Utils.toast(GaodeMapActivity.this, "错误" + rCode);
		}
	}

	@Override
	public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
		dismissDialog();
		if (rCode == 0) {
			if (result != null && result.getRegeocodeAddress() != null
					&& result.getRegeocodeAddress().getFormatAddress() != null) {
				addressName = result.getRegeocodeAddress().getFormatAddress()
						+ "附近";
				aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
						AMapUtil.convertToLatLng(latLonPoint), 15));
				regeoMarker.setPosition(AMapUtil.convertToLatLng(latLonPoint));
				address = addressName;
				editText.setText(addressName);
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
		Intent intent = new Intent();
		intent.putExtra("locationX", locationX);
		intent.putExtra("locationY", locationY);
		intent.putExtra("address", address);
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
		case R.id.issue_map_searchbtn:
			address = editText.getText().toString();
			cityString = cityEdit.getText().toString();
			getLatlon(address, cityString);
			break;

		default:
			break;
		}
	}

	@Override
	public void onMapClick(LatLng arg0) {
		latLonPoint = AMapUtil.convertToLatLonPoint(arg0);
		locationX = arg0.latitude;
		locationY = arg0.longitude;
		getAddress(latLonPoint);
	}
}