package top.yh.database.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yuhao
 * @date 2023/2/7
 * 用户表
 * 与user表映射
 * <p>
 * id int primary key Auto_increment,# id 自增
 * <p>
 * password char(32) not null default '12345',# 5位 默认12345  md5
 * <p>
 * name varchar(10) not null default 'yu'# 姓名
 **/
@Data
public class User implements Serializable {
    private Integer id;
    private String password;
    private String name;
}
