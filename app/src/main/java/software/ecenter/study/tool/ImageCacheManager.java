package software.ecenter.study.tool;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

/**
 * @author Mike
 * 图片缓存管理类：
 * 使用方式：
 *    1、判断缓存是否存在
 *     BitmapDrawable drawable = ImageCacheManager.getBitmapFromMemoryCache(url);
 *		if (drawable != null) {
 *			imageView.setImageDrawable(drawable);
 *		//取消掉后台的潜在任务 然后进行下载
 *		} else if (cancelPotentialWork(url, imageView)) {
 *			BitmapWorkerTask task = new BitmapWorkerTask(imageView);
 *	    	AsyncDrawable asyncDrawable = new AsyncDrawable(mContext.getResources(), mLoadingBitmap, task);
 *			imageView.setImageDrawable(asyncDrawable);
 *			task.execute(url);
 *		}
 */
public class ImageCacheManager {

	public final String TAG = "ImageCacheManager";
	/**
	 * 图片缓存技术的核心类，用于缓存所有下载好的图片，在程序内存达到设定值时会将最少最近使用的图片移除掉。
	 */
	private LruCache<String, BitmapDrawable> mMemoryCache;

	private Context mContext;

	private Bitmap mLoadingBitmap;

	/**是否缓存：默认缓存*/
	private boolean isCache;

	/**是否圆角图片**/
	private boolean isRoundImage;

	/**圆形**/
	private boolean isRoundedCornerImage;

	private int desWidth;

	private int desHeight;

	/**
	 * 
	 * @param maxSize kB  缓存大小
	 * @param defaultDrawableId 默认图片（加载图片前的默认图片）
	 *  开启缓存
	 */
	public ImageCacheManager(Context context, int maxSize, int defaultDrawableId) {
		this.mContext = context;
		this.isCache = true;
		mLoadingBitmap = BitmapFactory.decodeResource(context.getResources(), defaultDrawableId);
		mMemoryCache = new LruCache<String, BitmapDrawable>(maxSize * 1024) {
			@Override
			protected int sizeOf(String key, BitmapDrawable drawable) {
				return drawable.getBitmap().getRowBytes() * drawable.getBitmap().getHeight();
			}
		};

	}

	/**
	 * 当前运行程序最大内存的8分之一作为缓存
	 * 
	 * @param defaultDrawableId 默认图片（加载图片前的默认图片）
	 * @param isCache 是否开启缓存
	 */
	public ImageCacheManager(Context context, int defaultDrawableId, boolean isCache) {
		this.mContext = context;
		this.isCache = isCache;
		mLoadingBitmap = BitmapFactory.decodeResource(context.getResources(), defaultDrawableId);
		if (isCache) {
			// 获取应用程序最大可用内存
			int maxMemory = (int) Runtime.getRuntime().maxMemory();
			int cacheSize = maxMemory / 8;
			Log.d(TAG, "cacheSize " + cacheSize);
			mMemoryCache = new LruCache<String, BitmapDrawable>(cacheSize) {
				@Override
				protected int sizeOf(String key, BitmapDrawable drawable) {
					return drawable.getBitmap().getRowBytes() * drawable.getBitmap().getHeight();
				}
			};
		}
	}

	public void setImage(String url, ImageView image) {
		if (TextUtils.isEmpty(url)) {
			return;
		}
		BitmapWorkerTask task = new BitmapWorkerTask(image);
		AsyncDrawable asyncDrawable = new AsyncDrawable(mContext.getResources(), mLoadingBitmap, task);
		image.setImageDrawable(asyncDrawable);
		task.execute(url);
	}

	public void setImage(String url, ImageView image, int desWidth, int desHeight) {
		if (TextUtils.isEmpty(url)) {
			return;
		}
		this.desWidth = desWidth;
		this.desHeight = desHeight;
		BitmapWorkerTask task = new BitmapWorkerTask(image);
		AsyncDrawable asyncDrawable = new AsyncDrawable(mContext.getResources(), mLoadingBitmap, task);
		image.setImageDrawable(asyncDrawable);
		task.execute(url);
	}

	/**
	 * 自定义的一个Drawable，让这个Drawable持有BitmapWorkerTask的弱引用。
	 */
	class AsyncDrawable extends BitmapDrawable {

		private WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

		public AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask) {
			super(res, bitmap);
			bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
		}

		public BitmapWorkerTask getBitmapWorkerTask() {
			return bitmapWorkerTaskReference.get();
		}

	}

	/**
	 * 获取传入的ImageView它所对应的BitmapWorkerTask。
	 */
	private BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
		if (imageView != null) {
			Drawable drawable = imageView.getDrawable();
			if (drawable instanceof AsyncDrawable) {
				AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
				return asyncDrawable.getBitmapWorkerTask();
			}
		}
		return null;
	}

	/**
	 * 取消掉后台的潜在任务，当认为当前ImageView存在着一个另外图片请求任务时
	 * ，则把它取消掉并返回true，否则返回false。
	 */
	public boolean cancelPotentialWork(String url, ImageView imageView) {
		BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
		if (bitmapWorkerTask != null) {
			String imageUrl = bitmapWorkerTask.imageUrl;
			if (imageUrl == null || !imageUrl.equals(url)) {
				bitmapWorkerTask.cancel(true);
			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 * 将一张图片存储到LruCache中。
	 * 
	 * @param key
	 *            LruCache的键，这里传入图片的URL地址。
	 * @param drawable
	 *            LruCache的值，这里传入从网络上下载的BitmapDrawable对象。
	 */
	public void addBitmapToMemoryCache(String key, BitmapDrawable drawable) {
		if (getBitmapFromMemoryCache(key) == null) {
			mMemoryCache.put(key, drawable);
		}
	}

	/**
	 * 从LruCache中获取一张图片，如果不存在就返回null。
	 * 
	 * @param key
	 *            LruCache的键，这里传入图片的URL地址。
	 * @return 对应传入键的BitmapDrawable对象，或者null。
	 */
	public BitmapDrawable getBitmapFromMemoryCache(String key) {
		if (TextUtils.isEmpty(key)) {
			return null;
		}

		return mMemoryCache.get(key);
	}

	/**
	 * 异步下载图片的任务。
	 * 
	 * @author guolin
	 */
	class BitmapWorkerTask extends AsyncTask<String, Void, BitmapDrawable> {

		String imageUrl;

		private WeakReference<ImageView> imageViewReference;

		public BitmapWorkerTask(ImageView imageView) {
			imageViewReference = new WeakReference<ImageView>(imageView);
		}

		@Override
		protected BitmapDrawable doInBackground(String... params) {
			imageUrl = params[0];
			// 在后台开始下载图片

			Bitmap bitmap = downloadBitmap(imageUrl);

			if (bitmap != null) {
				BitmapDrawable drawable = new BitmapDrawable(mContext.getResources(), bitmap);
				if (isCache) {
					addBitmapToMemoryCache(imageUrl, drawable);
				}
				return drawable;
			}
			return null;
		}

		@Override
		protected void onPostExecute(BitmapDrawable drawable) {
			ImageView imageView = getAttachedImageView();
			if (imageView != null && drawable != null) {
				imageView.setImageDrawable(drawable);
			}
		}

		/**
		 * 获取当前BitmapWorkerTask所关联的ImageView。
		 */
		private ImageView getAttachedImageView() {
			ImageView imageView = imageViewReference.get();
			BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
			if (this == bitmapWorkerTask) {
				return imageView;
			}
			return null;
		}

		/**
		 * 建立HTTP请求，并获取Bitmap对象。
		 * 
		 * @param imageUrl
		 *            图片的URL地址
		 * @return 解析后的Bitmap对象
		 */
		private Bitmap downloadBitmap(String imageUrl) {
			Bitmap bitmap = null;
			HttpURLConnection con = null;
			try {
				long startNet = System.currentTimeMillis();
				URL url = new URL(imageUrl);
				con = (HttpURLConnection) url.openConnection();
				con.setConnectTimeout(5 * 1000);
				con.setReadTimeout(10 * 1000);
				//bitmap = BitmapFactory.decodeStream(con.getInputStream());
				//InputStream in = con.getInputStream();
				byte[] data = readInputStream(con.getInputStream());
				//联网的速度
				Log.d(TAG, "time netWork " + (System.currentTimeMillis() - startNet));
				long start = System.currentTimeMillis();
				//bitmap = decodeBitmap(data, 450, 150);
				//bitmap = decodeBitmap(data, 200, 200);
				//如果指定了目标宽度，就进行缩放
				if (desWidth != 0 && desHeight != 0) {

					bitmap = ImageTool.decodeBitmap(data, desWidth, desHeight);
				} else {
					//原图
					bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
				}
				Log.d(TAG, "bitmap.w = " + bitmap.getWidth() + "   bitmap.h = " + bitmap.getHeight());
				//圆角
				if (isRoundImage) {
					bitmap = ImageTool.getRoundedCornerBitmap(bitmap, 14);
				} else if (isRoundedCornerImage) {
					//圆形	
					bitmap = ImageTool.getRoundedCornerBitmap(bitmap);
				}
				//生成Bitmap 的速度
				Log.d(TAG, "time bitmap" + (System.currentTimeMillis() - start));
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (con != null) {
					con.disconnect();
				}
			}
			return bitmap;
		}
	}

	/**从输入流中获取数据并以字节数组返回
	 * @param inputStream
	 * @return
	 * @throws Exception
	 */
	public static byte[] readInputStream(InputStream inputStream) throws Exception {
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		while ((len = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, len);
		}
		outputStream.close();
		inputStream.close();
		return outputStream.toByteArray();
	}

	/**
	 * 圆角
	 * */
	public void setRoundImage(boolean isRoundImage) {
		this.isRoundImage = isRoundImage;
	}

	/**
	 * 圆形
	 * */
	public void setRoundedCornerImage(boolean isRoundedCornerImage) {
		this.isRoundedCornerImage = isRoundedCornerImage;
	}

}
