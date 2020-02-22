package leetcode.二叉树;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @Author: edison
 * @Date: 2020/2/8 20时56分
 * @Description:
 */
public class 反转二叉树 {
    //先序遍历
    public TreeNode reverseTree(TreeNode root) {
        if (root == null) {
            return root;
        }
        //交换左右
        TreeNode tmp = root.left;
        root.left = root.right;
        root.right = tmp;
        reverseTree(root.left);
        reverseTree(root.right);
        return root;
    }

    //后序也可以

    //中序
    public TreeNode reverseTree2(TreeNode root) {
        if (root == null) {
            return root;
        }
        reverseTree(root.left);
        //交换左右
        TreeNode tmp = root.left;
        root.left = root.right;
        root.right = tmp;

        reverseTree(root.left);
        return root;
    }

    //层序遍历
    public TreeNode reverseTree3(TreeNode root) {
        if (root == null) {
            return root;
        }
        Queue<TreeNode>  queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()){
            TreeNode node = queue.poll();

            TreeNode tmp = node.left;
            node.left = node.right;
            node.right = tmp;

            if(node.left!=null){
                queue.offer(node.left);
            }
            if(node.right!=null){
                queue.offer(node.right);
            }
        }

        return root;
    }
}
