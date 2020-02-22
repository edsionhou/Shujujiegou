package M.哈希表;

/**
 * @Author: edison
 * @Date: 2020/2/14 17时53分
 * @Description:
 */
public class MapMain {
   public static void main(String[] args) {
      Person p1 = new Person(18,1.1f,"h1");
      Person p2 = new Person(18,1.1f,"h1");
      Person p3 = new Person(18,1.1f,"h3");
       Map<Object,Integer> map = new HashMap<>();
       map.put(p1,1);
       map.put(p2,2);
       map.put(p3, 3);
       map.put("jack",3);
       map.put("rose",3);
       map.put("jack",4);
       map.put(null,55);
      /*  System.out.println(map.size());
       Integer jack = map.remove("jack");
       System.out.println(jack);
       System.out.println(map.size());
       System.out.println(map.get("jack"));*/

      System.out.println(map.containsKey(p1));
      System.out.println(map.containsKey(null));
      System.out.println(map.containsValue(1));
      System.out.println(map.containsValue(55));

        System.out.println("==========================");

        map.traversal(new Map.Visitor<Object, Integer>() {
            @Override
            public boolean visit(Object key, Integer value) {
                System.out.println(key+"_"+value);
                return false;
            }
        });
       System.out.println("==========================");
//        test1();
       test2();
   }
   public static void test1(){
       HashMap<Object,Integer> map = new HashMap<>();
       for (int i = 0; i <10 ; i++) {
           map.put(new Key(i),i);
       }
      map.print();
       map.put(new Key(2),20);
       System.out.println(map.get(new Key(2)));
       System.out.println(map.get(new Key(7)));
   }
    public static void test2(){
        HashMap<Object,Integer> map = new HashMap<>();

        System.out.println("测试："+map.size());
        System.out.println("jack".hashCode());
    }



}
