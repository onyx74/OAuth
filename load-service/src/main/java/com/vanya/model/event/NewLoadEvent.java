package com.vanya.model.event;

import com.vanya.model.entity.LoadEntity;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

@Data
public class NewLoadEvent extends ApplicationEvent {
    private LoadEntity loadEntity;

    public NewLoadEvent(LoadEntity loadEntity) {
        super(loadEntity);
        this.loadEntity = loadEntity;
    }
}
