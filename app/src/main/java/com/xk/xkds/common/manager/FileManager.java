package com.xk.xkds.common.manager;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 管理app文件路径以及文件操作的类
 * 
 */
public class FileManager {

	/**
	 * app 的sdcard上的主目录
	 */
	public static final String DIR_MAIN = "kukanTv";// FileManager.class.getPackage().getName();
	private static final String DIR_IMAGE = "images";
	private static final String DIR_SCREEN = "screen";
	public static final String DIR_DOWNLOAD = "download";
	public static final String DIR_APKS = "apks";
	public static final String DIR_SPEED= "speed";
	public static final String DIR_PLUGIN = "plugins";
	public static final String DIR_SPIRIT_DB = "db";
	public static final String FILE_CONFIG = "config.properties";
	public static final String DIR_DOWNLOAD_APK_PATH = FileManager.DIR_MAIN + File.separator
			+ FileManager.DIR_DOWNLOAD + File.separator + FileManager.DIR_APKS + File.separator;
	public static final String DIR_DOWNLOAD_PLUGIN_PATH = FileManager.DIR_MAIN + File.separator
			+ FileManager.DIR_PLUGIN + File.separator;
	public static final String DIR_NET_SPEED_PATH = FileManager.DIR_MAIN + File.separator
			+ FileManager.DIR_DOWNLOAD + File.separator + FileManager.DIR_SPEED + File.separator;


	public static String getRootSdPath() throws FileNotFoundException {
		if (!isSdcardAvalible()) {
			throw new FileNotFoundException("无效Sd卡");
		}
		String path = null;
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;

		}
		if (TextUtils.isEmpty(path)) {
			throw new FileNotFoundException("无效Sd卡");
		}
		return path;
	}


	/**
	 * 获取app sdcard主目录 e.g sdcard/myapp/
	 * 
	 * @return 主目录名，包括分隔符 “/”
	 * @throws FileNotFoundException
	 */
	public static String getMainPathInSd() throws FileNotFoundException {
		String rootPath = getRootSdPath();
		String path = rootPath + DIR_MAIN + File.separator;
		checkOrMkdirs(path);
		createNomediaFile(path);
		return path;
	}

	/**
	 *  返回主目录 用于
	 * @return
	 */
	public static String getMainPathInSd(Context context)  {
		String rootPath = null;
		String path=null;
		try {
			rootPath = getRootSdPath();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (rootPath!=null){
			 path = rootPath + DIR_MAIN + File.separator;
		}else {
			path = context.getCacheDir().getAbsolutePath() + File.separator;
		}
		checkOrMkdirs(path);
		createNomediaFile(path);
		return path;
	}


	public static String getMainPathInternal(Context context) {
		File internalFile = context.getFilesDir();
		return internalFile.getAbsolutePath() + File.separator;
		
	}
  

	/**
	 * mounted and writable
	 * 
	 * @return
	 */
	public static boolean isSdcardAvalible() {
		boolean avalible = false;
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			boolean writable = Environment.getExternalStorageDirectory().canWrite();
			avalible = writable;
		} else {
			avalible = false;
		}
		return avalible;
	}


	/**
	 * 图片缓存的路径
	 * 如果sdcard无效，则放到手机内部存储
	 * 
	 * @return
	 * @throws FileNotFoundException
	 */
	public static String getImageCachePath(Context context) {
		String path = null;
		try {
			path = getMainPathInSd() + DIR_IMAGE + File.separator;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			path = context.getCacheDir().getAbsolutePath() + File.separator;
		}
		checkOrMkdirs(path);
		return path;
	}

	/**
	 * 获取截屏路径,默认保存在内存中
	 * @param context
	 * @return
	 */
	public static String getScreenShotPath(Context context) {
		String path = context.getCacheDir().getAbsolutePath() + File.separator;
		checkOrMkdirs(path);
		return path;
	}

	private static void createNomediaFile(String path) {
		File file = new File(path, ".nomedia");
		if(file != null && !file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	/**
	 * 数据库目录
	 * 如果没有sdcard，则数据库默认保存在data/data/%packagename%/databases/ 目录下
	 * 
	 * @return
	 * @throws FileNotFoundException
	 */
	public static String getSpiritDbPath(Context context)   {
		String path = null;
		try {
			path = getMainPathInSd() + DIR_SPIRIT_DB + File.separator;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			path = context.getFilesDir().getAbsolutePath() + File.separator;
		}
		checkOrMkdirs(path);
		return path;
	}

	/**获取下载路径*/
	public static String getApksDownloadPath(Context context) {
		// String path = getMainPathInSd() + DIR_DOWNLOAD + File.separator + DIR_APKS + File.separator;
		String path = null;
		try {
			path = getRootSdPath() + DIR_DOWNLOAD_APK_PATH;
		} catch (FileNotFoundException e) {
			path = context.getFilesDir().getAbsolutePath() + File.separator;
			e.printStackTrace();
		}
		checkOrMkdirs(path);
		return path;
	}


	/**获取播放插件下载路径*/
	public static String getPlayPluginDownloadPath(Context context) {
		String path = null;
		try {
			path = getRootSdPath() + DIR_DOWNLOAD_PLUGIN_PATH;
		} catch (FileNotFoundException e) {
			path = context.getFilesDir().getAbsolutePath() + File.separator;
			e.printStackTrace();
		}
		checkOrMkdirs(path);
		return path;
	}

	/**获取插件下载路径*/
	public static String getPluginDownloadPath(Context context) {
		String path = null;
		try {
			path = getRootSdPath() + DIR_DOWNLOAD_PLUGIN_PATH;
		} catch (FileNotFoundException e) {
			path = context.getFilesDir().getAbsolutePath() + File.separator;
			e.printStackTrace();
		}
		checkOrMkdirs(path);
		return path;
	}

	/**获取下载路径*/
	public static String getSpeedDownloadPath(Context context) {
		String path = null;
		try {
			path = getRootSdPath() + DIR_NET_SPEED_PATH;
		} catch (FileNotFoundException e) {
			path = context.getFilesDir().getAbsolutePath() + File.separator;
			e.printStackTrace();
		}
		checkOrMkdirs(path);
		return path;
	}


	private static void checkOrMkdirs(String path) {
		if (TextUtils.isEmpty(path)) {
			throw new RuntimeException("FileManager:  invalid file path");

		} else {
			File dir = new File(path);
			if (!dir.exists()) {
				boolean isSuccess = dir.mkdirs();
			}
		}
	}


	/**
	 * app配置文件
	 * 
	 */
	public static File getConfigFile(Context context) {
		String mainPath = null;
		try {
			mainPath = getMainPathInSd();
		} catch (FileNotFoundException e1) {
			mainPath = null;
			e1.printStackTrace();
		}
		File retFile;
		if (TextUtils.isEmpty(mainPath)) {
			retFile = new File(context.getFilesDir(), FILE_CONFIG);
		} else {
			retFile = new File(mainPath, FILE_CONFIG);
		}
		if (!retFile.exists()) {
			try {
				retFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return retFile;
	}


	public static File getFileByName(Context context, String path, String fileName) {
		File dir = new File(path);
		if (!dir.exists()) {
			return null;
		}

		File[] files = dir.listFiles();
		if (files != null && files.length > 0) {
			for (File file : files) {
				if (fileName.equals(file.getName())) {
					return file;
				}
			}
		}

		return null;
	}


	/**
	 * 通过url解析出文件的名称 e.g http://app.coohua.com/apk/1024yy-1039.apk
	 */
	public static String parseNameOf(String url) {
		String appName = null;
		if (!TextUtils.isEmpty(url)) {
			int index = url.lastIndexOf("/");
			if (index > 0) {
				appName = url.substring(index + 1);
			}
		}

		return appName;
	}


	/**
	 * 没有做path dir的检查
	 * 
	 * TODO: 待test
	 */
	public static boolean writeObject(Context context, Serializable src, String dest) {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = new FileOutputStream(new File(dest), false);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(src);
			oos.flush();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				oos.close();
			} catch (Exception e) {
			}
			try {
				fos.close();
			} catch (Exception e) {
			}
		}
	}


	/**
	 * 将数据以Serializable的格式读出
	 * 
	 * TODO: 待测试
	 * 
	 * @param context
	 * @param src
	 * @return
	 */
	public static Serializable readObject(Context context, String src) {
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = new FileInputStream((new File(src)));
			ois = new ObjectInputStream(fis);
			return (Serializable) ois.readObject();
		} catch (FileNotFoundException e) {
		} catch (Exception e) {
			e.printStackTrace();
			// 反序列化失败 - 删除缓存文件
			// if(e instanceof InvalidClassException){
			// File data = mContext.getFileStreamPath(file);
			// data.delete();
			// }
		} finally {
			try {
				ois.close();
			} catch (Exception e) {
			}
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
		return null;
	}
	/**
	 * 创建指定大小和类型的文件
	 * @author cxq
	 * @param targetFile 文件路径以及文件名，需要加后缀
	 * @param fileLength 文件大小
	 * @retrun boolean
	 */
	public static boolean createFile(String targetFile, long fileLength) {
		//指定每次分配的块大小
		long KBSIZE = 1024;
		long MBSIZE1 = 1024 * 1024;
		long MBSIZE10 = 1024 * 1024 * 10;
		fileLength = fileLength * 1024*1024;
		/*switch (unit) {
			case KB:
				fileLength = fileLength * 1024;
				break;
			case MB:
				fileLength = fileLength * 1024*1024;
				break;
			case GB:
				fileLength = fileLength * 1024*1024*1024;
				break;

			default:
				break;
		}*/
		FileOutputStream fos = null;
		File file = new File(targetFile);
		if (file.exists()){
			return true;
		}
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			long batchSize = 0;
			batchSize = fileLength;
			if (fileLength > KBSIZE) {
				batchSize = KBSIZE;
			}
			if (fileLength > MBSIZE1) {
				batchSize = MBSIZE1;
			}
			if (fileLength > MBSIZE10) {
				batchSize = MBSIZE10;
			}
			long count = fileLength / batchSize;
			long last = fileLength % batchSize;

			fos = new FileOutputStream(file);
			FileChannel fileChannel = fos.getChannel();
			for (int i = 0; i < count; i++) {
				ByteBuffer buffer = ByteBuffer.allocate((int) batchSize);
				fileChannel.write(buffer);
			}
			if (last != 0) {
				ByteBuffer buffer = ByteBuffer.allocate((int) last);
				fileChannel.write(buffer);
			}
			fos.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}
