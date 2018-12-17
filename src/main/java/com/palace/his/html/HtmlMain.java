package com.palace.his.html;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;

import com.alibaba.fastjson.JSONObject;

public class HtmlMain {
	static CloseableHttpClient client = null;
	static CookieStore cookieStore = null;
	static String host="http://www.pss-system.gov.cn";
	public static void main(String[] args) throws Exception {
		init();
		doLogin();
	}
	
	public static void login() {
		
	}
	public static void init() {
		cookieStore = new BasicCookieStore();
		client = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
	}
	public static void doLogin() throws Exception {
		String loginUrl = "http://www.pss-system.gov.cn/sipopublicsearch/portal/index.shtml";
		String strRes = doPost(loginUrl,null);
		String imgSrc = Jsoup.parse(strRes).getElementById("codePic").attr("src");
		byte[]  bArr =  doPostToArr(host+imgSrc);
		File file = new File("e://pic//"+System.currentTimeMillis()+".jpg");
		if(!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		new FileOutputStream(file).write(bArr, 0,bArr.length);
		System.out.println(file.getAbsolutePath());
		Scanner scan = new Scanner(System.in) ;
		System.out.print("输入数据：") ;
		String code =  "";
		code = scan.next();
		
		loginUrl ="http://www.pss-system.gov.cn:80/sipopublicsearch/wee/platform/wee_security_check?v="+new SimpleDateFormat("yyyyMMdd").format(new Date());
		String res = doPost(loginUrl,null,"j_username","aGV3ZWljaHVuMTIxOQ==","j_password","aHdjMTIzNDU2Nzg5","j_validation_code",code);
		System.out.println(res);
	}
	
	public static String doPost(String url,Map<String,Object> header,String ...obj) throws Exception{
		HttpPost post = new HttpPost(url);
		if(obj != null && obj.length > 0) {
			post.setEntity(new StringEntity(getEnt(obj)));
		}
		if(header != null && !header.isEmpty()) {
			for(Map.Entry<String, Object> ent : header.entrySet()){
				post.setHeader(ent.getKey(),(String)ent.getValue());
			}
		}
		CloseableHttpResponse res = client.execute(post);
		return EntityUtils.toString(res.getEntity());
	}
	public static byte[] doPostToArr(String url,String ...obj) throws Exception{
		HttpPost post = new HttpPost(url);
		if(obj != null && obj.length > 0) {
			post.setEntity(new StringEntity(getEnt(obj)));
		}
		CloseableHttpResponse res = client.execute(post);
		return EntityUtils.toByteArray(res.getEntity());
	}
	public static String getEnt(String ...obj) {
		Map<String,String> map = new HashMap();
		for(int i=0;i<obj.length;) {
			map.put(obj[i], obj[i+1]);
			i+=2;
		}
		return JSONObject.toJSONString(map);
	}
}
