package com.tools;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;

@WebServlet(name = "UserSearchCheckServlet")
public class UserSearchCheckServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String account = request.getParameter("account");
        String result;
        ConnDB conn = new ConnDB();
        ResultSet rs = conn.executeQuery("select * from tb_member where account = '" + account + "'");
        try{
            if (rs.next()) {
                int activate = rs.getInt("activate");
                if(activate==1){
                    result = "2";
                }
                else{
                    result = "0";
                }
            } else {
                result = "1";
            }
        } catch (Exception e){
            result = "0";
            e.printStackTrace();
        }
        conn.close();
        PrintWriter out = response.getWriter();
        out.write(result);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
