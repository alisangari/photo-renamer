package services;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;

import exceptions.DateTimeTakenException;
import javaxt.io.File;


public class DateTimeService {


		private static DateTimeService instance = null;

		private DateTimeService() {

		}

		public static DateTimeService getInstance() {
			if (instance == null) {
				instance = new DateTimeService();
			}
			return instance;
		}

		public String getDate(String filename, boolean includeDate, boolean includeTime) {
			try {
				InputStream file = new File(filename).getInputStream();
				Metadata metadata = ImageMetadataReader.readMetadata(file);
				Date date = null;
				try {
					date = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class)
							.getDate(ExifIFD0Directory.TAG_DATETIME);
				} catch (NullPointerException e) {
					throw new DateTimeTakenException();
				}
				file.close();
				DateFormat df = DateFormat.getDateInstance();
				if (date != null) {
					df.format(date);
					int year = df.getCalendar().get(Calendar.YEAR);
					int month = df.getCalendar().get(Calendar.MONTH) + 1;
					int day = df.getCalendar().get(Calendar.DAY_OF_MONTH);
					int hour = df.getCalendar().get(Calendar.HOUR_OF_DAY);
					int min = df.getCalendar().get(Calendar.MINUTE);
					int sec = df.getCalendar().get(Calendar.SECOND);
					int milisec = df.getCalendar().get(Calendar.MILLISECOND);

					if (includeDate && includeTime) {
						return (year + "-" + month + "-" + day + " at " + hour + "-" + min);
					} else if (includeDate && !includeTime) {
						return (year + "-" + month + "-" + day);
					} else if (!includeDate && includeTime) {
						return ("At " + hour + "-" + min);
					} else {
						return "";
					}

				}

				else {
					return "";
				}
			} catch (ImageProcessingException ex) {
				System.out.println(ex.getMessage());
			} catch (DateTimeTakenException ex) {
				System.out.println(ex.getMessage());
			} catch (IOException ex) {
				System.out.println(ex.getMessage());
			}
			return "";
		}

		public static void main(String args[]) throws ImageProcessingException, IOException, DateTimeTakenException {

			String filename = "d://eclipse//img.jpg";
			System.out.println(DateTimeService.getInstance().getDate(filename, true, true));

		}
	}
