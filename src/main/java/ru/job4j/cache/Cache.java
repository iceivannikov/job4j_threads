package ru.job4j.cache;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class Cache {
    private final Map<Integer, Base> memory = new ConcurrentHashMap<>();

    public boolean add(Base model) {
        return memory.putIfAbsent(model.id(), model) == null;
    }

    public boolean update(Base model) throws OptimisticException {
        Base base = memory.get(model.id());
        if (Objects.isNull(base)) {
            throw new OptimisticException("Object with id " + model.id() + " not found");
        }
        int expectedVersion = model.version();
        Base updated = memory.computeIfPresent(model.id(), (id, existing) -> {
            if (expectedVersion == existing.version()) {
                return new Base(existing.id(), model.name(), existing.version() + 1);
            }
            return null;
        });
        if (Objects.isNull(updated)) {
            throw new OptimisticException("Version conflict for id " + model.id());
        }
        return true;
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
