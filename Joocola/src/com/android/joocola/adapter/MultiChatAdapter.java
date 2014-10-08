package com.android.joocola.adapter;

import java.util.Map;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 多人聊天的适配器,这个适配器暂时还未用到（因为目前行为和单聊适配器相同，为以防万一，暂时先做保留）
 * 
 * @author:LiXiaoSong
 * @copyright © joocola.com
 * @Date:2014-9-29
 */
public class MultiChatAdapter extends BaseAdapter {

	/**
	 * 用户头像存放地址，key为用户名，values为地址
	 */
	private Map<String, String> photos;
	/**
	 * 用户昵称存放地址，key为用户名，values为地址
	 */
	private Map<String, String> names;

	@Override
	public int getCount() {
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}

	public void addPhotos(String key, String value) {
		photos.put(key, value);
		notifyDataSetChanged();
	}

}
