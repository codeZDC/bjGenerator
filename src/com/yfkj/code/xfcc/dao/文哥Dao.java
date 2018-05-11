package com.yfkj.code.xfcc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.yfkj.code.xfcc.util.BeanHump;
import com.mysql.jdbc.StringUtils;
import com.yfkj.code.xfcc.entity.Column;
import com.yfkj.code.xfcc.entity.Table;
import com.yfkj.code.xfcc.util.PropertiesUtil;
import com.yfkj.code.xfcc.util.Type;

/**
 * 数据层
 * @author 胡汉三
 * 2017年1月19日 下午4:27:23
 */
public class 文哥Dao {
	
	/*时间格式化*/
	private static SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	
	/*获得数据库连接*/
	public static Connection getConnection(){
		Connection conn = null;
		try {
			Class.forName(PropertiesUtil.getValue("url.driver"));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}  
		try {
			conn = DriverManager.getConnection(PropertiesUtil.getValue("url.jdbc"), 
					PropertiesUtil.getValue("url.name"), PropertiesUtil.getValue("url.pass"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	/**
	 * 获得数据表对象
	 * @param tableName	数据库表名
	 * @param schema	数据库名
	 * @return
	 */
	public static Table getTable(String tableName,String schema,String indexOf){
		String sql = "select t.TABLE_COMMENT from information_schema.`TABLES` t where t.table_name= ? and t.TABLE_SCHEMA = ? ";
		Table table = new Table();
		Connection conn = getConnection();
		PreparedStatement stmt = null;
        ResultSet rs = null;
        String tStr = tableName;
        if(!StringUtils.isNullOrEmpty(indexOf) && tableName.toLowerCase().indexOf(indexOf.toLowerCase()) != -1){
        	tStr = tStr.substring(indexOf.length());
		}
        String sqlTable = BeanHump.underlineToCamel(tStr);
        table.setBeanLower(sqlTable.substring(0,1).toLowerCase()+sqlTable.substring(1));
        table.setBeanName(sqlTable.substring(0,1).toUpperCase()+sqlTable.substring(1));
        table.setDataTime(sf.format(new Date()));
        table.setTableName(tableName);
        table.setAuthorName(PropertiesUtil.getValue("author"));
        table.setCompanyName(PropertiesUtil.getValue("company"));
        try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, tableName);
			stmt.setString(2, schema);
			rs = stmt.executeQuery();
			if (rs.next()) {
				table.setEntityStrName(rs.getString("TABLE_COMMENT"));
            }
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			close(conn,stmt, null, rs);
		}
		return table;
	}
	
	/**
	 * 获得数据表中所有的列，返回数据列list集合
	 * @param tableName	数据库表名
	 * @param schema	数据库名
	 * @return
	 */
	public static List<Column> findColumn(String tableName,String schema){
		String sql = "select t.column_name,t.ORDINAL_POSITION, t.IS_NULLABLE,t.DATA_TYPE,t.CHARACTER_MAXIMUM_LENGTH,t.COLUMN_KEY,t.COLUMN_COMMENT from information_schema.columns t where t.table_name= ? and t.TABLE_SCHEMA = ? ";
		Connection conn = getConnection();
		PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Column> colList = new ArrayList<Column>();
        try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, tableName);
			stmt.setString(2, schema);
			rs = stmt.executeQuery();
			Column col = null;
			while (rs.next()) {
				col = new Column();
                col.setCharacterMaximumLength(rs.getString("CHARACTER_MAXIMUM_LENGTH"));
				col.setColumnComment(rs.getString("COLUMN_COMMENT"));
                col.setColumnKey(rs.getString("COLUMN_KEY"));
                col.setColumnName(rs.getString("column_name"));
				col.setDataType(rs.getString("DATA_TYPE"));
                col.setIsNullable(rs.getString("IS_NULLABLE"));
                col.setOrdinalPosition(rs.getInt("ORDINAL_POSITION"));
				
                String javaType = new Type().type.get(col.getDataType().toUpperCase());
                col.setJavaType(javaType);
                String javaCol = BeanHump.underlineToCamel(col.getColumnName()); 
                col.setJavaCol(javaCol);
                
				colList.add(col);
            }
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			close(conn,stmt, null, rs);
		}
        return colList;
	}
	/**
	 * 关闭数据库连接
	 * @param conn
	 * @param ps
	 * @param stmt
	 * @param rs
	 */
	public static void close(Connection conn,PreparedStatement ps,Statement stmt,ResultSet rs){
		try {
			if(rs != null){
				rs.close();
			}
			if(ps!=null){
				ps.close();
			}
			if(stmt != null){
				stmt.close();
			}
			if(conn != null){
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据数据列生成注解实体属性
	 * @param colList	数据库表列的集合
	 * @return
	 */
	public static String getColumn(List<Column> colList){
		StringBuilder colStr = new StringBuilder();
		for (Column c :colList) {
			/*生成注释*/
			colStr.append("    /**"+c.getColumnComment()+"**/\n");
			
			/**生成属性注解**/
			if("PRI".equals(c.getColumnKey())){
				/*如果是主键*/
				colStr.append("    @Id\n");
				colStr.append("    @GeneratedValue(generator = \"JDBC\")\n");
			}
			/**生成属性**/
			colStr.append("    private "+c.getJavaType()+" "+c.getJavaCol()+";\n\n");
		}
		/**生成get/set方法**/
		for (Column c :colList) {
			colStr.append("    /**"+c.getColumnComment()+"**/\n");
			colStr.append("    public "+c.getJavaType()+" get"+(c.getJavaCol().substring(0,1).toUpperCase()+c.getJavaCol().substring(1)) +"(){\n");
			colStr.append("        return "+c.getJavaCol()+";\n");
			colStr.append("    }\n");
			
			colStr.append("    /**"+c.getColumnComment()+"**/\n");
			colStr.append("    public void set"+(c.getJavaCol().substring(0,1).toUpperCase()+c.getJavaCol().substring(1)) +"("+c.getJavaType()+" "+c.getJavaCol()+"){\n");
			colStr.append("        this."+c.getJavaCol()+"= "+c.getJavaCol()+";\n");
			colStr.append("    }\n");
		}
		
		return colStr.toString();
	}
	
	/*
	 * mapresult 获取
	 */
	public static String getMapColumn(List<Column> colList){
		StringBuilder mapStr = new StringBuilder();
		for (Column c :colList) {
			if("PRI".equals(c.getColumnKey())){
				/*如果是主键*/
				mapStr.append("    <id column=\""+c.getColumnName()+"\" property=\""+c.getJavaCol()+"\" />\n");
			}else{
				mapStr.append("    <result column=\""+c.getColumnName()+"\" property=\""+c.getJavaCol()+"\" />\n");
			}
		}
		return mapStr.toString();
	}
	
	/*
	 * whereColumn 获取
	 */
	public static String getWhereColumn(List<Column> colList){
		StringBuilder mapStr = new StringBuilder();
		for (Column c :colList) {
			if(c.getJavaType().equals("String")){
				/*如果是String类型*/
				mapStr.append("    	<if test=\""+c.getJavaCol()+" != null and "+c.getJavaCol()+" != '' \">and "+c.getColumnName()+"=#{"+c.getJavaCol()+"}</if>\n");
			}else{
				mapStr.append("    	<if test=\""+c.getJavaCol()+" != null\">and "+c.getColumnName()+"=#{"+c.getJavaCol()+"}</if>\n");
			}
		}
		return mapStr.toString();
	}
	
	/*
	 * orderByColumn 获取排序字段
	 */
	public static String getOrderByColumn(List<Column> colList){
		StringBuilder mapStr = new StringBuilder();
		for (Column c :colList) {
			mapStr.append("    	<if test=\"'"+c.getJavaCol()+"' == sortName\">  "+c.getColumnName()+" \\$\\{sortOrder \\} </if>\n");
		}
		return mapStr.toString();
	}
	
	/**
	 * 获得主键
	 * @author 何祖文
	 * 2017年6月8日 上午10:53:58
	 * @param colList
	 * @return
	 */
	public static Column getPriKeyType(List<Column> colList){
		for (Column c :colList) {
			if("PRI".equals(c.getColumnKey())){
				return c;
			}
		}
		return new Column();
	}
	
	/**
	 * 获取插入时，要插入的字段——只插入不为null的数据
	 * @author 何祖文
	 * 2017年6月8日 上午10:34:08
	 * @param colList
	 * @return
	 */
	public static String getInsertNotNullCol(List<Column> colList){
		StringBuilder mapStr = new StringBuilder();
		for (Column c :colList) {
			mapStr.append("      <if test=\""+c.getJavaCol()+" != null\">\n");
			mapStr.append("      	"+c.getColumnName()+",\n");
			mapStr.append("      </if>\n");
		}
		return mapStr.toString();
	}
	
	/**
	 * 获取插入时，要插入字段的值——只插入不为null的数据
	 * @author 何祖文
	 * 2017年6月8日 上午10:34:08
	 * @param colList
	 * @return
	 */
	public static String getInsertNotNullValue(List<Column> colList){
		StringBuilder mapStr = new StringBuilder();
		for (Column c :colList) {
			mapStr.append("      <if test=\""+c.getJavaCol()+" != null\">\n");
			mapStr.append("      	#{"+c.getJavaCol()+"},\n");
			mapStr.append("      </if>\n");
		}
		return mapStr.toString();
	}
	
	/**
	 * 获取插入时，要插入的字段——全部插入
	 * @author 何祖文
	 * 2017年6月8日 上午10:34:08
	 * @param colList
	 * @return
	 */
	public static String getInsertAllCol(List<Column> colList){
		StringBuilder mapStr = new StringBuilder();
		for (Column c :colList) {
			mapStr.append(","+c.getColumnName());
		}
		return mapStr.toString().substring(1);
	}
	
	/**
	 * 获取插入时，要插入的字段的值——全部插入
	 * @author 何祖文
	 * 2017年6月8日 上午10:34:08
	 * @param colList
	 * @return
	 */
	public static String getInsertAllValue(List<Column> colList){
		StringBuilder mapStr = new StringBuilder();
		for (Column c :colList) {
			mapStr.append(","+"#{"+c.getJavaCol()+"}");
		}
		return mapStr.toString().substring(1);
	}
	
	/**
	 * 更新的sql语句——更新不为null的值
	 * @author 何祖文
	 * 2017年6月8日 上午9:53:31
	 * @return
	 */
	public static String getUpdateSetNotNull(List<Column> colList){
		StringBuilder mapStr = new StringBuilder();
		for (Column c : colList) {
			if("PRI".equals(c.getColumnKey())){
				//主键不更新
				continue;
			}
			mapStr.append("    	<if test=\""+c.getJavaCol()+" != null\">\n");
			mapStr.append("    		"+c.getColumnName()+" = #{"+c.getJavaCol()+"},\n");
			mapStr.append("    	</if>\n");
		}
		return mapStr.toString();
	}
	
	/**
	 * 更新的sql语句——更新所有的值
	 * @author 何祖文
	 * 2017年6月8日 上午9:53:31
	 * @return
	 */
	public static String getUpdateSetAll(List<Column> colList){
		StringBuilder mapStr = new StringBuilder();
		int len = 1 ,size = colList.size();
		for (Column c : colList) {
			if("PRI".equals(c.getColumnKey())){
				//主键不更新
				len ++;
				continue;
			}
			if(size == len){
				mapStr.append("    	"+c.getColumnName()+" = #{"+c.getJavaCol()+"}");
				break;
			}else{
				mapStr.append("    	"+c.getColumnName()+" = #{"+c.getJavaCol()+"},\n");
			}
			len ++;
		}
		return mapStr.toString();
	}
	
	/**
	 * 更新的sql语句——WHERE条件
	 * @author 何祖文
	 * 2017年6月8日 上午9:53:31
	 * @return
	 */
	public static String getUpdateAllWhere(List<Column> colList){
		StringBuilder mapStr = new StringBuilder();
		for (Column c : colList) {
			if("PRI".equals(c.getColumnKey())){
				mapStr.append(" "+c.getColumnName()+" = #{"+c.getJavaCol()+"}");
				break;
			}
		}
		return mapStr.toString();
	}
	
	/*
	 * SelColumn 获取
	 */
	public static String getSelColumn(List<Column> colList){
		StringBuilder selStr = new StringBuilder();
		for (Column c :colList) {
			selStr.append(","+c.getColumnName());
		}
		return selStr.substring(1);
	}
	
	public static String getColumnInfo(List<Column> colList){
		StringBuilder colAdd = new StringBuilder();
		for (Column c : colList) {
			colAdd.append("                        <div class=\"form-group\">\n");
			colAdd.append("                            <label class=\"col-sm-2 control-label col-lg-2\" for=\"inputSuccess\">"+c.getColumnComment()+":</label>\n");
			colAdd.append("                            <div class=\"col-lg-10\">\n");
			colAdd.append("                            	<p class=\"form-control-static\" name=\""+c.getJavaCol()+"\"></p>\n");
			colAdd.append("                            </div>\n");
			colAdd.append("                        </div>\n");
		}
		return colAdd.toString();
	}
	
	public static String getColumnAdd(List<Column> colList,boolean isEdit){
		StringBuilder colAdd = new StringBuilder();
		for (Column c : colList) {
			if(isEdit && "PRI".equals(c.getColumnKey())){
				colAdd.append("                        <input class=\"form-control m-bot15\" name=\""+c.getJavaCol()+"\" type=\"hidden\" >\n");
			}else if("PRI".equals(c.getColumnKey())){
				//新增时，不需要自增主键
				continue;
			}else{
				colAdd.append("                        <div class=\"form-group\">\n");
				colAdd.append("                            <label class=\"col-sm-2 control-label col-lg-2\" for=\"inputSuccess\">"+c.getColumnComment()+":</label>\n");
				colAdd.append("                            <div class=\"col-lg-10\">\n");
				colAdd.append("                                <input class=\"form-control m-bot15\" name=\""+c.getJavaCol()+"\" type=\"text\" placeholder=\"请填写"+c.getColumnComment()+"\">\n");
				colAdd.append("                            </div>\n");
				colAdd.append("                        </div>\n");
			}
		}
		return colAdd.toString();
	}
	
	public static String getColumnJS(List<Column> colList){
		StringBuilder colAdd = new StringBuilder();
		int i = 1;
		for (Column c : colList) {
			if("PRI".equals(c.getColumnKey())){
				colAdd.append("		    {checkbox: true },\n");
				continue;
			}
			colAdd.append("		    {title:'"+c.getColumnComment()+"',field: '"+c.getJavaCol()+"',sortable:true }");
			if(i++ < colList.size()-1){
				colAdd.append(",");
			}
			colAdd.append("\n");
		}
		return colAdd.toString();
	}
	
	public static String getColumnJsEdit(List<Column> colList,String isForm){
		StringBuilder colAdd = new StringBuilder();
		int i = 1;
		for (Column c : colList) {
			colAdd.append("				   "+c.getJavaCol()+":rows"+isForm+"."+c.getJavaCol()+"");
			if(i++ < colList.size()){
				colAdd.append(",");
			}
			colAdd.append("\n");
		}
		return colAdd.toString();
	}
	
	public static void main(String[] args) {
		
	}
}
