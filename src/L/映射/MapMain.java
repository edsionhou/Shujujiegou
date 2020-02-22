package L.映射;

/**
 * @Author: edison
 * @Date: 2020/2/13 13时17分
 * @Description:
 */
public class MapMain {
    public static void main(String[] args) {
       Map<String,Integer> map = new TreeMap<>();
        map.put("b",5);
       map.put("a",2);
        map.put("c",6);


        Integer aPublic = map.put("a", 8);
        System.out.println(aPublic);

        map.traversal(new Map.Visitor<String, Integer>() {
            @Override
            public boolean visit(String key, Integer value) {
                System.out.println(key+"_"+value);
                return false;
            }
        });
    }
}
