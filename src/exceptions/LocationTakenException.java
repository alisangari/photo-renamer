package exceptions;

public class LocationTakenException extends Exception {
	public String toString(){
		return ("GPS information unavailable! Maybe this image is a screenshot or a picture taken with an unsuported camera.");
	}
}
