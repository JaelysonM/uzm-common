package com.uzm.common.database.data.interfaces;

import com.uzm.common.database.data.DataContainer;

/**
 * @author Maxter
 */
public abstract class AbstractContainer {

    protected DataContainer dataContainer;

    public AbstractContainer(DataContainer dataContainer) {
        this.dataContainer = dataContainer;
    }

    public void gc() {
        this.dataContainer = null;
    }
}
