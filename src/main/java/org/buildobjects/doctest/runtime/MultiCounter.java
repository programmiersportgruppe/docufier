package org.buildobjects.doctest.runtime;

import java.util.HashMap;
import java.util.Map;

public class MultiCounter {
    private Map<String, Integer> counters = new HashMap<>();

    public synchronized int nextNumber(String counterId) {
        if (counters.containsKey(counterId)) {
            int currentValue = counters.get(counterId);
            counters.put(counterId, currentValue + 1);
            return currentValue;
        } else {
            counters.put(counterId, 1);
            return 0;
        }
    }

}
