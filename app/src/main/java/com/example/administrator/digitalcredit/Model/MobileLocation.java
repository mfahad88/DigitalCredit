package com.example.administrator.digitalcredit.Model;

public class MobileLocation {


	private Float accuracy;
	private String recId;
	private Double altitude;
	private Float bearing;
	private Long elapseTime;
	private Double latitude;
	private Double longitude;
	private String provider;
	private Float spead;
	private long locTime;
	private String address;
	private String knownNameLoc;
	private String placeNameLoc;
	private int radius;
	private int userId;
	
	public int getRadius() {
		return radius;
	}
	public void setRadius(int radius) {
		this.radius = radius;
	}
	public MobileLocation() {
		
		// TODO Auto-generated constructor stub
	}
	public MobileLocation(Float accuracy, String recId, Double altitude, Float bearing, Long elapseTime,
			Double latitude, Double longitude, String provider, Float spead, long locTime, String address,
			String knownNameLoc, String placeNameLoc) {
		super();
		this.accuracy = accuracy;
		this.recId = recId;
		this.altitude = altitude;
		this.bearing = bearing;
		this.elapseTime = elapseTime;
		this.latitude = latitude;
		this.longitude = longitude;
		this.provider = provider;
		this.spead = spead;
		this.locTime = locTime;
		this.address = address;
		this.knownNameLoc = knownNameLoc;
		this.placeNameLoc = placeNameLoc;
	}
	public Float getAccuracy() {
		return accuracy;
	}
	public void setAccuracy(Float accuracy) {
		this.accuracy = accuracy;
	}
	public String getRecId() {
		return recId;
	}
	public void setRecId(String recId) {
		this.recId = recId;
	}
	public Double getAltitude() {
		return altitude;
	}
	public void setAltitude(Double altitude) {
		this.altitude = altitude;
	}
	public Float getBearing() {
		return bearing;
	}
	public void setBearing(Float bearing) {
		this.bearing = bearing;
	}
	public Long getElapseTime() {
		return elapseTime;
	}
	public void setElapseTime(Long elapseTime) {
		this.elapseTime = elapseTime;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public Float getSpead() {
		return spead;
	}
	public void setSpead(Float spead) {
		this.spead = spead;
	}
	public long getLocTime() {
		return locTime;
	}
	public void setLocTime(long locTime) {
		this.locTime = locTime;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getKnownNameLoc() {
		return knownNameLoc;
	}
	public void setKnownNameLoc(String knownNameLoc) {
		this.knownNameLoc = knownNameLoc;
	}
	public String getPlaceNameLoc() {
		return placeNameLoc;
	}
	public void setPlaceNameLoc(String placeNameLoc) {
		this.placeNameLoc = placeNameLoc;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "MobileLocation{" +
				"accuracy=" + accuracy +
				", recId='" + recId + '\'' +
				", altitude=" + altitude +
				", bearing=" + bearing +
				", elapseTime=" + elapseTime +
				", latitude=" + latitude +
				", longitude=" + longitude +
				", provider='" + provider + '\'' +
				", spead=" + spead +
				", locTime=" + locTime +
				", address='" + address + '\'' +
				", knownNameLoc='" + knownNameLoc + '\'' +
				", placeNameLoc='" + placeNameLoc + '\'' +
				", radius=" + radius +
				", userId=" + userId +
				'}';
	}
}