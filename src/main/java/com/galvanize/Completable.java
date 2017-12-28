package com.galvanize;

public interface Completable {

    String getTextToDisplay();

    void markComplete();

    void markIncomplete();

    boolean isComplete();

}
