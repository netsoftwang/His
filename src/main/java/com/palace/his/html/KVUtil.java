package com.palace.his.html;

import java.util.HashMap;
import java.util.Map;

public class KVUtil {
	
	
	public static Map<String,Object> getKV(String data){
		Map<String,Object> map = new HashMap<String,Object>();
		for(String ss : data.split("\r\n")) {
			String[] arr = ss.split(":");
			if(arr.length > 1) {
				map.put(arr[0].trim(),arr[1].trim());
			}else {
				map.put(arr[0].trim(),"");
			}
		}
		return map;
	}
}
