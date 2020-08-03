package com.vxgzh.maoxiandao.utils;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;

/**
 * HttpUtil
 *
 * @Author: lmh
 * @CreateTime: 2020-07-19
 * @Description:
 */
public class HttpUtil {

    private static Logger log = LoggerFactory.getLogger(HttpUtil.class);

    /**
     * 发送get请求
     * @param url
     * @return
     */
    public static String httpsGet(String url) {
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 创建Get请求
        HttpGet httpGet = new HttpGet(url);
        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Get请求
            response = httpClient.execute(httpGet);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            log.info("GET响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                String str = EntityUtils.toString(responseEntity);
                log.info("GET响应内容为:" + str);
                return str;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 发送post请求
     * @param url
     * @param json
     * @return
     */
    public static String httpsPost(String url, String json) {
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        // 创建Post请求
        HttpPost httpPost = new HttpPost(url);
        StringEntity entity = new StringEntity(json, "UTF-8");

        // post请求是将参数放在请求体里面传过去的;这里将entity放入post请求体中
        httpPost.setEntity(entity);

        httpPost.setHeader("Content-Type", "application/json;charset=utf8");

        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Post请求
            response = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();

            log.info("POST响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                log.info("POST响应内容长度为:" + responseEntity.getContentLength());
                String str = EntityUtils.toString(responseEntity);
                log.info("POST响应内容为:" + str);
                return str;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     *
     * @param fileType 文件类型
     * @return 返回结果
     */
    /**
     * 文件上传
     * @param urlStr 地址
     * @param fileType 文件类型
     * @param file 文件
     * @return 结果
     */
    public static String upload(String urlStr,String fileType,File file){
        String result=null;
        try {
            String  token=AccessTokenUtil.getAccessToken();
            String urlString=urlStr.replace("ACCESS_TOKEN", token).replace("TYPE", fileType);
            URL url=new URL(urlString);
            HttpsURLConnection conn=(HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("POST");//以POST方式提交表单
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);//POST方式不能使用缓存
            //设置请求头信息
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            //设置边界
            String BOUNDARY="----------"+System.currentTimeMillis();
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            //请求正文信息
            //第一部分
            StringBuilder sb=new StringBuilder();
            sb.append("--");//必须多两条道
            sb.append(BOUNDARY);
            sb.append("\r\n");
            sb.append("Content-Disposition: form-data;name=\"media\"; filename=\"" + file.getName()+"\"\r\n");
            sb.append("Content-Type:application/octet-stream\r\n\r\n");
            log.info("发送文件请求正文信息:"+sb);

            //获得输出流
            OutputStream out=new DataOutputStream(conn.getOutputStream());
            //输出表头
            out.write(sb.toString().getBytes("UTF-8"));
            //文件正文部分
            //把文件以流的方式 推送道URL中
            DataInputStream din=new DataInputStream(new FileInputStream(file));
            int bytes=0;
            byte[] buffer=new byte[1024];
            while((bytes=din.read(buffer))!=-1){
                out.write(buffer,0,bytes);
            }
            din.close();
            //结尾部分
            byte[] foot=("\r\n--" + BOUNDARY + "--\r\n").getBytes("UTF-8");//定义数据最后分割线
            out.write(foot);
            out.flush();
            out.close();
            if(HttpsURLConnection.HTTP_OK==conn.getResponseCode()){

                StringBuffer strbuffer=null;
                BufferedReader reader=null;
                try {
                    strbuffer=new StringBuffer();
                    reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String lineString=null;
                    while((lineString=reader.readLine())!=null){
                        strbuffer.append(lineString);

                    }
                    if(result==null){
                        result=strbuffer.toString();
                        log.info("POST文件响应结果:"+result);
                    }
                } catch (IOException e) {
                    log.error("发送POST文件请求出现异常！",e);
                    e.printStackTrace();
                }finally{
                    if(reader!=null){
                        reader.close();
                    }
                }

            }
        }  catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}