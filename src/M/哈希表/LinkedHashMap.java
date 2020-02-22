package M.哈希表;

/**
 * @param <K>
 * @param <V> LinkedHashMap  主要用来遍历时，可以按照添加顺序来返回
 */
public class LinkedHashMap<K, V> extends HashMap<K,V> {
    private LinkedNode<K, V> first;
    private LinkedNode<K, V> last;


    @Override
    public Node<K, V> createNode(K key, V value, Node<K, V> parent) {
        LinkedNode<K, V> linknode = new LinkedNode<>(key, value, parent);
        if (first == null) {
            first = last = linknode;
        } else {
            last.next = linknode;
            linknode.prev = last;
            last = linknode;
        }
        return linknode;

    }

    @Override
    public void clear() {
        super.clear();
        first = null;
        last = null;
    }

    @Override
    public void traversal(Visitor<K, V> visitor) {
        if(visitor==null) return;
        LinkedNode<K, V> firstNode = this.first;
        while(firstNode!=null){
            if(visitor.visit(firstNode.key,firstNode.value)) return;
            firstNode = firstNode.next;
        }



    }

    @Override
    public V remove(K key) {
       Node<K, V> node =getNode(key);
       if(node==null) return null;
        LinkedNode next = ((LinkedNode) node).next;
        LinkedNode prev = ((LinkedNode) node).prev;
        if(prev==null){
            first = next;
            next.prev = first;
        }else{
            prev.next = next;
            next.prev = prev;
        }
        if(next==null){
            last = prev;
        }else{
            next.prev = prev;
        }

        return super.remove(key);

    }

    /*

     */

    @Override
    public void linkedAfterRemove(Node<K, V> willNode,Node<K,V> removeNode) {
        LinkedNode<K, V> node1 = (LinkedNode<K, V>) willNode;
        LinkedNode<K, V> node2 = (LinkedNode<K, V>) removeNode;

        if (node1 != node2) {  //度为2 需要交换 要删除点和实际被删除点的位置  否则遍历结果不对劲
            // 交换linkedWillNode和linkedRemovedNode在链表中的位置
            // 交换prev
            LinkedNode<K, V> tmp = node1.prev;
            node1.prev = node2.prev;
            node2.prev = tmp;
            if (node1.prev == null) {
                first = node1;
            } else {
                node1.prev.next = node1;
            }
            if (node2.prev == null) {
                first = node2;
            } else {
                node2.prev.next = node2;
            }

            // 交换next
            tmp = node1.next;
            node1.next = node2.next;
            node2.next = tmp;
            if (node1.next == null) {
                last = node1;
            } else {
                node1.next.prev = node1;
            }
            if (node2.next == null) {
                last = node2;
            } else {
                node2.next.prev = node2;
            }
        }

        LinkedNode<K, V> prev = node2.prev;
        LinkedNode<K, V> next = node2.next;
        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
        }

        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
        }


    }

    public static class LinkedNode<K, V> extends Node<K, V> {
        LinkedNode<K, V> prev;
        LinkedNode<K, V> next;

        public LinkedNode(K key, V value, Node<K, V> parent) {
            super(key, value, parent);
        }


    }


}
