package ru.job4j.cache;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class Cache {
    private final Map<Integer, Base> memory = new ConcurrentHashMap<>();

    public boolean add(Base model) {
        return memory.putIfAbsent(model.id(), model) == null;
    }

    public boolean update(Base model) throws OptimisticException {
        AtomicReference<OptimisticException> exception = new AtomicReference<>();
        boolean result;
        result = memory.computeIfPresent(model.id(), (existModel, existValue) -> {
            Base returnModel = new Base(model.id(), model.name(), model.version() + 1);
            if (existValue.version() != model.version()) {
                exception.set(new OptimisticException("Versions are not equal"));
                returnModel = existValue;
            }

            return returnModel;
        }) != null;

        if (exception.get() != null) {
            throw exception.get();
        }

        return result;
    }

    public void delete(int id) {
        memory.remove(id);
    }

    public Optional<Base> findById(int id) {
        return Stream.of(memory.get(id))
                .filter(Objects::nonNull)
                .findFirst();
    }
}