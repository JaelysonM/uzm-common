package com.uzm.common.containers;

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
