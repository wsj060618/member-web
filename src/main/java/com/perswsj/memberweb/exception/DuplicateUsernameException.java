package com.perswsj.memberweb.exception;

/**
 * 用户名唯一约束冲突（MySQL 错误码 1062）。
 * <p>
 * 单独定义成运行时异常，让控制层可以精确捕获「用户名已存在」这一种业务失败，
 * 而与其他 SQL 异常（连接失败、语法错误等）区分开来，避免把唯一约束冲突误报成"新增失败"。
 */
public class DuplicateUsernameException extends RuntimeException {

    /** MySQL 唯一索引冲突的错误码 */
    public static final int MYSQL_DUPLICATE_ENTRY = 1062;

    private final String username;

    public DuplicateUsernameException(String username, Throwable cause) {
        super("用户名已存在：" + username, cause);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
