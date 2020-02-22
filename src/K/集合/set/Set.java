package K.集合.set;

/**
 * @Author: edison
 * @Date: 2020/2/13 09时25分
 * @Description:
 */
public interface Set<E> {
    int size();
    boolean isEmpty();
    void clear();
    boolean contains(E element);
    void add(E element);
    void remove(E element);
    void traversal(Visitor<E> visitor);
    //提供 一个方法。stop来制造停止遍历
    public static abstract class Visitor<E> {
        boolean stop;
        public abstract boolean visit(E element);
    }
}
