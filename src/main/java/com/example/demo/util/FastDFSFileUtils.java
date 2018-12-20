package com.example.demo.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.DownloadCallback;
import org.csource.fastdfs.FileInfo;
import org.csource.fastdfs.ProtoCommon;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.csource.fastdfs.UploadCallback;
import org.csource.fastdfs.UploadStream;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.util.StringUtils;

public class FastDFSFileUtils {
    private static TrackerClient trackerClient;
    private static TrackerServer trackerServer;
    private static StorageServer storageServer;
    //private static StorageClient storageClient;
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
    private static final String CLIENT_CONFIG_FILE = "fdfs_client.conf";

    static {
        try {
            String classPath = new File(FastDFSFileUtil.class.getResource("/").getFile()).getCanonicalPath();

            String fdfsClientConfigFilePath = classPath + File.separator + CLIENT_CONFIG_FILE;
            ClientGlobal.init(fdfsClientConfigFilePath);

            trackerClient = new TrackerClient();
            trackerServer = trackerClient.getConnection();

            //storageClient = new StorageClient(trackerServer, storageServer);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传
     */
    public static String upload(File file, NameValuePair[] metaList) throws Exception {
        // 默认组名 - group1
        String groupName = "group1";

        FileInputStream fis = new FileInputStream(file);
        long fileSize = file.length();
        String fileExtName = FileUtil.getFileType(file);
        String[] urlArr = bigUpload(groupName, fileSize, new UploadStream(fis, fileSize), fileExtName, metaList);

        return "/" + urlArr[0] + "/" + urlArr[1];
    }

    /**
     * 上传
     */
    public static String upload(MultipartFile file, NameValuePair[] metaList) throws Exception {
        // 默认组名 - group1
        String groupName = "group1";
        long fileSize = file.getSize();
        String fileExtName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        String[] urlArr;
        if (fileSize > 1024 * 1024 * 10) {
            urlArr = bigUpload(groupName, fileSize, new UploadStream(file.getInputStream(), fileSize), fileExtName, metaList);
        } else {
            urlArr = smallUpload(groupName, file.getBytes(), fileExtName, metaList);
        }
        return "/" + urlArr[0] + "/" + urlArr[1];
    }

    /**
     * 文本上传
     *
     * @param content
     *            文本内容
     */
    public static String textUpload(String content, NameValuePair[] metaList) throws Exception {
        if (org.springframework.util.StringUtils.isEmpty(content)) {
            throw new Exception("文件内容不能为空！");
        }
        // 默认组名 group1
        String groupName = "group1";
        byte[] bytes = content.getBytes("UTF-8");
        String[] urlArr = smallUpload(groupName, bytes, "txt", metaList);
        return "/" + urlArr[0] + "/" + urlArr[1];
    }

    /**
     * 获取文本内容
     */
    public static String textDownload(String url) throws Exception {
        if (StringUtils.isEmpty(url)) {
            return "";
        }
        String[] strArr = analysisUrl(url);
        String content;
        byte[] bytes = smallDownload(strArr[0], strArr[1]);
        if (bytes != null) {
            content = new String(bytes, "UTF-8");
        } else {
            content = "";
        }
        return content;
    }

    /**
     * 下载
     */
    public synchronized static int download(String url, DownloadCallback callback) throws Exception {
        if (StringUtils.isEmpty(url)) {
            return -1;
        }
        String[] strArr = analysisUrl(url);
        return bigDownload(strArr[0], strArr[1], callback);
    }

    public synchronized static boolean downloadCacheFile(String url, String localFilename) throws Exception {
        if (StringUtils.isEmpty(url)) {
            return false;
        }
        String[] strArr = analysisUrl(url);
        return new StorageClient(trackerServer, storageServer).download_file(strArr[0], strArr[1], localFilename) == 0;
    }

    /**
     * 写缓存文件
     */
    public static boolean downloadCacheFile_old(String url, String filePath) {
        if (StringUtils.isEmpty(url)) {
            return false;
        }
        OutputStream outputStream = null;
        try {
            String[] strArr = analysisUrl(url);
            byte[] bytes = smallDownload(strArr[0], strArr[1]);
            File file = new File(filePath);
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            outputStream = new FileOutputStream(file);
            IOUtils.write(bytes, outputStream);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取视频播放地址
     *
     * @return
     */
    public static String getVideoUrl(String url) throws Exception {
        if (StringUtils.isEmpty(url)) {
            return "";
        }
        String[] strArr = analysisUrl(url);
        int ts = (int) (System.currentTimeMillis() / 1000);
        ts = ts + 100;
        String token = getToken(strArr[1], ts);
        String link = "http://192.168.1.151:8888/" + strArr[0] + "/" + strArr[1] + "?ts=" + ts + "&token=" + token;
        return link;
    }

    /**
     * 删除文件
     *
     * @return 0 : 删除成功；2：没有此文件；22：没有此路径
     */
    public synchronized static int delete(String url) throws Exception {
        if (StringUtils.isEmpty(url)) {
            return -1;
        }
        String[] strArr = analysisUrl(url);
        int i = -1;
        try {
            i = new StorageClient(trackerServer, storageServer).delete_file(strArr[0], strArr[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }

    /**
     * 获取文件信息
     */
    public synchronized static FileInfo getFileInfo(String url) throws Exception {
        if (StringUtils.isEmpty(url)) {
            return null;
        }
        String[] strArr = analysisUrl(url);
        return new StorageClient(trackerServer, storageServer).get_file_info(strArr[0], strArr[1]);
    }

    /**
     * 获取文件元数据
     */
    public synchronized static NameValuePair[] getMetadata(String url) throws Exception {
        if (StringUtils.isEmpty(url)) {
            return null;
        }
        String[] strArr = analysisUrl(url);
        return new StorageClient(trackerServer, storageServer).get_metadata(strArr[0], strArr[1]);
    }

    /**
     * 小文件上传 - 小于10M（内部调用）
     */
    private synchronized static String[] smallUpload(String groupName, byte[] fileBuff, String fileExtName, NameValuePair[] metaList) {
        String[] uploadResults = null;
        try {
            if (StringUtils.isEmpty(groupName)) {
                uploadResults = new StorageClient(trackerServer, storageServer).upload_file(fileBuff, fileExtName, metaList);
            } else {
                uploadResults = new StorageClient(trackerServer, storageServer).upload_file(groupName, fileBuff, fileExtName, metaList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uploadResults;
    }

    /**
     * 大文件上传 - 大于 10M（内部调用）
     */
    private synchronized static String[] bigUpload(String groupName, long fileSize, UploadCallback callback, String fileExtName, NameValuePair[] metaList) {
        String[] uploadResults = null;
        try {
            if (StringUtils.isEmpty(groupName)) {
                groupName = "group1";
            }
            uploadResults = new StorageClient(trackerServer, storageServer).upload_file(groupName, fileSize, callback, fileExtName, metaList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uploadResults;
    }

    /**
     * 小文件下载 - byte[]（内部调用）
     */
    private synchronized static byte[] smallDownload(String groupName, String remoteFilename) {
        byte[] bytes = null;
        try {
            bytes = new StorageClient(trackerServer, storageServer).download_file(groupName, remoteFilename);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     * 大文件下载 - 流（内部调用）
     */
    private synchronized static int bigDownload(String groupName, String remoteFilename, DownloadCallback callback) {
        int i = -1;
        try {
            i = new StorageClient(trackerServer, storageServer).download_file(groupName, remoteFilename, callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }

    /**
     * url 解析
     *
     * @param url
     * @return
     * @throws Exception
     */
    public static String[] analysisUrl(String url) throws Exception {
        if (StringUtils.isEmpty(url)) {
            throw new Exception("url 不能为空");
        }
        // url 格式：/group1/M00/00/00/wKgBmVvvAJCAFInlAAf_HaPmcQM241.jpg
        String groupName = url.substring(1, url.substring(1).indexOf("/") + 1);
        String remoteFilename = url.split(groupName + "/")[1];
        String[] strArr = { groupName, remoteFilename };
        return strArr;
    }

    /**
     * 获取 token
     *
     * @return
     */
    private static String getToken(String remoteFilename, int ts) {
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
     *
     * @param input
     * @param output
     * @throws IOException
     */
    public static long copyLarge(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        long count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

}