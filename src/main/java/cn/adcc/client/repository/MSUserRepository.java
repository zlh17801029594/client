package cn.adcc.client.repository;

import cn.adcc.client.DO.MSUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MSUserRepository extends JpaRepository<MSUser, Long> {
    List<MSUser> findMSUsersBy();

    MSUser findMSUserByUsername(String username);
}
