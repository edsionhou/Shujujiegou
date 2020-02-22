package N.二叉堆;

import I.二叉搜索树.printer.BinaryTrees;

import java.util.Comparator;

public class Main {
    public static void test1() {
        BinaryHeap<Integer> heap = new BinaryHeap<>();
        heap.add(68);
        heap.add(72);
        heap.add(43);
        heap.add(50);
        heap.add(38);
      BinaryTrees.print(heap);
      heap.replace(20);
      System.out.println();
        BinaryTrees.print(heap);

    }

    public static void main(String[] args) {
//        Integer[] data={88,44,53,41,16,6,70};
        Comparator<Integer> comparator = new Comparator<Integer>() {  //新建一个小顶堆
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2-o1;
            }
        };
        BinaryHeap<Integer> heap = new BinaryHeap<>(comparator);
        //找出前 K个最大的数
        Integer[] data = {76, 84, 5, 53, 62, 77, 44, 33, 2, 23, 99};
        int k=5;
        for (int i = 0; i <data.length ; i++) {
            if(heap.size()< k){   //先给小顶堆传5个值
                heap.add(data[i]);   //复杂度  logK
            }else{
                if(heap.get() < data[i]){
                    heap.replace(data[i]); //遍历其他的，替换堆顶    logK
                }
            }

        }
        //所以总的 复杂度  O(logK)

        BinaryTrees.print(heap);

    }
}
