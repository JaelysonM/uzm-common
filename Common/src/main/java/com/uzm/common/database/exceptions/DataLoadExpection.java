package com.uzm.common.database.exceptions;

/**
 * A complete and upgradable plugin for <strong>any</strong> use for any project..
 *
 * @author JotaMPê (UzmStudio)
 * @version 2.0.6
 */

public class DataLoadExpection extends Exception {
    static final long serialVersionUID = 3287516992918834123L;

    public DataLoadExpection(String reason) {
        super(reason);
    }
}
