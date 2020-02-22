package M.哈希表;

/**
 * @Author: edison
 * @Date: 2020/2/14 10时32分
 * @Description:
 */
public class Person {
    private int age;
    private float height;
    private String name;

    public Person(int age, float height, String name) {
        this.age = age;
        this.height = height;
        this.name = name;
    }


    @Override
    public int hashCode() {   //自定义hashCode   整型溢出也无所谓  负数更无所谓，
                                // 最终   hashCode &  talbe.length-1  总小于 table.length
       int hashCode = Integer.hashCode(age); // = age;
       hashCode = hashCode*31 +Float.floatToIntBits(height);
       hashCode = hashCode*31 + (name != null ? name.hashCode():0);
       return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if(this==obj) return true; //比较内存地址
        if(obj==null) return false; // null
        if(obj.getClass()!=this.getClass()) return false;  //是相同的类
//        if(obj==null|| !(obj instanceof Person)) return false;    不建议这个 这个真不好
        //比较 成员变量
        Person person = (Person) obj;
        return person.age==age &&  person.height==height &&
                (person.name==null ? name ==null : person.name.equals(name));
    }


}
