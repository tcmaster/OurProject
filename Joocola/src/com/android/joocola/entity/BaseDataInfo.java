package com.android.joocola.entity;

public class BaseDataInfo {
	private int PID;
	private int SortNo;
	private String ItemName;
	private String TypeName;

	public int getPID() {
		return PID;
	}

	public void setPID(int pID) {
		PID = pID;
	}

	public int getSortNo() {
		return SortNo;
	}

	public void setSortNo(int sortNo) {
		SortNo = sortNo;
	}

	public String getItemName() {
		return ItemName;
	}

	public void setItemName(String itemName) {
		ItemName = itemName;
	}

	public String getTypeName() {
		return TypeName;
	}

	public void setTypeName(String typeName) {
		TypeName = typeName;
	}

	@Override
	public String toString() {
		return "BaseDataInfo [PID=" + PID + ", SortNo=" + SortNo
				+ ", ItemName=" + ItemName + ", TypeName=" + TypeName + "]";
	}

}
