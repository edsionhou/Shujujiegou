package I.二叉搜索树;

/**
 * @Author: edison
 * @Date: 2020/2/8 14时50分
 * @Description:
 */
public class Person {
    private int age ;

    public Person(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return
                age+"";
    }
}
