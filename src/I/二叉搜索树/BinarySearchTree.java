package I.二叉搜索树;

import java.util.Comparator;

/**
 * @Author: edison
 * @Date: 2020/2/8 11时56分
 * @Description:
 */
public class BinarySearchTree<E> extends BinaryTree<E> {

    public BinarySearchTree() {
        this(null);
    }

    public BinarySearchTree(Comparator<E> comparator) {
        super(comparator);
    }

    public int size() {
        return size;
    }



    public void add(E element) { //while 进行添加
        elementNoNullCheck(element);
        //添加的root节点
        if (root == null) {
            root = createNode(element,null);
            size++;
            afterAdd(root);
            return;
        }
        Node<E> node = root;
        Node<E> parent = null;
        int com = 0; //保存记录的结果
        while (node != null) {
            parent = node; //每次循环结束的父节点
            com = compare(element, node.element);
            if (com > 0) {
                node = node.right;
            } else if (com < 0) {
                node = node.left;
            } else { //相等  替换掉原来的node里的数据
                node.element = element;
                return;
            }
        }
        Node<E> newNode = createNode(element,parent);
        if (com > 0) {
            parent.right = newNode;
        } else {
            parent.left = newNode;
        }
        size++;
        afterAdd(newNode);  //添加了节点后的处理
    }

    public void add2(E element) {//递归进行添加
        elementNoNullCheck(element);
        if (root == null) {
            root = new Node<>(element, null);
        }

        insert(root, element, root);
    }

    public void insert(Node<E> nowNode, E element, Node<E> parent) {
        if (nowNode == null) {  //需要加的位置
            nowNode = new Node<E>(element, parent);
            return;
        }
        //parent肯定不为null的
        int compare = comparator.compare(nowNode.element, element);
        if (compare < 0) {  // 小于父亲
            if (nowNode.right == null) {
                nowNode.right = new Node<>(element, nowNode);
            } else {
                insert(nowNode.right, element, nowNode);
            }
        } else if (compare > 0) {  // 大于父亲
            if (nowNode.left == null) {
                nowNode.left = new Node<>(element, nowNode);
            } else {
                insert(nowNode.left, element, nowNode);
            }
        } else {
            nowNode.element = element;  //等于就替换E
        }


    } //给add2递归用的

    //在添加之后，AVL树要进行平衡
    protected    void afterAdd(Node<E> node){
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





    public void remove(E element) {
        Node<E> node = getNode(element);
        remove(node);


    }
    public Node<E> getNode(E element)   {
        Node<E> node = this.root;
        while(node!=null){
            int compare = compare(node.element, element); // 节点值和 传入值比较
            if(compare<0){
                node = node.right;
            }else if(compare>0){
                node = node.left;
            }else{
                return node;
            }
        }
        return null;
    }

    public void remove(Node<E> node){
        if(node ==null){
            return ;
        }
        size--;
        //度为2的节点
        if(node.hasTwoChildren()){
            Node<E> predecessor = successor(node); //找到前驱节点即可
            //覆盖
            node.element = predecessor.element;
            //删除前驱
//            node = predecessor; //????? 不懂  那不乱了吗？
            /*
              传入node 其实就是 Node node = 传入值
              现在我 node = 新值
              对原来的内存地址没任何影响
             */
            node = predecessor;
        }
        //删除node节点 (node的度一定为1或者0)
        Node<E> replacement = node.left != null ? node.left : node.right;
        if(replacement!=null){
            //node是度为1
            replacement.parent = node.parent;
            if(node.parent==null){ // root
               root = replacement;
            }else if(node==node.parent.left){
                node.parent.left = replacement;
            }else if(node==node.parent.right){
                node.parent.right = replacement;
            }
            afterRemove(node,replacement); // 恢复平衡
            /**
             *  node目前并没有被删除，等会儿才被回收，但node.parent 还是指向的
             *  replacement.parent 所以 afterRemove传入 node，
             *  然后用node.parent进行平衡检测，恢复平衡时可行的
             */
        }else if(node==root){
            //node是叶子节点且是根节点
            root = null;
        }else{
            //node是叶子节点 一定有父亲
            if(node == node.parent.right){
                node.parent.right = null;

            }else{ //node == node.parent.left
                node.parent.left=null;

            }
            afterRemove(node,null);
        }
    }

    //恢复平衡，给avl使用的
    //恢复红黑树性质，给红黑树使用
    public void afterRemove(Node<E> node,Node<E> preplacement){

    }



    public boolean contains(E element) {
        Node<E> node = getNode(element);
        if(node!=null){
            return true;
        }
        return  false;
    }


}
