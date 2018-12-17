package com.palace.his.html;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

public class TestJsoup {
	static String host="http://www.pss-system.gov.cn";
	public static void main(String[] args) throws Exception {
		login();
	}
	
	public static void login() throws Exception {
		String url =host+"/sipopublicsearch/portal/index.shtml";
		url="http://www.pss-system.gov.cn/sipopublicsearch/portal/app/uilogin/js/mainLogin.js?v=20181218";
		Response res = Jsoup.connect(url).execute();
		System.out.println(res.body());
		String imageSrc = res.parse().getElementById("codePic").attr("src");
		System.out.println(imageSrc);
		HttpPost httpPost = new HttpPost(url+imageSrc);
		String path = TestJsoup.class.getResource("/").getPath();
		System.out.println(path);
	}
	 
}
