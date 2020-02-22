package AVL树;

import I.二叉搜索树.BalanceBinarySearchTree;

import java.util.Comparator;

/**
 * @Author: edison
 * @Date: 2020/2/9 10时28分
 * @Description:
 */
public class AVLTree<E> extends BalanceBinarySearchTree<E> {

    public AVLTree() {
        this(null);
    }

    public AVLTree(Comparator<E> comparator) {
        super(comparator);
    }

    //在添加之后进行平衡计算


    @Override //二叉查找树add节点后，执行重写， 进行平衡处理
    protected void afterAdd(Node<E> node) {
        while ((node = node.parent) != null) {
            if (isBalance(node)) {
                //更新高度            遍历更新所有父节点高度
                ((AVLNode<E>) node).updateHeight();
            } else {
                //恢复平衡
                rebalance(node);
                break; //只需要恢复第一个不平衡的节点，整棵树就会平衡
                /**
                 * 我懂了，加一个 平衡点， 父类--祖父 都会被updateHeight
                 * 但是 加一个不平衡的， avl后，高度会和加之前一样，所以
                 * 只会影响第一个不平衡的节点，不需要再向上检查了，直接break
                 */

            }
        }
    }


    @Override  //删除之后  恢复平衡
    public void afterRemove(Node<E> node,Node<E> preplacement) {
        while ((node = node.parent ) != null) {
            if(isBalance(node)){
                updateHeight(node);
            }else{
                rebalance(node);
            }

        }
    }

    @Override  //重写 创建node的方法，之前创建的是Node  AVL里创建 AVLnode
    protected Node<E> createNode(E element, Node<E> parent) {
        return new AVLNode<>(element, parent);
    }

    private boolean isBalance(Node<E> node) {
        return Math.abs(((AVLNode<E>) node).balanceFactor()) <= 1; //求个绝对值 <=1就是平衡
    }


    @Override
    public void afterRotate(Node<E> grand, Node<E> parent, Node<E> child) {
        super.afterRotate(grand, parent, child);
        //更新高度
        updateHeight(grand);
        updateHeight(parent);
    }

    /*
             恢复平衡   grand是高度不平衡的第一个点
         */
    private void rebalance(Node<E> grand) {
        Node<E> parent = ((AVLNode<E>) grand).tallerChild();
        Node<E> node = ((AVLNode<E>) parent).tallerChild();
        if (parent.isLeftChild()) {
            if (node.isLeftChild()) { //LL
                rotateRight(grand);
            } else { //LR
                rotateLeft(parent);
                rotateRight(grand);
            }
        } else {
            if (node.isRightChildren()) { //RR
                rotateLeft(grand);
            } else { //RL
                rotateRight(parent);
                rotateLeft(grand);

            }
        }

    }



    /**
     * 统一恢复平衡
     */
    public void rebalance2(Node<E> grand) {
        Node<E> parent = ((AVLNode<E>) grand).tallerChild();
        Node<E> node = ((AVLNode<E>) parent).tallerChild();
        if (parent.isLeftChild()) {
            if (node.isLeftChild()) { //LL
                rotate2(grand, node.left, node, node.right, parent, parent.right, grand, grand.right);
            } else { //LR
                rotate2(grand, parent.left, parent, node.left, node, node.right, grand, grand.right);
            }
        } else {
            if (node.isRightChildren()) { //RR
                rotate2(grand, grand.left, grand, parent.left, parent, node.left, node, node.right);
            } else { //RL
                rotate2(grand, grand.left, grand, node.left, node, node.right, parent, parent.right);

            }
        }

    }

    public void rotate2(Node<E> r, Node<E> a,
                        Node<E> b, Node<E> c, Node<E> d,
                        Node<E> e, Node<E> f, Node<E> g) {
        //让d成为根节点
        d.parent = r.parent;
        if (r.isRightChildren()) {
            r.parent.right = d;
        } else if (r.isLeftChild()) {
            r.parent.left = d;
        } else {  //r是root
            root = d;
        }
        // a b c
        b.left = a;
        b.right = c;
        if (a != null) {
            a.parent = b;
        }
        if (c != null) {
            c.parent = b;
        }
        updateHeight(b);

        //e f g
        f.left = e;
        f.right = g;
        if (e != null) {
            e.parent = f;
        }
        if (g != null) {
            g.parent = f;
        }
        updateHeight(f);

        // b d f
        d.left = b;
        d.right = f;
        b.parent = d;
        f.parent = d;
        updateHeight(d);

    }




    public void updateHeight(Node<E> node) {  //更改高度的方法，也就是个强制转换
        ((AVLNode<E>) node).updateHeight();
    }


    protected static class AVLNode<E> extends Node<E> {

        int height = 1;  //avl树多了个高度属性  新增的都是叶子就是1

        public AVLNode(E element, Node<E> parent) {
            super(element, parent);
        }

        public int balanceFactor() {  //平衡因子 节点的高度差 1以内就是平衡
            int leftHeight = left == null ? 0 : ((AVLNode<E>) left).height;
            int rightHeight = right == null ? 0 : ((AVLNode<E>) right).height;
            return leftHeight - rightHeight;
        }

        public void updateHeight() {
            int leftHeight = left == null ? 0 : ((AVLNode<E>) left).height;
            int rightHeight = right == null ? 0 : ((AVLNode<E>) right).height;
            height = 1 + Math.max(leftHeight, rightHeight);
        }

        public Node<E> tallerChild() {
            int leftHeight = left == null ? 0 : ((AVLNode<E>) left).height;
            int rightHeight = right == null ? 0 : ((AVLNode<E>) right).height;
            if (leftHeight > rightHeight) {
                return left;
            }
            if (leftHeight < rightHeight) {
                return right;
            }
            return isLeftChild() ? left : right;   //根据是哪个子节点来返回
        }

        @Override
        public String toString() {
            return element + "(" + height + ")";
        }
    }


}
