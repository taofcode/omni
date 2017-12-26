package com.mobilebanking.core.uiutils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

/**
 * 
 * <b>Author</b>:
 * <p>
 * Created by <b>Sasa Ilic</b> on Mar 22, 2016
 * </p>
 */
public class BitmapUtils {
	// Image size
	public static final int TARGET_IMAGE_SIZE_MAX = 500; // 1280;

	public static Bitmap rotateBitmapCW(Bitmap bitmap) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		// Setting post rotate to 90
		Matrix mtx = new Matrix();
		mtx.postRotate(90);
		// Rotating Bitmap
		Bitmap rotatedBMP = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
		return rotatedBMP;
	}

	public static String bitmapToBase64(Bitmap bitmap) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		String imageEncoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
		return imageEncoded;
	}

	// /**
	// * Get cached bitmap from the file
	// *
	// * @param context
	// * @param file
	// * - file pointing to the cached bitmap
	// * @return bitmap
	// */
	// public static Bitmap getBitmapFromCache(Context context, File file) {
	// if (file != null) {
	// if (file.exists()) {
	// try {
	// Bitmap _imageBitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(),
	// Constants.REQUESTED_BITMAP_SIZE_W, Constants.REQUESTED_BITMAP_SIZE_H);
	// return _imageBitmap;
	// } catch (Exception e) {
	// Logger.error("BitmapUtils#getBitmapFromCache", e.toString());
	// }
	// }
	// }
	// return null;
	// }
	//
	// /**
	// * Get cached bitmap by name
	// *
	// * @param context
	// * @param imageName
	// * @return
	// */
	// public static Bitmap getBitmapFromCache(Context context, String imageName) {
	// if (imageName != null) {
	// try {
	// Bitmap _imageBitmap = decodeSampledBitmapFromFile(imageName,
	// Constants.REQUESTED_BITMAP_SIZE_W, Constants.REQUESTED_BITMAP_SIZE_H);
	// return _imageBitmap;
	// } catch (Exception e) {
	// Logger.error("BitmapUtils#getBitmapFromCache", e.toString());
	// }
	// }
	// return null;
	// }

	/**
	 * Store an image representing by byte array in the cache
	 * 
	 * @param context
	 * @param image
	 *            - image to be stored
	 * @param imageName
	 *            - name for the image to be stored
	 * @return file name
	 */
	public static String byteArrayToFileCache(Context context, byte[] image, String imageName) {

		if (image == null)
			return null;
		String cacheDir = context.getCacheDir().getAbsolutePath();
		File fileDir = new File(cacheDir);
		if (!fileDir.exists()) {
			fileDir.mkdir();
		}
		File file = new File(fileDir, imageName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				Log.e("BitmapUtils#byteArrayToFileCache", e.toString());
			}
		}
		try {
			FileOutputStream fileOS = new FileOutputStream(file);
			fileOS.write(image);
			fileOS.close();
			return file.getAbsolutePath();
		} catch (FileNotFoundException e) {
			Log.e("BitmapUtils#byteArrayToFileCache", e.toString());
		} catch (IOException e) {
			Log.e("BitmapUtils#byteArrayToFileCache", e.toString());
		}
		return null;
	}

	/**
	 * Decode and sample down a bitmap from a file to the requested width and height.
	 * 
	 * @param filename
	 *            The full path of the file to decode
	 * @param reqWidth
	 *            The requested width of the resulting bitmap
	 * @param reqHeight
	 *            The requested height of the resulting bitmap
	 * @return A bitmap sampled down from the original with the same aspect ratio and
	 *         dimensions that are equal to or greater than the requested width and height
	 */
	public static Bitmap decodeSampledBitmapFromFile(String filename, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filename, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filename, options);
	}

	/**
	 * Calculate an inSampleSize for use in a {@link BitmapFactory.Options} object when
	 * decoding bitmaps using the decode* methods from {@link BitmapFactory}. This
	 * implementation calculates the closest inSampleSize that will result in the final
	 * decoded bitmap having a width and height equal to or larger than the requested
	 * width and height. This implementation does not ensure a power of 2 is returned for
	 * inSampleSize which can be faster when decoding but results in a larger bitmap which
	 * isn't as useful for caching purposes.
	 * 
	 * @param options
	 *            An options object with out* params already populated (run through a
	 *            decode* method with inJustDecodeBounds==true
	 * @param reqWidth
	 *            The requested width of the resulting bitmap
	 * @param reqHeight
	 *            The requested height of the resulting bitmap
	 * @return The value to be used for inSampleSize
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,
			int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee a final image
			// with both dimensions larger than or equal to the requested height
			// and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

			// This offers some additional logic in case the image has a strange
			// aspect ratio. For example, a panorama may have a much larger
			// width than height. In these cases the total pixels might still
			// end up being too large to fit comfortably in memory, so we should
			// be more aggressive with sample down the image (=larger
			// inSampleSize).

			final float totalPixels = width * height;

			// Anything more than 2x the requested pixels we'll sample down
			// further
			final float totalReqPixelsCap = reqWidth * reqHeight * 2;

			while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
				inSampleSize++;
			}
		}
		return inSampleSize;
	}

	public static byte[] InputStreamToArray(InputStream is) throws IOException {
		byte b[] = new byte[8192];
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		while (true) {
			int read = is.read(b);
			if (read < 1)
				break;
			out.write(b, 0, read);
		}
		out.close();
		return out.toByteArray();
	}

	// public static Bitmap getBitmapByUri(Context context, Uri uri) throws
	// FileNotFoundException,
	// IOException {
	// InputStream input = context.getContentResolver().openInputStream(uri);
	//
	// BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
	// onlyBoundsOptions.inJustDecodeBounds = true;
	// onlyBoundsOptions.inDither = true;// optional
	// onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional
	// BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
	// input.close();
	// if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1))
	// return null;
	//
	// int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ?
	// onlyBoundsOptions.outHeight
	// : onlyBoundsOptions.outWidth;
	//
	// double ratio = (originalSize > Constants.REQUESTED_BITMAP_SIZE_W) ? (originalSize /
	// Constants.REQUESTED_BITMAP_SIZE_H)
	// : 1.0;
	//
	// BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
	// bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
	// bitmapOptions.inDither = true;// optional
	// bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional
	// input = context.getContentResolver().openInputStream(uri);
	// Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
	// input.close();
	// return fixOrientation(bitmap, uri.getPath());
	// }

	public static Bitmap getBitmapByUri(Context context, Uri uri, int maxSize)
			throws FileNotFoundException, IOException {
		InputStream input = context.getContentResolver().openInputStream(uri);

		BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
		onlyBoundsOptions.inJustDecodeBounds = true;
		onlyBoundsOptions.inDither = true;// optional
		onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional
		BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
		input.close();
		if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1))
			return null;

		int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight
				: onlyBoundsOptions.outWidth;

		double ratio = (originalSize > maxSize) ? (originalSize / maxSize) : 1.0;

		BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
		bitmapOptions.inDither = true;// optional
		bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional
		input = context.getContentResolver().openInputStream(uri);
		Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
		input.close();

		return fixOrientation(bitmap, uri.getPath());
	}

	/**
	 * Fix image orientation according to EXIF info
	 * 
	 * @param bm
	 * @param path
	 * @return
	 */
	private static Bitmap fixOrientation(Bitmap bm, String path) {

		Bitmap bitmap = bm;
		try {
			ExifInterface exif = new ExifInterface(path);
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
			Log.e("orientation", "" + orientation);
			Matrix m = new Matrix();

			if ((orientation == 3)) {

				m.postRotate(180);
				Log.e("in orientation", "" + orientation);

				bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
				return bitmap;
			} else if (orientation == 6) {

				m.postRotate(90);

				Log.e("in orientation", "" + orientation);

				bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
				return bitmap;
			} else if (orientation == 8) {

				m.postRotate(270);

				Log.e("in orientation", "" + orientation);

				bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
				return bitmap;
			}
		} catch (Exception e) {
			Log.d("fixOrientation", e.toString());
		}
		return bitmap;
	}

	private static int getPowerOfTwoForSampleRatio(double ratio) {
		int k = Integer.highestOneBit((int) Math.floor(ratio));
		if (k == 0)
			return 1;
		else
			return k;
	}

	public static File bitmapToFile(Bitmap myBitmap, Context context) {

		File myDir = new File(context.getCacheDir(), context.getPackageName());
		if (!myDir.exists()) {
			myDir.mkdir();
		}
		String fname = System.currentTimeMillis() + ".jpg";
		File file = new File(myDir, fname);
		if (file.exists())
			file.delete();
		try {
			FileOutputStream out = new FileOutputStream(file);
			myBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
			out.flush();
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}

	/**
	 * Converts bitmap to byte array
	 * 
	 * @param bmp
	 * @return
	 */
	public static byte[] bitmapToByte(Bitmap bmp) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.JPEG, 80, stream);
		byte[] byteArray = stream.toByteArray();
		return byteArray;
	}

	public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
		int width = image.getWidth();
		int height = image.getHeight();

		float bitmapRatio = (float) width / (float) height;
		if (bitmapRatio > 1) {
			width = maxSize;
			height = (int) (width / bitmapRatio);
		} else {
			height = maxSize;
			width = (int) (height * bitmapRatio);
		}
		return Bitmap.createScaledBitmap(image, width, height, true);
	}
}
