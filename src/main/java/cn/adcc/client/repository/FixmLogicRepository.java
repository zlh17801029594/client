package cn.adcc.client.repository;

import cn.adcc.client.DO.FixmLogic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface FixmLogicRepository extends JpaRepository<FixmLogic, Long> {
    List<FixmLogic> findAllByVersion(String version);

    List<FixmLogic> findAllByVersionAndXsdnodeStartingWith(String version, String xsdnode);

    List<FixmLogic> findAllByVersionAndXsdnode(String version, String xsdnode);

    // 获取列名list
    @Query(value = "select column_name from information_schema.columns WHERE TABLE_SCHEMA = 'integrate' AND TABLE_NAME='integrate_flight_info_test'", nativeQuery = true)
    List<String> findIntegrateFlightInfoColumns();

    // 获取第一条数据
    @Query(value = "SELECT * FROM integrate.integrate_flight_info_test LIMIT 1", nativeQuery = true)
    Map<String, Object> findFirstIntegrateFlightInfo();

    // 获取列属性详情
    @Query(value = "DESC integrate.integrate_flight_info_test", nativeQuery = true)
    List<Map<String, String>> findIntegrateFlightInfoColumnsDesc();

    // 获取fixm version
    @Query(value = "SELECT version FROM integrate.fixm_logic GROUP BY version", nativeQuery = true)
    List<String> findFixmVersions();

    List<FixmLogic> findAllByVersionAndXsdnodeIn(String version, List<String> xsdnodes);
}
