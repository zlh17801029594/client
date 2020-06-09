package cn.adcc.client;

import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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
}
