package P.优先级队列;

import N.二叉堆.BinaryHeap;
import I.二叉搜索树.printer.BinaryTrees;

import java.util.Comparator;

/*
  出队时 要求每次出优先级最高的数据
  使用二叉堆实现即可
 */
public class PriorityQueue<E> {
    private BinaryHeap<E> heap ;  //使用二叉堆即可

    public PriorityQueue(Comparator<E> comparator) {
        this.heap = new BinaryHeap<>(comparator); //传入 优先级比较器
    }

    public PriorityQueue() {
        this(null);  //不传入的话 用 comparable
    }

    public int size() {
        return heap.size();
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public void clear() {
        heap.clear();
    }

    public void enQueue(E element) {
        heap.add(element);
    }

    public E deQueue() {
        return heap.remove();
    }

    public E front() {
        return heap.get();
    }

    public void  print(){
        BinaryTrees.println(heap);
    }
}
