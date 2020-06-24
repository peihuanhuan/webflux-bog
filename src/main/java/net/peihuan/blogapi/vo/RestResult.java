package net.peihuan.blogapi.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static net.peihuan.blogapi.vo.ResponseAPIStatus.SUCCESS;


@Getter
public final class RestResult implements Serializable {
    private int code;
    private String msg;
    private Map<String,Object> data;
    @JsonIgnore
    private ResponseAPIStatus status;

    @JsonIgnore
    public HttpStatus getHttpStatus() {
        return this.status.getHttpStatus();
    }

    public RestResult() {
        this.status = SUCCESS;
        this.code = this.status.getCode();
    }

    public RestResult(Map<String,Object> data) {
        status = SUCCESS;
        this.code = this.status.getCode();
        this.data = data;
    }

    public RestResult(String key, Object value) {
        status = SUCCESS;
        this.code = this.status.getCode();
        data = new HashMap<>();
        data.put(key,value);
    }

    public RestResult add(String key,Object value) {
        if (data == null) {
            data = new HashMap<>();
        }
        data.put(key,value);
        return this;
    }


    public RestResult(ResponseAPIStatus status, String msg) {
        this.status = status;
        this.code = status.getCode();
        this.msg = msg;
    }
}
