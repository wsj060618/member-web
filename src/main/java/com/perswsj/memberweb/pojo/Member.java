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
    private String email;
    private String phone;
    private String sex;
    private String hobby;

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
            case "sports" -> "运动";
            case "coding" -> "编程";
            default -> hobby;
        };
    }

}
