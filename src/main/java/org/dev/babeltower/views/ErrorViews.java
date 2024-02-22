package org.dev.babeltower.views;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ErrorViews {

    CASTING_FAIL("Error converting document to %s: ");

    private String messageFormat;

    public void printWith(Object... args) {
        System.err.println(String.format(messageFormat, args));
    }
}
