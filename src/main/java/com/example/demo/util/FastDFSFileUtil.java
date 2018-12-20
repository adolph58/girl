package com.example.demo.util;

import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FastDFSFileUtil {
	private static final long serialVersionUID = 1L;
	private static TrackerClient trackerClient;
	private static TrackerServer trackerServer;
	private static StorageServer storageServer;
	private static StorageClient storageClient;
	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
	private static final String CLIENT_CONFIG_FILE = "fdfs_client.conf";

	static {
		try {
			String classPath = new File(FastDFSFileUtil.class.getResource("/").getFile()).getCanonicalPath();

			String fdfsClientConfigFilePath = classPath + File.separator + CLIENT_CONFIG_FILE;
			ClientGlobal.init(fdfsClientConfigFilePath);

			trackerClient = new TrackerClient();
			trackerServer = trackerClient.getConnection();

			storageClient = new StorageClient(trackerServer, storageServer);


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取 token
	 * @return
	 */
	public static String getToken(String remoteFilename, int ts) {
		String token;
		try {
			token = ProtoCommon.getToken(remoteFilename, ts, ClientGlobal.getG_secret_key());
		} catch (Exception ex) {
			ex.printStackTrace();
			token = "";
		}
		return token;
	}

	/**
	 * 复制大文件
	 * @param input
	 * @param output
	 * @throws IOException
	 */
	public static long copyLarge(InputStream input, OutputStream output)
			throws IOException {
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		long count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

	/**
	 * 小文件上传 - 小于10M
	 */
	public static String[] smallUpload(String groupName, byte[] fileBuff, String fileExtName, NameValuePair[] metaList) {
		String[] uploadResults = null;
		try {
			if (StringUtils.isEmpty(groupName)) {
				uploadResults = storageClient.upload_file(fileBuff, fileExtName, metaList);
			} else {
				uploadResults = storageClient.upload_file(groupName, fileBuff, fileExtName, metaList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return uploadResults;
	}

	/**
	 * 大文件上传 - 大于 10M
	 */
	public static String[] bigUpload(String groupName, long fileSize, UploadCallback callback, String fileExtName, NameValuePair[] metaList){
		String[] uploadResults = null;
		try {
			if (StringUtils.isEmpty(groupName)) {
				groupName = "group1";
			}
			uploadResults = storageClient.upload_file(groupName, fileSize, callback, fileExtName, metaList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return uploadResults;
	}

	/**
	 *  小文件下载 - byte[]
	 */
	public static byte[] smallDownload(String groupName, String remoteFilename) {
		byte[] bytes = null;
		try {
			bytes = new StorageClient(trackerServer, storageServer).download_file(groupName, remoteFilename);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return  bytes;
	}

	/**
	 * 大文件下载 - 流
	 */
	public synchronized static int bigDownload(String groupName, String remoteFilename, DownloadCallback callback) {
		int i = -1;
		try {
			//i = storageClient.download_file(groupName, remoteFilename, callback);
			i = new StorageClient(trackerServer, storageServer).download_file(groupName, remoteFilename, callback);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return  i;
	}

	/**
	 * 指定范围下载 - 流
	 */
	public static int rangeDownload(String groupName, String remoteFilename, long fileOffset, long downloadBytes, DownloadCallback callback) throws Exception{
		int i = -1;
		i = new StorageClient(trackerServer, storageServer).download_file(groupName, remoteFilename, fileOffset, downloadBytes, callback);
		return  i;
	}

	/**
	 * 删除文件
	 */
	public static int delete(String groupName, String remoteFilename) {
		int i = -1;
		try {
			i = new StorageClient(trackerServer, storageServer).delete_file(groupName, remoteFilename);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

	/**
	 * 获取文件信息
	 * @param groupName
	 * @param remoteFilename
	 */
	public synchronized static FileInfo getFileInfo(String groupName, String remoteFilename) throws Exception{
		return new StorageClient(trackerServer, storageServer).get_file_info(groupName, remoteFilename);
	}

	/**
	 * 获取文件元数据
	 * @param groupName
	 * @param remoteFilename
	 */
	public static NameValuePair[] getMetadata(String groupName, String remoteFilename) throws Exception {
		return new StorageClient(trackerServer, storageServer).get_metadata(groupName, remoteFilename);
	}

}