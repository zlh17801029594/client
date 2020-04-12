package cn.adcc.client.repository;

import cn.adcc.client.DO.MSUserApi;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MSUserApiRepository extends JpaRepository<MSUserApi, Long> {

    MSUserApi findMSUserApiByUsernameAndApiRef(String username, Long apiRef);
}
