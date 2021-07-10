package com.uzm.common.managers;

import com.uzm.common.plugin.Common;
import org.bukkit.Bukkit;

import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A complete and updatable plugin for any usages.
 *
 * @author JotaMPê (UzmStudio)
 * @version 2.0.6
 */

public class TaskManager {

    public static <T> Consumer<T> syncConsume(final Consumer<T> consumer) {
        return (element) -> Bukkit.getScheduler().runTask(Common.getInstance(), () -> consumer.accept(element));
    }

    public static <T> Supplier<T> supplySync(final Supplier<T> supplier) {
        return () -> {
            try {
                return Bukkit.getScheduler().callSyncMethod(Common.getInstance(), supplier::get).get();
            } catch (final InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return null;
            }
        };
    }

}