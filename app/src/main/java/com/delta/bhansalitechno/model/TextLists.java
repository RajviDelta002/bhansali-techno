package com.delta.bhansalitechno.model;

public class TextLists {

    private final String textListId;
    private final String textList;

    public TextLists(String textListId, String textList) {
        this.textListId = textListId;
        this.textList = textList;
    }

    public String getTextListId() {
        return textListId;
    }

    public String getTextList() {
        return textList;
    }

}
