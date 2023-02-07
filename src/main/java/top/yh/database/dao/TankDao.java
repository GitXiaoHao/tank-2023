package top.yh.database.dao;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import top.yh.database.utils.JdbcByDruid;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author yuhao
 * @date 2023/2/7
 **/
public class TankDao<T> {
    private QueryRunner queryRunner;

    public TankDao() {
        queryRunner = new QueryRunner();
    }

    /**
     * dml语句
     * 增删改
     */
    public boolean dml(String sql, Object... params) {
        //获取连接
        Connection connection = JdbcByDruid.getConnection();
        //开启事务
        JdbcByDruid.startTransaction();
        //dml
        try {
            queryRunner.update(connection, sql, params);
        } catch (SQLException e) {
            e.printStackTrace();
            JdbcByDruid.rollback();
            return false;
        }
        JdbcByDruid.commit();
        return true;
    }

    /**
     * 查询语句
     */
    public T getQuerySingle(String sql, Class<T> tClass, Object... params) {
        Connection connection = JdbcByDruid.getConnection();
        try {
            return queryRunner.query(connection, sql, new BeanHandler<T>(tClass), params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
