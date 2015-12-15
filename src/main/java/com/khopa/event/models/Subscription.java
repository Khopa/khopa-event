package com.khopa.event.models;

import lombok.Data;

import java.lang.reflect.Method;

@Data
public class Subscription {
    private Object subscriber;
    private Method consumer;
    private int maxConsumption;
    private int priority;
}
