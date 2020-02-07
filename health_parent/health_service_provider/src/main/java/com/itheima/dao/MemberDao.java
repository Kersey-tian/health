package com.itheima.dao;

import com.itheima.pojo.Member;

public interface MemberDao {
    Member findByTelephone(String telephone);

    void add(Member member);
}
