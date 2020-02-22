package K.集合.set;

import L.映射.Map;
import L.映射.TreeMap;

/**
 * @Author: edison
 * @Date: 2020/2/13 15时57分
 * @Description:
 *
 *      采用 红黑树写的TreeMap来实现 Set
 */
public class TreeSet2<E>  implements  Set<E>{
    private Map<E,Object> map = new TreeMap<>();
    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public boolean contains(E element) {
        return map.containsKey(element);
    }

    @Override
    public void add(E element) {
        map.put(element,null);
    }

    @Override
    public void remove(E element) {
        map.remove(element);
    }

    @Override
    public void traversal(Visitor<E> visitor) {
        Map.Visitor<E,Object> mapV = new Map.Visitor<E, Object>() {
            @Override
            public boolean visit(E key, Object value) {
                boolean visit = visitor.visit(key);
                System.out.println("呵呵");
                return visit;
            }
        };
        map.traversal(mapV);

    }
}
