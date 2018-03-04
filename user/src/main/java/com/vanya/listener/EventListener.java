package com.vanya.listener;

import com.vanya.events.OnRegistrationCompleteEvent;
import com.vanya.events.ProjectEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EventListener implements ApplicationListener<ProjectEvent> {

    @Autowired
    private UserEventsProcessor userEventsProcessor;


    @Override
    public void onApplicationEvent(ProjectEvent event) {
        if (event instanceof OnRegistrationCompleteEvent) {
            userEventsProcessor.processConfirmRegistrationEvent((OnRegistrationCompleteEvent) event);
//        } else if (event instanceof OnChangePasswordEvent) {
//            userEventsProcessor.processChangePasswordEvent((OnChangePasswordEvent) event);
        } else {
            throw new IllegalArgumentException(String.format("Unsupported event type:%s", event.getClass()));
        }

    }
}
