package com.example.demo.controller;

import com.example.demo.domain.Result;
import com.example.demo.service.FastDFSService;
import com.example.demo.util.FastDFSFileUtil;
import com.example.demo.util.FastDFSFileUtils;
import org.csource.fastdfs.DownloadStream;
import org.csource.fastdfs.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by 程江涛 on 2018/11/20.
 */
@RestController
@RequestMapping(value = "/fdfs")
public class FastDFSController {

    @Autowired
    private FastDFSService fastDFSService;

    /**
     * 上传
     */
    @RequestMapping(value = "/fileUpload", method = { RequestMethod.POST })
    public Result<String> fdfsUpload(MultipartFile file){
        Result<String> result = new Result<>();
        try {
            if (file != null && !file.isEmpty()) {
                System.out.println(file.getOriginalFilename());
                String url = fastDFSService.upload(file, "group1");
                result.setCode(200);
                result.setMsg("上传成功！");
                result.setData(url);
            } else {
                result.setCode(201);
                result.setMsg("文件为空，上传失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(500);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 下载
     */
    @RequestMapping(value = "/fileDownload", method = { RequestMethod.GET })
    public void fdfsDownload(String url, String type, HttpServletResponse response) throws Exception {
        if (StringUtils.isEmpty(url)) {
            throw new Exception("url为必传参数");
        }
        ServletOutputStream sos = null;
        try {
            if ("image".equals(type)) {
                response.setContentType("image/jpeg");
            } else if ("video".equals(type)) {
                response.setContentType("video/mpeg4");
            } else if ("voice".equals(type)) {
                response.setContentType("audio/amr-wb");
            } else {
                response.setContentType("application/x-msdownload");
            }
            sos = response.getOutputStream();
            //fastDFSService.download(groupName, remoteFilename, new DownloadStream(sos));
            FastDFSFileUtils.download(url, new DownloadStream(sos));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(null != sos) {
                sos.close();
            }
        }
    }

    /**
     * 文本上传
     */
    @RequestMapping(value = "/textUpload", method = { RequestMethod.GET, RequestMethod.POST })
    public Result<String> textUpload(String text, String filename)throws Exception {
        if (StringUtils.isEmpty(text)) {
            throw new Exception("内容不能为空");
        }
        Result<String> result = new Result<>();
        try {
            String url = fastDFSService.textUpload(text, filename, "group1");
            if (!StringUtils.isEmpty(url)) {
                result.setCode(200);
                result.setMsg("上传成功！");
                result.setData(url);
            } else {
                result.setCode(201);
                result.setMsg("上传失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(500);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 文本下载
     */
    @RequestMapping(value = "/textDownload", method = { RequestMethod.GET })
    public Result<String> textDownload(String url) throws Exception {
        if (StringUtils.isEmpty(url)) {
            throw new Exception("url为必传参数");
        }
        Result<String> result = new Result<>();
        try {
            String text = FastDFSFileUtils.textDownload(url);
            if (!StringUtils.isEmpty(text)) {
                result.setCode(200);
                result.setMsg("上传成功！");
                result.setData(text);
            } else {
                result.setCode(201);
                result.setMsg("上传失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(500);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/fileDelete", method = { RequestMethod.GET })
    public Result fdfsDelete(String url) throws Exception {
        if (StringUtils.isEmpty(url)) {
            throw new Exception("url为必传参数");
        }
        String[] stringArr = url.split(",");
        String groupName = stringArr[0];
        String remoteFilename = stringArr[1];
        Result result = new Result();
        try {
            //int i = fastDFSService.delete(groupName, remoteFilename);
            int i = FastDFSFileUtils.delete(url);
            if (i == 0) {
                result.setCode(200);
                result.setMsg("删除文件成功！");
            } else if(i == 2) {
                result.setCode(201);
                result.setMsg("删除文件失败，没有此文件！");
            }else if(i == 22) {
                result.setCode(202);
                result.setMsg("删除文件失败，没有此路径！");
            }else {
                result.setCode(203);
                result.setMsg("删除文件失败，未知错误！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(500);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 获取视频播放地址
     */
    @RequestMapping(value = "/videoDownload", method = { RequestMethod.GET })
    public Result<String> videoDownload(String url) throws Exception {
        if (StringUtils.isEmpty(url)) {
            throw new Exception("url为必传参数");
        }
        Result<String> result = new Result<>();
        try {
            String link = FastDFSFileUtils.getVideoUrl(url);
            if (!StringUtils.isEmpty(link)) {
                result.setCode(200);
                result.setMsg("获取成功！");
                result.setData(link);
            } else {
                result.setCode(201);
                result.setMsg("获取失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(500);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @Deprecated
    @RequestMapping(value = "/testVideoDownload", method = { RequestMethod.GET })
    public void testVideoDownload(String url, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (StringUtils.isEmpty(url)) {
            throw new Exception("url为必传参数");
        }
        String[] strings = FastDFSFileUtils.analysisUrl(url);
        String groupName = strings[0];
        String remoteFilename = strings[1];
        FileInfo fileInfo = FastDFSFileUtils.getFileInfo(url);

        long fileLength = fileInfo.getFileSize();// 记录文件大小
        long pastLength = 0;// 记录已下载文件大小
        long toLength = 0;// 记录客户端需要下载的字节段的最后一个字节偏移量（比如bytes=27000-39000，则这个值是为39000）
        long contentLength = 0;// 客户端请求的字节总量
        String rangeBytes = "";// 记录客户端传来的形如“bytes=27000-”或者“bytes=27000-39000”的内容

        // ETag header
        // The ETag is contentLength + lastModified
        response.setHeader("ETag", "W/\"" + fileLength + "-" + fileInfo.getCreateTimestamp().getTime() + "\"");
        // Last-Modified header
        response.setHeader("Last-Modified", fileInfo.getCreateTimestamp().toString());

        if (request.getHeader("Range") != null) {// 客户端请求的下载的文件块的开始字节
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
            rangeBytes = request.getHeader("Range").replaceAll("bytes=", "");
            if (rangeBytes.indexOf('-') == rangeBytes.length() - 1) {// bytes=969998336-
                rangeBytes = rangeBytes.substring(0, rangeBytes.indexOf('-'));
                pastLength = Long.parseLong(rangeBytes.trim());
                toLength = fileLength - 1;
            } else {// bytes=1275856879-1275877358
                String temp0 = rangeBytes.substring(0, rangeBytes.indexOf('-'));
                String temp2 = rangeBytes.substring(rangeBytes.indexOf('-') + 1, rangeBytes.length());
                // bytes=1275856879-1275877358，从第 1275856879个字节开始下载
                pastLength = Long.parseLong(temp0.trim());
                // bytes=1275856879-1275877358，到第 1275877358 个字节结束
                toLength = Long.parseLong(temp2);
            }
        } else {// 从开始进行下载
            toLength = fileLength - 1;
        }
        // 客户端请求的是1275856879-1275877358 之间的字节
        contentLength = toLength - pastLength + 1;
        if (contentLength < Integer.MAX_VALUE) {
            response.setContentLength((int) contentLength);
        } else {
            // Set the content-length as String to be able to use a long
            response.setHeader("content-length", "" + contentLength);
        }
        //response.setContentType("video/mp4");
        response.setContentType("video/mpeg4");

        // 告诉客户端允许断点续传多线程连接下载,响应的格式是:Accept-Ranges: bytes
        response.setHeader("Accept-Ranges", "bytes");
        // 必须先设置content-length再设置header
        response.addHeader("Content-Range", "bytes " + pastLength + "-" + toLength + "/" + fileLength);

        response.setBufferSize(2048);

        InputStream istream = null;
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            istream = new BufferedInputStream(getInputStream(getUrl(fileInfo, groupName, remoteFilename)), 2048);
            try {
                //fastDFSService.rangeDownload(groupName, remoteFilename, pastLength, toLength - pastLength, new DownloadStream(os));
                copyRange(istream, os, pastLength, toLength);
            } catch (IOException ie) {
                /**
                 * 在写数据的时候， 对于 ClientAbortException 之类的异常， 是因为客户端取消了下载，而服务器端继续向浏览器写入数据时，
                 * 抛出这个异常，这个是正常的。 尤其是对于迅雷这种吸血的客户端软件， 明明已经有一个线程在读取 bytes=1275856879-1275877358，
                 * 如果短时间内没有读取完毕，迅雷会再启第二个、第三个。。。线程来读取相同的字节段， 直到有一个线程读取完毕，迅雷会 KILL
                 * 掉其他正在下载同一字节段的线程， 强行中止字节读出，造成服务器抛 ClientAbortException。 所以，我们忽略这种异常
                 */
                // ignore
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (istream != null) {
                try {
                    istream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void copyRange(InputStream istream, OutputStream ostream, long start, long end) throws IOException {

        long skipped = 0;
        skipped = istream.skip(start);

        if (skipped < start) {
            throw new IOException("skip fail: skipped=" + Long.valueOf(skipped) + ", start=" + Long.valueOf(start));
        }

        long bytesToRead = end - start + 1;

        byte buffer[] = new byte[2048];
        int len = buffer.length;
        while ((bytesToRead > 0) && (len >= buffer.length)) {
            try {
                len = istream.read(buffer);
                if (bytesToRead >= len) {
                    ostream.write(buffer, 0, len);
                    bytesToRead -= len;
                } else {
                    ostream.write(buffer, 0, (int) bytesToRead);
                    bytesToRead = 0;
                }
            } catch (IOException e) {
                len = -1;
                throw e;
            }
            if (len < buffer.length)
                break;
        }

    }

    protected String getUrl(FileInfo fileInfo, String groupName, String remoteFilename) throws Exception {
        int ts = (int)(System.currentTimeMillis()/1000);
        ts = ts + 100;
        String token = FastDFSFileUtil.getToken(remoteFilename, ts);
        String link;
        try {
            if (!StringUtils.isEmpty(token)) {
                link = "http://" + fileInfo.getSourceIpAddr() + ":8888/" + groupName + "/" + remoteFilename + "?ts=" + ts + "&token=" + token;
            } else {
                link = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            link = "";
        }
        return link;
    }

    protected InputStream getInputStream(String urlPath) throws Exception{
        // 统一资源
        URL url = new URL(urlPath);
        // 连接类的父类，抽象类
        URLConnection urlConnection = url.openConnection();
        // http的连接类
        HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
        // 设定请求的方法，默认是GET
        //httpURLConnection.setRequestMethod("POST");
        // 设置字符编码
        //httpURLConnection.setRequestProperty("Charset", "UTF-8");
        // 打开到此 URL 引用的资源的通信链接（如果尚未建立这样的连接）。
        //httpURLConnection.connect();

        return httpURLConnection.getInputStream();
    }
}
