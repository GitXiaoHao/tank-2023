package top.yh.database.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import lombok.SneakyThrows;

import javax.sql.DataSource;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;

/**
 * @author yuhao
 * @date 2023/2/7
 * druid连接
 **/
public class JdbcByDruid {
    /**
     * 创建一个ThreadLocal对象，用当前线程作为key
     */
    private static final ThreadLocal<Connection> TC;

    private static DataSource dataSource;

    static {
        TC = new ThreadLocal<>();
        //获取链接
        Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(Objects.requireNonNull(JdbcByDruid.class.getResourceAsStream("../druid.properties"))));
            System.out.println(properties.get("url"));
            dataSource = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 开启事务
     */
    public static void startTransaction() {
        try {
            // 获取连接
            Connection conn = getConnection();
            /// 开启事务
            /*
             * setAutoCommit总的来说就是保持数据的完整性，一个系统的更新操作可能要涉及多张表，需多个SQL语句进行操作
             * 循环里连续的进行插入操作，如果你在开始时设置了：conn.setAutoCommit(false);
             * 最后才进行conn.commit(),这样你即使插入的时候报错，修改的内容也不会提交到数据库，
             */
            conn.setAutoCommit(false);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void commit() {
        try {
            Connection conn = TC.get();
            if (conn != null) {
                conn.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void rollback() {
        try {
            // 从集合tc中得到一个连接
            Connection conn = TC.get();
            if (conn != null) {
                // 该方法用于取消在当前事务中进行的更改，并释放当前Connection对象持有的所有数据库锁。此方法只有在手动事务模式下才可用
                conn.rollback();
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 获取连接
     */
    @SneakyThrows
    public static Connection getConnection() {
        Connection connection = TC.get();
        if (connection == null) {
            connection = dataSource.getConnection();
            TC.set(connection);
        }
        return connection;
    }

    public static void close() {
        //判断是否为空
        try {
            Connection connection = TC.get();
            if(connection != null) {
                connection.close();
            }
            TC.remove();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
