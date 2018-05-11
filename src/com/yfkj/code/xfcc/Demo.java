package com.yfkj.code.xfcc;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.yfkj.code.xfcc.util.BeanHump;

public class Demo {
	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		list.add("1");
		list.add("2");
		list.add("3");
		int len = 1; 
		for (String string : list) {
			if(list.size() == len){
				System.out.println("fuck");
				break;
			}
			System.out.println(string);
			len++;
		}
	}
	
	@Test
	public void testCamel(){
		 String result = BeanHump.underlineToCamel("_wo_ai_ni");
		 System.out.println(result);
	}
}
