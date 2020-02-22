package K.集合.set;

/**
 * @Author: edison
 * @Date: 2020/2/13 09时40分
 * @Description:
 */
public class SetMain {
    public static void main(String[] args) {
        /*Set<Integer> listSet = new ListSet<>();
        listSet.add(1);
        listSet.add(2);
        listSet.add(3);
        listSet.add(4);
        listSet.traversal(new Set.Visitor<Integer>() {
            @Override
            public boolean visit(Integer element) {    //这种设计模式 我还没适应
                System.out.println(element);
                return element==3? true:false;
            }
        });*/
        Set<Integer> treeSet = new TreeSet2<>();
        treeSet.add(4);
        treeSet.add(3);
        treeSet.add(2);
        treeSet.add(1);
        treeSet.traversal(new Set.Visitor<Integer>() {
            @Override
            public boolean visit(Integer element) {
                System.out.println(element);
                return false;
            }
        });



    }
}
