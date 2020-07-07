package cn.adcc.client;

import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Null;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class CommonTest extends ClientApplicationTests {

    @Test
    public void testLastIndexOf() {
        String a1 = "";
        String a2 = "/a";
        String a3 = "/";
        String a4 = "/a/";

        System.out.println(a1.lastIndexOf("/"));
        System.out.println(a2.lastIndexOf("/"));
        System.out.println(a3.lastIndexOf("/"));
        System.out.println(a4.lastIndexOf("/"));

        System.out.println(a1.endsWith("/"));
        System.out.println(a2.endsWith("/"));
        System.out.println(a3.endsWith("/"));
        System.out.println(a4.endsWith("/"));
    }

    @Test
    void testContainsAll() {
        Set<Integer> a = new HashSet<>();
        a.add(1);
        a.add(2);
        a.add(3);
        Set<Integer> b = new HashSet<>();
        b.add(0);
        b.add(3);
        if (a.containsAll(b)) {
            System.out.println("success");
        } else {
            System.out.println("fail");
        }
    }

    @Test
    void testUUID() {
        String applyNum = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        System.out.println(applyNum);
        long uuid = UUID.randomUUID().timestamp();
        System.out.println(uuid);
    }

    @Test
    void testReplace() {
        String str = "aabcbabac";
        // bcbbc
        System.out.println(str.replace("a", ""));
        System.out.println(str);
        // abcbabac
        System.out.println(str.replaceFirst("a", ""));
    }

    @Test
    void listRemoveIndex() {
        // 不能使用remove(index)
        // List<String> strs = Arrays.asList("one", "two", "three");
        List<String> strs = new ArrayList<>();
        strs.add("one");
        strs.add("two");
        System.out.println(strs.remove(0));
        System.out.println(strs);
    }

    @Test
    void and() {
        boolean flag1 = true;
        boolean flag2 = false;
        if (!flag1 && flag2) {
            System.out.println("one success");
        }
        if ((!flag1) && flag2) {
            System.out.println("two success");
        }
    }

    @Test
    void splitTimes() {
        String str1 = "a->b->c";
        String str2 = "a->b";
        String str3 = "a";
        String SPLIT_SIGN = "->";
        // [a, b->c]
        System.out.println(Arrays.toString(str1.split(SPLIT_SIGN, 2)));
        // [a, b]
        System.out.println(Arrays.toString(str2.split(SPLIT_SIGN, 2)));
        // [a]
        System.out.println(Arrays.toString(str3.split(SPLIT_SIGN, 2)));
    }

    @Test
    void addAll() {
        List<String> stringList1 = new ArrayList<>();
        stringList1.add("one");
        stringList1.add("two");
        System.out.println(stringList1);
        List<String> stringList2 = new ArrayList<>();
        stringList2.add("three");
        System.out.println(stringList2);
        stringList1.addAll(stringList2);
        System.out.println(stringList1);
    }

    @Test
    void type() {
        Object map = new HashMap<>();
        Object i = null;
        boolean a = map instanceof Map;
        boolean b = map instanceof HashMap;
        boolean c = map instanceof LinkedHashMap;
        boolean d = i instanceof Integer;
        boolean e = i instanceof Null;
        System.out.println(String.format("a:%s,b:%s,c:%s,d:%s,e:%s", a, b, c, d, e));
    }

}
