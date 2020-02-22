package K.集合.set;

import java.util.LinkedList;
import java.util.List;

/**
 * @Author: edison
 * @Date: 2020/2/13 09时28分
 * @Description:    采用 linkedList实现
 */
public class ListSet<E> implements Set<E> {
    private List<E> list = new LinkedList<>();

    @Override
    public int size() {
        return
                list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public boolean contains(E element) {
          return   list.contains(element);
    }

    @Override
    public void add(E element) {
       /* if(list.contains(element)){  //增加
            list.add(element);
            return;
        }*/
        int index = list.indexOf(element);   //替换
        if(index != -1){
            list.set(index,element);
        }else{
            list.add(element);
        }
    }

    @Override
    public void remove(E element) {
        int index = list.indexOf(element);   //替换
        if(index != -1){
           list.remove(element);
        }

    }

    @Override
    public void traversal(Visitor<E> visitor) {   //遍历  需要传入visitor
        if(visitor == null) return;
        int size = list.size();
        for(int i=0;i<size;i++){         // ??? 这种遍历也太慢了把 没有 iterator ？？ 不太行
            if(visitor.visit(list.get(i))) return;
        }
    }
}
