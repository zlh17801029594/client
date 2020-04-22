package cn.adcc.client.repository;

import cn.adcc.client.DO.MSApply;
import cn.adcc.client.DO.MSApplyDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MSApplyDetailsRepository extends JpaRepository<MSApplyDetails, Long> {

    List<MSApplyDetails> findAllByMsApply(MSApply msApply);
}
