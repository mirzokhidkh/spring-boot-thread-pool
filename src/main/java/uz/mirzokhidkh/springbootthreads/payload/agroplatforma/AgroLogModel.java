package uz.mirzokhidkh.springbootthreads.payload.agroplatforma;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgroLogModel {

    @JsonProperty("method_name")
    private String methodName;

    @JsonProperty("status_code")
    private Integer statusCode;

    @JsonProperty("msg")
    private String msg;

    @JsonProperty("request_v")
    private String requestV;

    @JsonProperty("response_v")
    private String responseV;

//  method_name NVARCHAR2(50) not null,
//  status_code NUMBER not null,
//  msg         NVARCHAR2(100) not null,
//  request_v   VARCHAR2(4000) not null,
//  response_v  VARCHAR2(4000) not null,

}