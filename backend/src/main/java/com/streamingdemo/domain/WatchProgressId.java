package com.streamingdemo.domain;

import java.io.Serializable;
import java.util.Objects;

public class WatchProgressId implements Serializable {

    private String userId;
    private String titleId;

    public WatchProgressId() {
    }

    public WatchProgressId(String userId, String titleId) {
        this.userId = userId;
        this.titleId = titleId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WatchProgressId that)) {
            return false;
        }
        return Objects.equals(userId, that.userId) && Objects.equals(titleId, that.titleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, titleId);
    }
}
