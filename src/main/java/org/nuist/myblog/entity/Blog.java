package org.nuist.myblog.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Blog implements Serializable {
    private String blog_id;
    private String blog_title;
    private String blog_content;
    private String blog_summary;
    private String user_number;
    private String username;
    private String user_avatar;
    private Integer type_id;
    private Integer blog_status;
    private Timestamp create_time;
    private Timestamp update_time;
    private String cover_image;
}
