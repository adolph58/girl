package com.example.demo.service;

import com.example.demo.util.FastDFSFileUtil;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.DownloadCallback;
import org.csource.fastdfs.FileInfo;
import org.csource.fastdfs.UploadCallback;
import org.csource.fastdfs.UploadStream;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by 程江涛 on 2018/11/20.
 */
@Service
public class FastDFSService {

    /**
     * 上传
     */
    public String upload (MultipartFile file, String groupName) throws Exception {
        long fileSize = file.getSize();
        String fileExtName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        NameValuePair[] metaList = new NameValuePair[3];
        metaList[0] = new NameValuePair("Filename", file.getOriginalFilename());
        metaList[1] = new NameValuePair("fileSize", String.valueOf(file.getSize()));
        metaList[2] = new NameValuePair("fileExtName", fileExtName);
        String url;
        if (fileSize > 1024*1024*10) {
            url = bigUpload(groupName, fileSize, new UploadStream(file.getInputStream(), fileSize), fileExtName, metaList);
        } else {
            url = smallUpload(groupName, file.getBytes(), fileExtName, metaList);
        }
        return url;
    }

    /**
     * 文本上传
     * @param content 文本内容
     * @param filename 自定义文件名
     */
    public String textUpload(String content, String filename, String groupName) throws Exception {
        if(StringUtils.isEmpty(content)) {
            throw new Exception("文件内容不能为空！");
        }
        byte[] bytes = content.getBytes("UTF-8");
        NameValuePair[] metaList = new NameValuePair[3];
        metaList[0] = new NameValuePair("Filename", filename);
        metaList[1] = new NameValuePair("fileSize", String.valueOf(bytes.length));
        metaList[2] = new NameValuePair("fileExtName", "txt");
        String url = smallUpload(groupName, bytes, "txt", metaList);
        return url;
    }

    /**
     * 获取文本内容
     */
    public String textDownload(String groupName, String remoteFilename) throws Exception {
        String content;
        byte[] bytes = FastDFSFileUtil.smallDownload(groupName, remoteFilename);
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
    public int download(String groupName, String remoteFilename,  DownloadCallback callback) {
        return FastDFSFileUtil.bigDownload(groupName, remoteFilename, callback);
    }

    /**
     * 获取视频播放地址
     * @return
     */
    public String getVideoUrl(String groupName, String remoteFilename) throws Exception{
        int ts = (int)(System.currentTimeMillis()/1000);
        ts = ts + 100;
        String token = FastDFSFileUtil.getToken(remoteFilename, ts);
        FileInfo fileInfo = FastDFSFileUtil.getFileInfo(groupName, remoteFilename);
        String link = "http://" + fileInfo.getSourceIpAddr() + ":8888/" + groupName + "/" + remoteFilename + "?ts=" + ts + "&token=" + token;
        return link;
    }

    /**
     * 指定范围下载
     */
    public int rangeDownload(String groupName, String remoteFilename, long fileOffset, long downloadBytes,DownloadCallback callback) throws Exception{
        return FastDFSFileUtil.rangeDownload(groupName, remoteFilename, fileOffset, downloadBytes, callback);
    }

    /**
     * 删除文件
     */
    public int delete(String groupName, String remoteFilename) {
        return FastDFSFileUtil.delete(groupName, remoteFilename);
    }

    /**
     * 获取文件信息
     */
    public static FileInfo getFileInfo(String groupName, String remoteFilename) throws Exception{
        return FastDFSFileUtil.getFileInfo(groupName, remoteFilename);
    }

    /**
     * 获取文件元数据
     */
    public static NameValuePair[] getMetadata(String groupName, String remoteFilename) throws Exception {
        return FastDFSFileUtil.getMetadata(groupName, remoteFilename);
    }

    private String smallUpload (String groupName, byte[] fileBuff, String fileExtName, NameValuePair[] metaList) {
        String[] uploadResults = FastDFSFileUtil.smallUpload(groupName, fileBuff, fileExtName, metaList);
        return "/" + uploadResults[0] + "/" + uploadResults[1];
    }

    private String bigUpload (String groupName, long fileSize, UploadCallback callback, String fileExtName, NameValuePair[] metaList) {
        String[] uploadResults = FastDFSFileUtil.bigUpload(groupName, fileSize, callback, fileExtName, metaList);
        return "/" + uploadResults[0] + "/" + uploadResults[1];
    }
}
