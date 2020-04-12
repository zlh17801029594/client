package cn.adcc.client;

import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;

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
}
