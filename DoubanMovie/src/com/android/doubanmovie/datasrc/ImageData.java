package com.android.doubanmovie.datasrc;

public class ImageData {
	// ר��ID
	public String album_id;
	// ͼƬ����
	public String desc;
	// ͼƬID
	public String id;
	// ��һ��ͼƬ��ID
	public String next_photo;
	// ��һ��ͼƬ��ID
	public String prev_photo;
	// ͼƬ��ַ(��ͼ)
	public String image;
	// ͼƬ��ַ����ͼ��
	public String thumb;
	// ͼƬ��ַ��Сͼ��
	public String icon;

	@Override
	public String toString() {
		return "ImageData [album_id=" + album_id + ", desc=" + desc + ", id="
				+ id + ", next_photo=" + next_photo + ", prev_photo="
				+ prev_photo + ", image=" + image + ", icon=" + icon + "]";
	}

}
