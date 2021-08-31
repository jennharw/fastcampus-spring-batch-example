package com.example.spring.batch.part3;

import org.springframework.batch.item.ItemProcessor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class DuplicateValidationProcessor<T> implements ItemProcessor<T, T> {

    private final Map<String, Object> keyPool = new ConcurrentHashMap<>();
    private final Function<T, String> keyExtractor;
    private final boolean allowDuplicate;

    public DuplicateValidationProcessor(Function<T, String> keyExtractor, boolean allowDulicate) {
        this.keyExtractor = keyExtractor;
        this.allowDuplicate = allowDulicate;
    }

    @Override
    public T process(T item) throws Exception {
        if(allowDuplicate){
            return item;
        }

        String key = keyExtractor.apply(item);

        if (keyPool.containsKey(key)){
            return null;
        }
        keyPool.put(key, key);
        return item;
    }
}
