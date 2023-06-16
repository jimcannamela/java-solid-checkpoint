package com.galvanize;

public interface Completable extends Displayable {

    void markComplete();

    void markIncomplete();

    boolean isComplete();

}
