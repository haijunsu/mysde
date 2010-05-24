package com.navy.docman.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtil {

	private static final Logger logger = LoggerFactory
			.getLogger(FileUtil.class);

	public static final String LINE_SEPARATOR = System
			.getProperty("line.separator");

	/**
	 * Read a file into byte array without encoding specified
	 *
	 * @param filePath
	 *            the file path.
	 * @return the file content in bytes.
	 */
	public static byte[] read(String filePath) throws Exception {
		File file = new File(filePath.trim());
		return read(file);
	}

	/**
	 * Read a file into byte array without encoding specified
	 *
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static byte[] read(File file) throws Exception {
		byte[] data = null;
		FileInputStream fis = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
		try {
			logger.debug("readFile: " + file.getAbsolutePath());
			fis = new FileInputStream(file);
			copy(fis, bos);
			data = bos.toByteArray();
		} catch (Exception e) {
			logger.error("readFile failed", e);
			throw e;
		} finally {
			closeFis(fis);
			closeFos(bos);
		}
		return data;
	}

	/**
	 * Read a file with encoding specified
	 *
	 * @param filePath
	 * @param encoding
	 * @return
	 * @throws Exception
	 */
	public static String read(String filePath, String encoding)
			throws Exception {
		return read(new File(filePath.trim()), encoding);
	}

	/**
	 * Read file with encoding specified
	 *
	 * @param file
	 * @param encoding
	 * @return
	 * @throws Exception
	 */
	public static String read(File file, String encoding) throws Exception {
		String content = "";
		if (file == null)
			return content;
		FileInputStream fis = null;
		BufferedReader br = null;
		String line = "";
		try {
			fis = new FileInputStream(file);
			br = new BufferedReader(new InputStreamReader(fis, encoding));
			while (((line = br.readLine()) != null)) {
				content += line;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			closeBr(br);
			closeFis(fis);
		}
		return content;
	}

	/**
	 * Writes byte[] content to the file
	 *
	 * @param content
	 * @param filePath
	 * @throws Exception
	 */
	public static void write(String filePath, byte[] content) throws Exception {
		write(new File(filePath.trim()), content);
	}

	/**
	 * Writes byte[] content to the file
	 *
	 * @param content
	 *            a <code>byte[]</code> value
	 * @param destFile
	 *            a <code>File</code> value
	 * @exception Exception
	 *                if an error occurs
	 */
	public static void write(File destFile, byte[] content) throws Exception {
		FileOutputStream fos = null;
		try {
			checkAndCreateParentDir(destFile);
			fos = new FileOutputStream(destFile);
			fos.write(content);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw e;
		} finally {
			closeFos(fos);
		}
	}

	/**
	 * Writes String content to the file without encoding specified
	 *
	 * @param destFile
	 * @param content
	 * @throws Exception
	 */
	public static void write(String destFile, String content) throws Exception {
		write(new File(destFile), content);
	}

	public static void append(String destFile, String content) throws Exception {
		append(new File(destFile), content);
	}

	/**
	 * Writes String content to the file without encoding specified
	 *
	 * @param destFile
	 *            a <code>File</code> value
	 * @param content
	 *            a <code>String</code> value
	 * @exception Exception
	 *                if an error occurs
	 */
	public static void write(File destFile, String content) throws Exception {
		write(destFile, content, null);
	}

	public static void append(File destFile, String content) throws Exception {
		append(destFile, content, null);
	}

	/**
	 * Writes String content to the file with encoding
	 *
	 * @param filePath
	 * @param content
	 * @param encoding
	 * @throws Exception
	 */
	public static void write(String filePath, String content, String encoding)
			throws Exception {
		write(new File(filePath.trim()), content, encoding);
	}

	public static void append(String filePath, String content, String encoding)
			throws Exception {
		append(new File(filePath.trim()), content, encoding);
	}

	/**
	 * Writes String content to the file with encoding
	 *
	 * @param content
	 *            a <code>String</code> value
	 * @param destFile
	 *            a <code>File</code> value
	 * @exception Exception
	 *                if an error occurs
	 */
	public static void write(File destFile, String content, String encoding)
			throws Exception {
		write(destFile, content, encoding, false);
	}

	public static void append(File destFile, String content, String encoding)
			throws Exception {
		write(destFile, content, encoding, true);
	}

	private static void write(File destFile, String content, String encoding,
			boolean append) throws Exception {
		BufferedWriter bw = null;
		FileOutputStream fos = null;
		try {
			checkAndCreateParentDir(destFile);
			fos = new FileOutputStream(destFile, append);
			if (encoding == null) {
				bw = new BufferedWriter(new OutputStreamWriter(fos));
			} else {
				bw = new BufferedWriter(new OutputStreamWriter(fos, encoding));
			}
			bw.write(content, 0, content.length());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		} finally {
			closeBw(bw);
			closeFos(fos);
		}
	}

	/**
	 * This function list the file names of files under the input directory with
	 * names start with prefix.
	 */
	public static List<String> listFiles(String dir, String prefix)
			throws Exception {
		return listFiles(dir, prefix, true);
	}

	public static List<String> listFiles(String dir, String pattern,
			boolean prefix) throws Exception {
		File dirFile = new File(dir);
		List<String> list = new ArrayList<String>();
		String[] files = dirFile.list();
		for (int i = 0; i < files.length; i++) {
			boolean add = false;
			if (prefix) {
				add = files[i].startsWith(pattern);
			} else {
				add = files[i].endsWith(pattern);
			}

			if (add) {
				list.add(files[i]);
			}
		}
		return list;
	}

	public static String getSuffix(String fileName) {
		int index = fileName.lastIndexOf(".");
		return fileName.substring(index + 1);
	}

	/**
	 * This function creates a temporary directory in the specified dir with the
	 * prefix.
	 *
	 * @param dir
	 *            the directory name where the tmp dir is to be created.
	 * @param prefix
	 *            the prefix of the tmp dir name.
	 * @return the temporary directory file
	 */
	public static File createTmpDir(String dir, String prefix) throws Exception {
		File dirFile = new File(dir);

		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}

		File tempFile = File.createTempFile(prefix, "", dirFile);
		tempFile.delete();
		tempFile.mkdir();
		return tempFile;
	}

	/**
	 * Copy one directory to the destination direction. It overwrites any files
	 * if present in destination direction.
	 *
	 * @param srcFile
	 *            Source dir
	 * @param destFile
	 *            Destination dir
	 * @return <code>boolean</code> true if and only if all files were copied
	 */
	public static boolean xcopy(File srcFile, File destFile) {
		return xcopy(srcFile, destFile, true);
	}

	/**
	 * Copy one directory to the destination direction. It overwrites any files
	 * if present in destination direction.
	 *
	 * @param srcFile
	 *            Source dir
	 * @param destFile
	 *            Destination dir
	 * @return <code>boolean</code> true if and only if all files were copied
	 */
	public static boolean xcopy(File srcFile, File destFile, boolean overwrite) {
		logger.debug("Copy srcFile[" + srcFile.getAbsolutePath()
				+ "] to destFile[" + destFile.getAbsolutePath() + "] and"
				+ " overwrite file if present");
		System.out.println("Copy srcFile[" + srcFile.getAbsolutePath()
				+ "] to destFile[" + destFile.getAbsolutePath() + "] and"
				+ " overwrite file if present");
		boolean result = false;
		destFile.setReadable(srcFile.canRead());
		destFile.setWritable(srcFile.canWrite());
		destFile.setExecutable(srcFile.canExecute());
		destFile.setLastModified(srcFile.lastModified());
		if (srcFile.isDirectory()) {
			if (!destFile.isDirectory() && !destFile.mkdirs()) {
				return false;
			}
			// Now copy all files from source to dest
			File[] listFiles = srcFile.listFiles();
			if (listFiles.length == 0) {
				return true;
			}
			for (int i = 0; i < listFiles.length; i++) {
				File curFile = listFiles[i];
				System.out.println("Coping: " + curFile.getAbsolutePath());
				if (curFile.isDirectory()) {
					result = xcopy(curFile, new File(destFile, curFile
							.getName()), overwrite);
				} else {
					result = copy(curFile,
							new File(destFile, curFile.getName()), overwrite); // overwrite
				}
				System.out.println("Copy result: " + result + " "
						+ curFile.getAbsolutePath());
				if (result == false) {
					// if error, then stop at this point
					return result;
				}
			}
		} else if (srcFile.isFile()) {
			result = copy(srcFile, new File(destFile, srcFile.getName()),
					overwrite);
		}
		return result;
	}

	/**
	 * Copy one file to the other with optionally overwriting the dest file
	 *
	 * @param srcFile
	 *            Source file
	 * @param destFile
	 *            Destination file
	 * @param overwrite
	 *            whether to overwrite the dest file if it exists
	 * @return <code>boolean</code> true if and only if file was copied
	 */
	public static boolean copy(File srcFile, File destFile, boolean overwrite) {
		logger.debug("Copy srcFile[" + srcFile.getAbsolutePath()
				+ "] to destFile[" + destFile.getAbsolutePath() + "] and"
				+ " overwrite[" + overwrite + "]");
		if (!srcFile.exists()) {
			logger.debug("copyFile: Source file[" + srcFile + "] not found");
			return false;
		}
		if (!destFile.exists()) {
			checkAndCreateParentDir(destFile);
		} else {
			if (!overwrite) {
				logger.debug("copyFile: overwrite == false");
				return false;
			}
		}
		destFile.setReadable(srcFile.canRead());
		destFile.setWritable(srcFile.canWrite());
		destFile.setExecutable(srcFile.canExecute());
		destFile.setLastModified(srcFile.lastModified());
		FileInputStream fileIn = null;
		FileOutputStream fileOut = null;
		boolean success = false;
		try {
			fileIn = new FileInputStream(srcFile);
			fileOut = new FileOutputStream(destFile);
			success = copy(fileIn, fileOut);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			return false;
		} finally {
			closeFis(fileIn);
			closeFos(fileOut);
		}
		return success;
	}

	/**
	 * Creates Directory for the given path.
	 *
	 * @param dirPath
	 *            a <code>String</code> value
	 * @return <code>boolean</code>
	 */
	public static boolean createDir(String dirPath) {
		File dirFile = new File(dirPath);
		if (!dirFile.exists()) {
			return dirFile.mkdirs();
		}
		return true;
	}

	/**
	 * Remove the directory, include all the files and directories under the
	 * directory
	 *
	 * @param dir
	 */
	public static void removeDir(String dir) {
		File file = new File(dir);
		removeDir(file);
	}

	public static void removeFile(String filePath) {
		File file = new File(filePath);
		file.delete();
	}

	/**
	 * Remove the directory, include all the files and directories under the
	 * directory
	 *
	 * @param file
	 */
	public static void removeDir(File file) {
		if (!file.exists()) {
			return;
		}
		Stack<File> sk = new Stack<File>();
		sk.push(file);
		while (!sk.empty()) {
			File ff = sk.pop();
			if (ff.delete()) {
				continue;
			}
			sk.push(ff);
			File[] fs = ff.listFiles();
			for (int i = 0; i < fs.length; i++) {
				if (fs[i].isDirectory()) {
					sk.push(fs[i]);
				} else {
					fs[i].delete();
				}
			}
		}
	}

	/**
	 * If the parent of the file does not exists, the parent directory will be
	 * created
	 *
	 * @param file
	 * @return true if parent directory exists or created successfully
	 */
	public static boolean checkAndCreateParentDir(File file) {
		File parentFile = file.getParentFile();

		if (parentFile != null && !parentFile.exists()) {
			return parentFile.mkdirs();
		}

		return true;
	}

	private static boolean copy(InputStream from, OutputStream to)
			throws IOException {
		BufferedInputStream bIn = new BufferedInputStream(from);
		BufferedOutputStream bOut = new BufferedOutputStream(to);
		byte[] bContent = new byte[8192];
		int len = 0;
		while ((len = bIn.read(bContent)) > 0) {
			bOut.write(bContent, 0, len);
		}
		bOut.flush();
		return true;
	}

	private static void closeFis(InputStream fis) {
		if (fis == null)
			return;
		try {
			fis.close();
		} catch (Exception e) {
			// ignore
			logger.error(e.getMessage(), e);
		}
	}

	private static void closeFos(OutputStream fos) {
		if (fos == null)
			return;
		try {
			fos.close();
		} catch (Exception e) {
			// ignore
			logger.error(e.getMessage(), e);
		}
	}

	private static void closeBw(BufferedWriter bw) {
		if (bw == null)
			return;
		try {
			bw.close();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private static void closeBr(BufferedReader br) {
		if (br == null)
			return;
		try {
			br.close();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * Returns whether the specified file exsits.
	 */
	public static boolean exists(String filePath) {
		File file = new File(filePath.trim());
		return file.exists();
	}

	/**
	 * Move source File to destination File path. If the parent directory of
	 * destination doesn't exist. It will create it automatically. This method
	 * can move both files or directories.
	 *
	 * @param srcFilePath
	 *            source file path
	 * @param destFilePath
	 *            destination file path
	 * @return
	 */
	public static boolean renameTo(String srcFilePath, String destFilePath) {
		return renameTo(new File(srcFilePath), new File(destFilePath));
	}

	/**
	 * Move source File to destination File path. If the parent directory of
	 * destination doesn't exist. It will create it automatically. This method
	 * can move both files or directories.
	 *
	 * @param src
	 *            Source file
	 * @param dest
	 *            Destination file
	 * @return
	 */
	public static boolean renameTo(File src, File dest) {
		File parentDir = dest.getParentFile();
		if ((parentDir != null) && (!parentDir.exists())) {
			dest.getParentFile().mkdirs();
		}
		return src.renameTo(dest);
	}

	/**
	 * Convert file size with unit KB or MB
	 *
	 * @param file
	 * @return size
	 */
	public static String getPrintFileSize(File file) {
		long size = file.length();
		DecimalFormat df = new DecimalFormat("###.##");
		float f;
		if (size < 1024 * 1024) {
			f = (float) ((float) size / (float) 1024);
			return (df.format(new Float(f).doubleValue()) + " KB");
		} else {
			f = (float) ((float) size / (float) (1024 * 1024));
			return (df.format(new Float(f).doubleValue()) + " MB");
		}
	}
}
