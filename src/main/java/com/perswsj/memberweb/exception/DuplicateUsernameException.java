package com.perswsj.memberweb.exception;

/**
 * 用户名唯一约束冲突。单独定义成异常，让控制层能把它与其他 SQL 失败区分开，
 * 不至于把"用户名已存在"误报成"新增失败"。
 */
public class DuplicateUsernameException extends RuntimeException {

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
