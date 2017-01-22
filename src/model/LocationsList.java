package model;

import java.util.ArrayList;

public class LocationsList {
	ArrayList<Location> list;

	public LocationsList() {
		list = new ArrayList<Location>();
	}

	public String getNameFor(double lat, double lng) {
		for(Location location: list){
			if(location.isEqual(lat, lng)){
				return location.getName();
			}
		}
		return null;
	}

	public boolean add(Location location) {
		if (!exists(location)) {
			return list.add(location);
		}
		return false;
	}

	private boolean exists(Location location) {
		for (Location loc : list) {
			if (loc.isEqual(location)) {
				return true;
			}
		}
		return false;
	}
}