package com.android.doubanmovie.httptask;

import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.doubanmovie.datasrc.DataSrc;
import com.android.doubanmovie.datasrc.ImageData;
import com.android.doubanmovie.datasrc.IntroduceData;
import com.android.doubanmovie.datasrc.ShowData;
import com.android.doubanmovie.utils.HttpUtils;
import com.android.doubanmovie.utils.JsonUtils;

@SuppressLint("HandlerLeak")
public class GetJsonTask {
	public static final int SHOWTASK = 1;
	public static final int INTRODUCETASK = 2;
	public static final int IMAGETASK = 3;
	public static final int SCTASK = 4;
	public static final int CDTASK = 5;
	private int flag;
	private Handler myHandler;
	Object addInfo;// 需要附加的信息

	public GetJsonTask(int flag, final JsonCallBack callback, Object info) {
		this.flag = flag;
		addInfo = info;
		myHandler = new Handler() {
			@SuppressWarnings("unchecked")
			@Override
			public void dispatchMessage(Message msg) {
				switch (msg.what) {
				case SHOWTASK:
					JsonCallBackAboutShow cb = (JsonCallBackAboutShow) callback;
					cb.getData((ShowData) msg.obj);
					break;
				case INTRODUCETASK:
					JsonCallBackAboutIntroduce icb = (JsonCallBackAboutIntroduce) callback;
					icb.getData((IntroduceData) msg.obj);
					break;
				// case IMAGETASK:
				// JsonCallBackAboutImage imgcb = (JsonCallBackAboutImage)
				// callback;
				// imgcb.getData((List<ImageData>) msg.obj);
				// break;
				case SCTASK:
					JsonCallBackAboutSC sccb = (JsonCallBackAboutSC) callback;
					sccb.getData((List<Map<String, String>>) msg.obj);
					break;
				case CDTASK:
					JsonCallBackAboutSC cdcb = (JsonCallBackAboutSC) callback;
					cdcb.getData((List<Map<String, String>>) msg.obj);
					break;
				default:
					break;
				}
				super.dispatchMessage(msg);
			}
		};
	}

	public void execTask() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String jsonStr = null;
				Message msg = Message.obtain();
				msg.what = flag;
				switch (flag) {
				case SHOWTASK:
					jsonStr = HttpUtils
							.getData(DataSrc.SHOWPATH, null, "utf-8");
					ShowData sData = JsonUtils.parseShowJSON(jsonStr);
					msg.obj = sData;
					break;
				case INTRODUCETASK:
					jsonStr = HttpUtils.getData(DataSrc.INTRODUCEPATHBEFORE
							+ addInfo + DataSrc.INTRODUCEPATHAFTER, null,
							"utf-8");
					IntroduceData iData = JsonUtils.parseIntroduceJSON(jsonStr);
					msg.obj = iData;
					break;
				// case IMAGETASK:
				// jsonStr = HttpUtils.getData(DataSrc.IMAGEPATHBEFORE
				// + addInfo + DataSrc.IMAGEPATHAFTER, null, "utf-8");
				// List<ImageData> imgData = JsonUtils.parseImageJSON(jsonStr);
				// msg.obj = imgData;
				// break;
				case SCTASK:
					jsonStr = HttpUtils.getData(DataSrc.SCOMMENTBEFORE
							+ addInfo + DataSrc.SCOMMENTAFTER, null, "utf-8");
					List<Map<String, String>> scdata = JsonUtils
							.parseSCJSON(jsonStr);
					msg.obj = scdata;
					break;
				case CDTASK:
					jsonStr = HttpUtils
							.getData(DataSrc.COMMENTDEEPBEFORE + addInfo
									+ DataSrc.COMMENTDEEPAFTER, null, "utf-8");
					List<Map<String, String>> cdata = JsonUtils
							.parseCDJSON(jsonStr);
					Log.v("test", cdata.toString());
					msg.obj = cdata;
					break;
				default:
					break;
				}
				myHandler.sendMessage(msg);
			}
		}).start();
	}

	public interface JsonCallBack {

	}

	public interface JsonCallBackAboutShow extends JsonCallBack {
		public void getData(ShowData data);
	}

	public interface JsonCallBackAboutIntroduce extends JsonCallBack {
		public void getData(IntroduceData data);
	}

	public interface JsonCallBackAboutImage extends JsonCallBack {
		public void getData(List<ImageData> data);
	}

	/**
	 * 
	 * @author tc
	 * @category 这个接口影评，短评皆可用
	 */
	public interface JsonCallBackAboutSC extends JsonCallBack {
		public void getData(List<Map<String, String>> data);
	}
}
