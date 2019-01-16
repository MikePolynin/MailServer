package com.itmo.air.mysql.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.data.domain.Page;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "accounts",
        "errors"
})

public class GettingAccounts {

    @JsonProperty("accounts")
    private Page<Account> accounts;
    @JsonProperty("errors")
    private String errors;

    @JsonProperty("accounts")
    public Page<Account> setAccounts() {
        return accounts;
    }

    @JsonProperty("accounts")
    public void setAccounts(Page<Account> accounts) {
        this.accounts = accounts;
    }

    @JsonProperty("errors")
    public String getErrors() {
        return errors;
    }

    @JsonProperty("errors")
    public void setErrors(String errors) {
        this.errors = errors;
    }
}