package cn.adcc.client.repository;

import cn.adcc.client.DO.FixmOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FixmOrderRepository extends JpaRepository<FixmOrder, Long> {
    List<FixmOrder> findAllByVersion(String version);

    /**
     * 1.更新节点（拖拽）：更新节点下全部排序数据的xsdnode；更新原父节点排序序列、更新新父节点排序序列
     * 2.删除节点：删除节点下全部排序数据
     * @param version
     * @param xsdnode
     * @return
     */
    List<FixmOrder> findAllByVersionAndXsdnodeStartingWith(String version, String xsdnode);

    List<FixmOrder> findAllByVersionAndXsdnode(String version, String xsdnode);
}
