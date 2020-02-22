package J.红黑树;

import I.二叉搜索树.BalanceBinarySearchTree;

import java.util.Comparator;

/**
 * @Author: edison
 * @Date: 2020/2/10 15时21分
 * @Description:
 */
public class RedBlackTree<E> extends BalanceBinarySearchTree<E> {
    private static final boolean RED = false;   //红色 = false
    private static final boolean BLACK = true; // 黑色 = true

    public RedBlackTree() {
        this(null);
    }

    public RedBlackTree(Comparator<E> comparator) {
        super(comparator);
    }


    @Override
    protected void afterAdd(Node<E> node) {
        Node<E> parent = node.parent;

        if (parent == null) { //加的是根节点  不可调换顺序
            makeBlack(node);
            return;
        }
        if (isBlack(parent)) { //如果父节点是黑色，不用处理 4种
            return;
        }

        //8种  parent是 RED
        //前4种，uncle是红
        Node<E> uncle = parent.sibling();
        Node<E> grand = parent.parent;
        if (isRed(uncle)) {  //uncle是红
            // parent uncle染黑
            makeBlack(parent);
            makeBlack(uncle);
            //祖父 一定上溢，当作新节点刚添加完  看待
            makeRed(grand);
            afterAdd(grand);
            return;
        }
        // uncle不是红
        if (parent.isLeftChild()) {
            if (node.isLeftChild()) {  // ll
                makeBlack(parent);
                makeRed(grand);
                rotateRight(grand);
            } else { // LR
                makeBlack(node);
                makeRed(grand);
                rotateLeft(parent);
                rotateRight(grand);

            }
        } else {
            if (node.isLeftChild()) { // RL
                makeBlack(node);
                makeRed(grand);
                rotateRight(parent);
                rotateLeft(grand);
            } else {  //RR
                makeBlack(parent);
                makeRed(grand);
                rotateLeft(grand);
            }

        }


    }

    @Override  // 这个方法只传入 replacement 也可以，   if (isRed(node)) {return} 这里改成replacement就行了
                //然后 BST 的 度为1的 那个afterRemove 传入replacement
    public void afterRemove(Node<E> node, Node<E> replacement) {
        if (isRed(node)) {
            return;  //红色不用处理 R-B-R 模式
        }
        //用以取代node的子节点是红色   R-B  B-R
        if (isRed(replacement)) {
            makeBlack(replacement);
            return;
        }
        //删除的是黑色叶子节点
        //1 删除的是root
        Node<E> parent = node.parent;
        if (parent == null) {
            return;
        }
        //2. 删除的是叶子   走到这步，删除的必定是 度为0的 黑节点  且 红黑树=B树，必有兄弟
        // node.parent 还是能指到的，但是 node.parent.left or right 已经是null了，无法寻到node
        //判断被删除的node是左还是右
//        Node<E> sibling = node.sibling();  这个不行
        /*
        这是如果从remove方法传的 node，可以判断parent.left == null  ，但如果是afterRemove(parent, null)
        递归上来的，node.parent是存在的，所以node.isLeftChild()判断下是哪个子节点
         */
        boolean left = parent.left == null || node.isLeftChild();
        Node<E> sibling = left ? parent.right : parent.left;
        // 我这里一直有疑问， node的父节点只有 一个节点怎么办， 左右都是null
        // 我知道了。 4阶B树，节点数 2-4之间，叶子必定有兄弟
        //！！！注意  我们删除一定是在叶子节点， 即最后一层，所以 兄弟不可能再有黑色节点了，最多有红色
        if (left) {   //被删除的node在左边，兄弟在右边
            if (isRed(sibling)) {   //兄弟是红色，旋转成黑色的情况
                makeBlack(sibling);
                makeRed(parent);
                rotateLeft(parent);
                //parent现在的left 成了兄弟
                sibling = parent.right;
            }
            //兄弟节点 必然黑色
            if (isBlack(sibling.left) && isBlack(sibling.right)) {
                //兄弟 没有1个红色子节点
                // 兄弟是黑的！  父节点要向下和兄弟 合并
                boolean parentBlack = isBlack(parent);
                makeBlack(parent);
                makeRed(sibling);
                if (parentBlack) {
                    afterRemove(parent, null);
                }

            } else {  //兄弟 至少有1个红色子节点
                if (isBlack(sibling.right)) {  // null 也是黑！  先进行 LL
                    rotateRight(sibling);
                    sibling = parent.right;   // 针对上面的 LL  兄弟其实已经换了 76--->78,其他情况 76是兄弟
                }


                //统一对parent RR
                color(sibling, colorOf(parent));   // 兄弟染成 parent的颜色
                makeBlack(sibling.right);
                makeBlack(parent);
                rotateLeft(parent);
             }

        } else {  //被删除的node在右边，兄弟在左边
            if (isRed(sibling)) {   //兄弟是红色，旋转成黑色的情况
                makeBlack(sibling);
                makeRed(parent);
                rotateRight(parent);
                //parent现在的left 成了兄弟
                sibling = parent.left;
            }
            //兄弟节点 必然黑色
            if (isBlack(sibling.left) && isBlack(sibling.right)) {
                //兄弟 没有1个红色子节点
                // 兄弟是黑的！  父节点要向下和兄弟 合并
                boolean parentBlack = isBlack(parent);
                makeBlack(parent);
                makeRed(sibling);
                if (parentBlack) {
                    afterRemove(parent, null);
                }

            } else {    //兄弟 至少有1个红色子节点
                if (isBlack(sibling.left)) {  // null 也是黑！  先进行 LL
                    rotateLeft(sibling);
                    sibling = parent.left;   // 针对上面的 LL  兄弟其实已经换了 76--->78,其他情况 76是兄弟
                }


                //统一对parent RR
                color(sibling, colorOf(parent));   // 兄弟染成 parent的颜色
                makeBlack(sibling.left);
                makeBlack(parent);
                rotateRight(parent);


            }


        }


    }


    public Node<E> color(Node<E> node, boolean color) {   //  染色方法
        if (node == null) {
            return node;
        }
        ((RBNode<E>) node).color = color;
        return node;
    }

    public Node<E> makeRed(Node<E> node) {   //染成 RED
        return color(node, RED);
    }

    public Node<E> makeBlack(Node<E> node) {  //染成 BLACK
        return color(node, BLACK);
    }

    public boolean colorOf(Node<E> node) {   //判断颜色， 空 = black， 否则 返回 自己的颜色
        return node == null ? BLACK : ((RBNode<E>) node).color;
    }

    public boolean isBlack(Node<E> node) {  //  isBLACK ??
        return colorOf(node) == BLACK;
    }

    public boolean isRed(Node<E> node) {  //is RED??
        return colorOf(node) == RED;
    }

    @Override
    protected Node<E> createNode(E element, Node<E> parent) {
        return new RBNode<>(element, parent);
    }


    // 红黑树 node节点类
    public static class RBNode<E> extends Node<E> {

        public boolean color = RED;  //建议新加的节点都是 red

        public RBNode(E element, Node<E> parent) {
            super(element, parent);
        }

        @Override
        public String toString() {
            String str = "";
            if (color == RED) {
                str = "R_";
            }
            return str + element.toString();
        }
    }

}
