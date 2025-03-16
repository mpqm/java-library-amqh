package com.amqh.amqh;


import java.io.Serializable;

public class MessageDto implements Serializable {

    private String destination;
    private String title;
    private String content;

    public MessageDto() { }

    public MessageDto(String destination, String title, String content) {
        this.destination = destination;
        this.title = title;
        this.content = content;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}