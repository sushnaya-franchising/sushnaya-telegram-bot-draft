package com.sushnaya.entity;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Entity {
    // todo: storage must generate id (and persist id-generator state in optimized version)
    private static AtomicInteger ID_GENERATOR = new AtomicInteger();

    private int id;

    public Entity() {
        this.id = ID_GENERATOR.incrementAndGet();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdAsHexString() {
        return Integer.toHexString(getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entity)) return false;
        Entity entity = (Entity) o;
        return getId() == entity.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}

