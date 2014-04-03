package ca.ualberta.cs.team5geotopics;


import java.util.ArrayList;
import com.google.gson.Gson;
import android.util.Log;
import java.io.FileOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;

public class CacheIO {
	private ArrayList<String> fileDir;

	public ArrayList<String> getFileDir() {
		return fileDir;
	}

	public void setFileDir(ArrayList<String> fileDir) {
		this.fileDir = fileDir;
	}

	/**
	 * Saves the file directory back to disk.
	 */
	public void saveFileList(String path) {
		Gson gson = new Gson();
		String jsonString = gson.toJson(this.fileDir);
		replaceFileHistory(jsonString, "files.sav", path);
	}

	/**
	 * Replaces the current file history with a new one.
	 * @param jsonString The file history we are replacing the old one with in json format
	 * @param filename The location of the file history we are replacing
	 */
	public void replaceFileHistory(String jsonString, String filename,
			String path) {
		if (!this.fileDir.contains(filename) && !filename.equals("files.sav")) {
			this.fileDir.add(filename);
			saveFileList(path);
			Log.w("Cache", "added file to fileDir: " + filename);
		}
		FileOutputStream fos = null;
		try {
			File file = new File(path + "/history", filename);
			fos = new FileOutputStream(file);
			fos.write(jsonString.getBytes());
			Log.w("Cache", "Writing this to disk");
			Log.w("Cache", jsonString);
		} catch (FileNotFoundException e) {
			Log.w("Cache", "File not found");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fileDir.add(filename);
				if (fos != null)
					fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Loads the file directory list from disk for the cache.
	 */
	public void loadFileList(String path) {
		File file = new File(path + "/history", "files.sav");
		if (file.exists()) {
			try {
				FileInputStream fis = new FileInputStream(file);
				InputStreamReader isr = new InputStreamReader(fis);
				Gson gson = new Gson();
				Type type = new TypeToken<ArrayList<String>>() {
				}.getType();
				this.fileDir = gson.fromJson(isr, type);
			} catch (IOException e) {
				Log.w("Cache", "IO exception in reading fileDir");
			}
		}
	}
}