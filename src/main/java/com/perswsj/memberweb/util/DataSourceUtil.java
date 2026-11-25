package com.perswsj.memberweb.util;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据源工具类：统一从 Tomcat 连接池获取数据库连接。
 * <p>
 * 连接池由 {@code META-INF/context.xml} 里的 {@code jdbc/memberDS} 资源提供，
 * 连接由连接池创建和回收，调用方 {@code close()} 即为归还连接。
 */
public final class DataSourceUtil {

    /** 与 META-INF/context.xml 中 Resource 的 name 对应 */
    private static final String JNDI_NAME = "java:comp/env/jdbc/memberDS";

    /** DataSource 线程安全，全应用共享一份即可 */
    private static final DataSource DATA_SOURCE;

    static {
        try {
            Object found = new InitialContext().lookup(JNDI_NAME);
            if (!(found instanceof DataSource dataSource)) {
                throw new IllegalStateException("JNDI 名称 " + JNDI_NAME + " 查到的对象不是 DataSource，实际类型："
                        + (found == null ? "null" : found.getClass().getName()));
            }
            DATA_SOURCE = dataSource;
        } catch (NamingException e) {
            throw new IllegalStateException(
                    "数据源查找失败：" + JNDI_NAME
                            + "。请检查 META-INF/context.xml 是否已正确配置该 Resource，"
                            + "并确认它随项目一起部署；同时确认 MySQL 驱动已放入 Tomcat 的 lib 目录。", e);
        }
    }

    private DataSourceUtil() {
    }

    /**
     * 从连接池取一个连接。配合 try-with-resources 使用，关闭即归还连接池。
     */
    public static Connection getConnection() throws SQLException {
        return DATA_SOURCE.getConnection();
    }
}
