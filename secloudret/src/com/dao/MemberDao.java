package com.dao;

import com.model.Member;

import java.util.List;

public interface MemberDao {
    public int insert(Member m);
    public List select();

}
