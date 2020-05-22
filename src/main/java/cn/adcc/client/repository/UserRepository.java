package cn.adcc.client.repository;

import cn.adcc.client.DO.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findDistinctByUsername(String username);

    List<User> findBy();
}
