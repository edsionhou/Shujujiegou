package I.二叉搜索树;

import java.util.Comparator;

/**
 * @Author: edison
 * @Date: 2020/2/10 17时18分
 * @Description:
 *              平衡二叉树， 主要是提取了 平衡的方法
 *              AVL 平衡后需要记录高度
 *              updateHeight（）；
 *              所以重写了 afterRotate
 */
public class BalanceBinarySearchTree<E> extends  BinarySearchTree<E>  {

    public BalanceBinarySearchTree() {
        this(null);
    }

    public BalanceBinarySearchTree(Comparator<E> comparator) {
        super(comparator);
    }

    protected void rotateLeft(Node<E> grand) {  //  旋转 L
        Node<E> parent = grand.right;
        Node<E> child = parent.left;
        grand.right = child;
        parent.left = grand;
        afterRotate(grand, parent, child);

    }

    protected void rotateRight(Node<E> grand) {  //旋转 R
        Node<E> parent = grand.left;
        Node<E> child = parent.right;

        grand.left = child;
        parent.right = grand;
        afterRotate(grand, parent, child);

    }

    //Rotate 旋转的第二个步骤 提取出来公共的
    public void afterRotate(Node<E> grand, Node<E> parent, Node<E> child) {
        parent.parent = grand.parent; //这部挺重要
        //grand 的爹 的儿子 应该改为parent
        if (grand.isRightChildren()) {
            grand.parent.right = parent;
        } else if (grand.isLeftChild()) {
            grand.parent.left = parent;
        } else {
            root = parent; //已经修改过parent的爹了 parent.parent = grand.parent
        }
        grand.parent = parent;  //g
        if (child != null) {
            child.parent = grand;//t1

        }

    }

}
