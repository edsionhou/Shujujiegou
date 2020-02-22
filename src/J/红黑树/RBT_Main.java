package J.红黑树;

import I.二叉搜索树.printer.BinaryTrees;

/**
 * @Author: edison
 * @Date: 2020/2/10 18时06分
 * @Description:
 */
public class RBT_Main {
    public static void main(String[] args) {
        Integer data[] = new Integer[] {
                55,87,56,74,96,22,62,20,70,68,90,50
        };

        RedBlackTree<Integer> tree = new RedBlackTree<>();
        for (int i = 0; i < data.length; i++) {
            tree.add(data[i]);
          /*  System.out.println("["+data[i]+"]");
            BinaryTrees.println(tree);
            System.out.println("---------------------------");*/

        }



        BinaryTrees.print(tree);

        System.out.println();
        for (int i = 0; i < data.length; i++){
            tree.remove(data[i]);
            System.out.println("["+data[i]+"]");

            BinaryTrees.println(tree);
            System.out.println("---------------------");
        }




    }
}
