package com.polytech.spik.controllers;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by momo- on 16/12/2015.
 */
public class ObservableMultiList<E> extends ObservableListBase<E> {

    private Set<ObservableList<E>> lists = new TreeSet<>();

    public void addList(ObservableList<E> list){
        if(lists.add(list)){
            list.addListener(new ListChangeListener<E>() {
                @Override
                public void onChanged(Change<? extends E> c) {
                    
                }
            });
        }
    }

    @Override
    public E get(int index) {
        if(index > size())
            throw new IndexOutOfBoundsException();

        int pos = 0;
        for(List<E> l : lists){
            if(pos + l.size() > index)
                return l.get(index - pos);

            pos += l.size();
        }

        return null;
    }

    @Override
    public int size() {
        return lists.parallelStream().mapToInt(List::size).sum();
    }
}
