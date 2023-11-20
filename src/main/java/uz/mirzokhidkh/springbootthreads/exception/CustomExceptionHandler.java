package uz.mirzokhidkh.springbootthreads.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import uz.mirzokhidkh.springbootthreads.payload.ApiResponse;


@ControllerAdvice
@RestController
public class CustomExceptionHandler  extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);


    @ExceptionHandler(Exception.class)
    public final ResponseEntity<?> handleAllExceptions(Exception ex, WebRequest request) {
        ApiResponse result = new ApiResponse();
        result.setCode(1);
        result.setMsg("" + ex.getMessage() + " Request:" + request.getDescription(false));
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(WebClientResponseException.class)
    public final ResponseEntity<?> handleWebClientResponseException(WebClientResponseException ex, WebRequest request) throws JsonProcessingException {
        ApiResponse result = new ApiResponse();
        result.setCode(2);
        ObjectMapper objectMapper = new ObjectMapper();
        result.setMsg(ex.getMessage());
//        byte[] responseBodyAsByteArray = ex.getResponseBodyAsByteArray();
//        String s = new String(responseBodyAsByteArray, StandardCharsets.UTF_8);
        String s = ex.getResponseBodyAsString();
        Object object = objectMapper.readValue(s, Object.class);
        result.setObj(object);

        /*please read
        https://stackoverflow.com/questions/43502332/how-to-get-the-requestbody-in-an-exceptionhandler-spring-rest
        * */
//        return new ResponseEntity<>(result, HttpStatus.valueOf(ex.getRawStatusCode()));
        return ResponseEntity.status(ex.getRawStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);
    }


}
