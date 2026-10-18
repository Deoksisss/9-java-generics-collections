package com.example.task02;

import java.io.*;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class SavedList<E extends Serializable> extends AbstractList<E> {

    private final File file;
    private List<E> memoryList;

    @SuppressWarnings("unchecked")
    public SavedList(File file) {
        this.file = file;

        if (file != null && file.exists() && file.length() > 0) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                this.memoryList = (List<E>) ois.readObject();
            } catch (Exception e) {
                this.memoryList = new ArrayList<>();
            }
        } else {
            this.memoryList = new ArrayList<>();
        }
    }

    private void save() {
        if (file == null) return;
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(memoryList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public E get(int index) {
        return memoryList.get(index);
    }

    @Override
    public int size() {
        return memoryList.size();
    }

    @Override
    public E set(int index, E element) {
        E old = memoryList.set(index, element);
        save();
        return old;
    }

    @Override
    public void add(int index, E element) {
        memoryList.add(index, element);
        save();
    }

    @Override
    public E remove(int index) {
        E removed = memoryList.remove(index);
        save();
        return removed;
    }
}