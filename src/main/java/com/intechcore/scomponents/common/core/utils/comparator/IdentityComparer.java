package com.intechcore.scomponents.common.core.utils.comparator;

import java.util.Comparator;

/**
 * Created to compare class types between each other and such other objects ({@code Runnable}s) to use with appropriate {@code TreeMap}s
 * @param <TItem> type of the target objects to be compared
 */
public class IdentityComparer<TItem> implements Comparator<TItem> {
    @Override
    public int compare(TItem o1, TItem o2) {
        return Integer.compare(System.identityHashCode(o1), System.identityHashCode(o2));
    }
}
