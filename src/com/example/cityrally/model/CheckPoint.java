package com.example.cityrally.model;


public class CheckPoint {
	
	private int id;
	
	private double latitude;
	private double longitude; 
	
	private String challangeID;
	private String clue;
	
	private boolean solved;
	private boolean giveUp;
	
	
	//private int order;
	
	//empty constructor
	
	public CheckPoint() {
		// TODO Auto-generated constructor stub
	}
	
	//constructor 
	public CheckPoint(int id, double latitude, double longitude, String challangeID, String clue, boolean solved, boolean giveUp){
		
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
		this.challangeID = challangeID;
		this.clue = clue;
		this.solved = solved;
		this.giveUp = giveUp;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getChallangeID() {
		return challangeID;
	}

	public void setChallangeID(String challangeID) {
		this.challangeID = challangeID;
	}

	public String getClue() {
		return clue;
	}

	public void setClue(String clue) {
		this.clue = clue;
	}

	public boolean isSolved() {
		return solved;
	}

	public void setSolved(boolean solved) {
		this.solved = solved;
	}
	
	public boolean isGiveUp() {
		return giveUp;
	}

	public void setGiveUp(boolean giveUp) {
		this.giveUp = giveUp;
	}
	

}
