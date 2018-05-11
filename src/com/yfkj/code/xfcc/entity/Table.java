package com.yfkj.code.xfcc.entity;

/**
 * 实体类Table
 * @author 胡汉三
 * 2017年1月19日 下午4:26:09
 */
public class Table {
	
	/**类的名称(java)**/
	private String beanName;
	/**类对象的名称(通常都是类名首字母小写)**/
	private String beanLower;
	/**实体类的注释**/
	private String entityStrName;
	/**创建时间**/
	private String dataTime;
	/**要被创建的文件名称**/
	private String newFileName;
	/**依赖创建的temp文件名称**/
	private String tempName;
	/**数据表的名称**/
	private String tableName;
	/**作者名称**/
	private String authorName;
	/**公司名称**/
	private String companyName;
	
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getBeanName() {
		return beanName;
	}
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}
	public String getEntityStrName() {
		return entityStrName;
	}
	public String getBeanLower() {
		return beanLower;
	}
	public void setBeanLower(String beanLower) {
		this.beanLower = beanLower;
	}
	public void setEntityStrName(String entityStrName) {
		this.entityStrName = entityStrName;
	}
	public String getDataTime() {
		return dataTime;
	}
	public void setDataTime(String dataTime) {
		this.dataTime = dataTime;
	}
	public String getNewFileName() {
		return newFileName;
	}
	public void setNewFileName(String newFileName) {
		this.newFileName = newFileName;
	}
	public String getTempName() {
		return tempName;
	}
	public void setTempName(String tempName) {
		this.tempName = tempName;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	
}
