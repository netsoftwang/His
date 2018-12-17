package com.palace.his.html;

import org.junit.Test;

public class TestJsoup2 {
	 
	@Test
	public void testData() {
		String data = "resultPagination.limit: 10\r\n" ;
		if(data.trim().contains("\r\n")) {
			System.out.println(data);
		}
	}
	 
}
