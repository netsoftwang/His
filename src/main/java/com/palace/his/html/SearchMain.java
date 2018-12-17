package com.palace.his.html;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;

import com.alibaba.fastjson.JSONObject;

public class SearchMain {
	
	
	static String head = "Accept:application/json, text/javascript, */*; q=0.01\r\n" + 
			"Accept-Encoding:gzip, deflate\r\n" + 
			"Accept-Language:zh-CN,zh;q=0.9,en;q=0.8\r\n" + 
			"Connection:keep-alive\r\n" + 
			"Content-Type:application/x-www-form-urlencoded; charset=UTF-8\r\n" + 
			"Host:www.pss-system.gov.cn\r\n" + 
			"Origin:http://www.pss-system.gov.cn\r\n" + 
			"Referer:http://www.pss-system.gov.cn/sipopublicsearch/patentsearch/tableSearch-showTableSearchIndex.shtml\r\n" + 
			"User-Agent:Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36\r\n" + 
			"X-Requested-With:XMLHttpRequest";
	static String params  = "resultPagination.limit: 10\r\n" + 
			"resultPagination.sumLimit: 10\r\n" + 
			"resultPagination.start: 10\r\n" + 
			"resultPagination.totalCount: 492102\r\n" + 
			"searchCondition.sortFields: -APD,+PD\r\n" + 
			"searchCondition.searchType: Sino_foreign\r\n" + 
			"searchCondition.originalLanguage: \r\n" + 
			"searchCondition.searchExp: (申请日=20170701:20171231 AND 申请（专利权）人=(公司 OR 企业 OR 集团 OR 厂 OR 合作社)) AND (发明类型=(\"I\" OR \"U\" OR \"D\") AND 公开国家/地区/组织=(CN))\r\n" + 
			"searchCondition.executableSearchExp: VDB:(((APD='20170701' to '20171231' AND (PAVIEW='公司' OR PAVIEW='企业' OR PAVIEW='集团' OR PAVIEW='厂' OR PAVIEW='合作社')) AND ((DOC_TYPE='I' OR DOC_TYPE='U' OR DOC_TYPE='D') AND CC='CN')) AND ((DOC_TYPE='I')))\r\n" + 
			"searchCondition.dbId: \r\n" + 
			"searchCondition.literatureSF: (申请日=20170701:20171231)\r\n" + 
			"searchCondition.targetLanguage: \r\n" + 
			"searchCondition.resultMode: undefined\r\n" + 
			"searchCondition.strategy: STRATEGY_CALCULATE\r\n" + 
			"searchCondition.searchKeywords: [集][ ]{0,}[团][ ]{0,}\r\n" + 
			"searchCondition.searchKeywords: [合][ ]{0,}[作][ ]{0,}[社][ ]{0,}\r\n" + 
			"searchCondition.searchKeywords: [厂][ ]{0,}\r\n" + 
			"searchCondition.searchKeywords: [公][ ]{0,}[司][ ]{0,}\r\n" + 
			"searchCondition.searchKeywords: [企][ ]{0,}[业][ ]{0,}\r\n" + 
			"searchCondition.searchKeywords: [C][ ]{0,}[N][ ]{0,}\r\n" + 
			"searchCondition.searchKeywords: [2][ ]{0,}[0][ ]{0,}[1][ ]{0,}[7][ ]{0,}[.][ ]{0,}[1][ ]{0,}[2][ ]{0,}[.][ ]{0,}[3][ ]{0,}[1][ ]{0,}\r\n" + 
			"searchCondition.searchKeywords: [2][ ]{0,}[0][ ]{0,}[1][ ]{0,}[7][ ]{0,}[.][ ]{0,}[0][ ]{0,}[7][ ]{0,}[.][ ]{0,}[0][ ]{0,}[1][ ]{0,}";
/*	static String params="searchCondition.searchExp:(申请日=20170701:20171231 AND 申请（专利权）人=(公司 OR 企业 OR 集团 OR 厂 OR 合作社)) AND (发明类型=(\"I\" OR \"U\" OR \"D\") AND 公开国家/地区/组织=(CN))\r\n" + 
			"searchCondition.originalLanguage:\r\n" + 
			"searchCondition.targetLanguage:\r\n" + 
			"searchCondition.dbId:VDB\r\n" + 
			"searchCondition.searchType:Sino_foreign\r\n" + 
			"searchCondition.registerSS:true\r\n" + 
			"wee.bizlog.modulelevel:0200807";*/
	
	
	static CloseableHttpClient client = null;
	static CookieStore cookieStore = null;
	static String host="http://www.pss-system.gov.cn";
	static {
		cookieStore = new BasicCookieStore();
		client = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
	}
	public static void main(String[] args) throws Exception {
		String loginUrl = "http://www.pss-system.gov.cn/sipopublicsearch/portal/index.shtml";
		//访问首页
		String strRes = doPost(loginUrl,null,null);
		//打印cookie
		printCookie();
		String imgSrc = Jsoup.parse(strRes).getElementById("codePic").attr("src");
		//获取验证码
		byte[]  bArr =  doPostToArr(host+imgSrc);
		File file = new File("e://pic//"+System.currentTimeMillis()+".jpg");
		if(!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		new FileOutputStream(file).write(bArr, 0,bArr.length);
		System.out.println(file.getAbsolutePath());
		Scanner scan = new Scanner(System.in) ;
		System.out.print("请输入验证码数据：") ;
		String code =  "";
		code = scan.next();
		
		
		//登录请求
		loginUrl ="http://www.pss-system.gov.cn:80/sipopublicsearch/wee/platform/wee_security_check?v="+new SimpleDateFormat("yyyyMMdd").format(new Date());
		Map<String,Object> pMap = new HashMap<String, Object>();
		pMap.put("j_username", "aGV3RTaFTaDBZWljaHVuMTIxOQ==");
		pMap.put("j_password", "aHdjMTIzNDU2SEADfATAAefaNzg5");
		pMap.put("j_validation_code",code);
		String res = doGet(loginUrl,null,pMap);
		System.out.println(res);
		printCookie();
		
		//执行查询
		String url = "http://www.pss-system.gov.cn/sipopublicsearch/patentsearch/executeSearchStrategy-executeSearchByExp.shtml";
		Map<String,Object> paraMap = KVUtil.getKV(params);
		setCookie(paraMap);
		String queryRes = doPostPara(url,null,paraMap);
		System.out.println("queryResult=========================================================================================");
		System.out.println(queryRes);
 
	}
	private static void setCookie(Map<String, Object> paraMap) {
		StringBuilder sb = new StringBuilder();
		for(Cookie cookie :cookieStore.getCookies()) {
			String ss = cookie.getName()+":"+cookie.getValue();
			//paraMap.put(cookie.getName(), cookie.getValue());
			sb.append(cookie.getName()).append("=").append(cookie.getValue()).append("; ");
		}
		paraMap.put("Cookie", sb.toString());
	}
	public static void printCookie() {
		System.out.println("cookies============================================================================================");
		for(Cookie cookie :cookieStore.getCookies()) {
			String ss = cookie.getName()+":"+cookie.getValue();
			System.out.println(ss);
		}
	}
	
	public static String doPostPara(String url,Map<String,Object> header,Map<String,Object> data) throws Exception{
		HttpPost post = new HttpPost(url);
		if(data != null ) {
			post.setEntity(new StringEntity(JSONObject.toJSONString(data)));
		}
		if(header != null && !header.isEmpty()) {
			for(Map.Entry<String, Object> ent : header.entrySet()){
				post.setHeader(ent.getKey(),(String)ent.getValue());
			}
		}
		CloseableHttpResponse res = client.execute(post);
		return EntityUtils.toString(res.getEntity());
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
	
	
	public static String doGet(String url,Map<String,Object> header,Map<String,Object> data) throws Exception{
		URIBuilder builder = new URIBuilder(url);
		if(data != null) {
	        Set<String> set = data.keySet();
	        for(String key: set){
	            builder.setParameter(key, (String)data.get(key));
	        }
		}
		HttpGet get = new HttpGet(builder.build());
		
		if(header != null && !header.isEmpty()) {
			for(Map.Entry<String, Object> ent : header.entrySet()){
				get.setHeader(ent.getKey(),(String)ent.getValue());
			}
		}
		CloseableHttpResponse res = client.execute(get);
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
