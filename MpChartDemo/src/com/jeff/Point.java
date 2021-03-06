package com.jeff;

public class Point {
	private int id; 
	private int x;
	private int probe1Temp;
	private int probe2Temp;
	private int btiTemp;
	private String myDate;
	private String myDateList;

	public Point(){
	}
	
	public Point(int x, int probe1Temp, int probe2Temp, int btiTemp, String myDate, String myDateList) {
		this.x = x;
		this.probe1Temp = probe1Temp;
		this.probe2Temp = probe2Temp;
		this.btiTemp = btiTemp;
		this.myDate = myDate;
		this.myDateList = myDateList;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getProbe1Temp() {
		return probe1Temp;
	}

	public void setProbe1Temp(int probe1Temp) {
		this.probe1Temp = probe1Temp;
	}

	public int getProbe2Temp() {
		return probe2Temp;
	}

	public void setProbe2Temp(int probe2Temp) {
		this.probe2Temp = probe2Temp;
	}

	public int getBtiTemp() {
		return btiTemp;
	}

	public void setBtiTemp(int btiTemp) {
		this.btiTemp = btiTemp;
	}

	public String getMyDate() {
		return myDate;
	}

	public void setMyDate(String myDate) {
		this.myDate = myDate;
	}

	public String getMyDateList() {
		return myDateList;
	}

	public void setMyDateList(String myDateList) {
		this.myDateList = myDateList;
	}
}
