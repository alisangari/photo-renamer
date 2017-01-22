package services;

import java.io.File;

import exceptions.LocationTakenException;

public class RenameService {

	public static String start(String dir, String currFilename, String prefix, String postfix, boolean includeDate,
			boolean includeTime, boolean includeLocation) {
		// String dir = "d://eclipse//";
		// String filename = "img.jpg";
		String absFilePath = dir + currFilename;
		// get location
		String location = "";
		if (includeLocation) {
			try {
				location = LocationService.getInstance().getLocation(absFilePath);
			} catch (LocationTakenException e) {
				System.out.println(e);
			}
		}

		// get date
		String date = "";
		if (includeDate || includeTime) {
			date = DateTimeService.getInstance().getDate(absFilePath, includeDate, includeTime);
		}
		String ext = currFilename.substring(currFilename.lastIndexOf("."), currFilename.length());

		// rename file as concat.ext
		if ((includeDate || includeTime || includeLocation) && (location.isEmpty() && date.isEmpty())) {
			// System.out.println("file skipped due missing date and location
			// information.");
			return "file skipped due missing date and location information.";
		} else {
			return rename(dir, currFilename, prefix, postfix, date, location, ext);
			// System.out.println("done!");
		}

	}

	private static String rename(String dir, String oldFilename, String prefix, String postfix, String date,
			String location, String ext) {
		// File (or directory) with old name
		File file = new File(dir + oldFilename);

		// File (or directory) with new name
		location = cleanse(location);
		String newFilename = "";
		String newFilenameMidSection = "";
		if (!date.isEmpty() && !location.isEmpty()) {
			newFilenameMidSection = date + "_" + location;
		} else if (date.isEmpty() && !location.isEmpty()) {
			newFilenameMidSection = location;
		} else if (!date.isEmpty() && location.isEmpty()) {
			newFilenameMidSection = date;
		}
		prefix = prefix.trim();
		if (!prefix.isEmpty()) {
			prefix = prefix + " ";
		}
		postfix = postfix.trim();
		if (!postfix.isEmpty()) {
			postfix = postfix + " ";
		}
		newFilename = prefix + newFilenameMidSection + postfix + ext;

		File file2 = new File(dir + newFilename);

		if (!file2.exists()) {
			// throw new java.io.IOException("file exists");

			// Rename file (or directory)
			boolean success = file.renameTo(file2);

			if (success) {
				return file2.getName();
			} else {
				// File was not successfully renamed
				System.out.println("Rename failed!");
			}

		} else {
			int c = 1;
			// boolean success = false;
			while (file2.exists()) {
				c++;
				newFilename = prefix + newFilenameMidSection + postfix + c + ext;
				file2 = new File(dir + newFilename);
				boolean success = file.renameTo(file2);
				if (success) {
					return file2.getName();
				}
				if (c > 100) { // to avoid long/endless loops
					// File was not successfully renamed
					System.out.println("Rename failed!");
					return "Rename failed!";
				}
			}
			// System.out.println("skipped - file name exists!");
		}
		return "Rename failed!";
	}

	private static String cleanse(String str) {
		str = str.replaceAll(", ", "-");
		str = str.replaceAll(" ,", "-");
		str = str.replaceAll(" , ", "-");
		str = str.replaceAll(",", "-");
//		str = str.replaceAll(" ", "");
		return str;
	}
}