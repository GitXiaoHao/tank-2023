package top.yh.database.service;


import top.yh.database.dao.UserDao;
import top.yh.database.entity.User;

/**
 * @author yuhao
 */
public class RegisterService {
    private final UserDao DAO = new UserDao();
    /**添加用户*/
    public boolean addUser(String name , String password){
        String sql = "insert into user values(null,?,md5(?))";
        return DAO.dml(sql, name,password);
    }
    /**判断是否有该用户
     * 如果有 就返回true
     * 没有就返回false
     * */
    public boolean ifUser(String name){
        String sql = "select * from `user` where name = ?";
        User user = DAO.getQuerySingle(sql, User.class,name);
        return user != null;
    }
}
