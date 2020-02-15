package com.itheima.dao;

import com.itheima.pojo.Member;

public interface MemberDao {
    Member findByTelephone(String telephone);

    void add(Member member);

    Integer findMemberCountBeforeDate(String s);

    Integer findMemberCountByDate(String today);

    Integer findMemberCount();

    Integer findMemberCountAfterDate(String thisWeekMonday);
}
