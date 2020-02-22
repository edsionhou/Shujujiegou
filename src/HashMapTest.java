/**
 * @Author: edison
 * @Date: 2020/2/7 20时26分
 * @Description:
 */
public class HashMapTest {

    public static void main(String[] args) {
        System.out.println(8>>1);
            System.out.println(Integer.toBinaryString(-8));
            System.out.println(-8>>>2);
            System.out.println(Integer.toBinaryString((-8>>>2)));

            Integer a  =110;
            Float b = 10.6f;
            Long c = 1561L;
            Double d = 10.9;
           a.hashCode();            // return value;
           b.hashCode();       //floatToIntBits(value)
           c.hashCode();     //(int)(value ^ (value >>> 32));
            d.hashCode();   // long bits= doubleToLongBits(value);   -->>(int)(bits ^ (bits >>> 32));

    }
}
