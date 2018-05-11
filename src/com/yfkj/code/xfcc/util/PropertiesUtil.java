package com.yfkj.code.xfcc.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


/**
 * 获取属性文件内容
 * @author 胡汉三
 * 2017年1月19日 下午4:23:58
 */
public class PropertiesUtil {
	private static Properties prop = new Properties();// 属性集合对象
	
	/**
	 * 根据属性名称获取——本方法只获取版本属性文件
	 * @param key
	 * @return
	 */
	public static String getValue(String key){
		try {
			FileInputStream fis = new FileInputStream(PropertiesUtil.class.getResource("/").getFile()+"db.properties");// 属性文件输入流     
			// 将属性文件流装载到Properties对象中
			prop.load(fis);
			fis.close();// 关闭流  
			return prop.getProperty(key);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 根据文件名称跟属性名称获取
	 * @param key
	 * @param fileName
	 * @return
	 */
	public static String getValue(String key,String fileName){
		try {
			FileInputStream fis = new FileInputStream(PropertiesUtil.class.getResource("/").getFile()+fileName);// 属性文件输入流     
			// 将属性文件流装载到Properties对象中
			prop.load(fis);
			fis.close();// 关闭流  
			return prop.getProperty(key);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		System.out.println(getValue("table.name"));
	}
}
