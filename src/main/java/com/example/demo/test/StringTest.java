package com.example.demo.test;

/**
 * Created by 程江涛 on 2018/12/1.
 */

public class StringTest {
    public static void main(String[] args) {
        String url = "/group10000/M00/00/00/wKgBmVvvAJCAFInlAAf_HaPmcQM241.jpg";
        String groupName = url.substring(1, url.substring(1).indexOf("/") + 1);
        String remoteFilename = url.split(groupName + "/")[1];
        System.out.println(remoteFilename);
    }
}
