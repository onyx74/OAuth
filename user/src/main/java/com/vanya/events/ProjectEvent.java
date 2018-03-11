package com.vanya.events;

import org.springframework.context.ApplicationEvent;

public class ProjectEvent extends ApplicationEvent {

    public ProjectEvent(Object source) {
        super(source);
    }
}
