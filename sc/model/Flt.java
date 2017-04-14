package com.sc.sc.model;

import java.util.Date;

public class Flt {
	private String fltId;

	private String fltNo;

	private String fltDepartureDatetime;

	private String fltArrivalDatetime;

	private String fltPrice;

	private String fltType;

	private String fltCount;

	private Date putDatetime;

	private String cityNo;

	public String getCityNo() {
		return cityNo;
	}

	public String getFltArrivalDatetime() {
		return fltArrivalDatetime;
	}

	public String getFltCount() {
		return fltCount;
	}

	public String getFltDepartureDatetime() {
		return fltDepartureDatetime;
	}

	public String getFltId() {
		return fltId;
	}

	public String getFltNo() {
		return fltNo;
	}

	public String getFltPrice() {
		return fltPrice;
	}

	public String getFltType() {
		return fltType;
	}

	public Date getPutDatetime() {
		return putDatetime;
	}

	public void setCityNo(String cityNo) {
		this.cityNo = cityNo;
	}

	public void setFltArrivalDatetime(String fltArrivalDatetime) {
		this.fltArrivalDatetime = fltArrivalDatetime;
	}

	public void setFltCount(String fltCount) {
		this.fltCount = fltCount;
	}

	public void setFltDepartureDatetime(String fltDepartureDatetime) {
		this.fltDepartureDatetime = fltDepartureDatetime;
	}

	public void setFltId(String fltId) {
		this.fltId = fltId;
	}

	public void setFltNo(String fltNo) {
		this.fltNo = fltNo;
	}

	public void setFltPrice(String fltPrice) {
		this.fltPrice = fltPrice;
	}

	public void setFltType(String fltType) {
		this.fltType = fltType;
	}

	public void setPutDatetime(Date putDatetime) {
		this.putDatetime = putDatetime;
	}

}