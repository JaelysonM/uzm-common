package com.uzm.common.java.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;
import java.util.LinkedList;

/**
 * A complete and upgradable plugin for <strong>any</strong> use for any project..
 *
 * @author JotaMPê (UzmStudio)
 * @version 2.0.5
 */

@Getter
@AllArgsConstructor
public final class EvictingList<T> extends LinkedList<T> {


    private final int maxSize;

    /**
     * Constructor to EvictingList, your mechanic is a max sized list that
     * removes the first element if necessary.
     *
     * @param c       Class to extends.
     * @param maxSize Max list size.
     */

    public EvictingList(Collection<? extends T> c, int maxSize) {
        super(c);
        this.maxSize = maxSize;
    }

    /**
     * Makes the list mechanic algorithm.
     *
     * @param t Element to add do list
     * @return If can add element to list.
     */

    @Override
    public boolean add(T t) {
        if (size() >= getMaxSize()) removeFirst();
        return super.add(t);
    }

    /**
     * Check if the list is full.
     *
     * @return If list is full based on maxSize.
     */

    public boolean isFull() {
        return size() >= getMaxSize();
    }
}