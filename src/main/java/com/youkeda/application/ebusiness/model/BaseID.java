package com.youkeda.application.ebusiness.model;

import org.springframework.core.serializer.Serializer;
import org.yaml.snakeyaml.events.Event;
import tools.jackson.core.ObjectReadContext;

import java.io.Serializable;

public class BaseID <T>implements Serializable {

    private T  id;

    public BaseID() {}

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }


}
