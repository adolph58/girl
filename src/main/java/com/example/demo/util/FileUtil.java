package com.example.demo.util;

import java.io.*;

/**
 * Relating to file operation
 * 
 * @author Malik Xu
 */
public class FileUtil {
	/**
	 * Private the util from constructed by user
	 */
	private FileUtil() {

	}

	/**
	 * Modify file last access time ,if file not exist ,then create this file
	 * 
	 * @param file
	 *            need to modify file last access time
	 * @since 0.1
	 */
	public static void touch(File file) {
		long currentTime = System.currentTimeMillis();
		if (!file.exists()) {
			System.err.println("file not found : " + file.getName());
			System.err.println("Create a new file : " + file.getName());
			try {
				if (file.createNewFile()) {
					System.out.println("Succeeded!");
				} else {
					System.err.println("Create file failed!");
				}
			} catch (IOException e) {
				System.err.println("Create file failed!");
				e.printStackTrace();
			}
		}
		boolean result = file.setLastModified(currentTime);
		if (!result) {
			System.err.println("touch failed :  " + file.getName());
		}
	}

	/**
	 * Modify file last access time ,if file not exist ,then create this file
	 * 
	 * @param fileName
	 *            need to modify file last access time and file name
	 * @since 0.1
	 */
	public static void touch(String fileName) {
		File file = new File(fileName);
		touch(file);
	}

	/**
	 * Modify file last access time ,if file not exist ,then create this file
	 * 
	 * @param files
	 *            need to modify file last access time
	 * @since 0.1
	 */
	public static void touch(File[] files) {
		for (int i = 0; i < files.length; i++) {
			touch(files[i]);
		}
	}

	/**
	 * Modify file last access time ,if file not exist ,then create this file
	 * 
	 * @param fileNames
	 *            need to modify file last access time
	 * @since 0.1
	 */
	public static void touch(String[] fileNames) {
		File[] files = new File[fileNames.length];
		for (int i = 0; i < fileNames.length; i++) {
			files[i] = new File(fileNames[i]);
		}
		touch(files);
	}

	/**
	 * Judge file is exist or not
	 * 
	 * @param fileName
	 * 
	 * @return true : file exist;false : not exist
	 * @since 0.1
	 */
	public static boolean isFileExist(String fileName) {
		return new File(fileName).isFile();
	}

	/**
	 * Make file directory <b>PS : this method might create part directory even when
	 * return value is false</b>
	 * 
	 * @param file
	 * 
	 * @return true : create file directory with no error,false : during creating
	 *         file directory error occurred
	 * @since 0.1
	 */
	public static boolean makeDirectory(File file) {
		File parent = file.getParentFile();
		if (parent != null) {
			return parent.mkdirs();
		}
		return false;
	}

	public static boolean existDirectory(File file) {
		File parent = file.getParentFile();
		if (parent != null) {
			return parent.exists();
		}
		return false;
	}

	/**
	 * Make file directory <b>PS : this method might create part directory even when
	 * return value is false</b>
	 * 
	 * @param fileName
	 *            file directory name
	 * @return true : create file directory with no error,false : during creating
	 *         file directory error occurred
	 * @since 0.1
	 */
	public static boolean makeDirectory(String fileName) {
		File file = new File(fileName);
		return makeDirectory(file);
	}

	public static boolean existDirectory(String fileName) {
		File file = new File(fileName);
		return existDirectory(file);
	}

	/**
	 * 
	 * Delete all files in the specified directory, if some error occurred during
	 * deleting, it will be return false, This method do not delete the child
	 * directory and its files of the specified.
	 * 
	 * @param directory
	 *            directory want to be emptied
	 * @return true : all files in the directory have been deleted successfully
	 *         .false : error occurred during deleting
	 * @since 0.1
	 */
	public static boolean emptyDirectory(File directory) {
		@SuppressWarnings("unused")
		boolean result = false;
		File[] entries = directory.listFiles();
		for (int i = 0; i < entries.length; i++) {
			if (!entries[i].delete()) {
				result = false;
			}
		}
		return true;
	}

	/**
	 * Delete all files in the specified directory, if some error occurred during
	 * deleting, it will be return false, This method do not delete the child
	 * directory and its files of the specified.
	 * 
	 * @param directoryName
	 *            directory want to be emptied
	 * @return true : all files in the directory have been deleted successfully
	 *         .false : error occurred during deleting
	 * @since 0.1
	 */
	public static boolean emptyDirectory(String directoryName) {
		File dir = new File(directoryName);
		return emptyDirectory(dir);
	}

	public static boolean deleteFile(String fName) {
		return deleteFile(new File(fName));
	}

	public static boolean deleteFile(File f) {
		if (f != null && f.exists())
			return f.delete();
		else
			return false;
	}

	/**
	 * Delete all files in the specified directory
	 * 
	 * @param dirName
	 *            directory want to be deleted
	 * @return true : directory and all file have been deleted successfully ;false :
	 *         error occurred during deleting operation.
	 * @since 0.1
	 */
	public static boolean deleteDirectory(String dirName) {
		return deleteDirectory(new File(dirName));
	}

	/**
	 * Delete all files in the specified directory
	 * 
	 * @param dir
	 *            directory want to be deleted
	 * @return true : directory and all file have been deleted successfully ;false :
	 *         error occurred during deleting operation.
	 * @since 0.1
	 */
	public static boolean deleteDirectory(File dir) {
		if ((dir == null) || !dir.isDirectory()) {
			throw new IllegalArgumentException("Argument " + dir + " is not a directory. ");
		}

		File[] entries = dir.listFiles();

		int sz = entries.length;

		for (int i = 0; i < sz; i++) {
			if (entries[i].isDirectory()) {
				if (!deleteDirectory(entries[i])) {
					return false;
				}
			} else {
				if (!entries[i].delete()) {
					return false;
				}
			}
		}

		if (!dir.delete()) {
			return false;
		}
		return true;
	}

	/**
	 * Get file name by the given file path
	 * 
	 * @param filePath
	 * 
	 * @return file name
	 * @since 0.4
	 */
	public static String getFileName(String filePath) {
		File file = new File(filePath);
		return file.getName();
	}

	/**
	 * Get file absolute path
	 * 
	 * @param fileName
	 * 
	 * @return file path
	 * @since 0.4
	 */
	public static String getFilePath(String fileName) {
		File file = new File(fileName);
		return file.getAbsolutePath();
	}

	/**
	 * 
	 * Converse DOS/Windows type path to UNIX/Linux type's
	 * 
	 * @param filePath
	 * 
	 * @return conversed path
	 * @since 0.4
	 */
	public static String toUNIXpath(String filePath) {
		return filePath.replace('\\', '/');
	}

	/**
	 * 
	 * Get file absolute path from the given file name
	 * 
	 * @param fileName
	 * 
	 * @return UNIX file path
	 * @since 0.4
	 * @see #toUNIXpath(String filePath) toUNIXpath
	 */
	public static String getUNIXfilePath(String fileName) {
		File file = new File(fileName);
		return toUNIXpath(file.getAbsolutePath());
	}

	/**
	 * 
	 * Get file extension
	 * 
	 * @param fileName
	 * 
	 * @return file extension
	 * @since 0.5
	 */
	public static String getTypePart(String fileName) {
		int point = fileName.lastIndexOf('.');
		int length = fileName.length();
		if (point == -1 || point == length - 1) {
			return "";
		} else {
			return fileName.substring(point + 1, length);
		}
	}

	/**
	 * Get file extension
	 * 
	 * @param file
	 * 
	 * @return a string for file type
	 */
	public static String getFileType(File file) {
		return getTypePart(file.getName());
	}

	/**
	 * 
	 * Get file name by the given file path
	 * 
	 * @param fileName
	 * 
	 * @return file name
	 */
	public static String getNamePart(String fileName) {
		int point = getPathLastIndex(fileName);
		int length = fileName.length();
		if (point == -1) {
			return fileName;
		} else if (point == length - 1) {
			int secondPoint = getPathLastIndex(fileName, point - 1);
			if (secondPoint == -1) {
				if (length == 1) {
					return fileName;
				} else {
					return fileName.substring(0, point);
				}
			} else {
				return fileName.substring(secondPoint + 1, point);
			}
		} else {
			return fileName.substring(point + 1);
		}
	}

	/**
	 * 
	 * Get file parent path
	 * 
	 * @param fileName
	 * 
	 * @return parent file path, if not exist ,then return ""
	 * @since 0.5
	 */
	public static String getPathPart(String fileName) {
		int point = getPathLastIndex(fileName);
		int length = fileName.length();
		if (point == -1) {
			return "";
		} else if (point == length - 1) {
			int secondPoint = getPathLastIndex(fileName, point - 1);
			if (secondPoint == -1) {
				return "";
			} else {
				return fileName.substring(0, secondPoint);
			}
		} else {
			return fileName.substring(0, point);
		}
	}

	/**
	 * 
	 * Get first path separator position from the given file path
	 * 
	 * @param fileName
	 * 
	 * @return first path separator position ,if not exist ,return -1.
	 * @since 0.5
	 */
	public static int getPathIndex(String fileName) {
		int point = fileName.indexOf('/');
		if (point == -1) {
			point = fileName.indexOf('\\');
		}
		return point;
	}

	/**
	 * 
	 * Get first path separator position from the given file path
	 * 
	 * @param fileName
	 * 
	 * @param fromIndex
	 *            begin to find position.
	 * @return first path separator position ,if not exist ,return -1.
	 * @since 0.5
	 */
	public static int getPathIndex(String fileName, int fromIndex) {
		int point = fileName.indexOf('/', fromIndex);
		if (point == -1) {
			point = fileName.indexOf('\\', fromIndex);
		}
		return point;
	}

	/**
	 * Get last path separator position from the given file path
	 * 
	 * @param fileName
	 *            file name
	 * @return last path separator position ,if not exist ,return -1.
	 * @since 0.5
	 */
	public static int getPathLastIndex(String fileName) {
		int point = fileName.lastIndexOf('/');
		if (point == -1) {
			point = fileName.lastIndexOf('\\');
		}
		return point;
	}

	/**
	 * 
	 * Get specified path separator position from the given file path
	 * 
	 * @param fileName
	 *            file path
	 * @param fromIndex
	 *            begin to find position
	 * @return specified path separator position ,if not exist ,return -1.
	 * @since 0.5
	 */
	public static int getPathLastIndex(String fileName, int fromIndex) {
		int point = fileName.lastIndexOf('/', fromIndex);
		if (point == -1) {
			point = fileName.lastIndexOf('\\', fromIndex);
		}
		return point;
	}

	/**
	 * 
	 * Remove the file extension
	 * 
	 * @param filename
	 * 
	 * @return file name without extension
	 * @since 0.5
	 */
	public static String trimType(String filename) {
		int index = filename.lastIndexOf(".");
		if (index != -1) {
			return filename.substring(0, index);
		} else {
			return filename;
		}
	}

	/**
	 * Get relative file path
	 * 
	 * @param pathName
	 *            directory name
	 * @param fileName
	 * 
	 * @return file relative path , if file not exist, return file name
	 * @since 0.5
	 */
	public static String getSubpath(String pathName, String fileName) {
		int index = fileName.indexOf(pathName);
		if (index != -1) {
			return fileName.substring(index + pathName.length() + 1);
		} else {
			return fileName;
		}
	}

	/**
	 * Copy file
	 * 
	 * @param fromFileName
	 *            source file name
	 * @param toFileName
	 *            target file name
	 * @return true : file copy success. false : file copy failure
	 * @since 0.6
	 */
	public static boolean copy(String fromFileName, String toFileName) {
		return copy(fromFileName, toFileName, false);
	}

	/**
	 * Copy file
	 * 
	 * @param fromFileName
	 *            source file name
	 * @param toFileName
	 *            target file name
	 * @param override
	 *            if file exist ,override or not
	 * @return true : file copy success. false : file copy failure
	 * @since 0.6
	 */
	public static boolean copy(String fromFileName, String toFileName, boolean override) {
		File fromFile = new File(fromFileName);
		File toFile = new File(toFileName);

		if (!fromFile.exists() || !fromFile.isFile() || !fromFile.canRead()) {
			return false;
		}

		if (toFile.isDirectory()) {
			toFile = new File(toFile, fromFile.getName());

		}
		if (toFile.exists()) {
			if (!toFile.canWrite() || override == false) {
				return false;
			}
		} else {
			String parent = toFile.getParent();
			if (parent == null) {
				parent = System.getProperty("user.dir");
			}
			File dir = new File(parent);
			if (!dir.exists() || dir.isFile() || !dir.canWrite()) {
				return false;
			}
		}

		FileInputStream from = null;
		FileOutputStream to = null;
		try {
			from = new FileInputStream(fromFile);
			to = new FileOutputStream(toFile);
			byte[] buffer = new byte[4096];
			int bytes_read;
			while ((bytes_read = from.read(buffer)) != -1) {
				to.write(buffer, 0, bytes_read);
			}
			return true;
		} catch (IOException e) {
			return false;
		} finally {
			if (from != null) {
				try {
					from.close();
				} catch (IOException e) {
					;
				}
			}
			if (to != null) {
				try {
					to.close();
				} catch (IOException e) {
					;
				}
			}
		}
	}

	/**
	 * Copy file
	 * 
	 * @param toFileName
	 *            target file name
	 * @param fromFileName
	 *            source file name
	 * @throws IOException
	 */
	public static void copyDirectiory(String toFileName, String fromFileName) throws IOException {

		boolean bCreate = true;
		File toFile = new File(toFileName);
		if (!toFile.exists()) {
			bCreate = toFile.mkdirs();
		}

		if (bCreate) {
			File[] file = (new File(fromFileName)).listFiles();
			for (int i = 0; i < file.length; i++) {
				if (file[i].isFile()) {
					FileInputStream input = new FileInputStream(file[i]);
					FileOutputStream output = new FileOutputStream(toFileName + "/" + file[i].getName());
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (file[i].isDirectory()) {
					copyDirectiory(toFileName + "/" + file[i].getName(), fromFileName + "/" + file[i].getName());
				}
			}
		}
	}

	/** 写入为UTF-8编码的文件存储 */
	public static void writeToFile(String content, String filePath) {
		try {
			String dir = filePath.substring(0, filePath.lastIndexOf("/"));
			File fDir = new File(dir);
			if (!fDir.exists()) {
				fDir.mkdirs();
			}
			FileOutputStream fos = new FileOutputStream(filePath);
			OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
			osw.write(content);
			osw.flush();
			osw.close();
			fos.close();
			// BufferedWriter bfWriter = new BufferedWriter(new FileWriter(new
			// File(filePath)));
			// bfWriter.write(content);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/** 写入为UTF-8编码的文件存储 */
	public static void writeToFile(String content, File file) {
		try {
			if (content == null || "".equals(content))
				return;

			File fDir = file.getParentFile();
			if (!fDir.exists())
				fDir.mkdirs();

			FileOutputStream fos = new FileOutputStream(file.getAbsoluteFile());

			OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
			osw.write(content);
			osw.flush();

			osw.close();
			fos.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public static String readFromFile(String filePath) {
		StringBuffer content = new StringBuffer("");
		if (!new File(filePath).isFile() || !new File(filePath).exists()) {
			return "";
		}
		try {
			// BufferedReader bfReader = new BufferedReader(new FileReader(new
			// File(filePath)));
			BufferedReader bfReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
			String str = null;
			while ((str = bfReader.readLine()) != null)
				content.append(str).append("\n");

			bfReader.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		return content.toString();
	}

	public static String readFromFile(File file) {
		StringBuffer content = new StringBuffer("");
		if (!file.isFile()) {
			return "";
		}
		try {
			// BufferedReader bfReader = new BufferedReader(new
			// FileReader(file));
			BufferedReader bfReader = new BufferedReader(new InputStreamReader(new FileInputStream(file.getAbsolutePath()), "UTF-8"));
			String str = null;
			while ((str = bfReader.readLine()) != null)
				content.append(str).append("\n");

			bfReader.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		return content.toString();
	}

	public static String getFilesize(long sz) {// 可允许上传的文件最大为999g
		String s = "";
		if (sz < 1000) {
			s = String.valueOf(sz) + "B";
		} else if (sz > 1000 && sz < 1000 * 1000) {
			s = String.valueOf((int) Math.floor(sz / 1000)) + "K";
		} else if (sz > 1000 * 1000 && sz < 1000 * 1000 * 1000) {
			s = String.valueOf((int) Math.floor(sz / (1000 * 1000))) + "M";
		} else if (sz > 1000 * 1000 * 1000 && sz < 1000 * 1000 * 1000 * 1000) {
			s = String.valueOf((int) Math.floor(sz / (1000 * 1000))) + "G";
		}
		return s;
	}

}
