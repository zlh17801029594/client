package cn.adcc.client.repository;

import cn.adcc.client.DO.Apply;
import cn.adcc.client.DO.ApplyDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface ApplyRepository extends JpaRepository<Apply, Long> {
    List<Apply> findDistinctByStatusAndApplyDetailssIn(Integer status, Set<ApplyDetails> applyDetailss);

    List<Apply> findByUserIdAndStatus(Long userId, Integer status);
}
