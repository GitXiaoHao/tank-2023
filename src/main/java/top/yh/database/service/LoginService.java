package top.yh.database.service;

import top.yh.database.dao.UserDao;
import top.yh.database.entity.User;

/**
 * @author yuhao
 * @date 2023/2/7
 **/
public class LoginService {
    private final UserDao DAO = new UserDao();

    /**判断是否有该用户
     * 如果有该用户 则返回true
     * 如果没有则返回false
     * */
    public boolean ifUser(String name, String password) {
        String sql = "select * from user where name = ? and password = md5(?)";
        User user = DAO.getQuerySingle(sql, User.class, name, password);
        return user != null;
    }
}
