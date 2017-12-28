package com.galvanize;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class TodoList {

    private final List<Completable> completables = new ArrayList<>();

    public void add(Completable completable) {
        completables.add(completable);
    }

    public List<Completable> all() {
        return new ArrayList<>(completables);
    }

    public List<Completable> completed() {
        return completables.stream()
                .filter(Completable::isComplete)
                .collect(toList());
    }

    public List<Completable> uncompleted() {
        return completables.stream()
                .filter(completable -> !completable.isComplete())
                .collect(toList());
    }

    public void completeAll() {
        completables.forEach(Completable::markComplete);
    }

    public void uncompleteAll() {
        completables.forEach(Completable::markIncomplete);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        completables.forEach(completable -> {
            if (completable.isComplete()) {
                builder.append("√ ");
            } else {
                builder.append("□ ");
            }
            builder.append(completable.getTextToDisplay()).append("\n");
        });
        return builder.toString();
    }

}
