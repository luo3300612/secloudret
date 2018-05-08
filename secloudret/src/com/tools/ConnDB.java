package com.tools;

import java.sql.*;


public class ConnDB {
    //定义7个成员变量
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;

    //链接数据库的方法getConnction()
    private static Connection getConnection() {
        Connection conn = null;
        String dbClassName = "com.mysql.jdbc.Driver";
        String dbUrl = "jdbc:mysql://localhost:3306/se?characterEncoding=utf8&useSSL=true";
        String dbUser = "root";
        String dbPwd = "12345";
        try {
            Class.forName(dbClassName);
            conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (conn == null) {
            System.err.println("DbConnectionManager.getConnection():"
                    + dbClassName + "\r\n :" + dbUrl + "\r\n "
                    + dbUser + "/" + dbPwd);
        }
        return conn;
    }

    //编写执行更新数据的方法
    public int executeUpdate(String sql) {
        int result = 0;
        try {
            conn = getConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            result = stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            result = 0;
            ex.printStackTrace();
        }
        try {
            stmt.close();
        } catch (SQLException ex1) {
            ex1.printStackTrace();
        }
        return result;
    }

    //编写查询数据的方法
    public ResultSet executeQuery(String sql) {
        try{
            conn = getConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
        } catch(SQLException ex){
            ex.printStackTrace();
        }
        return rs;
    }

    public void close(){
        try{
            if (rs != null){
                rs.close();
            }
            if (stmt != null){
                stmt.close();
            }
            if (conn != null){
                conn.close();
            }
        }catch (Exception e){
            e.printStackTrace(System.err);
        }
    }

    //测试数据库连接是否成功
    public static void main(String[] args){
        if (getConnection() != null){
            System.out.println("数据库连接成功！");
        }
    }
}
