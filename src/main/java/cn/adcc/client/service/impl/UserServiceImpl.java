package cn.adcc.client.service.impl;

import cn.adcc.client.DO.User;
import cn.adcc.client.DTO.UserDto;
import cn.adcc.client.VO.PageRequestDto;
import cn.adcc.client.repository.UserRepository;
import cn.adcc.client.service.ApplyService;
import cn.adcc.client.service.UserService;
import cn.adcc.client.sso.SsoUser;
import cn.adcc.client.utils.CopyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ApplyService applyService;

    @Override
    public void save(User user) {
        log.info("[新用户加入管理系统], {}", user);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateUser(SsoUser ssoUser) {
        log.info("[门户更新用户信息], {}", ssoUser);
        int ssoSens = ssoUser.getSensitiveLevel();
        User user = this.findByUsername(ssoUser.getUsername());
        if (user != null) {
            int userSens = user.getSensitiveNum();
            if (userSens != ssoSens) {
                /*赋值新的敏感级别*/
                user.setSensitiveNum(ssoSens);
                if (ssoSens < userSens) {
                    /*用户敏感级别调低，可能需要更新申请状态*/
                    applyService.updateApplyByUserSens(user);
                }
                log.info("[用户敏感级别调整], {}=>{}", user, ssoUser);
                /*更新用户*/
                userRepository.save(user);
            }
        }
    }

    @Override
    public void delete(UserDto userDto) {

    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findDistinctByUsername(username);
    }

    @Override
    public UserDto findById(Long id) {
        return userRepository.findById(id).map(user -> CopyUtil.copy(user, UserDto.class)).orElse(null);
    }

    @Override
    public void list(PageRequestDto<UserDto> pageRequestDto) {
        UserDto userDto = pageRequestDto.getData();
        User user = CopyUtil.copy(userDto, User.class);
        Example<User> example = Example.of(user);
        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "id");
        Sort sort = Sort.by(order);
        PageRequest pageRequest = PageRequest.of(pageRequestDto.getPage() - 1, pageRequestDto.getLimit(), sort);
        Page<User> userPage = userRepository.findAll(example, pageRequest);
        List<UserDto> userDtos = CopyUtil.copyList(userPage.getContent(), UserDto.class);
        pageRequestDto.setList(userDtos);
        pageRequestDto.setTotal(userPage.getTotalElements());
    }

}
