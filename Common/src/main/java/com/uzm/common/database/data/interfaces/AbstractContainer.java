package com.uzm.common.database.data.interfaces;

import com.uzm.common.database.data.DataContainer;

/**
 * A complete and upgradable plugin for <strong>any</strong> use for any project..
 *
 * @author JotaMPê (UzmStudio)
 * @version 2.0.5
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
