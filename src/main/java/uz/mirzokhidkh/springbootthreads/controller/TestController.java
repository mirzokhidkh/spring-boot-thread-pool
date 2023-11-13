package uz.mirzokhidkh.springbootthreads.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;

@RestController
public class TestController {
    @Value("${test.data.json.path}")
    private String testDataJsonPath;

    @PostMapping(value = "/receive",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> receiver(@RequestBody Object obj) {
        return ResponseEntity.status(200).body(obj);
    }

    @GetMapping(value = "/getData",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getData(@RequestParam String id) {
        Object mockDataObj = readJsonFile(testDataJsonPath);
        return ResponseEntity.status(200).body(mockDataObj);
    }

    public static void main(String[] args) {
//        System.out.println(readJsonFile(testDataJsonPath));
//        System.out.println(readJsonFile("C:\\Users\\User\\IdeaProjects\\fido-biznes-esbs\\test-api\\esb-test-api\\esb-test-api\\src\\main\\resources\\mock\\response.json"));
    }

    public static Object readJsonFile(String path) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Object data = mapper.readValue(new File(path), Object.class);
            return data;
            // process the data here
        } catch (IOException e) {
            // handle the exception here
            throw new RuntimeException(e);
        }
    }

}
