package M.哈希表;

/**
 * @Author: edison
 * @Date: 2020/2/14 10时32分
 * @Description:
 */
public class 哈希值计算 {
    public static void main(String[] args) {
        System.out.println(Integer.toBinaryString(-8));
        System.out.println(-8>>>2);
        System.out.println(Integer.toBinaryString((-8>>>2)));

        Integer a  =110;
        Float b = 10.6f;
        Long c = 1561L;
        Double d = 10.9;
        a.hashCode();            // return value;
        b.hashCode();       //floatToIntBits(value)    float会被计算出一个整数值
        c.hashCode();     //(int)(value ^ (value >>> 32));
        d.hashCode();   // long bits= doubleToLongBits(value);   -->>(int)(bits ^ (bits >>> 32));

    Person p1 =new Person(10,167.1f,"jack");
    Person p2 =new Person(10,167.1f,"jack");
    System.out.println("哈希值"+p1.hashCode());
    System.out.println("哈希值"+p2.hashCode());
    System.out.println(p1);
    }
}
