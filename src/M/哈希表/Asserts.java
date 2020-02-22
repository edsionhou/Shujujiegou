package M.哈希表;

public class Asserts {
	public static void test(boolean value) {
		try {
			System.out.println("value"+value);
			if (!value) throw new Exception("测试未通过");
			System.out.println("通过");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
