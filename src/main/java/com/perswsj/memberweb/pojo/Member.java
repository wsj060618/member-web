package com.perswsj.memberweb.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member implements Serializable {
    private int id;
    private String uname;
    /** 密码：对应 member 表的 password 列，新增会员时需要写入 */
    private String password;
    private String email;
    private String phone;
    private String sex;
    private String hobby;
    /** 备注：对应 member 表的 remark 列 */
    private String remark;

    public String getSexText() {
        return switch (sex == null ? "" : sex) {
            case "male" -> "男";
            case "female" -> "女";
            default -> sex;
        };
    }

    public String getHobbyText() {
        return switch (hobby == null ? "" : hobby) {
            case "reading" -> "阅读";
            case "music" -> "音乐";
            case "traveling" -> "旅行";
            case "sports", "sport" -> "运动";
            case "coding" -> "编程";
            default -> hobby;
        };
    }

}
