package K.集合.set;

import I.二叉搜索树.BinaryTree;
import J.红黑树.RedBlackTree;

import java.util.Comparator;

/**
 * @Author: edison
 * @Date: 2020/2/13 09时50分
 * @Description:
 *
 * 采用红黑树作为 Set实现类   复杂度
 * 增删 O（log n ）  add: logn次遍历 +  上溢(O(1)次的旋转 )      delete: logn次遍历+ 下溢（据统计也是 0(1) ）
 *  遍历 O（log n）
 *  但要求数据必须具备可比较性！
！
 *      比listSet 增删都快很多  但listSet不需要可比较性
 *      list  add: 因为需要比较一次是否相等  O(n)  + 新增O(1)
 *      delete O(n)
 *
 */
public class TreeSet<E> implements Set<E> {
    private RedBlackTree<E> tree ;

 public TreeSet() {
  tree = new RedBlackTree<>();
 }

 public TreeSet(Comparator<E> comparator) {
  tree = new RedBlackTree<>(comparator);

 }

 @Override
    public int size() {
        return tree.size();
    }

    @Override
    public boolean isEmpty() {
        return tree.isEmpty();
    }

    @Override
    public void clear() {
        tree.clear();
    }

    @Override
    public boolean contains(E element) {
        return tree.contains(element);
    }

    @Override
    public void add(E element) {   //我们设计的 树默认就是去重的
        tree.add(element);
    }

    @Override
    public void remove(E element) {
        tree.remove(element);
    }

    @Override
    public void traversal(Visitor<E> visitor) {
       tree.preOrderPrintV(new BinaryTree.Visitor<E>() {//需要传入 二叉树的visitor
        @Override
        public boolean visitor(E e) {
         boolean visit = visitor.visit(e);

         return visit;
        }
       });
    }
}
