package org.nuist.myblog.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {
    private final static long serialVersionUID = 1L;
    private boolean success;
    private String message;
    private T data;
}
