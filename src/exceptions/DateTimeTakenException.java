package exceptions;

public class DateTimeTakenException extends Exception {
	public String toString(){
		return ("Date information unavailable! Maybe it's a screenshot or a picture take with an unsupported camera.");
	}
}
