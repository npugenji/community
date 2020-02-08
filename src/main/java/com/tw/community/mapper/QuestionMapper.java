package com.tw.community.mapper;

import com.tw.community.dto.QuestionDTO;
import com.tw.community.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {

    @Insert("insert into question(title, description, gmt_create, gmt_modified, creator, tag) values( #{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag} )")
    void create(Question question);

    @Select("select * from question limit #{offset}, #{size}")
    List<Question> list(@Param("offset") Integer offset, @Param("size") Integer size);

    @Select("select count(1) from question")
    Integer getCount();

    @Select("select count(1) from question where creator = #{accountId}")
    Integer getCountByAccountId(@Param("accountId") String accountId);

    @Select("select * from question where creator = #{accountId} limit #{offset}, #{size}")
    List<Question> listByAccountId(@Param("accountId") String accountId, @Param("offset") Integer offset, @Param("size") Integer size);

    @Select("select * from question where id = #{id}")
    Question getById(@Param("id") Integer id);

    @Update("update question set title = #{title}, description = #{description}, gmt_modified = #{gmtModified} where id = #{id}")
    void update(Question question);
}
