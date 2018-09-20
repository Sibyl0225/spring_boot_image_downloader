package com.neo.util;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

//操作数据库
public class databaseTest {
    private static List<String> result = new ArrayList<String>();  
    private static Connection conn = null;
    //jdbc连接mysql，返回连接字符串;连接信息从配置文件读取;
    private static void get_mysql_connection() throws FileNotFoundException, ClassNotFoundException, SQLException{

    	Class.forName("com.mysql.cj.jdbc.Driver"); //指定驱动名称 

    	String url = "jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC&useSSL=false";//本机默认端口为3306
    	String user="sion";
    	String password="sion8940";
    	conn=DriverManager.getConnection(url,user,password); //创建Connection对象 
    }
    //查询mysql，返回查询结果list;
    public static List<String> select_from_mysql(String sql) throws SQLException, FileNotFoundException, JSONException{

        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        ResultSetMetaData rm = rs.getMetaData();
        while(rs.next()){
            JSONObject json_obj = new JSONObject();
            for (int i=1; i<=rm.getColumnCount(); i++){
                json_obj.put(rm.getColumnLabel(i), rs.getString(i));
            }
            result.add(json_obj.toString());
        }
        rs.close();
        conn.close();
        return result;
    }
    
    public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, SQLException, JSONException {
    	get_mysql_connection();
    	List<String> nsrxxs = select_from_mysql("select * from dj_nsrxx");
    	for (String nsrxx : nsrxxs) {
			System.out.println(nsrxx);
		}
	}

}