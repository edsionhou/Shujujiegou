package M.哈希表;


import I.二叉搜索树.printer.BinaryTreeInfo;
import I.二叉搜索树.printer.BinaryTrees;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

/**
 * @Author: edison
 * @Date: 2020/2/14 15时06分
 * @Description:
 */
public class HashMap<K, V> implements Map<K, V> {
    private static final boolean RED = false;   //红色 = false
    private static final boolean BLACK = true; // 黑色 = true
    private int size;
    public Node<K, V>[] table;   // 数组 每棵红黑树的 根节点
    private static final int DEFAULT_CAPACITY = 1 << 4;   // 1 0000    = 16
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;


    public HashMap() {
        this.size = 0;
        this.table = new Node[DEFAULT_CAPACITY]; // 2^4  16
    }

    public HashMap(int count) {
        size = 0;
        this.table = new Node[count];
    }

    //这方法不能用 ！！！！
    public int compare(K e1, K e2, int e1code, int e2code) {  //  hashmap的比较规则   null = 0
        /*int h1 = e1 == null? 0 : e1.hashCode();
        int h2 = e2 == null? 0 : e2.hashCode();   不好！！！  计算太多次了，hashcode计算比较复杂
*/
        //先比 hashCode     不等直接分出大小
        int result = e1code - e2code;
        if (result != 0) return result;

        //hashCode相等
        // 比较 equals
        if (e1 == null ? e2 == null : e1.equals(e2)) return 0;  //1 都为null 0-0 √   3 两个都不为null  √
        //equals相等，必然同类
        // 剩下hashCode等 equals 不等
        // 以及相等时 2，一个不为null 但code=0，一个为null
        // equals不等 比较类名大小   但还是存在情况 2
        if (e1 != null && e2 != null && e1.getClass() == e2.getClass()
                && e1 instanceof Comparable) {
           /* String name1 = e1.getClass().getName();
            String name2 = e2.getClass().getName();
            result = name1.compareTo(name2);   //必能比出不同类String的大小
            if (result != 0) return result;
            //剩下  同类
            */
            //如果同类 且能比较
            if (e1 instanceof Comparable) {
                return ((Comparable) e1).compareTo(e2);
            }
        }
        //剩下  1 同类，不能比较  hashCode等 equals不等  2 一个不为null 但code=0，一个为null
        // 比较内存地址  null的内存地址为0
        return System.identityHashCode(e1) - System.identityHashCode(e2);

    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        if (size == 0) return;
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
    }

    @Override
    public V put(K key, V value) {
       resize();

        int index = index(key);
        //取出index处的红黑树节点
        Node<K, V> root = table[index];
        if (root == null) {
//            root = new Node<>(key, value, null);
            root = createNode(key,value,null);
            table[index] = root;
            size++;
            afterPut(root);  //维护红黑树性质
            return null;
        }

        //来到这里   哈希冲突了
        //添加的不是root，找到parent
        Node<K, V> node = root;
        Node<K, V> parent = null;
        int com = 0; //保存记录的结果
        int keyhashCode = (key == null ? 0 : key.hashCode());   //传入key 的 hashCode
        Node<K, V> result = null;
        boolean searchKey = false;
        do {
            parent = node; //保存比较前的node,退出循环时作为parent
            K k2 = node.key;
            int k2hashCode = node.hash;

            if (keyhashCode > k2hashCode) {
                com = 1;
            } else if (keyhashCode < k2hashCode) {
                com = -1;
            } else if (Objects.equals(key, k2)) {
                com = 0;
            } else if (key != null && k2 != null && key instanceof Comparable
                    && key.getClass() == k2.getClass()) {
                int c = ((Comparable) key).compareTo(k2);
                if (c != 0) {
                    com = c;    //==0什么也不干
                }
            } else if (searchKey == true) {
//                com = System.identityHashCode(key) - System.identityHashCode(k2);
//                com = 1;
                com = System.identityHashCode(key) - System.identityHashCode(k2);
            } else if (searchKey == false) { //hashcode等 equals不等， 无法比较  甚至存在一个为null的情况
                //扫描，没得话 根据内存地址添加
                if ((node.left != null && (result = getNode(node.left, key)) != null)
                        || (node.right != null && (result = getNode(node.right, key)) != null)) {

                    //找到了Node
                    com = 0;
                    node = result;   //我们找到了result，需要覆盖result  而不是node，所以 reuslt指向node
                } else {
                    //找不到   随便选个地方加把
                    //细想：能来到else，必定扫描了了一遍没结果，如果进入下面的if，又要来一次while循环，
                    //又要递归扫描，扫描的路径都是一样的，没必要，所以加个标记。
                    searchKey = true;
                    com = System.identityHashCode(key) - System.identityHashCode(k2);

                }
            }


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
        } while (node != null);
        //判断新节点是哪个儿子
//        Node<K, V> newNode = new Node<>(key, value, parent);
        Node<K, V> newNode = createNode(key, value, parent);
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


    public Node<K,V> createNode(K key, V value, Node<K, V> parent){ //创建节点， 给linked子类重写
        return new Node<>(key,value,parent);
    }
    /*
      扩容  load_factor >0.75
      节点扩容后  新的index两种情况
      1.不变
      2. index = index + 旧容量
     */
    private void resize() {
        if (size / table.length <= DEFAULT_LOAD_FACTOR) return;
        Node<K, V>[] oldTable = this.table;
        this.table = new Node[oldTable.length << 1];

        Queue<Node<K, V>> queue = new LinkedList<>();
        for (int i = 0; i < oldTable.length; i++) {
            if(oldTable[i]==null) continue;
            queue.offer(oldTable[i]);
            while (!queue.isEmpty()) {
                Node<K, V> poll = queue.poll();
                if (poll.left != null) {
                    queue.offer(poll.left);
                }
                if (poll.right != null) {
                    queue.offer(poll.right);
                }

                moveNode(poll);

            }

        }

    }

    private void moveNode(Node<K, V> newNode) {       //挪动代码， 从old桶 转移到新桶
        //先清空node的关系，否则会混乱
        newNode.parent = null;
        newNode.left = null;
        newNode.right = null;
        newNode.color = RED;

        int index = index(newNode);
        //取出index处的红黑树节点
        Node<K, V> root = table[index];
        if (root == null) {
            root = newNode;
            table[index] = root;
//            size++;
            afterPut(root);  //维护红黑树性质
            return;
        }

        //来到这里   哈希冲突了
        //添加的不是root，找到parent
        Node<K, V> node = root;
        Node<K, V> parent = null;
        int com = 0; //保存记录的结果
        int keyhashCode = (newNode == null ? 0 : newNode.hashCode());   //传入key 的 hashCode
        K key = newNode.key;
        do {
            parent = node; //保存比较前的node,退出循环时作为parent
            K k2 = node.key;
            int k2hashCode = node.hash;

            if (keyhashCode > k2hashCode) {
                com = 1;
            } else if (keyhashCode < k2hashCode) {
                com = -1;
            } else if (key != null && k2 != null && key instanceof Comparable
                    && key.getClass() == k2.getClass()) {
                int c = ((Comparable) key).compareTo(k2);

                    com = c;    //不可能等0

            } else {   //挪动的时候，不可能存在哈希值相等，equals又等  不用扫描了
                com = System.identityHashCode(key) - System.identityHashCode(k2);
            }


            if (com > 0) {
                node = node.right;
            } else if (com < 0) {
                node = node.left;
            } else {
                //不可能有等于0的情况
            }
        } while (node != null);
        //判断新节点是哪个儿子
        if (com > 0) {
            parent.right = newNode;
        } else {
            parent.left = newNode;
        }
        newNode.parent = parent;    //添加上parent
        afterPut(newNode);
    }

    @Override
    public V get(K key) {

        Node<K, V> node = getNode(key);

        return node == null ? null : node.value;
    }

    public Node<K, V> getNode(K k1) {
        int index = index(k1);
//        System.out.println("get方法查找的root: " + table[index]);
        Node<K, V> root = table[index];
        return root == null ? null : getNode(root, k1);
    }

    private Node<K, V> getNode(Node<K, V> node, K k1) {   //扫描方法 递归遍历节点左右子树是否有相同的key
        int k1HashCode = k1 == null ? 0 : k1.hashCode();
        Node<K, V> result = null;
        while (node != null) {
            int k2hashCode = node.hash;
            K k2 = node.key;

            if (k1HashCode > k2hashCode) {
                node = node.right;
            } else if (k1HashCode < k2hashCode) {
                node = node.left;
            } else if (k1 == null ? k2 == null : k1.equals(k2)) {
                return node;
            } else if (k1 != null && k2 != null && k1 instanceof Comparable
                    && k1.getClass() == k2.getClass()) {
                int com = ((Comparable) k1).compareTo(k2);
                node = com > 0 ? node.right : (com != 0 ? node.left : node);

            } else if (node.right != null && (result = getNode(node.right, k1)) != null) {
                return result;
//            } else if (node.left != null && (result = getNode(node.left, k1)) != null) {
//                return result;
//            } else {
//                return null;
//            }
            } else {
                node = node.left; //优化
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(K key) {

        return getNode(key) != null;
    }

    @Override
    public V remove(K key) { //从TreeMap移植的
        Node<K, V> node = getNode(key);  //获取实际删除的Node
        Node<K,V> willNode = node;      //给linkedhashmap使用的
        if (node == null) return null;
        size--;
        V oldVlue = node.value;
        //度为2的节点
        if (node.hasTwoChildren()) {
            Node<K, V> predecessor = successor(node); //找到后继节点即可
            //覆盖
            node.key = predecessor.key;
            node.value = predecessor.value;
            node.hash = predecessor.hash;   //这个值之前忘写了！
            //删除后继
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

        int index = index(node);
        if (replacement != null) {  //node是度为1
            replacement.parent = node.parent;
            if (node.parent == null) { // root
                table[index] = replacement;
            } else if (node == node.parent.left) {
                node.parent.left = replacement;
            } else if (node == node.parent.right) {
                node.parent.right = replacement;
            }
            afterRemove(node, replacement); // 恢复平衡

            /**
             *  node目前并没有被删除，等会儿才被回收，但node.parent 还是指向的replacement.parent
             *  所以 afterRemove传入 node，
             *  然后用node.parent进行平衡检测，恢复平衡是可行的
             */
        } else if (node == table[index]) {  //度为0
            //node是叶子节点且是根节点
            table[index] = null;

        } else {  //node是叶子节点 一定有父亲  度为 0

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
        //交给 LinkedHashmap去实现
        linkedAfterRemove(willNode,node); //要删除的node  实际被删除的node

        return oldVlue;

    }
    public  void linkedAfterRemove(Node<K, V> willNode,Node<K,V> removeNode){

    }

    public void afterRemove(Node<K, V> node, Node<K, V> replacement) {
        if (isRed(node)) {
            return;  //红色不用处理 R-B-R   B-R R-B模式
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
    public boolean containsValue(V value) {      //是否包含value  必须全部遍历
        if (size == 0) return false;

        for (int i = 0; i < table.length; i++) {
            if (table[i] == null) continue;
            Queue<Node<K, V>> queue = new LinkedList<>();
            queue.offer(table[i]);
            while (!queue.isEmpty()) {
                Node<K, V> node = queue.poll();
                if (Objects.equals(node.value, value)) return true;
                //这居然是左边的小黄灯自动提示的！ 跟那个vscode功能一样啊
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
        }

        return false;
    }

    @Override
    public void traversal(Visitor<K, V> visitor) {     //遍历!!!
        //我们设计的visitor是让用户随时可以停止遍历  或者打印输出
        if (size == 0) return;

        for (int i = 0; i < table.length; i++) {
            if (table[i] == null) continue;
            Queue<Node<K, V>> queue = new LinkedList<>();
            queue.offer(table[i]);
            while (!queue.isEmpty()) {
                Node<K, V> node = queue.poll();
                if (visitor.visit(node.key, node.value)) return;
                //这居然是左边的小黄灯自动提示的！ 跟那个vscode功能一样啊
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
        }
    }

    /*
            //生成索引
     */
    public int index(K key) {
        if (key == null) return 0;
        int hash = key.hashCode();
        hash = hash ^ (hash >>> 16);   //jdk为了保险 又做了一次无符号右移
        return hash & (table.length - 1);
    }

    private int index(Node<K, V> node) {
        if (node == null) return 0;

        int hash = node.hash ^ (node.hash >>> 16);   //jdk为了保险 又做了一次无符号右移
        return hash & (table.length - 1);
    }

    protected Node<K, V> successor(Node<K, V> node) {  //后继节点
        if (node == null) return null;

        // 前驱节点在左子树当中（right.left.left.left....）
        Node<K, V> p = node.right;
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
//            root = parent; //已经修改过parent的爹了 parent.parent = grand.parent
            table[index(grand)] = parent;  // 这几个node 的index都是一样  选哪个都行
        }
        grand.parent = parent;  //g
        if (child != null) {
            child.parent = grand;//t1

        }

    }

    public void print() {
        if (size == 0) return;
        for (int i = 0; i < table.length; i++) {
            Node<K, V> root = table[i];
            System.out.println("index等于 "+i +"  "+ table[i]);
            BinaryTrees.println(new BinaryTreeInfo() {
                @Override
                public Object root() {
                    return root;
                }

                @Override
                public Object left(Object node) {
                    Node<K, V> left = ((Node<K, V>) node).left;
                    return left;
                }

                @Override
                public Object right(Object node) {
                    Node<K, V> right = ((Node<K, V>) node).right;
                    return right;
                }

                @Override
                public Object string(Object node) {
                    return node;
                }
            });
        }

    }

    //--------------  红黑树的 染色------------------------
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

    //---------------------------NODE  红黑树node节点----------------------
    public static class Node<K, V> {
        K key;
        V value;
        int hash;  //hashCode 新建Node时就计算出key的hashCode 减少以后用到时的计算开销
        public boolean color = RED;  //建议新加的节点都是 red
        public Node<K, V> left;
        public Node<K, V> right;
        public Node<K, V> parent;

        public Node(K key, V value, Node<K, V> parent) {   //一个新节点，包括K V  parent
            this.key = key;
            this.hash = key == null ? 0 : key.hashCode();   //计算key的 hashcode 保存下来
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

        @Override
        public String toString() {
            return "{ key=" + key +
                    ", value=" + value + "[" + (color = (color == false ? RED : BLACK)) + "]" +
                    '}';
        }
    }


}
