package com.dao;

import com.model.Member;
import com.tools.ChStr;
import com.tools.ConnDB;

import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/*
create table tb_member(
id int primary key auto_increment,
account varchar(20),
psw varchar(20),
tel varchar(11),
activate int)charset=utf8;

 */
public class MemberDaoImpl implements MemberDao{
    private ConnDB conn = new ConnDB();
    private ChStr chStr = new ChStr();
    @Override
    public int insert(Member m) {
        int ret = -1;
        try{
            String sql = "insert into tb_member(account,psw,tel,activate)" +
                     " values('" +
                    chStr.chStr(m.getAccount()) + "','" + chStr.chStr(m.getPassword()) + "','" +
                    chStr.chStr(m.getTel()) + "',1)";
            ret = conn.executeUpdate(sql);
        }catch (Exception e){
            e.printStackTrace();
            ret = 0;
        }
        conn.close();
        return ret;
    }

    @Override
    public List select() {
        Member form = null;
        List list = new ArrayList();
        String sql = "select * from tb_member";
        ResultSet rs = conn.executeQuery(sql);
        try{
            while(rs.next()){
                form = new Member();
                form.setId(rs.getInt(1));
                list.add(form);
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        conn.close();
        return list;
    }

    public int activate(String account){
        int ret = -1;
        try{
            String sql = "update tb_member set activate = 0 where account = '"+account+ "'" ;
            ret = conn.executeUpdate(sql);
        }catch (Exception e){
            e.printStackTrace();
            ret = 0;
        }
        conn.close();
        return ret;
    }

    public boolean is_activate(String account){
        int ret=-1;
        try{
            String sql = "select * from tb_member where account = '"+account+"'";
            ResultSet rs = conn.executeQuery(sql);
            while(rs.next()){
                ret = rs.getInt("activate");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        conn.close();
        if(ret==1){
            return false;
        }
        else{
            return true;
        }
    }
}
