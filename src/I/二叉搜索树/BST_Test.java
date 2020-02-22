package I.二叉搜索树;

import I.二叉搜索树.printer.BinaryTrees;

import java.util.Comparator;

/**
 * @Author: edison
 * @Date: 2020/2/8 14时51分
 * @Description:
 */
public class BST_Test {
    public static void main(String[] args) {
        Comparator<Person> comparator = new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return o1.getAge() - o2.getAge();
            }
        };

        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        /*for (int i = 0; i < 10; i++) {
           Person person = new Person((int) ((Math.random()) * 100));
//            Person person = new Person(i);

            tree.add2(person);

        }*/
       int[] arr = new int[]{
               1,2,3,4,5,6,7,8,9,10
       };
       for(int i=0;i<10;i++){
           tree.add(arr[i]);
       };


        /**
         * 根据 打印根节点数据的时机，分为 前 中 后序遍历
         *
         * 层序遍历  从打印根节点，左 右  依次来
         */
       /* tree.preOrderPrint();  //前序遍历，文件夹就是这种模式
        tree.inOrderPrint(); //中序遍历，结果居然是从小到大排序,或者从大到小排序，看你先遍历哪边的子树
        tree.postOrderPrint();*/

        /*//层序遍历 使用了设计模式
        tree.LevelOrder(new BinarySearchTree.Visitor<Person>() {

            @Override
            public void visitor(Person person) {
                System.out.print(" "+person.getAge());
            }
        });*/
              // 前序遍历 设计模式
       /* tree.preOrderPrintV(new BinarySearchTree.Visitor<Person>() {
            @Override
            public boolean visitor(Person person) {
                System.out.print("呵呵 "+person.getAge());
                return tree.bianli==4? true:false;
            }
        });*/

        System.out.println("=====");

     BinaryTrees.print(tree);
//        System.out.println(tree.toString());   使用前序来 打印
        System.out.println("=====");
       System.out.println("高度 "+tree.height());  // 使用 迭代返回高度
       System.out.println("高度 "+tree.height2());  //使用 层序遍历 返回高度

//        tree.remove(new Person(12));
        BinaryTrees.print(tree);
        System.out.println("\n"+"=====");


    }
}
