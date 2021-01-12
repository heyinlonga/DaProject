package software.ecenter.study.tool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class ImageTool {
	/**
	 * 缩放图片
	 * @author silent
	 * @param data 数据
	 * @param desWidth 目标宽
	 * @param desHeight 目标高
	 *
	 * */
	public static Bitmap decodeBitmap(byte[] data, int desWidth, int desHeight) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;// 设置成了true,不占用内存，只获取bitmap宽高
		//BitmapFactory.decodeFile(filePath, opts);
		BitmapFactory.decodeByteArray(data, 0, data.length, opts);

		//如，insamplesize = = 4返回一个图像是1 / 4的宽度/高度 ，和1 / 16像素数。
		//opts.inSampleSize = computeSampleSize(opts, -1, 1024 * 800);
		opts.inSampleSize = computeSampleSize(opts, -1, desWidth * desHeight);
		opts.inJustDecodeBounds = false;// 这里一定要将其设置回false，因为之前我们将其设置成了true
		Bitmap bitmap = null;
		try {
			//	bitmap = BitmapFactory.decodeStream(is, null, opts);
			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	//控制图片内存溢出
	public static Bitmap decodeBitmap(String filePath, int desWidth, int desHeight) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;// 设置成了true,不占用内存，只获取bitmap宽高
		BitmapFactory.decodeFile(filePath, opts);

		//如，insamplesize = = 4返回一个图像是1 / 4的宽度/高度 ，和1 / 16像素数。
		opts.inSampleSize = computeSampleSize(opts, -1, desWidth * desHeight);
		opts.inJustDecodeBounds = false;// 这里一定要将其设置回false，因为之前我们将其设置成了true
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeFile(filePath, opts);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	public static Bitmap decodeBitmap(String filePath) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;// 设置成了true,不占用内存，只获取bitmap宽高
		BitmapFactory.decodeFile(filePath, opts);

		//如，insamplesize = = 4返回一个图像是1 / 4的宽度/高度 ，和1 / 16像素数。
		opts.inSampleSize = computeSampleSize(opts, -1, 250 * 200);
		opts.inJustDecodeBounds = false;// 这里一定要将其设置回false，因为之前我们将其设置成了true
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeFile(filePath, opts);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/***
	 * @dec 此方法是防止拍出的照片过大，取图片为屏幕的两倍
	 * @param context
	 * @param sourFilePath  原图位置
	 * @param destinationFilePath 生成缩略图文件 如果为 null 覆盖原图
	 * @return 缩略图位置
	 * 压缩 图片
	 */
	public static String getThumPicPath(Context context, String sourFilePath, String destinationFilePath) {
		String thumPicPath = null;
		if (sourFilePath != null) {
			try {
				DisplayMetrics metrics = new DisplayMetrics();
				WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
				wm.getDefaultDisplay().getMetrics(metrics);

				int maxWidth = metrics.widthPixels * 2;
				int maxHeight = metrics.heightPixels * 2;
				/*				Log.d("ImageTool", "widthPixels: " + metrics.widthPixels + " heightPixels : " + metrics.heightPixels);
								Log.d("ImageTool", "maxWidth: " + maxWidth + " maxHeight : " + maxHeight);*/
				//缩放原图为屏幕长宽的两倍
				Bitmap bitmap = decodeBitmap(sourFilePath, maxWidth, maxHeight);

				int degree = getBitmapDegree(sourFilePath);

				if (degree > 0) {
					bitmap = rotateBitmapByDegree(bitmap, degree);
				}
				File sourFile = new File(sourFilePath);
				//原文件存在
				if (sourFile.exists()) {
					//覆盖原文件
					if (destinationFilePath == null) {
						File outFile = new File(sourFilePath + "_temp");
						FileOutputStream out = new FileOutputStream(outFile);
						if (bitmap.compress(Bitmap.CompressFormat.JPEG, 72, out)) {
//							if (sourFile.delete()) {
//								outFile.renameTo(sourFile);
//								thumPicPath = sourFilePath;
//							} else {
//								thumPicPath = sourFilePath;
//							}
                            thumPicPath = sourFilePath;
						}
					} else {
						File outFile = new File(destinationFilePath);
						FileOutputStream out = new FileOutputStream(outFile);
						if (bitmap.compress(Bitmap.CompressFormat.JPEG, 72, out)) {
							thumPicPath = destinationFilePath;
						}
					}

				}
				bitmap.recycle();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return thumPicPath;
	}

	/**获取图片文件角度*/
	public static int getBitmapDegree(String path) throws IOException {
		int degree = 0;

		// 从指定路径下读取图片，并获取其EXIF信息
		ExifInterface exifInterface = new ExifInterface(path);
		// 获取图片的旋转信息
		int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
		switch (orientation) {
		case ExifInterface.ORIENTATION_ROTATE_90:
			degree = 90;
			break;
		case ExifInterface.ORIENTATION_ROTATE_180:
			degree = 180;
			break;
		case ExifInterface.ORIENTATION_ROTATE_270:
			degree = 270;
			break;
		}

		return degree;
	}

	/**
	 * 将图片按照某个角度进行旋转
	 *
	 * @param bm
	 *            需要旋转的图片
	 * @param degree
	 *            旋转角度
	 * @return 旋转后的图片
	 */
	public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
		Bitmap returnBm = null;

		// 根据旋转角度，生成旋转矩阵
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);
		try {
			// 将原始图片按照旋转矩阵进行旋转，并得到新的图片
			returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
		} catch (OutOfMemoryError e) {
		}
		if (returnBm == null) {
			returnBm = bm;
		}
		if (bm != returnBm) {
			bm.recycle();
		}
		return returnBm;
	}

	/**
	 * 计算图片缩放多少倍
	 * @param minSideLength 最小边长
	 * @param maxNumOfPixels 最大像素
	 * **/
	public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			//比如 计算出 initialSize 为4 name 则 roundedSize 结果为4
			//第一次 1<4 》》  roundedSize = 2  第二次  2<4 》》 roundedSize = 4 然后跳出循环
			//这样写的目的应该是解码器是基于 2的 倍数解析的，如果 initialSize = 5 则 roundedSize = 6;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			//如果大于8  就基于 8 向上取整。 如 initialSize = 9  取整后roundedSize 为 16
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	/**
	 * 计算图片缩放多少倍
	 * @param minSideLength 最小边长
	 * @param maxNumOfPixels 最大像素
	 * **/
	private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		//下限 ： Math.ceil() 向上取整 如 Math.ceil(12.2)//返回13  这里计算 原图片是 目标最大图片的几倍
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
		//上限： Math.floor() 向下取整. 如 floor(9.999999) = 9.0  floor(-3.14) = -4.0 原图最小边 是目标最小边的几倍
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	/**
	 * 图片转成圆型
	 *
	 * @param bitmap
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		int min = Math.min(bitmap.getWidth(), bitmap.getHeight());
		Bitmap output = Bitmap.createBitmap(min, min, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, min, min);
		final RectF rectF = new RectF(rect);
		final float roundPx = bitmap.getWidth() / 2;

		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	/**
	 * 图片转成圆角
	 *
	 * @param bitmap
	 * @param roundPx 像素 (圆角)
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int roundPx) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);//抗锯齿
		paint.setFilterBitmap(true);//用来对位图进行滤波处理
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}
}
