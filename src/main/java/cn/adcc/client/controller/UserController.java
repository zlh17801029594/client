package cn.adcc.client.controller;

import cn.adcc.client.VO.LoginUser;
import cn.adcc.client.VO.Result;
import cn.adcc.client.enums.ResultEnum;
import cn.adcc.client.exception.UserException;
import cn.adcc.client.service.UserService;
import cn.adcc.client.service.impl.UserServiceImpl;
import cn.adcc.client.sso.Constant;
import cn.adcc.client.utils.ResultUtil;
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
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/info")
    public Result userInfo(){
        return ResultUtil.success(userService.getUserInfo());
    }

    /**
     * 请求网关方式
     * @param req
     * @return
     * @throws UnsupportedEncodingException
     * @throws URISyntaxException
     */
    @RequestMapping("/api")
    public Object api(HttpServletRequest req) throws UnsupportedEncodingException, URISyntaxException {
        String sessionid = req.getHeader(Constant.SSO_SESSIONID);
        if (sessionid == null) {
            sessionid = req.getParameter(Constant.SSO_SESSIONID);
        }
        String xxl_sso_sessionid = URLEncoder.encode(sessionid, "UTF-8");
        URI url = new URI("http://localhost:9000/service-resource/resourceserver/person?xxl_sso_sessionid=" + xxl_sso_sessionid);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        return responseEntity.getBody();
    }
}
