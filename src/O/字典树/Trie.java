package O.字典树;

import M.哈希表.HashMap;

public class Trie<V> {
    private int size;
    private Node<V> root = new Node<>();

    public int size() {
        return  size;
    }

    public boolean isEmpty() {
            return size==0;
    }

    public void clear() {
        size=0;
        root = null;
    }

    public V get(String key){

        Node<V> node = getNode(key);
        return node==null? null : node.value;
    }

    public Node<V> getNode(String key){  //从key获取node
        if(root==null) return null;
        keyCheck(key);
        Node<V> node = this.root;
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
           node = node.getChildren().get(c); //递归，一直向下找
           if(node==null) return null;

        }

        return node.word ? node : null;
    }

    public boolean  contains(String key){
        return getNode(key)!=null;
    }

    public V add(String key,V value){
        keyCheck(key);

        Node<V> node = this.root;  //此时 root还没 创建hashmap
        for (int i = 0; i <key.length();i++) {
            char c = key.charAt(i);
           Node<V> childNode = node.getChildren().get(c);
            if(childNode==null){
                childNode = new Node<>();
                node.getChildren().put(c,childNode);
                node = childNode;
            }else{
                node = childNode;
            }
        }

        if(node.word==false){  //新增一个单词
            node.word=true;
            node.value =  value;
            size++;
            return  null;
        }
        //存在
        V oldValue = node.value;
        node.value = value;
        return oldValue;

    }


    public boolean  startWith(String key){
        keyCheck(key);
        Node<V> node = this.root;
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            node = node.getChildren().get(c);
            if(node==null) return false;
        }
        return true;
    }

    public V remove(String key){
        keyCheck(key);
        Node<V> node = this.root;
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            node = node.getChildren().get(c);
            if(node==null) return null;
        }
        if(node.word==true){
            V v = node.value;
            node.value=null;
            return v;
        }
        return null;
    }

    public void keyCheck(String key){
        if(key==null || key.length()==0){
            throw new IllegalArgumentException("传入参数不合法");
        }
    }


    public static class Node<V>{
        boolean  word;  //判断是否到了单词的结尾
        HashMap<Character,Node<V>> children;
        V value;



        public HashMap<Character, Node<V>> getChildren() {  //获取children
            return children==null? (children = new HashMap<>()) : children;
        }
    }
}
