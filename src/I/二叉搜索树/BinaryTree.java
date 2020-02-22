package I.二叉搜索树;

import I.二叉搜索树.printer.BinaryTreeInfo;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @Author: edison
 * @Date: 2020/2/9 09时36分
 * @Description: 提取二叉树的公共规则
 */
public class BinaryTree<E> implements BinaryTreeInfo {
    protected int size;
    protected Node<E> root;
    protected Comparator<E> comparator; //比较器
    protected E element;

    protected int bianli;  // 前序遍历 设计模式用的

    public BinaryTree() {
        this(null);
    }

    public BinaryTree(Comparator<E> comparator) {
        this.comparator = comparator;
    }


    public boolean isEmpty() {
        return size == 0;

    }

    public void clear() {
        root = null;
        size =0;
    }


    /**
     * compare
     * return = 0; e1 ==e2,   <0; e1<e2 ,  >0; e1>e2
     */
    public int compare(E e1, E e2) {  //有传入比较器，就用比较器，没有 用Comparable
        if (comparator != null) {
            return comparator.compare(e1, e2);
        }

        int result = ((Comparable<E>) e1).compareTo(e2);
        //如果 E 没有实现Comparable，那正符合我们的意思，此对象没有定义比较规则，报错
        return result;

    }

    public void elementNoNullCheck(E elememt) {  //测试添加的元素是否为null
        if (elememt == null) {
            throw new IllegalArgumentException("元素不能为null");
        }
    }


    public void preOrderPrint() {
        preOrderPrint(root);
    }

    //前序遍历，根节点-》左子树-》右子树
    public void preOrderPrint(Node<E> root) {  //递归
        if (root == null) {
            return;
        }
        System.out.print(root.element + " ");
        preOrderPrint(root.left);
        preOrderPrint(root.right);
    }


    public void inOrderPrint() {
        inOrderPrint(root);
    }

    public void inOrderPrint(Node<E> root) {
        if (root == null) {
            return;
        }
        inOrderPrint(root.left);
        System.out.print(root.element + " ");
        inOrderPrint(root.right);
    }

    public void postOrderPrint() {
        postOrderPrint(root);
    }

    public void postOrderPrint(Node<E> root) {
        if (root == null) {
            return;
        }
        postOrderPrint(root.left);
        postOrderPrint(root.right);
        System.out.print(root.element + " ");
    }


    public void LevelOrderPrint() {
        if (root == null) {
            return;
        }
        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            Node<E> node = queue.poll();
            System.out.print(node.element + " ");
            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }
        }

    }

    public static abstract class Visitor<E> { //嵌套类，不允许 public class
        boolean stop;

        public abstract boolean visitor(E e);
    }

    //设计模式进行前序
    public void preOrderPrintV(Visitor<E> visitor) {
        preOrderPrint(visitor, root);
    }

    public void preOrderPrint(Visitor<E> visitor, Node<E> root) {
        if (root == null || visitor.stop) {
            return;
        }
        bianli++;
        visitor.stop = visitor.visitor(root.element);

        if (visitor.stop == false) {
            preOrderPrint(visitor, root.left);
            preOrderPrint(visitor, root.right);
        }

    }

    //设计模式
    public void LevelOrder(Visitor<E> visitor) {
        if (root == null || visitor == null) {
            return;
        }
        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            Node<E> node = queue.poll();
            visitor.visitor(node.element);
            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }
        }


    }

    public int height() {
        int height = height(root);
        return height;
    }

    public int height(Node<E> node) {  //迭代遍历
        if (node == null) {
            return 0;
        }
        return 1 + Math.max(height(node.left), height(node.right));
    }


    public int height2() {  // 利用层序遍历
        if (root == null) {
            return 0;
        }
        //层
        int floor = 1;
        int height = 0;
        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {

            Node<E> node = queue.poll();
            floor--;
            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }
            if (floor == 0) {   //当一层出队完毕，剩余的数量正好是下一层的总和
                floor = queue.size();
                height++;
            }
        }

        return height;

    }


    public boolean isComplete() {   //是否为完全二叉树  层序遍历实现
        if (root == null) {
            return false;
        }
        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);
        boolean leaf = false;
        while (!queue.isEmpty()) {
            Node<E> node = queue.poll();
            if (leaf && !node.isLeaf()) {
                return false;
            }

            if (node.left != null) {
                queue.offer(node.left);
            } else if (node.right != null) {
                return false;
            }  // left == null && right == null
            if (node.right != null) {
                queue.offer(node.right);
            } else { //right==null
                //剩下 左右都空， 左不空，又空
                leaf = true;
            }
        }
        return true;


    }


    protected Node<E> predecessor(Node<E> node) {
        if (node == null) {
            return null;
        }
        //前驱节点在左子树中
        Node<E> p = node.left;
        if (p != null) {
            while (p.right != null) {
                p = p.right;
            }
            return p;
        }
        //在父节点的左子树中
        while (node.parent != null && node == node.parent.left) {
            node = node.parent;
        }
        return node.parent;


    }

    protected Node<E> successor(Node<E> node) {  //后继节点
        if (node == null) return null;

        // 前驱节点在左子树当中（right.left.left.left....）
        Node<E> p = node.right;
        if (p != null) {
            while (p.left != null) {
                p = p.left;
            }
            return p;
        }

        // 从父节点、祖父节点中寻找前驱节点
        while (node.parent != null && node == node.parent.right) {
            node = node.parent;
        }

        return node.parent;
    }

    //创建节点的方法，具体new 什么类型的Node 自己重写
    protected Node<E> createNode(E element, Node<E> parent) {
        return new Node<>(element, parent);
    }

    protected static class Node<E> {
        public E element;
        public Node<E> left;
        public Node<E> right;
        public Node<E> parent;

        public boolean isLeaf() {
            return left == null && right == null;
        }

        public  boolean hasTwoChildren() {
            return left != null && right != null;
        }
        public boolean isLeftChild(){
            return parent!=null && this==parent.left;
        }
        public boolean isRightChildren(){
            return parent!=null && this ==parent.right;
        }

        public Node<E> sibling(){ //兄弟节点
            if(isLeftChild()){
                return parent.right;
            }
            if(isRightChildren())   {
                return parent.left;
            }
            return  null;
        }


        public Node(E element, Node<E> parent) {
            this.element = element;
            this.left = left;
            this.right = right;
            this.parent = parent;
        }
    }


    /********************************************/


    @Override
    public Object root() {
        return root;
    }

    @Override
    public Object left(Object node) {
        return ((Node<E>) node).left;
    }

    @Override
    public Object right(Object node) {
        return ((Node<E>) node).right;
    }

    @Override
    public Object string(Object node) {
        return ((Node<E>) node);
    }

}
