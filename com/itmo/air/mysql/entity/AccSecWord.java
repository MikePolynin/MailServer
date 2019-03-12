package com.itmo.air.mysql.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "nicName",
        "secureWord",
        "errors"
})

public class AccSecWord {

    @JsonProperty("nicName")
    private String nicName;

    public String getNicName() {
        return nicName;
    }

    public void setNicName(String nicName) {
        this.nicName = nicName;
    }

    public String getSecureWord() {
        return secureWord;
    }

    public void setSecureWord(String secureWord) {
        this.secureWord = secureWord;
    }

    @JsonProperty("secureWord")
    private String secureWord;

}
