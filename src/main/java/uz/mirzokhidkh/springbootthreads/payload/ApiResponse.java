package uz.mirzokhidkh.springbootthreads.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse {

    private String msg;

    private int code;

    private Object obj;


    public ApiResponse(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }

    public ApiResponse(String msg, Object obj) {
        this.msg = msg;
        this.obj = obj;
    }

    public ApiResponse(String msg) {
        this.msg = msg;
    }
}
