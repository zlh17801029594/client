package cn.adcc.client.service.impl;

import cn.adcc.client.DO.User;
import cn.adcc.client.DTO.UserDto;
import cn.adcc.client.VO.PageRequestDto;
import cn.adcc.client.repository.UserRepository;
import cn.adcc.client.service.UserService;
import cn.adcc.client.utils.CopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDto save(UserDto userDto) {
        User user = CopyUtil.copy(userDto, User.class);
        userRepository.save(user);
        userDto.setId(user.getId());
        return userDto;
    }

    @Override
    public void delete(UserDto userDto) {

    }

    @Override
    public UserDto findByUsername(String username) {
        User user = userRepository.findDistinctByUsername(username);
        if (user != null) {
            return CopyUtil.copy(user, UserDto.class);
        }
        return null;
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
