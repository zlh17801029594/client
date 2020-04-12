package cn.adcc.client.controller;

import cn.adcc.client.VO.LoginUser;
import cn.adcc.client.service.UserService;
import cn.adcc.client.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

@CrossOrigin
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @RequestMapping("/user/info")
    public Object userInfo(){
        return userService.getUserInfo();
    }

    @RequestMapping("/api")
    public Object api(HttpServletRequest req) throws UnsupportedEncodingException, URISyntaxException {
        String xxl_sso_sessionid = req.getParameter("xxl_sso_sessionid");
        if (xxl_sso_sessionid != null) {
            xxl_sso_sessionid = URLEncoder.encode(xxl_sso_sessionid, "UTF-8");
        }
        URI url = new URI("http://localhost:9000/service-resource/resourceserver/person?xxl_sso_sessionid="+xxl_sso_sessionid);
        //String url = "http://localhost:9000/service-resource/resourceserver/person";
        //String url = "http://localhost:9003/resourceserver/methods";
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        return responseEntity.getBody();
    }
}
