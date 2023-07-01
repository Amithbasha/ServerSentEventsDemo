package com.project.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
@CrossOrigin(origins = "http://localhost:4200/")
@RestController
@RequestMapping("/api")
public class PrimaryResource {

    private static final Logger LOG = LoggerFactory.getLogger(PrimaryResource.class);
    @Autowired
    private EventService eventService;
    @GetMapping("/test")
    public ResponseEntity<String> getResponse(){
        LOG.debug("Test method invoked");
        return ResponseEntity.ok("{\"data\": \"Test Successful\"}");
    }

    @GetMapping("/event-stream")
    public ResponseEntity<SseEmitter> getStreamingResponses(){
        LOG.debug("SSE Emitter events");
        SseEmitter emitter = new SseEmitter();
        eventService.sendEvents(emitter);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "text/event-stream");
        return ResponseEntity.ok().headers(headers).body(emitter);
    }

}
