package software.ecenter.study.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * description:
 */
public class FileAccessor {

	public static long getFileSize(String path) {
		File fc = null;
		try {

			fc = new File(path);
			if (fc.exists()) {
				return fc.length();
			}
		} catch (Exception e) {
			//#debug error
			System.out.println(e);
		} finally {
			if (fc != null) {
				//				closeFileConnection(fc);
				fc = null;
			}
		}
		return 0;
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param path
	 *            文件名
	 * @return
	 */
	public static final boolean exists(String path) throws IOException {
		File fc = null;
		try {

			fc = new File(path);
			if (fc.exists()) {
				return true;
			}
		} catch (Exception e) {
			//#debug error
			System.out.println(e);
		} finally {
			if (fc != null) {

				fc = null;
			}
		}
		return false;
	}

	/**
	 * @param dir
	 * @throws IOException 
	 */
	public static void makeDirs(String dir) throws IOException {
		File fc = null;
		try {

			fc = new File(dir);
			if (!fc.exists()) {
				fc.mkdirs();
			}

		} catch (Exception e) {
			//#debug error
			System.out.println(e);
		} finally {
			if (fc != null) {

				fc = null;
			}
		}
	}

	/**
	 * @param file
	 * @throws IOException 
	 */
	public static void createFile(String path) throws IOException {
		File fc = null;
		try {

			fc = new File(path);
			if (fc.exists()) {
				fc.delete();
				fc.createNewFile();
				return;
			}
			fc.createNewFile();
		} catch (IOException e) {
			//#debug error
			System.out.println(e);
			throw e;
		} finally {
			if (fc != null) {

				fc = null;
			}
		}

	}

	/**
	 * @
	 * @throws IOException 
	 */
	public static void deleteFile(String path) throws IOException {
		File fc = null;
		try {

			fc = new File(path);
			if (fc.exists()) {
				fc.delete();
			}

		} catch (Exception e) {
			//#debug error
			System.out.println(e);
		} finally {
			if (fc != null) {

				fc = null;
			}
		}

	}

	/**
	 * 重命名文件
	 * @param oldPath 旧文件路径
	 * @param newPath 新文件路径
	 * @throws IOException
	 */
	public static void renameFile(String oldPath, String newPath) throws IOException {
		File fc = null;
		try {

			fc = new File(oldPath);
			if (fc.exists()) {
				File newFile = new File(newPath);
				if(newFile.exists()){
					newFile.delete();
				}
				fc.renameTo(newFile);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fc != null) {

				fc = null;
			}
		}

	}

	public static void deleteDir(String dir) {
		try {
			File file = new File(dir);
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				File f = files[i];
				if (f.isDirectory()) {
					deleteDir(dir + "/" + f.getName());
					f.delete();
				} else {
					f.delete();
				}
			}
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean WriteStringToFile(String filePath,String string) {
		boolean ok= false;
		try {
			FileOutputStream fos = new FileOutputStream(filePath);
			fos.write(string.getBytes());
			fos.close();
			ok =true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ok;
	}
	public  static String readString(File file) {

		String str="";

		try {
			FileInputStream in=new FileInputStream(file);
			// size  为字串的长度 ，这里一次性读完
			int size=in.available();

			byte[] buffer=new byte[size];

			in.read(buffer);

			in.close();

			str=new String(buffer);

		} catch (IOException e) {
			e.printStackTrace();

		}
		return str;

	}

	/***
	 * 用id，具有唯一性,其他属性后台可能更改,不带后缀
	 *
	 * @param id
	 * @return
	 */
	public static String getFileName(String id) {
		String fileName = Md5Tool.hashKey(id) + ".";
		return fileName;
	}
}
