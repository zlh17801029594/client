package cn.adcc.client.service;

import cn.adcc.client.DTO.UserDto;
import cn.adcc.client.VO.PageRequestDto;
import org.springframework.data.domain.Page;

public interface UserService {
    /**
     * 新建用户、更新用户敏感级别
     * @param userDto
     */
    UserDto save(UserDto userDto);

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
    UserDto findByUsername(String username);

    UserDto findById(Long id);

    /**
     * 用户列表
     * @param pageRequestDto
     * @return
     */
    void list(PageRequestDto<UserDto> pageRequestDto);
}
