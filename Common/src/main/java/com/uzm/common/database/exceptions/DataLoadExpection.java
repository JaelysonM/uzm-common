package com.uzm.common.database.exceptions;

public class DataLoadExpection extends Exception {
    static final long serialVersionUID = 3287516992918834123L;
    public DataLoadExpection(String reason) {
        super(reason);
    }
}
