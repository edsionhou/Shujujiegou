package AVL树;

import I.二叉搜索树.printer.BinaryTrees;

/**
 * @Author: edison
 * @Date: 2020/2/9 10时29分
 * @Description:
 */
public class Main {
    public static void main(String[] args) {

        Integer data[] = new Integer[] {
                40, 87, 93, 68, 71, 80, 48, 38, 63, 34, 39, 86, 43, 69, 98, 13, 82
        };

        AVLTree<Integer> tree = new AVLTree<>();
        for (int i = 0; i < data.length; i++) {
            tree.add(data[i]);
        }


        /*for (int i = 0; i < 10; i++) {
            Person person = new Person((int) ((Math.random()) * 100));
            tree.add2(person);
        }*/


       // BinaryTrees.print(tree);

        tree.remove(13);
        tree.remove(34);

        BinaryTrees.print(tree);

    }
}
