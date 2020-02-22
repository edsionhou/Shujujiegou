/**
 * @Author: edison
 * @Date: 2020/2/7 21时29分
 * @Description:
 */
public class MyHashMap<K,V> {
    private Entry<K,V>[] table;
    private static Integer CAPLITY=8;
    private static int size = 0;

    public MyHashMap() {
        this.table = new Entry[CAPLITY];
    }

    public int size() {
        return size;
    }

    public V get(K key) {
        int hashCode = key.hashCode();
        int i= hashCode % CAPLITY;
        for (Entry<K,V> entry=table[i]; entry!=null;entry=entry.next){
            if(entry.k.equals(key)){
                return entry.v;
            }
        }

        return null;
    }

    public V put(K key, V value) {

        int hashCode = key.hashCode();
        int i= hashCode % CAPLITY;

        //key相同就替换value
        for (Entry<K,V> entry=table[i]; entry!=null;entry=entry.next){
            if(entry.k.equals(key)){
                V old = entry.v;
                entry.v=value;
                return  old;
            }
        }
        addEntry(key,value,i);
        size++;
        return null;

    }

    public void addEntry(K key,V value, int i){
        Entry<K,V> entry = new Entry<>(key,value,table[i]); //新加入的都当作头节点
        table[i] = entry;
    }


    public static class Entry<K,V>{
        private K k;
        private V v;
        private Entry<K,V> next;

        public Entry(K k, V v, Entry<K, V> next) {
            this.k = k;
            this.v = v;
            this.next = next;
        }
    }
}
