package N.二叉堆;

import java.util.Comparator;

public abstract  class AbstractHeap<E> implements Heap<E> {
    protected int size;
    protected Comparator<E> comparator;


    public AbstractHeap(Comparator<E> comparator) {
        this.comparator = comparator;
    }



    protected int compare(E e1, E e2) {   //比较 需要比较器 or 继承Comparable
        if (comparator != null) {
            return comparator.compare(e1, e2);
        }
        return ((Comparable<E>) e1).compareTo(e2);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

}
