package com.cloud.filesystem.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserTicket {
    private int userId;
    private String userTitle;
    private UUID ticket;    //UUID uuid = UUID.randomUUID();
    private LocalDateTime RetrievalDateTime;

    public UserTicket() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserTitle() {
        return userTitle;
    }

    public void setUserTitle(String userTitle) {
        this.userTitle = userTitle;
    }

    public UUID getTicket() {
        return ticket;
    }

    public void setTicket(UUID ticket) {
        this.ticket = ticket;
    }

    public LocalDateTime getRetrievalDateTime() {
        return RetrievalDateTime;
    }

    public void setRetrievalDateTime(LocalDateTime retrievalDateTime) {
        RetrievalDateTime = retrievalDateTime;
    }
}