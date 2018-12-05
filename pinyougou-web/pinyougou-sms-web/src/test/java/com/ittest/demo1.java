package com.ittest;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class demo1 {

    private CloseableHttpClient httpClient= HttpClients.createDefault();


    @Test
    public void get() throws IOException {
        HttpGet httpGet=new HttpGet("http://www.baidu.com");
        CloseableHttpResponse execute = httpClient.execute(httpGet);
        if (execute.getStatusLine().getStatusCode()==200){
            String s= EntityUtils.toString(execute.getEntity(),"utf-8");
            System.out.println(s);
        }
        execute.close();

    }
    @Test
    public void get1() throws IOException, URISyntaxException {


        URIBuilder uriBuilder=new URIBuilder("http://www.baidu.com/s");
        uriBuilder.setParameter("wd","java");
        URI build = uriBuilder.build();
        System.out.println(build);
        HttpGet httpGet=new HttpGet(build);
        CloseableHttpResponse execute = httpClient.execute(httpGet);
        if (execute.getStatusLine().getStatusCode()==200){
            String s= EntityUtils.toString(execute.getEntity(),"utf-8");
            System.out.println(s);
        }
        execute.close();

    }
    @Test
    public void post2() throws Exception {
        // 创建HttpPost对象
        HttpPost httpPost = new HttpPost("http://www.baidu.com");
        // 定义List集合封装请求参数
        List<NameValuePair> nvpList = new ArrayList<>();
        // 添加请求参数
        nvpList.add(new BasicNameValuePair("username", "admin"));
        // 添加请求参数
        nvpList.add(new BasicNameValuePair("password", "123456"));
        // 创建form表单实体
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nvpList, "UTF-8");
        // 设置请求参数实体
        httpPost.setEntity(formEntity);
        // 执行请求，得到响应对象
        CloseableHttpResponse response = httpClient.execute(httpPost);
        // 判断响应状态码 200
        if (response.getStatusLine().getStatusCode() == 200) {
            // 获取响应数据
            String content = EntityUtils.toString(response.getEntity(), "UTF-8");
            System.out.println(content);
        }
        // 关闭响应对象
        response.close();
    }

    }
