package ru.itmo.mit.util;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class HashMultiset<E> implements Multiset<E> {

    LinkedHashMap<E, Integer> map = new LinkedHashMap<>();

    @Override
    public int count(Object element) {
        var r = map.get(element);
        return r == null ? 0 : r;
    }

    @Override
    public Set<E> elementSet() {
        return map.keySet();
    }

    @Override
    public Set<Entry<E>> entrySet() {
        return new AbstractSet<>() {

            final Set<Map.Entry<E, Integer>> internalSet = map.entrySet();

            @Override
            public Iterator<Entry<E>> iterator() {

                Iterator<Map.Entry<E, Integer>> internalIterator = internalSet.iterator();
                return new Iterator<>() {
                    @Override
                    public boolean hasNext() {
                        return internalIterator.hasNext();
                    }

                    @Override
                    public Entry<E> next() {
                        var r = internalIterator.next();
                        return new Entry<E>() {
                            @Override
                            public E getElement() {
                                return r.getKey();
                            }

                            @Override
                            public int getCount() {
                                return r.getValue();
                            }
                        };
                    }

                    @Override
                    public void remove() {
                        internalIterator.remove();
                    }
                };
            }

            @Override
            public int size() {
                return internalSet.size();
            }
        };
    }

    @Override
    public int size() {
        int result = 0;
        for (int e: map.values()) {
            result+=e;
            if(result<0){
                return Integer.MAX_VALUE;
            }
        }
        return result;
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        return new Iterator<E>() {
            int c = 0;
            boolean removed=false;
            final Iterator<Entry<E>> internalIterator = entrySet().iterator();
            Entry<E> currentElem = internalIterator.hasNext()?internalIterator.next():null;

            @Override
            public boolean hasNext() {
                return currentElem!=null && (c < currentElem.getCount()|| internalIterator.hasNext());
            }

            @Override
            public E next() {
                removed=false;
                if(currentElem==null){
                    throw new NoSuchElementException();
                }
                if(c<currentElem.getCount()){
                    c++;
                    return currentElem.getElement();
                }else{
                    c=0;
                    currentElem=internalIterator.next();
                    return currentElem.getElement();
                }
            }

            @Override
            public void remove() {
                if(removed){
                    throw new IllegalStateException();
                }
                removed=true;
                if(currentElem.getCount()>1) {
                    if(c>0){
                        c--;
                    }
                    HashMultiset.this.remove(currentElem.getElement());
                }
                else{
                internalIterator.remove();
                }
            }
        };
    }

    @NotNull
    @Override
    public Object[] toArray() {
        Object[] result = new Object[size()];
        int c=0;
        for (Map.Entry<E,Integer>e : map.entrySet()){
            for (int i = 0; i < e.getValue(); i++) {
                result[c++]=e.getKey();
            }
        }
        return result;
    }

    @NotNull
    @Override
    public <T> T[] toArray(@NotNull T[] a) {
        return (T[])toArray();
    }

    @Override
    public boolean add(E e) {
        var r = map.putIfAbsent(e, 1);
        if (r != null) {
            map.put(e, r + 1);
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        var o_ = (E) o;
        var r = map.get(o_);
        if (r == null) {
            return false;
        }
        if (r > 1) {
            map.put(o_, r - 1);
        }
        else{
            map.remove(o_);
        }
        return true;
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return map.keySet().containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends E> c) {
        var result=false;
        for (E o:c) {
            result|=add(o);
        }
        return result;
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        var result=false;
        for (Object o:c) {
            result|=remove(o);
        }
        return result;
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return map.keySet().retainAll(c);
    }

    @Override
    public void clear() {
        map.clear();
    }
}
