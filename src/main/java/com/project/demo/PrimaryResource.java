package com.project.demo;

import org.apache.coyote.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@CrossOrigin(origins = "http://localhost:4200/")
@RestController
@RequestMapping("/api")
public class PrimaryResource {

    private ExecutorService nonBlockingService = Executors.newCachedThreadPool();
    @GetMapping("/test")
    public ResponseEntity<String> getResponse(){
        System.out.println("Test api invoked");
        return ResponseEntity.ok("{\"data\": \"Test Successful\"}");
    }

    @GetMapping("/event-stream")
    public ResponseEntity<SseEmitter> getStreamingResponses(){
        System.out.println("inside stream");
        SseEmitter emitter = new SseEmitter();
        nonBlockingService.execute(() -> {
            for(int i=1; i<6; i++){
                try{
                    SseEmitter.SseEventBuilder builder = SseEmitter.event().data("EVENT - "+ LocalTime.now().toString())
                                    .id(String.valueOf(i)).name("sseEvent");
                    System.out.println("Event"+i);
                    emitter.send(builder);
                    Thread.sleep(1000);
                } catch (Exception e){
                    System.out.println(e);
                    emitter.completeWithError(e);
                }
            }
            emitter.complete();
        });
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "text/event-stream");
        return ResponseEntity.ok().headers(headers).body(emitter);
    }

}
