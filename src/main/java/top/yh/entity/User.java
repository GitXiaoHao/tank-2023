package top.yh.entity;


import java.io.Serializable;
import lombok.Data;

/**
 * 用户信息
 * @TableName t_user
 */
@Data
public class User implements Serializable {
    /**
     * 用户表主键
     */

    private Long uid;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 真实姓名
     */
    private String name;

    /**
     * 性别 1:男 2:女
     */
    private String sex;

    /**
     * 手机号
     */
    private String telephone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 状态 0:禁用，1:正常
     */
    private Integer status;

    /**
     * 版本
     */
    private Integer version;

    /**
     * 是否删除
     */
    private Integer isDeleted;

    /**
     * 修改外键
     */
    private Long updateId;
}