package com.android.doubanmovie.datasrc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//用来存储上映界面的数据结构
public class ShowData extends Data {
	public int count;
	public int start;
	public int total;
	public List<Map<String, Object>> subjects;

	public ShowData() {
		super();
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<Map<String, Object>> getSubjects() {
		return subjects;
	}

	public void setSubjects() {
		this.subjects = new ArrayList<Map<String, Object>>();
	}

}
