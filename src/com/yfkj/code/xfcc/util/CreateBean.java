package com.yfkj.code.xfcc.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.yfkj.code.xfcc.dao.Dao;
import com.yfkj.code.xfcc.entity.Column;
import com.yfkj.code.xfcc.entity.Table;
import com.yfkj.code.xfcc.util.PropertiesUtil;

/**
 * 生成主体类
 * @author 胡汉三
 * 2017年1月19日 下午4:35:23
 */
public class CreateBean {
	//代码模版路径
	private String tempPath = System.getProperty("user.dir")+"//resources//template//";
	//代码生成文件路径
	private String javaPath = PropertiesUtil.getValue("code.url");
	public static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static Table table = null;
	public static List<Column> colList = null;
	public static String schema = null;
	
	/**
	 * 生成器构造
	 * @param tableName	数据库表名
	 * @param schema	数据库名
	 */
	public CreateBean(String tableName,String indexOf){
		File tfile = new File(tempPath);
		File jfile = new File(javaPath);
		if(!tfile.exists()){
			tfile.mkdirs();
		}
		if(!jfile.exists()){
			jfile.mkdirs();
		} 
		tfile.delete();
		jfile.delete();
		
		
		table = Dao.getTable(tableName,schema,indexOf);
		colList = Dao.findColumn(tableName,schema);
	}
	
	/**
	 * 开始生成
	 */
	public static void create(){
		schema = PropertiesUtil.getValue("table.schema");
		String tables = PropertiesUtil.getValue("tables");
		String indexOf = PropertiesUtil.getValue("index.of");
		//如果tables不为空，则批量生成
		if(tables!=null&&!"".equals(tables)){
			createTables(tables.split(","),indexOf);
		}else{
			//只生成一张表
			System.err.println("没有指定表名称!");
		}
	}
	
	/**创建实体类**/
	public void createTable(Table t){
		ReadWriteFile r = new ReadWriteFile(tempPath+t.getTempName());
		r.readTxtFile(); //读  
		r.getReplace("@PACKIMPORT", schema);
		r.getReplace("@entityStrName", t.getEntityStrName()); 
		r.getReplace("@authorName", t.getAuthorName());
		r.getReplace("@dataTimeStr", t.getDataTime());
		r.getReplace("@entityTableName", t.getBeanName());
		r.getReplace("@entityTable", t.getTableName());
		r.getReplace("@ColumnStr", Dao.getColumn(colList));
		r.getReplace("@companyName", t.getCompanyName());//zdc add
		try {
			String path = javaPath+"//domain//";
			File f = new File(path);
			if(!f.exists()){
				f.mkdirs();
			}
			f = new File(path+t.getNewFileName());
			if(f.exists()){
				f.delete();
			}
			r.writeTxtFile(path+t.getNewFileName());
		} catch (IOException e) {
			e.printStackTrace();
		}  
		System.out.println("成功创建实体类"); 
	}
	
	/**
	 * 生成数据库接口
	 */
	public void createIDao(Table t){
		ReadWriteFile r = new ReadWriteFile(tempPath+t.getTempName());
		r.readTxtFile(); //读  
		r.getReplace("@PACKIMPORT", schema);
		r.getReplace("@beanName", t.getBeanName());
		r.getReplace("@beanLower", t.getBeanLower());
		r.getReplace("@entityStrName", t.getEntityStrName());
		r.getReplace("@authorName", t.getAuthorName());
		r.getReplace("@dataTimeStr", t.getDataTime());
		r.getReplace("@companyName", t.getCompanyName());//zdc
		Column c =  Dao.getPriKeyType(colList);
		
		r.getReplace("@PRITYPE", c.getJavaType());
		r.getReplace("@PRINAME", c.getJavaCol());
		try {
			String path = javaPath+"//mapper//";
			File f = new File(path);
			if(!f.exists()){
				f.mkdirs();
			}
			f = new File(path+t.getNewFileName());
			if(f.exists()){
				f.delete();
			}
			r.writeTxtFile(path+t.getNewFileName());
		} catch (IOException e) {
			e.printStackTrace();
		}  
		System.out.println("成功创建Dao"); 
	}
	
	/**
	 * 生成Mappering.xml
	 */
	public void createMappering(Table t){
		ReadWriteFile r = new ReadWriteFile(tempPath+t.getTempName());
		r.readTxtFile(); //读  
		r.getReplace("@PACKIMPORT", schema);
		r.getReplace("@beanName", t.getBeanName());
		r.getReplace("@MapColumn", Dao.getMapColumn(colList));
		r.getReplace("@WhereColumn", Dao.getWhereColumn(colList));
		r.getReplace("@SelColumn", Dao.getSelColumn(colList));
		r.getReplace("@entityTable", t.getTableName());
		r.getReplace("@OrderByColumn", Dao.getOrderByColumn(colList));
		r.getReplace("@companyName", t.getCompanyName());//zdc
		
		Column c = Dao.getPriKeyType(colList);
		r.getReplace("@PRITYPE", c.getJavaType());
		r.getReplace("@KEYPROPERTYVALUE", c.getColumnName());
		
		r.getReplace("@UPDATESETALL", Dao.getUpdateSetAll(colList));
		r.getReplace("@UPDATEALLWHERE", Dao.getUpdateAllWhere(colList));
		r.getReplace("@UPDATESETNOTNULL", Dao.getUpdateSetNotNull(colList));
		r.getReplace("@INSERTALLCOL", Dao.getInsertAllCol(colList));
		r.getReplace("@INSERTALLVALUE", Dao.getInsertAllValue(colList));
		r.getReplace("@INSERTNOTNULLCOL", Dao.getInsertNotNullCol(colList));
		r.getReplace("@INSERTNOTNULLVALUE", Dao.getInsertNotNullValue(colList));
		try {
			String path = javaPath+"//mapping//";
			File f = new File(path);
			if(!f.exists()){
				f.mkdirs();
			}
			f = new File(path+t.getNewFileName());
			if(f.exists()){
				f.delete();
			}
			r.writeTxtFile(path+t.getNewFileName());
		} catch (IOException e) {
			e.printStackTrace();
		}  
		System.out.println("成功创建Dao"); 
	}
	
	/**
	 * 生成业务层接口
	 */
	public void createIService(Table t){
		ReadWriteFile r = new ReadWriteFile(tempPath+t.getTempName());
		r.readTxtFile(); //读  
		r.getReplace("@PACKIMPORT", schema);
		r.getReplace("@beanName", t.getBeanName());
		r.getReplace("@beanLower", t.getBeanLower());
		r.getReplace("@entityStrName", t.getEntityStrName());
		r.getReplace("@authorName", t.getAuthorName());
		r.getReplace("@dataTimeStr", t.getDataTime());
		r.getReplace("@companyName", t.getCompanyName());//zdc
		Column c =  Dao.getPriKeyType(colList);
		r.getReplace("@PRITYPE", c.getJavaType());
		r.getReplace("@PRINAME", c.getJavaCol());
		try {
			String path = javaPath+"//service//";
			File f = new File(path);
			if(!f.exists()){
				f.mkdirs();
			}
			f = new File(path+t.getNewFileName());
			if(f.exists()){
				f.delete();
			}
			r.writeTxtFile(path+t.getNewFileName());
		} catch (IOException e) {
			e.printStackTrace();
		}  
		System.out.println("成功创建Service"); 
	}
	
	/** 
	 * 生成业务层接口实现类
	 */ 
	public void createServiceImpl(Table t){
		ReadWriteFile r = new ReadWriteFile(tempPath+t.getTempName());
		r.readTxtFile(); //读
		r.getReplace("@PACKIMPORT", schema);
		r.getReplace("@beanName", t.getBeanName());
		r.getReplace("@beanLower", t.getBeanLower());  
		r.getReplace("@entityStrName", t.getEntityStrName());
		r.getReplace("@authorName", t.getAuthorName());
		r.getReplace("@dataTimeStr", t.getDataTime());
		r.getReplace("@companyName", t.getCompanyName());//zdc
		Column c =  Dao.getPriKeyType(colList);
		r.getReplace("@PRITYPE", c.getJavaType());
		r.getReplace("@PRINAME", c.getJavaCol());
		try {
			String path = javaPath+"//service//impl//";
			File f = new File(path);
			if(!f.exists()){
				f.mkdirs();
			}
			f = new File(path+t.getNewFileName());
			if(f.exists()){
				f.delete();
			}
			r.writeTxtFile(path+t.getNewFileName());
		} catch (IOException e) {
			e.printStackTrace();
		}  
		System.out.println("成功创建ServiceImpl"); 
	}
	
	/** 
	 * 生成展现层
	 */ 
	public void createController(Table t){
		ReadWriteFile r = new ReadWriteFile(tempPath+t.getTempName());
		r.readTxtFile(); //读  
		r.getReplace("@PACKIMPORT", schema);
		r.getReplace("@beanName", t.getBeanName());
		r.getReplace("@beanLower", t.getBeanLower());  
		r.getReplace("@entityStrName", t.getEntityStrName());
		r.getReplace("@authorName", t.getAuthorName());
		r.getReplace("@dataTimeStr", t.getDataTime());
		r.getReplace("@companyName", t.getCompanyName());//zdc
		Column c =  Dao.getPriKeyType(colList);
		r.getReplace("@PRITYPE", c.getJavaType());
		r.getReplace("@PRINAME", c.getJavaCol());
		try {
			String path = javaPath+"//controller//";
			File f = new File(path);
			if(!f.exists()){
				f.mkdirs();
			}
			f = new File(path+t.getNewFileName());
			if(f.exists()){
				f.delete();
			}
			r.writeTxtFile(path+t.getNewFileName());
		} catch (IOException e) { 
			e.printStackTrace();
		}  
		System.out.println("成功创建Controller"); 
	}
	
	/** 
	 * 生成Test
	 */ 
	public void createTest(Table t){
		ReadWriteFile r = new ReadWriteFile(tempPath+t.getTempName());
		r.readTxtFile(); //读  
		r.getReplace("@PACKIMPORT", schema);
		r.getReplace("@beanName", t.getBeanName());
		r.getReplace("@beanLower", t.getBeanLower());  
		r.getReplace("@entityStrName", t.getEntityStrName());
		r.getReplace("@authorName", t.getAuthorName());
		r.getReplace("@dataTimeStr", t.getDataTime());
		r.getReplace("@companyName", t.getCompanyName());//zdc
		try {
			String path = javaPath+"//test//";
			File f = new File(path);
			if(!f.exists()){
				f.mkdirs();
			}
			f = new File(path+t.getNewFileName());
			if(f.exists()){
				f.delete();
			}
			r.writeTxtFile(path+t.getNewFileName());
		} catch (IOException e) { 
			e.printStackTrace();
		}  
		System.out.println("成功创建Controller"); 
	}
	
	/** 
	 * 生成JSP
	 */ 
	public void createJSP(Table t){
		ReadWriteFile r = new ReadWriteFile(tempPath+t.getTempName());
		r.readTxtFile(); //读  
		r.getReplace("@entityStrName", t.getEntityStrName());
		r.getReplace("@beanLower", t.getBeanLower());  
		//r.getReplace("@JSPSAVE", Dao.getColumnAdd(colList, false));
		//r.getReplace("@JSPEDIT", Dao.getColumnAdd(colList, true));
		//r.getReplace("@JSPINFO", Dao.getColumnInfo(colList));
		r.getReplace("@addFormInput", Dao.getAddOrEditFormInput(colList));
		r.getReplace("@editFormInput", Dao.getAddOrEditFormInput(colList));
		r.getReplace("@viewFormInput", Dao.getViewFormInput(colList));
		try { 
			String path = javaPath+"//jsp//";
			File f = new File(path);
			if(!f.exists()){
				f.mkdirs();
			}
			f = new File(path+t.getNewFileName());
			if(f.exists()){
				f.delete();
			}
			r.writeTxtFile(path+t.getNewFileName());
		} catch (IOException e) { 
			e.printStackTrace();
		}  
		System.out.println("成功创建JSP"); 
	}
	
	/** 
	 * 生成JS
	 */ 
	public void createJavaScript(Table t){
		ReadWriteFile r = new ReadWriteFile(tempPath+t.getTempName());
		r.readTxtFile(); //读  
		r.getReplace("@entityStrName", t.getEntityStrName());
		r.getReplace("@entityName", t.getBeanName());
		r.getReplace("@beanLower", t.getBeanLower());  
		//bootstrapTable => r.getReplace("@columnsJSList", Dao.getColumnJS(colList));
		r.getReplace("@buildTableFunctionJs", Dao.getBuildTableFunctionJs(colList));
		try { 
			String path = javaPath+"//jsp//js//";
			File f = new File(path);
			if(!f.exists()){
				f.mkdirs();
			}
			f = new File(path+t.getNewFileName());
			if(f.exists()){
				f.delete();
			}
			r.writeTxtFile(path+t.getNewFileName());
		} catch (IOException e) { 
			e.printStackTrace();
		}  
		System.out.println("成功创建JS"); 
	}

	/**
	 * 根据表名跟数据库名批量生成代码
	 * @param tables	以","逗号分割
	 * @param schema
	 */
	public static void createTables(String[] tables,String indexOf){
		for (int i = 0; i < tables.length; i++) {
			if(tables[i]==null||"".equals(tables[i])){
				continue;
			}
			createTable(tables[i],indexOf);
		}
	}
	
	/**
	 * 根据表名跟数据库名生成——单表
	 * @param tableName
	 * @param schema
	 */
	public static void createTable(String tableName,String indexOf){
		
		CreateBean c = new CreateBean(tableName,indexOf);
		System.out.println(df.format(new Date())+"  :"+ table.getTableName()+"("+table.getEntityStrName()+")==========>>"+"开始生成=========>>");
		
		/*创建实体bean*/
		table.setTempName("model.temp");
		table.setNewFileName(table.getBeanName()+".java");
		c.createTable(table);
		
		/*创建dao接口*/
		table.setTempName("mapper.temp"); 
		table.setNewFileName("I"+table.getBeanName()+"Mapper.java");
		c.createIDao(table);  
		 
		/*创建mappering*/
		table.setTempName("mapping.temp");
		table.setNewFileName(table.getBeanName()+"Mapper.xml");
		c.createMappering(table);
		
		/*创建service接口*/
		table.setTempName("service.temp"); 
		table.setNewFileName("I"+table.getBeanName()+"Service.java");
		c.createIService(table);
		
		/*创建service接口实现类*/
		table.setTempName("serviceImpl.temp"); 
		table.setNewFileName(table.getBeanName()+"ServiceImpl.java");
		c.createServiceImpl(table); 
		
		/*创建控制器*/
		table.setTempName("controller.temp"); 
		table.setNewFileName(table.getBeanName()+"Controller.java");
		c.createController(table);  
		
		/*创建test*/
		table.setTempName("test.temp"); 
		table.setNewFileName(table.getBeanName()+"Test.java");
		c.createTest(table);
		
		/*创建jsp页面*/
		table.setTempName("jsp.temp");  
		table.setNewFileName(table.getBeanLower()+".jsp");  
		c.createJSP(table);
		
		/*创建js代码*/
		table.setTempName("js.temp");  
		table.setNewFileName(table.getBeanLower()+".js");  
		c.createJavaScript(table);
		System.out.println(df.format(new Date())+"  :"+table.getTableName()+"("+table.getEntityStrName()+")==========>>"+"生成结束=========>>");
	}
	
}
