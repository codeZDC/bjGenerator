package com.yfkj.code.xfcc.util;

import java.util.HashMap;
import java.util.Map;

/**
 * java——mysql数据类型对照
 * @author 胡汉三
 * 2017年1月19日 下午4:22:46
 */
public class Type {
	
	/***
	 * Mysql
	 */
	public Type(){
		type.put("VARCHAR", "String");
		type.put("CHAR", "String");
		type.put("BLOB", "byte[]");
		type.put("TEXT", "String");
		type.put("LONGTEXT", "String");
		type.put("INTEGER", "Long");
		type.put("TINYINT", "Integer");
		type.put("SMALLINT", "Integer");
		type.put("MEDIUMINT", "Integer");
		type.put("BIT", "Boolean");
		type.put("BIGINT", "Long");
		type.put("FLOAT", "Float");
		type.put("DOUBLE", "Double");
		type.put("DECIMAL", "BigDecimal");
		type.put("BOOLEAN", "Integer");
		type.put("ID", "Long");
		type.put("INT", "Integer");
		type.put("DATE", "java.sql.Date");
		type.put("TIME", "java.sql.Time");
		type.put("DATETIME", "java.util.Date");
		type.put("TIMESTAMP", "java.sql.Timestamp");
		type.put("YEAR", "java.sql.Date");
	}
	
	public Map<String, String> type = new HashMap<String, String>();
}
