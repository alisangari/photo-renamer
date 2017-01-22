package services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import exceptions.LocationTakenException;
import model.Location;
import model.LocationsList;

public class LocationService {
	private static LocationService instance = null;
	private String key;
	private String baseUrl;
	private LocationsList locationsList;
	private ArrayList<String> firstClassLocationTypes;
	private ArrayList<String> secondClassLocationTypes;
	
	private LocationService() {
		key = "AIzaSyApWY6S2OF849Dq4x61Sl_YTeit42F0ZXQ";
		// baseUrl = "https://maps.googleapis.com/maps/api/geocode/json?";//Map
		// API
		baseUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?"; // Places
																					// API
		// https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=22.665,-78.97305555555556&radius=50&rankBy=distance&key=AIzaSyAnmGp47O7m0beCTTh9MWEf6xL528U7Uqs

		locationsList = new LocationsList();
		importLocationTypes();
		
	}

	public static LocationService getInstance() {
		if (instance == null) {
			instance = new LocationService();
		}
		return instance;
	}

	public String getLocation(String filename) throws LocationTakenException {
		javaxt.io.Image image = new javaxt.io.Image(filename);
		double[] gps = image.getGPSCoordinate();
		image=null;
		if (gps == null) {
			throw new LocationTakenException();
		}
		double lng = gps[0];
		double lat = gps[1];
		return getNameFor(lat, lng);
	}

	private String getNameFor(double lat, double lng) {
		String name = locationsList.getNameFor(lat, lng);
		if (name != null) {
			return name;
		}

		// return getNameFromMapApi(lat, lng);
		return getFirstNameFromPlacesApi(lat, lng);

	}

	private String getFirstNameFromPlacesApi(double lat, double lng) {
		int radius = 1;
		String name = "";
		name = getNameFromPlacesApi(lat, lng, radius);
		for (int i = 1; i <= 100; i++) {
			if (name.isEmpty()) {
				if (i <= 10) {
					radius += 10;
				} else {
					radius += 100;
				}
				name = getNameFromPlacesApi(lat, lng, radius);
			} else {
				return name;
			}
		}
		return "LocationError";
	}

	private String getNameFromPlacesApi(double lat, double lng, int radius) {
		String name = "";
		try {
			URL url = new URL(
					baseUrl + "location=" + lat + "," + lng + "&radius=" + radius + "&language=en" + "&key=" + key);// Places
			// API
			System.out.println("Querying: " + url);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output;
			StringBuilder rawJson = new StringBuilder();
			while ((output = br.readLine()) != null) {
				rawJson.append(output);
			}
			JSONObject obj = new JSONObject(rawJson.toString());
			if (obj.getJSONArray("results").isNull(0)) {
				return "";
			}
//			JSONObject obj2 = (JSONObject) obj.getJSONArray("results").get(0);
//			conn.disconnect();
//			name = obj2.get("name").toString();
//			locationsList.add(new Location(lat, lng, name));
//			return name;

			
			
			
			JSONObject obj2;
			int i=0;
			while(!obj.getJSONArray("results").isNull(i)){
				obj2 = (JSONObject) obj.getJSONArray("results").get(i);
				int j=0;
				while(!obj2.getJSONArray("types").isNull(j)){
					if(firstClassLocationTypes.contains(obj2.getJSONArray("types").get(j))){
						conn.disconnect();
						name = obj2.get("name").toString();
						locationsList.add(new Location(lat, lng, name));
						return name;
					}
					j++;
				}
				i++;
			}
			obj2 = (JSONObject) obj.getJSONArray("results").get(0);
			conn.disconnect();
			name = obj2.get("name").toString();
			locationsList.add(new Location(lat, lng, name));
			return name;
			
			
			
			
			
			
			
			
			
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	private String getNameFromMapApi(double lat, double lng) {
		String name = "";
		try {
			URL url = new URL(baseUrl + "latlng=" + lat + "," + lng + "&key=" + key);// Map
																						// API
			System.out.println("Querying: " + url);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;
			StringBuilder rawJson = new StringBuilder();
			while ((output = br.readLine()) != null) {
				rawJson.append(output);
			}
			JSONObject obj = new JSONObject(rawJson.toString());
			JSONObject obj2 = (JSONObject) obj.getJSONArray("results").get(1);
			conn.disconnect();
			name = obj2.get("formatted_address").toString();
			locationsList.add(new Location(lat, lng, name));
			return name;

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "LocationError";
	}
	
	
	private void importLocationTypes() {
		firstClassLocationTypes = new ArrayList<String>();
		secondClassLocationTypes = new ArrayList<String>();

		ClassLoader classLoader = getClass().getClassLoader();
		firstClassLocationTypes = readTextFileToArrayList(new File(classLoader.getResource("first-class-location-types.properties").getFile()));
		secondClassLocationTypes = readTextFileToArrayList(new File(classLoader.getResource("second-class-location-types.properties").getFile()));
		
	}
	
	private ArrayList<String> readTextFileToArrayList(File file) {
		ArrayList<String> content = new ArrayList<String>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				content.add(line.trim());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return content;
	}


	public static void main(String[] args) {

		double lat = 22.665;
		double lng = -78.97305555555556;

		String dir = "d://pics//";
		String filename = "20161222_140249.jpg";
		String absFilePath = dir + filename;

		LocationService location = LocationService.getInstance();
		try {
			System.out.println(location.getLocation(absFilePath));
		} catch (LocationTakenException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
