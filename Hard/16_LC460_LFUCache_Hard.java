import java.util.*;

class LFUCache {

    class Node {
        int key, value, freq;

        Node(int key, int value) {
            this.key = key;
            this.value = value;
            this.freq = 1;
        }
    }

    int capacity;
    int minFreq;

    HashMap<Integer, Node> cache;
    HashMap<Integer, LinkedHashSet<Integer>> freqMap;

    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.minFreq = 0;
        cache = new HashMap<>();
        freqMap = new HashMap<>();
    }

    public int get(int key) {
        if (!cache.containsKey(key))
            return -1;

        Node node = cache.get(key);
        updateFreq(node);
        return node.value;
    }

    public void put(int key, int value) {
        if (capacity == 0)
            return;

        if (cache.containsKey(key)) {
            Node node = cache.get(key);
            node.value = value;
            updateFreq(node);
            return;
        }

        if (cache.size() >= capacity) {
            int evictKey = freqMap.get(minFreq).iterator().next();
            freqMap.get(minFreq).remove(evictKey);
            cache.remove(evictKey);
        }

        Node newNode = new Node(key, value);
        cache.put(key, newNode);

        minFreq = 1;
        freqMap.computeIfAbsent(1, k -> new LinkedHashSet<>());
        freqMap.get(1).add(key);
    }

    private void updateFreq(Node node) {
        int freq = node.freq;

        freqMap.get(freq).remove(node.key);

        if (freq == minFreq && freqMap.get(freq).isEmpty()) {
            minFreq++;
        }

        node.freq++;

        freqMap.computeIfAbsent(node.freq, k -> new LinkedHashSet<>());
        freqMap.get(node.freq).add(node.key);
    }
}