package model;

public class Location {
	double lat;
	double lng;
	String name;

	public Location(double lat, double lng, String name) {
		this.lat = lat;
		this.lng = lng;
		this.name = name;
	}

	public double getLat() {
		return this.lat;
	}

	public double getLng() {
		return this.lng;
	}

	public boolean isEqual(double lat, double lng) {
		return (this.lat == lat && this.lng == lng);
	}

	public boolean isEqual(Location loc) {
		return (this.lat == loc.getLat() && this.lng == loc.getLng());
	}

	public String getName() {
		return this.name;
	}

}
