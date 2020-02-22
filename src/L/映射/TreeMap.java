package L.映射;


import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @Author: edison
 * @Date: 2020/2/13 11时12分
 * @Description: 采用红黑树 来实现 TreeMap
 * 链表  数组 太慢了   AVL树没红黑树好，唯一的缺点： 元素可比较 or  传入 比较器
 *
 *  TreeMap 就可以去实现Set  当作TreeSet
 *  需要改动的地方就是 Node<K,Object>  全程只使用 Key  value置为空即可。
 */
public class TreeMap<K, V> implements Map<K, V> {
    private static final boolean RED = false;   //红色 = false
    private static final boolean BLACK = true; // 黑色 = true
    protected Comparator<K> comparator; //比较器  用K比较
    protected int size;
    protected Node<K, V> root;

    public TreeMap(Comparator<K> comparator) {
        this.comparator = comparator;
    }

    public TreeMap() {
        this(null);
    }

    public int compare(K e1, K e2) {  //有传入比较器，就用比较器，没有 用Comparable
        if (comparator != null) {
            return comparator.compare(e1, e2);
        }

        int result = ((Comparable<K>) e1).compareTo(e2);
        //如果 E 没有实现Comparable，那正符合我们的意思，此对象没有定义比较规则，报错
        return result;

    }

    @Override
    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;

    }

    public void clear() {
        root = null;
        size = 0;
    }


    //--------------------------------------------------------------------------------------
    public void keyNoNullCheck(K elememt) {  //测试添加的元素是否为null
        if (elememt == null) {
            throw new IllegalArgumentException("元素不能为null");
        }
    }

    @Override
    public V put(K key, V value) {  //从二叉搜索树移植
        keyNoNullCheck(key);
        //添加的root节点
        if (root == null) {
            root = new Node<>(key, value, null);
            size++;
            afterPut(root);
            return null;
        }
        //添加的不是root，找到parent
        Node<K, V> node = root;
        Node<K, V> parent = null;
        int com = 0; //保存记录的结果
        while (node != null) {
            parent = node; //保存比较前的node,退出循环时作为parent
            com = compare(key, node.key);
            if (com > 0) {
                node = node.right;
            } else if (com < 0) {
                node = node.left;
            } else { //相等  替换掉原来的node里的数据
                V oldv = node.value;
                node.key = key;
                node.value = value;
                return oldv;
            }
        }
        //判断新节点是哪个儿子
        Node<K, V> newNode = new Node<>(key, value, parent);
        if (com > 0) {
            parent.right = newNode;
        } else {
            parent.left = newNode;
        }
        size++;
        afterPut(newNode);
        return null;
    }

    //修复红黑树性质的  afterPut
    public void afterPut(Node<K, V> node) {
        Node<K, V> parent = node.parent;

        if (parent == null) { //加的是根节点  不可调换顺序
            makeBlack(node);
            return;
        }
        if (isBlack(parent)) { //如果父节点是黑色，不用处理 4种
            return;
        }

        //8种  parent是 RED
        //前4种，uncle是红
        Node<K, V> uncle = parent.sibling();
        Node<K, V> grand = parent.parent;
        if (isRed(uncle)) {  //uncle是红
            // parent uncle染黑
            makeBlack(parent);
            makeBlack(uncle);
            //祖父 一定上溢，当作新节点刚添加完  看待
            makeRed(grand);
            afterPut(grand);
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


    @Override
    public V get(K key) {
        Node<K, V> node = getNode(key);
        if(node!=null){
            return node.value;
        }
        return null;
    }
    public Node<K,V> getNode(K key){
        Node<K, V> node = this.root;
        while (node != null) {
            int compare = compare(node.key, key); // 节点值和 传入值比较
            if (compare < 0) {
                node = node.right;
            } else if (compare > 0) {
                node = node.left;
            } else {
                return node;
            }
        }
        return null;
    }

    //---------------------------
    @Override
    public V remove(K key) {   //从二叉搜索树移植
        if (key == null) {
            return null;
        }
        size--;
        Node<K, V> node = getNode(key);
        V oldVlue = node.value;
        //度为2的节点
        if (node.hasTwoChildren()) {
            Node<K, V> predecessor = successor(node); //找到前驱节点即可
            //覆盖
            node.key = predecessor.key;
            node.value = predecessor.value;
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
        Node<K, V> replacement = node.left != null ? node.left : node.right;
        if (replacement != null) {
            //node是度为1
            replacement.parent = node.parent;
            if (node.parent == null) { // root
                root = replacement;
            } else if (node == node.parent.left) {
                node.parent.left = replacement;
            } else if (node == node.parent.right) {
                node.parent.right = replacement;
            }
            afterRemove(node, replacement); // 恢复平衡


            /**
             *  node目前并没有被删除，等会儿才被回收，但node.parent 还是指向的
             *  replacement.parent 所以 afterRemove传入 node，
             *  然后用node.parent进行平衡检测，恢复平衡时可行的
             */
        } else if (node == root) {
            //node是叶子节点且是根节点
            root = null;
            return null;
        } else {
            //node是叶子节点 一定有父亲
            if (node == node.parent.right) {
                node.parent.right = null;

            } else { //node == node.parent.left
                node.parent.left = null;

            }
            afterRemove(node, null);
        }
        /*
               最终  返回的是node最初的值，如果用后继节点替代了，
               需要先保存old值， 最后返回old
         */
        return oldVlue;

    }
    public void afterRemove(Node<K, V> node, Node<K, V> replacement) {
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
        Node<K, V> parent = node.parent;
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
        Node<K, V> sibling = left ? parent.right : parent.left;
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

    @Override
    public boolean containsKey(K key) {
        Node<K,V> node = getNode(key);
        if(node!=null){
            return true;
        }
        return  false;
    }

    @Override
    public boolean containsValue(V value) {
        if(root ==null) return false;

        //我们通过层序遍历
        Queue<Node<K,V>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()){
            Node<K, V> node = queue.poll();
            if(valEquals(node.value, value)){
                return true;
            }
            if(node.left!=null){
                queue.offer(root.left);
            }
            if(node.right!=null){
                queue.offer(root.right);
            }
        }


        return false;
    }
    public boolean valEquals(V v1,V v2){
        return v1==null? v2==null:v1.equals(v2);
    }

    @Override
    public void traversal(Visitor visitor) {
        if(root==null|| visitor==null) return;
        traversal(root,visitor);


    }
    private  void traversal(Node<K,V> node,Visitor visitor) {  //中序测试
        if(node==null||visitor.stop==true) return;



            traversal(node.left,visitor);
            if(visitor.stop) return;
            visitor.visit(node.key, node.value);
            traversal(node.right,visitor);




    }

    //==================================旋转 ==========================
    protected void rotateLeft(Node<K, V> grand) {  //  旋转 L
        Node<K, V> parent = grand.right;
        Node<K, V> child = parent.left;
        grand.right = child;
        parent.left = grand;
        afterRotate(grand, parent, child);

    }

    protected void rotateRight(Node<K, V> grand) {  //旋转 R
        Node<K, V> parent = grand.left;
        Node<K, V> child = parent.right;

        grand.left = child;
        parent.right = grand;
        afterRotate(grand, parent, child);

    }

    //Rotate 旋转的第二个步骤 提取出来公共的
    public void afterRotate(Node<K, V> grand, Node<K, V> parent, Node<K, V> child) {
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
//==============================================================================================

    public Node<K, V> color(Node<K, V> node, boolean color) {   //  染色方法
        if (node == null) {
            return node;
        }
        node.color = color;
        return node;
    }

    public Node<K, V> makeRed(Node<K, V> node) {   //染成 RED
        return color(node, RED);
    }

    public Node<K, V> makeBlack(Node<K, V> node) {  //染成 BLACK
        return color(node, BLACK);
    }

    public boolean colorOf(Node<K, V> node) {   //判断颜色， 空 = black， 否则 返回 自己的颜色
        return node == null ? BLACK : node.color;
    }

    public boolean isBlack(Node<K, V> node) {  //  isBLACK ??
        return colorOf(node) == BLACK;
    }

    public boolean isRed(Node<K, V> node) {  //is RED??
        return colorOf(node) == RED;
    }

    protected Node<K,V> successor(Node<K,V> node) {  //后继节点
        if (node == null) return null;

        // 前驱节点在左子树当中（right.left.left.left....）
        Node<K,V> p = node.right;
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
    //treemap的Node
    private static class Node<K, V> {
        K key;
        V value;
        public boolean color = RED;  //建议新加的节点都是 red
        public Node<K, V> left;
        public Node<K, V> right;
        public Node<K, V> parent;

        public Node(K key, V value, Node<K, V> parent) {   //一个新节点，包括K V  parent
            this.key = key;
            this.value = value;
            this.parent = parent;
        }

        public boolean isLeaf() {
            return left == null && right == null;
        }

        public boolean hasTwoChildren() {
            return left != null && right != null;
        }

        public boolean isLeftChild() {
            return parent != null && this == parent.left;
        }

        public boolean isRightChildren() {
            return parent != null && this == parent.right;
        }

        public Node<K, V> sibling() { //兄弟节点
            if (isLeftChild()) {
                return parent.right;
            }
            if (isRightChildren()) {
                return parent.left;
            }
            return null;
        }
    }
}
