package com.uzm.common.database.cache;

import com.google.common.collect.ImmutableList;
import com.uzm.common.reflections.Accessors;
import lombok.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Getter
@Setter
@ToString(includeFieldNames = true)
@AllArgsConstructor
@Data(staticConstructor = "of")
public abstract class CacheHandler {


    public abstract void gc();

    public abstract void save(boolean async);


    public abstract void load();


    /**
     * A cache handler based on a {@param containerClass} and a  {@param key} where you can optionally pass parameters
     *
     * @param key            Unique value for that container context.
     * @param containerClass Extended container class.
     * @param params         Default parameters.
     * @return {@link CompletableFuture<T>} fetched from  {@param key} in context of {@param containerClass}
     */

    @SuppressWarnings("unchecked")
    public static <T extends CacheHandler> CompletableFuture<T> getCache(@NonNull String key, @NonNull Class<T> containerClass, Object... params) {
        return CompletableFuture.supplyAsync(() -> {
            if (!CACHE.containsKey(containerClass)) {
                CACHE.put(containerClass, new HashMap<>());
            }
            if (!CACHE.get(containerClass).containsKey(key)) {
                if (params.length > 0)
                    CACHE.get(containerClass).put(key, Accessors.getConstructor(containerClass, containerClass.getConstructors()[0].getParameterTypes()).newInstance(params));
            }
            return (T) CACHE.get(containerClass).get(key);
        });
    }

    /**
     * Destroy {@param containerClass} cache context from {@param key} unique.
     *
     * @param key            Unique value for that container context.
     * @param containerClass Extended container class
     */

    public static <T extends CacheHandler> void destroy(@NonNull String key, @NonNull Class<T> containerClass) {
        if (!CACHE.containsKey(containerClass)) {
            CACHE.put(containerClass, new HashMap<>());
        }
        Map<String, CacheHandler> container = CACHE.get(containerClass);
        if (container.containsKey(key))
            container.get(key).gc();
        CACHE.get(containerClass).remove(key);
    }

    /**
     * Clear a cache contexto from {@param containerClass} .
     */

    public static <T extends CacheHandler> void clear(@NonNull Class<T> containerClass) {
        if (!CACHE.containsKey(containerClass)) {
            CACHE.put(containerClass, new HashMap<>());
        }
        CACHE.get(containerClass).values().forEach(CacheHandler::gc);
        CACHE.get(containerClass).clear();
    }

    /**
     * Returns a collection from {@param containerClass} context cache.
     *
     * @param containerClass Extended container class
     * @return {@link Collection<T>} in context of {@param containerClass}
     */

    @SuppressWarnings("unchecked")
    public static <T extends CacheHandler> Collection<T> collections(@NonNull Class<T> containerClass) {
        if (!CACHE.containsKey(containerClass)) {
            CACHE.put(containerClass, new HashMap<>());
        }
        return (Collection<T>) ImmutableList.copyOf(CACHE.get(containerClass).values());
    }


    private static Map<Class<? extends CacheHandler>, Map<String, CacheHandler>> CACHE = new HashMap<>();
}
