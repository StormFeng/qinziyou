package midian.baselib.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * 文件操作帮助类
 * 
 * @author leeib
 * 
 */
public class FDFileUtil {

	public static final String BASE_FILE_PATH = "midian";
	/**
	 * 生成文件目录
	 * 
	 * @param context
	 * @param imageSubDirInSDCard
	 *            文件缓存在sd卡中的子目录
	 * @return
	 */
	public static File getFileCacheDir(Context context,
			String imageSubDirInSDCard) {
		File cacheDir = null;
		if (imageSubDirInSDCard == null) {
			imageSubDirInSDCard = "FastDevelop";
		}
		// 如果有SD卡则在SD卡中建一个LazyList的目录存放缓存的图片
		// 没有SD卡就放在系统的缓存目录中
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState()))
			cacheDir = new File(
					Environment.getExternalStorageDirectory(),
					imageSubDirInSDCard);
		else
			cacheDir = context.getCacheDir();
		if (!cacheDir.exists())
			cacheDir.mkdirs();

		return cacheDir;
	}

	/**
	 * 生成文件目录
	 * 
	 * @param context
	 * @param imageSubDirInSDCard
	 * @return
	 */
	public static File getStorageDir(Context context, String imageSubDirInSDCard) {
		File cacheDir = null;
		if (imageSubDirInSDCard == null) {
			imageSubDirInSDCard = "FastDevelop";
		}
		// 如果有SD卡则在SD卡中建一个LazyList的目录存放缓存的图片
		// 没有SD卡就放在系统的缓存目录中
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState()))
			cacheDir = new File(
					Environment.getExternalStorageDirectory(),
					imageSubDirInSDCard);
		else
			cacheDir = context.getCacheDir();
		if (!cacheDir.exists())
			cacheDir.mkdirs();

		return Environment.getExternalStorageDirectory();
	}

	public static String getPhotoPath(Context context) {
		File cacheDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				BASE_FILE_PATH);
		if (!cacheDir.exists())
			cacheDir.mkdirs();
		return cacheDir.getAbsolutePath();
	}

	/**
	 * 取到内部存储的根路径
	 * 
	 * @param context
	 * @return
	 */
	public static String getRootPath(Context context) {

		String path;
		File fileDir;
		File sdcardDir;
		fileDir = context.getFilesDir();
		sdcardDir = Environment.getExternalStorageDirectory();
		if (Environment.MEDIA_REMOVED.equals(Environment
				.getExternalStorageState())) {
			path = fileDir.getParent() + File.separator
					+ fileDir.getName();
		} else {
			path = sdcardDir.getParent() + "/" + sdcardDir.getName();
		}
		return path;
	}

	/**
	 * 
	 * 要拷贝的文件
	 * 拷贝到的新文件
	 * 是否覆盖文件
	 */
	public static void copyFile(File fromFile, File toFile, Boolean rewrite) {
		if (!fromFile.exists()) {
			return;
		}
		if (!fromFile.isFile()) {
			return;
		}

		if (!fromFile.canRead()) {

			return;

		}

		if (!toFile.getParentFile().exists()) {

			toFile.getParentFile().mkdirs();

		}

		if (toFile.exists() && rewrite) {

			toFile.delete();

		}

		try {

			FileInputStream fosfrom = new FileInputStream(fromFile);

			FileOutputStream fosto = new FileOutputStream(toFile);

			byte bt[] = new byte[1024];

			int c;

			while ((c = fosfrom.read(bt)) > 0) {

				fosto.write(bt, 0, c); // 将内容写到新文件当中

			}
			fosfrom.close();

			fosto.close();

		} catch (Exception ex) {
			Log.e("readfile", "");
		}

	}

	/**
	 * 把流写进sd卡
	 * 
	 * @param inputStream
	 * @param filePathName
	 * @return
	 */
	public static boolean writeInputStream2SDCard(InputStream inputStream,
			String filePathName) {
		boolean bIsSuc = true;
		OutputStream outputStream = null;
		String dirpath = filePathName.substring(0,
				filePathName.lastIndexOf("/"));
		File dir = new File(dirpath);

		if (!dir.isDirectory()) {
			dir.mkdirs();
		}
		File file = new File(filePathName);

		if (!file.exists()) {
			try {
				file.createNewFile();
				Runtime.getRuntime().exec("chmod 766 " + file);
			} catch (IOException e) {
				System.out.println("writeInputStream2SDCard create " + e.toString());
				bIsSuc = false;
			}

		} else {// 存在
			return true;
		}

		try {

			outputStream = new FileOutputStream(file);

			int nLen = 0;

			byte[] buff = new byte[1024 * 1];
			while ((nLen = inputStream.read(buff)) > 0) {
				outputStream.write(buff, 0, nLen);
			}
			// 完成
		} catch (IOException e) {
			System.out.println("writeInputStream2SDCard write  " + e.toString());
			bIsSuc = false;
		} finally {
			try {
				if (outputStream != null) {
					outputStream.close();
				}

				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				// bIsSuc = false;
			}

		}
		return bIsSuc;
	}

	/**
	 * 解压zip文件. 将zipFile文件解压到folderPath目录下.
	 * 
	 * @throws Exception
	 */
	public static void upZipFile(File zipFile, String folderPath)
			throws ZipException, IOException {
		// public static void upZipFile() throws Exception{
		ZipFile zfile = new ZipFile(zipFile);
		Enumeration zList = zfile.entries();
		ZipEntry ze = null;
		byte[] buf = new byte[1024];
		while (zList.hasMoreElements()) {
			ze = (ZipEntry) zList.nextElement();
			if (ze.isDirectory()) {
				String dirstr = folderPath + ze.getName();
				// dirstr.trim();
				dirstr = new String(dirstr.getBytes("8859_1"), "utf-8");
				File f = new File(dirstr);
				f.mkdir();
				continue;
			}
			OutputStream os = new BufferedOutputStream(new FileOutputStream(
					getRealFileName(folderPath, ze.getName())));
			InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
			int readLen = 0;
			while ((readLen = is.read(buf, 0, 1024)) != -1) {
				os.write(buf, 0, readLen);
			}
			is.close();
			os.close();
		}
		zfile.close();
	}

	/**
	 * 解压zip文件. 将zipFile文件解压到folderPath目录下.
	 * 
	 * @throws Exception
	 */
	public static void upZipFile(File zipFile, String folderPath, String name)
			throws ZipException, IOException {
		// public static void upZipFile() throws Exception{
		ZipFile zfile = new ZipFile(zipFile);
		Enumeration zList = zfile.entries();
		ZipEntry ze = null;
		byte[] buf = new byte[1024];
		while (zList.hasMoreElements()) {
			ze = (ZipEntry) zList.nextElement();
			if (ze.isDirectory()) {
				String dirstr = folderPath + name;
				// dirstr.trim();
				dirstr = new String(dirstr.getBytes("8859_1"), "utf-8");
				File f = new File(dirstr);
				f.mkdir();
				continue;
			}
			String path = ze.getName();
			path = path.substring(path.indexOf("/"), path.length());
			path = name + path;
			OutputStream os = new BufferedOutputStream(new FileOutputStream(
					getRealFileName(folderPath, path)));
			InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
			int readLen = 0;
			while ((readLen = is.read(buf, 0, 1024)) != -1) {
				os.write(buf, 0, readLen);
			}
			is.close();
			os.close();
		}
		zfile.close();
	}

	/**
	 * 给定根目录，返回一个相对路径所对应的实际文件名.
	 * 
	 * @param baseDir
	 *            指定根目录
	 * @param absFileName
	 *            相对路径名，来自于ZipEntry中的name
	 * @return java.io.File 实际的文件
	 */
	public static File getRealFileName(String baseDir, String absFileName) {
		String[] dirs = absFileName.split("/");
		File ret = new File(baseDir);
		String substr = null;
		if (dirs.length > 1) {
			for (int i = 0; i < dirs.length - 1; i++) {
				substr = dirs[i];
				try {
					// substr.trim();
					substr = new String(substr.getBytes("8859_1"), "utf-8");

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ret = new File(ret, substr);

			}
			if (!ret.exists())
				ret.mkdirs();
			substr = dirs[dirs.length - 1];
			try {
				// substr.trim();
				substr = new String(substr.getBytes("8859_1"), "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			ret = new File(ret, substr);
			return ret;
		}
		return ret;
	}

	public static void deleteFile(File file, Context mContext) {
		if (file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文件
				file.delete(); // delete()方法 你应该知道 是删除的意思;
			} else if (file.isDirectory()) { // 否则如果它是一个目录
				File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
				for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
					mContext.deleteFile(files[i].getAbsolutePath()); // 把每个文件
																		// 用这个方法进行迭代
				}
			}
			file.delete();
		} else {
		}
	}

	/**
	 * 递归删除文件
	 * 
	 * @param file
	 */
	public static void deleteFile(File file) {

		if (file.isFile()) {
			renameToDeleteFile(file);
			return;
		}

		if (file.isDirectory()) {
			File[] childFiles = file.listFiles();
			if (childFiles == null || childFiles.length == 0) {
				renameToDeleteFile(file);
				return;
			}

			for (int i = 0; i < childFiles.length; i++) {
				deleteFile(childFiles[i]);
			}
			renameToDeleteFile(file);
		}
	}

	private static void renameToDeleteFile(File file) {
		String path = file.getAbsolutePath();
		File bpath = new File(path.substring(0, path.lastIndexOf("/") + 1)
				+ System.currentTimeMillis());
		file.renameTo(bpath);
		bpath.delete();
	}

	public static boolean sdcardExist() {
		return "mounted".equals(Environment.getExternalStorageState());
	}

	public static String getSdcardPath() {
		if (sdcardExist())
			return Environment.getExternalStorageDirectory().getAbsolutePath();
		return null;
	}

	
}
