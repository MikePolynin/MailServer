package com.itmo.air.mysql.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "messages",
        "errors"
})

public class GettingMessages {

    @JsonProperty("messages")
    private Set<Message> messages;
    @JsonProperty("errors")
    private String errors;

    @JsonProperty("messages")
    public Set<Message> setMessages() {
        return messages;
    }

    @JsonProperty("messages")
    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    @JsonProperty("errors")
    public String isErrors() {
        return errors;
    }

    @JsonProperty("errors")
    public void setErrors(String errors) {
        this.errors = errors;
    }
}