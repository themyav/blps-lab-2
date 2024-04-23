package com.blps.lab3.util;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModeratorComment {
    @JsonProperty("textcomment")
    String textComment;

    public ModeratorComment(String textComment) {
        this.textComment = textComment;
    }

    public String getComment() {
        return textComment;
    }

    public void setComment(String comment) {
        this.textComment = comment;
    }
}
