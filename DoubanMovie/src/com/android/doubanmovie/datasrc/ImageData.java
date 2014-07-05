package com.android.doubanmovie.datasrc;

public class ImageData {
	// 专辑ID
	public String album_id;
	// 图片描述
	public String desc;
	// 图片ID
	public String id;
	// 下一张图片的ID
	public String next_photo;
	// 上一张图片的ID
	public String prev_photo;
	// 图片地址(大图)
	public String image;
	// 图片地址（中图）
	public String thumb;
	// 图片地址（小图）
	public String icon;

	@Override
	public String toString() {
		return "ImageData [album_id=" + album_id + ", desc=" + desc + ", id="
				+ id + ", next_photo=" + next_photo + ", prev_photo="
				+ prev_photo + ", image=" + image + ", icon=" + icon + "]";
	}

}
