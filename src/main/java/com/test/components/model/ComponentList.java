package com.test.components.model;

import java.util.List;
import java.util.Objects;

public class ComponentList {

    List<Component> components;

    public List<Component> getComponents() {
        return components;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComponentList that = (ComponentList) o;
        return Objects.equals(components, that.components);
    }

    @Override
    public int hashCode() {

        return Objects.hash(components);
    }
}
