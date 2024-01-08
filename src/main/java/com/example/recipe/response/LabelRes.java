package com.example.recipe.response;

/**
 * Formats labels to return as wanted
 */
@SuppressWarnings("unused")
public class LabelRes {
    private String type;
    private int id;

    public LabelRes(String type, int id) {
        this.type = type;
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
