package com.project.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalTime;

@Service
public class EventService {

    private static final Logger LOG = LoggerFactory.getLogger(EventService.class);
    @Async
    public void sendEvents(SseEmitter emitter){
        for(int i=1; i<6; i++){
            try{
                SseEmitter.SseEventBuilder builder = SseEmitter.event().data("EVENT - "+ LocalTime.now())
                        .id(String.valueOf(i)).name("sseEvent");
                LOG.debug("Event {}", i);
                emitter.send(builder);
                Thread.sleep(1000);
            } catch (Exception e){
                LOG.error("Exception while sending event stream: ", e);
                emitter.completeWithError(e);
            }
        }
        emitter.complete();
    }
}
