package com.android.doubanmovie.service;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.android.doubanmovie.datasrc.ShowData;
import com.android.doubanmovie.httptask.GetJsonTask;
import com.android.doubanmovie.provider.BackupDataHelper;

public class BackupService extends Service {
	private IBinder binder;
	private ShowData data = null;

	@Override
	public void onCreate() {
		binder = new MyBinder();
		super.onCreate();
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.v("test", "�Ѿ���");
		return binder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Log.v("test", "�Ѿ������");
		return super.onUnbind(intent);
	}

	public class MyBinder extends Binder {
		public BackupService getService() {
			return BackupService.this;
		}
	}

	// ���´�������������,����UI
	public void refresh(final Uri url) {
		new GetJsonTask(GetJsonTask.SHOWTASK,
				new GetJsonTask.JsonCallBackAboutShow() {

					@Override
					public void getData(ShowData data) {
						// �õ��������ݣ���ʾUI����
						BackupDataHelper.BackupData(data, BackupService.this,
								url);
						Toast.makeText(BackupService.this, "ˢ�³ɹ�",
								Toast.LENGTH_SHORT).show();
					}
				}, null).execTask();

	}

	public ShowData getShowData() {
		return data;
	}

	public void backupData(final Uri url) {

		data = BackupDataHelper.getShowData(this, url);
		if (data == null) {
			// ���ݿ���������,���غ�̨����
			Log.v("test", "���ݿ���������,���غ�̨����");
			refresh(url);
		} else {
			// ���ݿ��������ݣ�����UI
			Log.v("test", "�״����У�ˢ��һ�����ݿ�");
			BackupDataHelper.BackupData(data, this, url);
			// ��̨�������ݣ�ȡ����������
			refresh(url);
		}
	}

}
