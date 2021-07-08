package com.uzm.common.java.types;

import lombok.Getter;

/**
 * A complete and upgradable plugin for <strong>any</strong> use for any project..
 *
 * @author JotaMPê (UzmStudio)
 * @version 2.0.5
 */

@Getter
public final class Pair<X, Y> {

    public Pair(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    private X x;
    private Y y;

}