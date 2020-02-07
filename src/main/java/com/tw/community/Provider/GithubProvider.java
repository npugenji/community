package com.tw.community.Provider;

import com.alibaba.fastjson.JSON;
import com.tw.community.dto.AccessTokenDTO;
import com.tw.community.dto.GithubUser;
import com.tw.community.dto.SSLSocketClient;
import okhttp3.*;
import org.springframework.stereotype.Component;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Component
public class GithubProvider {

    public String getAccessToken(AccessTokenDTO accessTokenDTO){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
//        System.setProperty("javax.net.ssl.trustStore","D:\\IDEA\\jbr\\lib\\security\\jssecacerts");
//        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())//配置
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())//配置
                .build();
        RequestBody body = RequestBody.create(JSON.toJSONString(accessTokenDTO), mediaType);
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            String accessToken = string.split("&")[0].split("=")[1];
            System.out.println("AccessToken = " + accessToken);
            return accessToken;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public GithubUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())//配置
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())//配置
                .build();
//        System.setProperty("javax.net.ssl.trustStore","D:\\IDEA\\jbr\\lib\\security\\jssecacerts");
//        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token=" + accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            System.out.println("in provider " + githubUser.getName());
            return githubUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
