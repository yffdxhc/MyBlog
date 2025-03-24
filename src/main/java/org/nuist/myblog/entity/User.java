package org.nuist.myblog.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String user_id;
    private String username;
    private String password;
    private String email;
    private String sex;
    private String avatar;
    private String bio;
    private String is_superuser;
    private Timestamp last_login;
    private Timestamp create_time;
    private Integer status;
}
