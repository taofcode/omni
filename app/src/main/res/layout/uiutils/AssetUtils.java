package com.mobilebanking.core.uiutils;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class AssetUtils {
	/**
	 * Reads file located in the assets folder. Note that arguments passed
	 * through are file name and file extension, without "."(dot)
	 *
	 * @param context
	 * @param fileName
	 * @param fileExtension
	 * @return string
	 */
	public static String readAssetsFile(Context context, String fileName, String fileExtension) {

		String fileContent = null;

		AssetManager assetManager = context.getResources().getAssets();
		InputStream inputStream = null;

		try {
			inputStream = assetManager.open(fileName + "." + fileExtension);
			if (inputStream != null) {
				fileContent = readStreamToString(inputStream);
			}
		} catch (IOException e) {
			Log.e("AssetUtils", "Error occurred while reading file " + fileName + "." + fileExtension);
			e.printStackTrace();
		}

		return fileContent;
	}

	/**
	 * Converts input stream to string
	 *
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static String readStreamToString(InputStream inputStream) throws IOException {
		BufferedInputStream bufferedInputStream = null;
		InputStreamReader reader = null;
		try {
			bufferedInputStream = new BufferedInputStream(inputStream);
			reader = new InputStreamReader(bufferedInputStream);
			StringBuilder stringBuilder = new StringBuilder();

			final int bufferSize = 1024 * 2;
			char[] buffer = new char[bufferSize];
			int n = 0;
			while ((n = reader.read(buffer)) != -1) {
				stringBuilder.append(buffer, 0, n);
			}

			return stringBuilder.toString();
		} finally {
			closeQuietly(bufferedInputStream);
			closeQuietly(reader);
		}
	}

	public static void closeQuietly(Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		} catch (IOException ioe) {
			// ignore
		}
	}
}
