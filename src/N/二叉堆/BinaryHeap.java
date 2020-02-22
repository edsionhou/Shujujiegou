package N.二叉堆;

import I.二叉搜索树.printer.BinaryTreeInfo;

import java.util.Comparator;

/**
 * 二叉堆是一个完全二叉树 : 从根节点->左儿子->右儿子 这种顺序位置插入的， 我们用数组实现， i的左右儿子分别是 2i+1 2i+2
 * <p>
 * 我们设计一个 大顶堆
 *
 * 小顶堆就是 compare改变下大小的 大顶堆
 *
 * @param <E>
 */
public class BinaryHeap<E> extends AbstractHeap<E> implements BinaryTreeInfo {
    protected E[] elements;
    protected static final int DEFAULT_CAPALITY = 10;
    //批量建堆
    public BinaryHeap(Comparator<E> comparator, E[] elements) {
        super(comparator);
        if(elements==null || elements.length==0){
            this.elements = (E[]) new Object[DEFAULT_CAPALITY];
        }else{
           // this.elements = elements; 不能这样，这样指针指向外部传的数组，
                                    // 外部修改后，这里也会被改，我们应该自己申请内存空间
            this.elements = (E[]) new Object[elements.length];
            System.arraycopy(elements,0,this.elements,0,elements.length);
            size=elements.length;
            heapify();
        }
    }



    public BinaryHeap(Comparator<E> comparator) {
        super(comparator);
        this.elements = (E[]) new Object[DEFAULT_CAPALITY];
    }

    public BinaryHeap(E[] elements) {
       this(null,elements);
    }

    public BinaryHeap() {
      this(null,null);
    }



    @Override
    public void clear() {

        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }


    /*
            完全二叉堆， 每个元素 绝对有父亲， 除了新增节点的父亲，任意一个父节点都有俩儿子。
     */
    @Override
    public void add(E element) {
        elementNotNullCheck(element);
        ensureCapacity(size + 1);

        elements[size++] = element;
        siftUp(size - 1); //上滤
    }

    /*
    上滤  让index位置元素上滤
  */
    private void siftUp(int index) {
        E son = elements[index];
        while (index > 0) {
            int parentindex = (index - 1) >> 1;   //父节点 = 儿子*2+1
            E parent = elements[parentindex];
            if (compare(parent, son) >= 0) break;
            //交换 父子的值  不好  用上滤优化
//           E tmp = elements[index];
//           elements[index] = elements[parentindex];
//           elements[parentindex] = tmp;
            elements[index] = elements[parentindex];
            index = parentindex;  //把index传递给父亲再比较
        }
        elements[index] = son;  //上滤实现
    }

    public void emptyCheck() {  //检查堆是否为空
        if (size == 0) {
            throw new IndexOutOfBoundsException("堆为空");
        }
    }

    public void elementNotNullCheck(E element) {
        if (element == null) {
            throw new IllegalArgumentException("传入参数不能为空");
        }
    }

    @Override
    public E get() {
        emptyCheck();
        return elements[0];
    }

    @Override
    public E remove() {
        emptyCheck();
        E top = elements[0];
        elements[0] = elements[size - 1];   //用最后覆盖 第一个
        elements[size - 1] = null;
        size--;
        siftDown(0);
        return top;
    }

    private void siftDown(int index) {
        //非叶子节点数等于 size/2
        //第一个叶子节点的索引==非叶子节点的数量
        int lastParent = (size >> 1)-1;
        E element = elements[index];
        while (index <= lastParent) {   //必须保证index位置是非叶子节点
            // 1 只有左  2 左右都有
            int childIndex = (index<<1)+1;
            E child = elements[childIndex];
            //选出最大子节点
            int rightIndex = childIndex+1;
            if(rightIndex < size && compare(elements[rightIndex],child) > 0){
//                child = elements[rightIndex];
//                childIndex = rightIndex;
                child = elements[childIndex = rightIndex];
            }
            if(compare(element,child)>0) break;
            //将子节点存放到index位置
            elements[index] = child;
            index = childIndex;
        }
        elements[index] = element;
    }


    @Override
    public E replace(E element) {
        elementNotNullCheck(element);
        if(size==0){
            elements[0] =element;
            size++;
            return null;
        }
        E top = elements[0];
        elements[0] = element;
        siftDown(0);
        return top;
    }

    public void ensureCapacity(int capacity) {     //扩容！
        int oldCapacity = elements.length;
        if (oldCapacity > capacity) return;

        //扩容 1.5倍
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        System.arraycopy(oldCapacity, 0, newCapacity, 0, oldCapacity);
        System.out.println("扩容 从" + oldCapacity + "到" + newCapacity);
    }

    private void heapify() {  //批量建堆
        //自上而下的上滤   复杂度 nlogn
//        for (int i = 1; i <size ; i++) {
////            siftUp(i);
////        }

        //自下而上的下滤   复杂度 O(n)
        for (int i = (size>>1)-1; i >=0; i--) {
                siftDown(i);
        }
    }

    //===============================================================================
    @Override
    public java.lang.Object root() {
        return 0;
    }

    @Override
    public java.lang.Object left(java.lang.Object node) {
        Integer index = (Integer) node;
        index = (index << 1) + 1;
        return index >= size ? null : index;
    }

    @Override
    public java.lang.Object right(java.lang.Object node) {
        Integer index = (Integer) node;
        index = (index << 1) + 2;
        return index >= size ? null : index;

    }

    @Override
    public java.lang.Object string(java.lang.Object node) {
        Integer index = (Integer) node;
        return elements[index];
    }
}
