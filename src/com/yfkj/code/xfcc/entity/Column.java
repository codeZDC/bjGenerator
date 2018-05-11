package com.yfkj.code.xfcc.entity;

/**
 * 数据列
 * @author 胡汉三
 * 2017年1月19日 下午4:25:54
 */
public class Column {
	/**列名称**/
	private String columnName;
	/**列顺序**/
	private int ordinalPosition;
	/**是否为空**/
	private String isNullable;
	/**列数据库数据类型**/
	private String dataType;
	/**列长度**/
	private String characterMaximumLength;
	/**列类型：PRI、UNI等**/
	private String columnKey;
	/**列注释**/
	private String columnComment;
	
	/**列，Java类型**/
	private String javaType;
	/**列，Java字段(属性)**/
	private String javaCol;
	
	

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public int getOrdinalPosition() {
		return ordinalPosition;
	}

	public void setOrdinalPosition(int ordinalPosition) {
		this.ordinalPosition = ordinalPosition;
	}

	public String getIsNullable() {
		return isNullable;
	}

	public void setIsNullable(String isNullable) {
		this.isNullable = isNullable;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getCharacterMaximumLength() {
		return characterMaximumLength;
	}

	public void setCharacterMaximumLength(String characterMaximumLength) {
		this.characterMaximumLength = characterMaximumLength;
	}

	public String getColumnKey() {
		return columnKey;
	}

	public void setColumnKey(String columnKey) {
		this.columnKey = columnKey;
	}

	public String getColumnComment() {
		return columnComment;
	}

	public void setColumnComment(String columnComment) {
		this.columnComment = columnComment;
	}

	public String getJavaType() {
		return javaType;
	}

	public void setJavaType(String javaType) {
		this.javaType = javaType;
	}

	public String getJavaCol() {
		return javaCol;
	}

	public void setJavaCol(String javaCol) {
		this.javaCol = javaCol;
	}
	
}
