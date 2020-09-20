package cn.adcc.client.repository;

import cn.adcc.client.DO.Apply;
import cn.adcc.client.DO.ApplyDetails;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface ApplyRepository extends JpaRepository<Apply, Long> {
    /*接口变动逆向查询申请*/
    List<Apply> findDistinctByStatusAndApplyDetailssIn(Integer status, Set<ApplyDetails> applyDetailss);

    /*用户变动，通过用户id查询申请*/
    List<Apply> findByStatusAndUserId(Integer status, Long userId);

    List<Apply> findByUserIdAndStatus(Long userId, Integer status);

    /*申请过期*/
    List<Apply> findByStatusAndExpireTimeBefore(Integer status, Date nowTime);

    @EntityGraph(value = "Apply.Graph")
    List<Apply> findAllBy();

    // 待解决 分页+EntityGraph 产生警告问题
    @Override
    @EntityGraph(value = "Apply.Graph")
    <S extends Apply> Page<S> findAll(Example<S> var1, Pageable var2);
}
