package ru.itmo.mit.util;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("SimplifyStreamApiCallChains")
public class DictionaryImpl<K, V> implements Dictionary<K, V> {

    public final double DEFAULT_LOAD_FACTOR = 0.7;
    private double loadFactor = DEFAULT_LOAD_FACTOR;
    private int elementCount = 0;

    public final int DEFAULT_BUCKET_COUNT = 256;
    private int initialBucketCount = DEFAULT_BUCKET_COUNT;
    private int bucketCount = DEFAULT_BUCKET_COUNT;

    private List<ArrayList<Entry<K, V>>> buckets;

    DictionaryImpl() {
        rehash(bucketCount);
    }

    DictionaryImpl(double loadFactor) {
        this.loadFactor = loadFactor;
        rehash(bucketCount);
    }

    DictionaryImpl(int bucketCount) {
        this.bucketCount = bucketCount;
        initialBucketCount = bucketCount;
        rehash(bucketCount);
    }

    DictionaryImpl(double loadFactor, int bucketCount) {
        this.loadFactor = loadFactor;
        this.bucketCount = bucketCount;
        initialBucketCount = bucketCount;
        rehash(bucketCount);
    }

    private void rehash(int newBucketCount) {
        var newBuckets = Stream.generate(() -> (ArrayList<Entry<K, V>>) null).limit(newBucketCount).collect(Collectors.toList());
        if (buckets == null) {
            buckets = newBuckets;
            return;
        }

        for (var oldBucket : buckets) {
            if (oldBucket == null) continue;
            for (var entry : oldBucket) {
                var bucketIdx = getBucketIdx(entry.getKey());
                var bucket = newBuckets.get(bucketIdx);
                if (bucket == null) {
                    bucket = new ArrayList<>();
                    newBuckets.set(bucketIdx, bucket);
                }
                bucket.add(entry);
            }
        }

        buckets = newBuckets;
    }

    private double getLoad() {
        return (double) elementCount / bucketCount;
    }

    private void rehashIfNeeded() {
        var load = getLoad();
        if (load < loadFactor / 3 && bucketCount > initialBucketCount) {
            rehash(bucketCount / 2);
        } else if (load > loadFactor) {
            rehash(bucketCount * 2);
        }
    }

    private int getBucketIdx(Object o) {
        if (o == null) return 0;
        var result = o.hashCode() % bucketCount;
        if (result < 0) result += bucketCount;
        return result;
    }

    @Override
    public int size() {
        return elementCount;
    }

    @Override
    public boolean isEmpty() {
        return elementCount == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        var bucket = buckets.get(getBucketIdx(key));
        if (bucket == null) return false;
        for (var entry : bucket) {
            if (Objects.equals(entry.getKey(), key)) return true;
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        for (var bucket : buckets) {
            if (bucket == null) continue;
            for (var entry : bucket) {
                if (Objects.equals(entry.getValue(), value)) return true;
            }
        }
        return false;
    }

    @Override
    public V get(Object key) {
        var bucket = buckets.get(getBucketIdx(key));
        if (bucket == null) return null;
        for (var entry : bucket) {
            if (Objects.equals(entry.getKey(), key)) return entry.getValue();
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        var bucketIdx = getBucketIdx(key);
        var bucket = buckets.get(bucketIdx);
        if (bucket == null) {
            bucket = new ArrayList<>();
            buckets.set(bucketIdx, bucket);
            bucket.add(new AbstractMap.SimpleEntry<>(key, value));
            elementCount++;
            rehashIfNeeded();
            return null;
        }
        for (var entry : bucket) {
            if (Objects.equals(entry.getKey(), key)) {
                var oldValue = entry.getValue();
                entry.setValue(value);
                return oldValue;
            }
        }
        bucket.add(new AbstractMap.SimpleEntry<>(key, value));
        elementCount++;
        rehashIfNeeded();
        return null;
    }

    @Override
    public V remove(Object key) {
        var bucketIdx = getBucketIdx(key);
        var bucket = buckets.get(bucketIdx);
        if (bucket == null) {
            return null;
        }
        for (var entry : bucket) {
            if (Objects.equals(entry.getKey(), key)) {
                var oldValue = entry.getValue();
                bucket.remove(entry);
                if(bucket.isEmpty()){
                    buckets.set(bucketIdx, null);
                }
                elementCount--;
                rehashIfNeeded();
                return oldValue;
            }
        }
        return null;
    }

    @Override
    public void putAll(@NotNull Map<? extends K, ? extends V> m) {
        m.forEach(this::put);
    }

    @Override
    public void clear() {
        bucketCount = initialBucketCount;
        elementCount = 0;
        buckets = new ArrayList<>(bucketCount);
    }

    @Override
    public @NotNull Set<K> keySet() {
        return new AbstractSet<>() {
            @Override
            public Iterator<K> iterator() {
                return entrySet().stream().map(Entry::getKey).iterator();
            }

            @Override
            public int size() {
                return DictionaryImpl.this.size();
            }
        };
    }

    @Override
    public @NotNull Collection<V> values() {
        return new AbstractCollection<>() {
            @Override
            public Iterator<V> iterator() {
                return entrySet().stream().map(Entry::getValue).iterator();
            }

            @Override
            public int size() {
                return DictionaryImpl.this.size();
            }
        };
    }

    @Override
    public @NotNull Set<Entry<K, V>> entrySet() {
        return new AbstractSet<>() {
            @Override
            public Iterator<Entry<K, V>> iterator() {
                return new Iterator<>() {

                    private final Iterator<ArrayList<Entry<K, V>>> bucketIterator = buckets.iterator();
                    private ArrayList<Entry<K, V>> currentBucket = null;
                    private ArrayList<Entry<K, V>> nextBucket = null;
                    private int c = 0;
                    private boolean removed=false;

                    {
                        while (bucketIterator.hasNext()) {
                            currentBucket = bucketIterator.next();
                            if (currentBucket != null) break;
                        }

                        while (bucketIterator.hasNext()) {
                            nextBucket = bucketIterator.next();
                            if (nextBucket != null) break;
                        }
                    }

                    @Override
                    public boolean hasNext() {
                        return nextBucket != null || (currentBucket != null && c < currentBucket.size()-1);
                    }

                    @Override
                    public Entry<K, V> next() {
                        if (currentBucket == null) {
                            throw new NoSuchElementException();
                        }
                        removed=false;
                        if (c < currentBucket.size()) {
                            c++;
                        } else {
                            c = 1;
                            currentBucket = nextBucket;
                            nextBucket = null;
                            while (bucketIterator.hasNext()) {
                                nextBucket = bucketIterator.next();
                                if (nextBucket != null) break;
                            }
                        }
                        return currentBucket.get(c-1);
                    }

                    @Override
                    public void remove() {
                        if(removed || currentBucket==null){
                            throw new IllegalStateException();
                        }
                        removed=true;
                        DictionaryImpl.this.remove(currentBucket.get(c));
                        if(c>0) c--;
                    }
                };
            }

            @Override
            public int size() {
                return DictionaryImpl.this.size();
            }
        };
    }
}
