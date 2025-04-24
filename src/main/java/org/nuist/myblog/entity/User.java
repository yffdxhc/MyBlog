package org.nuist.myblog.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String user_number;
    private String user_id;
    private String password;
    private String username;
    private String email;
    private String sex;
    private String avatar;
    private String bio;
    private Integer follows;
    private Integer followers;
    private Integer visits;
    private Boolean is_superuser;
    private Timestamp last_login;
    private Timestamp created_at;
    private Integer status;
    private String verificationCode;
}
