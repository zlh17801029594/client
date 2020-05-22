package cn.adcc.client.service;

import cn.adcc.client.DO.User;
import cn.adcc.client.DTO.UserDto;
import cn.adcc.client.VO.PageRequestDto;
import cn.adcc.client.sso.SsoUser;
import org.springframework.data.domain.Page;

public interface UserService {
    /**
     * 新建用户
     */
    void save(User user);

    /**
     * 根据门户用户信息 更新 用户信息、敏感级别，敏感级别影响到的申请问题
     */
    void updateUser(SsoUser ssoUser);

    /**
     * 暂无此业务
     * @param userDto
     */
    void delete(UserDto userDto);

    /**
     * 根据用户名查找用户
     * @param username
     * @return
     */
    User findByUsername(String username);

    UserDto findById(Long id);

    /**
     * 用户列表
     * @param pageRequestDto
     * @return
     */
    void list(PageRequestDto<UserDto> pageRequestDto);
}
