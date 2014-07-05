package com.android.doubanmovie.myfragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.AMap.OnMarkerDragListener;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.poisearch.PoiSearch.SearchBound;
import com.android.doubanmovie.R;
import com.android.doubanmovie.layout.MyListView;
import com.android.doubanmovie.layout.MyTextView;
import com.android.doubanmovie.myadapter.MyCinemaItemAdapter;
import com.android.doubanmovie.myfragment.ShowFragment.CityCallBack;

public class CinemaFragment extends Fragment implements OnClickListener {
	// ��ȥ�ĵ�ӰԺ
	LinearLayout oftenContainer;
	// ����ĵ�ӰԺ
	MyListView nearbyContainer;
	// ��ͼҳ��
	MapView mapView;
	// ���ֵ�ʵ��
	View view;
	// ��ͼ��
	AMap aMap;
	// ��ͼ������
	MapHelper helper;
	// �����ͼ��������
	MyCinemaItemAdapter adapter;

	// ���ڴ�ŵ�ӰԺ����Ϣ
	private List<Map<String, String>> cinemaList;

	// ��ǰ���λ��
	AMapLocation currentLocation;
	private CityCallBack callBack;

	public static CinemaFragment newInstance(Bundle bundle) {
		CinemaFragment fm = new CinemaFragment();
		fm.setHasOptionsMenu(true);
		fm.setArguments(bundle);
		return fm;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		callBack = (CityCallBack) getActivity();
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.cinemafragment, container, false);
		initCinemaInterface();
		initMap(savedInstanceState);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		mapView.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		mapView.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		getActivity().getActionBar().setTitle("�ҵ�λ��");
		super.onCreateOptionsMenu(menu, inflater);
	}

	private void initCinemaInterface() {
		oftenContainer = (LinearLayout) view.findViewById(R.id.Cinema1line);
		nearbyContainer = (MyListView) view.findViewById(R.id.nearbyCinemaList);
		mapView = (MapView) view.findViewById(R.id.map);
		nearbyContainer.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				final Map<String, String> itemInfo = cinemaList.get(position);
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				final String[] items = new String[3];
				items[0] = "ӰԺ��� : " + itemInfo.get("name");
				items[1] = "��ַ : " + itemInfo.get("address");
				items[2] = "�绰 : " + itemInfo.get("tel");
				builder.setTitle("��ӰԺ������Ϣ")
						.setItems(items, null)
						.setPositiveButton("���Ϊ��ȥӰԺ", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (oftenContainer.findViewWithTag(itemInfo
										.get("name")) == null) {
									View view = LayoutInflater.from(
											getActivity()).inflate(
											R.layout.cinemaitem, null, false);
									((MyTextView) view
											.findViewById(R.id.cinemaName))
											.setText(itemInfo.get("name"));
									((TextView) view
											.findViewById(R.id.distance))
											.setText((Double.valueOf(itemInfo
													.get("distance")) / 1000)
													+ "����");
									view.setTag(itemInfo.get("name"));
									view.setClickable(true);
									view.setOnClickListener(new View.OnClickListener() {

										@Override
										public void onClick(View v) {
											AlertDialog.Builder builder = new AlertDialog.Builder(
													getActivity());
											builder.setTitle("��ӰԺ������Ϣ")
													.setItems(items, null)
													.setPositiveButton(
															"����ڵ�ͼ��",
															new OnClickListener() {

																@Override
																public void onClick(
																		DialogInterface dialog,
																		int which) {
																	MarkerOptions marker = new MarkerOptions();
																	marker.position(new LatLng(
																			Double.valueOf(itemInfo
																					.get("Latitude")),
																			Double.valueOf(itemInfo
																					.get("Longitude"))));
																	marker.title(itemInfo
																			.get("name"));
																	marker.perspective(true);
																	marker.draggable(true);
																	aMap.addMarker(marker);
																	Toast.makeText(
																			getActivity(),
																			"��ǳɹ��������ʾ��ӰԺ��ƣ���ҷɾ��ñ��",
																			Toast.LENGTH_SHORT)
																			.show();

																}
															})
													.setNegativeButton(
															"�˳��ý���", null)
													.setCancelable(false)
													.create().show();
										}
									});
									oftenContainer.addView(view);
								} else {
									Toast.makeText(getActivity(),
											"�õ�ӰԺ�Ѿ�����", Toast.LENGTH_SHORT)
											.show();
								}

							}
						}).setNegativeButton("�˳�����", null)
						.setCancelable(false).create().show();
			}
		});
		nearbyContainer.setSelected(true);
	}

	private void initMap(Bundle savedInstanceState) {
		mapView.onCreate(savedInstanceState);
		aMap = mapView.getMap();
		helper = new MapHelper(aMap, getActivity());
		helper.setLayer(MapHelper.NORMAL);
		helper.setupLocation();
		aMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker arg0) {
				Toast.makeText(getActivity(), arg0.getTitle(),
						Toast.LENGTH_SHORT).show();
				return false;
			}
		});
		aMap.setOnMarkerDragListener(new OnMarkerDragListener() {

			@Override
			public void onMarkerDragStart(Marker arg0) {

			}

			@Override
			public void onMarkerDragEnd(Marker arg0) {
				arg0.destroy();

			}

			@Override
			public void onMarkerDrag(Marker arg0) {

			}
		});
	}

	// �������ӰԺ����Ϣ���µ�listView��
	private void initList(List<Map<String, String>> result) {
		if (adapter == null) {
			adapter = new MyCinemaItemAdapter(result, getActivity());
			nearbyContainer.setAdapter(adapter);
		} else {
			adapter.bindData(result);
			adapter.notifyDataSetChanged();
		}

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
	}

	private class MapHelper implements LocationSource, AMapLocationListener,
			OnPoiSearchListener {
		public static final int NORMAL = 1;
		public static final int SATELLITE = 2;
		private AMap map;
		private Context context;
		private LocationManagerProxy locationManagerProxy;
		private OnLocationChangedListener mlistener;
		private AMapLocation location;
		private PoiSearch search;

		public MapHelper(AMap map, Context context) {
			this.map = map;
			this.context = context;
		}

		public void setLayer(int layerName) {
			switch (layerName) {
			case NORMAL:
				map.setMapType(AMap.MAP_TYPE_NORMAL);
				break;
			case SATELLITE:
				map.setMapType(AMap.MAP_TYPE_SATELLITE);
				break;
			}
		}

		public void searchMovieInfo() {
			// ���õ���λ�ã����ݵ�ǰ��λ�û�ȡӰԺ��Ϣ
			if (location != null) {
				PoiSearch.Query query = new PoiSearch.Query("��ӰԺ", "��ӰԺ",
						location.getCity());
				query.setPageSize(15);
				query.setPageNum(0);
				search = new PoiSearch(context, query);
				search.setBound(new SearchBound(new LatLonPoint(location
						.getLatitude(), location.getLongitude()), 10000, true));
				search.setOnPoiSearchListener(this);
				search.searchPOIAsyn();

			}
		}

		// �趨��λ��Դ�������Ϣ�������Ҫ��λ���÷����������
		public void setupLocation() {
			// �Զ���ϵͳ��λ����
			MyLocationStyle myLocationStyle = new MyLocationStyle();
			// �Զ��嶨λ����ͼ��
			myLocationStyle.myLocationIcon(BitmapDescriptorFactory
					.fromResource(R.drawable.ic_launcher));
			// �Զ��徫�ȷ�Χ��Բ�α߿���ɫ
			myLocationStyle.strokeColor(Color.BLACK);
			// �Զ��徫�ȷ�Χ��Բ�α߿���
			myLocationStyle.strokeWidth(5);
			// ����LocationManagerProxy����
			locationManagerProxy = LocationManagerProxy.getInstance(context);
			locationManagerProxy.setGpsEnable(false);
			// ���Զ����myLocationStyle������ӵ���ͼ��
			map.setMyLocationStyle(myLocationStyle);
			// ���ö�λ��Դ��������ô˶�λ��Դ��λ��ť���ɵ����
			map.setLocationSource(this);
			// ����Ĭ�϶�λ��ť�Ƿ���ʾ
			map.getUiSettings().setMyLocationButtonEnabled(true);
			// ����Ϊtrue��ʾ��ʾ��λ�㲢�ɴ�����λ��false��ʾ���ض�λ�㲢���ɴ�����λ��Ĭ����false
			map.setMyLocationEnabled(true);
		}

		@Override
		public void activate(OnLocationChangedListener arg0) {
			mlistener = arg0;
			if (locationManagerProxy != null) {
				Log.v("test", "׼����λ");
				locationManagerProxy.requestLocationUpdates(
						LocationProviderProxy.AMapNetwork, 1000 * 60, 10, this);
			}
		}

		@Override
		public void deactivate() {
			mlistener = null;
			if (locationManagerProxy != null) {
				locationManagerProxy.removeUpdates(this);
				locationManagerProxy.destory();
			}
			locationManagerProxy = null;
		}

		// �˷����ѷϣ����������û�ã��мǣ��м�
		@Override
		public void onLocationChanged(Location location) {

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		@Override
		public void onProviderEnabled(String provider) {

		}

		@Override
		public void onProviderDisabled(String provider) {

		}

		@Override
		public void onLocationChanged(AMapLocation arg0) {
			if (mlistener != null && arg0 != null) {
				if (currentLocation == null) {
					Log.v("test",
							arg0.getLatitude() + " " + arg0.getLongitude());
					mlistener.onLocationChanged(arg0);
					map.setMyLocationRotateAngle(map.getCameraPosition().bearing);
					location = arg0;
					// ��λ���õ�������Ϣ
					currentLocation = arg0;
					callBack.getCity(arg0.getCity());
					searchMovieInfo();
				} else {
					if ((Math.abs(currentLocation.getLatitude()
							- arg0.getLatitude()) > 0.01)
							|| (Math.abs(currentLocation.getLongitude()
									- arg0.getLongitude()) > 0.01)) {
						mlistener.onLocationChanged(arg0);
						map.setMyLocationRotateAngle(map.getCameraPosition().bearing);
						location = arg0;
						// ��λ���õ�������Ϣ
						currentLocation = arg0;
						callBack.getCity(arg0.getCity());
						searchMovieInfo();
					}
				}

			}
		}

		@Override
		public void onPoiItemDetailSearched(PoiItemDetail arg0, int arg1) {
			// Cinema cinema = arg0.getCinema();
		}

		@Override
		public void onPoiSearched(PoiResult arg0, int arg1) {
			Log.v("test", "���ؽ��");
			List<PoiItem> items = arg0.getPois();
			cinemaList = new ArrayList<Map<String, String>>();
			if (items != null && items.size() != 0) {
				for (int i = 0; i < items.size(); i++) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("id", items.get(i).getPoiId());
					map.put("distance", items.get(i).getDistance() + "");
					map.put("address", items.get(i).getSnippet());
					map.put("tel", items.get(i).getTel());
					map.put("name", items.get(i).getTitle());
					LatLonPoint point = items.get(i).getLatLonPoint();
					map.put("Latitude", point.getLatitude() + "");
					map.put("Longitude", point.getLongitude() + "");
					Log.v("test", map.toString());
					// search.searchPOIDetailAsyn(items.get(i).getPoiId());
					cinemaList.add(map);
				}
				initList(cinemaList);
			}

		}
	}

}
