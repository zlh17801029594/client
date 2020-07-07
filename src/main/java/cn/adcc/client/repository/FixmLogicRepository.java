package cn.adcc.client.repository;

import cn.adcc.client.DO.FixmLogic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FixmLogicRepository extends JpaRepository<FixmLogic, Long> {
    List<FixmLogic> findAllByVersion(String version);

    List<FixmLogic> findAllByVersionAndXsdnodeStartingWith(String version, String xsdnode);

    List<FixmLogic> findAllByVersionAndXsdnode(String version, String xsdnode);
}
