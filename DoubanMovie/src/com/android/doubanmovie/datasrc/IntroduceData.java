package com.android.doubanmovie.datasrc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IntroduceData extends Data {
	public IntroduceData() {
		rating = new HashMap<String, String>();
		popular_comments = new ArrayList<Map<String, String>>();
		writers = new ArrayList<Map<String, String>>();
		casts = new ArrayList<Map<String, String>>();
		directors = new ArrayList<Map<String, String>>();
	}

	// �Ǽ�������
	public Map<String, String> rating;
	// ��Ӱ����
	public String title;
	// ��½��ӳ����
	public String mainland_pubdate;
	// ͼƬ���ô�����ȡ��ͼ
	public String images;
	// ������Ϣ����Ҫȡ�������У�rating�е�value
	// author�е�name,���������
	// content,������ useful_count
	public List<Map<String, String>> popular_comments;
	// Ӱ����Ϣ��������Ҫȡ������Ϊ��
	// name.name_en.avatarsͷ��ȡ��ͼ
	public List<Map<String, String>> writers;
	// ��Ӱ����
	public String genres;
	// Ԥ��ƬURL��ֻȡ��һ��
	public String trailer_urls;
	// ��������
	public String countries;
	// ���ǣ�ȡname,name_en,avatars
	public List<Map<String, String>> casts;
	// ���
	public String summary;
	// ����,ȡname,name_en,avatars
	public List<Map<String, String>> directors;
	// ��������
	public long ratings_count;

	@Override
	public String toString() {
		return "IntroduceData [rating=" + rating + ", title=" + title
				+ ", mainland_pubdate=" + mainland_pubdate + ", images="
				+ images + ", popular_comments=" + popular_comments
				+ ", writers=" + writers + ", genres=" + genres
				+ ", trailer_urls=" + trailer_urls + ", countries=" + countries
				+ ", casts=" + casts + ", summary=" + summary + ", directors="
				+ directors + ", ratings_count=" + ratings_count + "]";
	}

}
