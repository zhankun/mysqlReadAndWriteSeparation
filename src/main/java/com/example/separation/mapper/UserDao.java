package com.example.separation.mapper;

import com.example.separation.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;


/**
 * Created by zhankun on 2018/11/13.
 */
@Repository
public interface UserDao {

    @Select("select * from user where id = #{id}")
    User findById(int id);

    @Insert("insert into user(name,sex,age,address) values (#{name},#{sex},#{age},#{address})")
    void insert(User user);

    @Update({
            "<script>"+
                    "update user <set> ",
                    "<trim suffixOverrides=\",\">"+
                    "<if test = 'name != null'> name = #{name},</if>"+
                    "<if test = 'sex != null'> sex = #{sex},</if>"+
                    "<if test = 'age != null'> age = #{age},</if>"+
                    "<if test = 'address != null'> address = #{address},</if>"+
                    "</trim>"+
                    "</set>"+
                    "where id = #{id}"+
                    "</script>"
    })
    int updateById(User user);
}
