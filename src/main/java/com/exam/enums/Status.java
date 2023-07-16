package com.exam.enums;

public enum Status {

    SUCCESS ("SUCCESS"),

    /* There was an error while processing the request */
    ERROR ("ERROR"),

    /* Failed to process the request */
    FAILURE ("FAILURE");

    private String value;

    public String getValue() {
        return value;
    }
    Status(String value){
        this.value = value;
    }
}
