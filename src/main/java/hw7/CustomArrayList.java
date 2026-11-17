package hw7;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class CustomArrayList<E> implements CustomList<E> {
    private Object[] elements = new Object[10];
    private int size = 0;

    @Override
    public void add(E element) {
        if (element == null) {
            throw new NullPointerException();
        }
        if (size == elements.length) {
            int newCapacity = elements.length + Math.max(1, elements.length / 2);
            elements = Arrays.copyOf(elements, newCapacity);
        }
        elements[size] = element;
        size++;
    }

    @SuppressWarnings("unchecked")
    @Override
    public E get(int index) {
        checkIndex(index);
        return (E) elements[index];
    }

    @SuppressWarnings("unchecked")
    @Override
    public E remove(int index) {
        checkIndex(index);
        E removed = (E) elements[index];
        System.arraycopy(elements, index + 1, elements, index, size - index - 1);
        size--;
        elements[size] = null;
        return removed;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", size: " + size);
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return (E) elements[index++];
            }
        };
    }
}