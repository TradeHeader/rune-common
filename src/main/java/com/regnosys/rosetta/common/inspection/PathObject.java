package com.regnosys.rosetta.common.inspection;

import java.util.*;
import java.util.stream.Collectors;

public class PathObject<T> {

    private final LinkedList<Element<T>> elements;

    PathObject(String accessor, T t) {
        LinkedList<Element<T>> newElements = new LinkedList<>();
        newElements.add(new Element<>(accessor, t));
        this.elements = newElements;
    }

    public PathObject(PathObject<T> parent, String accessor, T t) {
        LinkedList<Element<T>> newElements = new LinkedList<>();
        newElements.addAll(parent.elements);
        newElements.add(new Element<>(accessor, t));
        this.elements = newElements;
    }

    public PathObject(PathObject<T> parent, String accessor, int index, T t) {
        LinkedList<Element<T>> newElements = new LinkedList<>();
        newElements.addAll(parent.elements);
        newElements.add(new Element<>(accessor, index, t));
        this.elements = newElements;
    }

    public String buildPath() {
        List<String> path = getPath();
        // remove root element, so it starts with the first accessor
        path.remove(0);
        return String.join(".", path);
    }

    public String fullPath() {
        return String.join(".", getPath());
    }

    public List<String> getPath() {
        return elements.stream()
                .map(e -> e.getAccessor() + (e.getIndex().map(i -> "(" + i + ")").orElse("")))
                .collect(Collectors.toList());
    }

    public LinkedList<T> getPathObjects() {
        return elements.stream().map(Element::getObject).collect(Collectors.toCollection(LinkedList::new));
    }

    public T getObject() {
        return elements.getLast().getObject();
    }

    @Override
    public String toString() {
        return "PathObject{" +
                "elements=" + elements +
                '}';
    }

    private static class Element<T> {
        private final String accessor;
        private final Optional<Integer> index;
        private final T object;

        Element(String accessor, T object) {
            this.accessor = accessor;
            this.index = Optional.empty();
            this.object = object;
        }

        Element(String accessor, int index, T object) {
            this.accessor = accessor;
            this.index = Optional.of(index);
            this.object = object;
        }

        String getAccessor() {
            return accessor;
        }

        Optional<Integer> getIndex() {
            return index;
        }

        T getObject() {
            return object;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Element<?> element = (Element<?>) o;
            return Objects.equals(accessor, element.accessor) &&
                    Objects.equals(index, element.index) &&
                    Objects.equals(object, element.object);
        }

        @Override
        public int hashCode() {
            return Objects.hash(accessor, index, object);
        }

        @Override
        public String toString() {
            return "Element{" +
                    "accessor='" + accessor + '\'' +
                    ", index=" + index +
                    ", object=" + object +
                    '}';
        }
    }
}
