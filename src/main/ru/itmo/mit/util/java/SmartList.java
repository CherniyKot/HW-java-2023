package ru.itmo.mit.util.java;

import java.util.*;

@SuppressWarnings("unused")
public class SmartList<T> extends AbstractList<T> implements List<T> {
    private final int SOO_SIZE=5;
    private int size=0;
    private Object storage=null;

    public SmartList(){}

    @SuppressWarnings("unchecked")
    public SmartList(Collection<T> collection){
        size=collection.size();
        if(size==0){return;}
        var arr =(T[]) collection.toArray();
        if(size==1){
            storage=arr[0];
            return;
        }
        if(size<=SOO_SIZE){
            storage=arr;
            return;
        }
        storage= new ArrayList<>(List.of(arr));
    }

    @SuppressWarnings("unchecked")
    @Override
    public T get(int index) {
        if(index<0 || index>=size){
            throw new IndexOutOfBoundsException();
        }
        if(size == 1){
            return (T)storage;
        }
        if(size<=SOO_SIZE){
            return ((T[])storage)[index];
        }
        return ((ArrayList<T>) storage).get(index);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T set(int index, T element) {
        if(index<0 || index>=size){
            throw new IndexOutOfBoundsException();
        }
        if(size==1){
            T t = (T) storage;
            storage = element;
            return t;
        }
        if(size<=SOO_SIZE){
            var arr = (T[])storage;
            T t = arr[index];
            arr[index]=element;
            return t;
        }
        return ((ArrayList<T>)storage).set(index,element);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void add(int index, T element) {
        if(index<0 || index>size){
            throw new IndexOutOfBoundsException();
        }
        size++;
        if(size==1){
            storage = element;
            return;
        }
        if(size==2){
            T t = (T) storage;
            T[] arr = (T[]) new Object[SOO_SIZE];
            storage = arr;
            if(index==0){
                arr[0]=element;
                arr[1]=t;
            }
            else{
                arr[0]=t;
                arr[1]=element;
            }
            return;
        }
        if(size<=SOO_SIZE){
            T[] arr = (T[]) storage;
            for (int i = index; i < size-1; i++) {
                arr[i+1]=arr[i];
            }
            arr[index]=element;
            return;
        }
        if(size==SOO_SIZE+1){
            T[] arr = (T[]) storage;
            storage = new ArrayList<>(List.of(arr));
        }
        ((ArrayList<T>)storage).add(index,element);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T remove(int index) {
        if(index<0 || index>=size){
            throw new IndexOutOfBoundsException();
        }
        size--;
        if(size==0){
            T t = (T) storage;
            storage=null;
            return t;
        }
        if(size==1){
            var arr =(T[])storage;
            if(index==0) {
                storage = arr[1];
                return arr[0];
            }
            else{
                storage=arr[0];
                return arr[1];
            }
        }
        if(size<SOO_SIZE){
            var arr =(T[])storage;
            T t = arr[index];
            for (int i = index; i < size; i++) {
                arr[i]=arr[i+1];
            }
            arr[size]=null;
            return t;
        }
        if(size==SOO_SIZE){
            var list = (ArrayList<T>)storage;
            T t = list.remove(index);
            storage = list.toArray();
            return t;
        }
        return ((ArrayList<T>)storage).remove(index);
    }

    @Override
    public int size() {
        return size;
    }
}
