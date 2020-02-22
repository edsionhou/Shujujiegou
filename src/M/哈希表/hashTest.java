package M.哈希表;

/**
 * @Author: edison
 * @Date: 2020/2/15 20时55分
 * @Description:
 */
public class hashTest {
    public static void main(String[] args) throws InterruptedException {
        HashMap<Object, Integer> map = new HashMap<>();
     /*   System.out.println(map.index("jake"));
        System.out.println(map.index("test4"));
        test2(map);
        map.clear();
        test3(map);
        map.clear();
        test4(map);
        map.clear();
//      test10(map);*/
//        test5(map);
//        map.print();
       test6();
    }


    static void test2(HashMap<Object, Integer> map) {
        for (int i = 1; i <= 20; i++) {
            map.put(new Key(i), i);
        }
        for (int i = 5; i <= 7; i++) {
            map.put(new Key(i), i + 5);
        }
        Asserts.test(map.size() == 20);
        Asserts.test(map.get(new Key(4)) == 4);
        Asserts.test(map.get(new Key(5)) == 10);
        Asserts.test(map.get(new Key(6)) == 11);
        Asserts.test(map.get(new Key(7)) == 12);
        Asserts.test(map.get(new Key(8)) == 8);
    }

    static void test3(HashMap<Object, Integer> map) {
        map.put(null, 1); // 1
        map.put(new Object(), 2); // 2
        map.put("jack", 3); // 3
        map.put(10, 4); // 4
        map.put(new Object(), 5); // 5
        map.put("jack", 6);
        map.put(10, 7);
        map.put(null, 8);
        map.put(10, null);
        Asserts.test(map.size() == 5);
        Asserts.test(map.get(null) == 8);
        Asserts.test(map.get("jack") == 6);
        Asserts.test(map.get(10) == null);
        Asserts.test(map.get(new Object()) == null);
        Asserts.test(map.containsKey(10));
        Asserts.test(map.containsKey(null));
        Asserts.test(map.containsValue(null));
        Asserts.test(map.containsValue(1) == false);
    }

    static void test10(HashMap<Object, Integer> map) throws InterruptedException {
        map.put("jack", 1);
        map.put("rose", 2);
        map.put("jim", 3);
        map.put("jake", 4);
        map.remove("jack");
        map.remove("jim");
        for (int i = 1; i <= 10; i++) {
            map.put("test" + i, i);
            map.put(new Key(i), i);
            map.print();
        }
//        Thread.sleep(3000);

        map.print();
        System.out.println("================================");

        for (int i = 5; i <= 7; i++) {
            System.out.println("====");
//            System.out.println("测试结果" + i + "   " + (map.remove(new Key(i)) == i));
//            System.out.println(map.remove(new Key(i)));

            System.out.println("1================================");
            System.out.println("get测试一下"+map.get(new Key(i)));
//            Object t =map.remove(new Key(i));
//            System.out.println("删除了"+i+"返回了"+t);
            map.print();
//            Asserts.test((map.remove(new Key(i)) == i));
            Asserts.test(map.remove(new Key(i))== i);
        }
       /* Asserts.test((map.remove(new Key(5)) == 5));
        Asserts.test((map.remove(new Key(6)) == 6));
        map.print();*/
    }

    static void test4(HashMap<Object, Integer> map) {
        map.put("jack", 1);
        map.put("rose", 2);
        map.put("jim", 3);
        map.put("jake", 4);
        map.remove("jack");
        map.remove("jim");
        for (int i = 1; i <= 10; i++) {
            map.put("test" + i, i);
            map.put(new Key(i), i);
        }
        for (int i = 5; i <= 7; i++) {
            Asserts.test(map.remove(new Key(i)) == i);
        }
        for (int i = 1; i <= 3; i++) {
            map.put(new Key(i), i + 5);
        }
        Asserts.test(map.size() == 19);
        Asserts.test(map.get(new Key(1)) == 6);
        Asserts.test(map.get(new Key(2)) == 7);
        Asserts.test(map.get(new Key(3)) == 8);
        Asserts.test(map.get(new Key(4)) == 4);
        Asserts.test(map.get(new Key(5)) == null);
        Asserts.test(map.get(new Key(6)) == null);
        Asserts.test(map.get(new Key(7)) == null);
        Asserts.test(map.get(new Key(8)) == 8);
        map.traversal(new Map.Visitor<Object, Integer>() {
            public boolean visit(Object key, Integer value) {
                System.out.println(key + "_" + value);
                return false;
            }
        });
    }
    static void test5(HashMap<Object, Integer> map) {
        for (int i = 1; i <= 20; i++) {
            map.put(new SubKey1(i), i);
        }
        map.put(new SubKey2(1), 5);
        Asserts.test(map.get(new SubKey1(1)) == 5);
        Asserts.test(map.get(new SubKey2(1)) == 5);
        Asserts.test(map.size() == 20);
    }
    static void test6() {
        HashMap<Object,Integer> map = new LinkedHashMap<>();
        map.put("jack", 1);
        map.put("rose", 2);
        map.put("jim", 3);
        map.put("jake", 4);
//        map.remove("jack");
//        map.remove("jim");
        for (int i = 1; i <= 10; i++) {
            map.put("test" + i, i);
            map.put(new Key(i), i);

        }
        map.print();
        map.traversal(new Map.Visitor<Object, Integer>() {
            @Override
            public boolean visit(Object key, Integer value) {
                System.out.println(key+"_"+value);
                return false;
            }
        });
    }

}
