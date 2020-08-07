package cn.adcc.client.service;

import cn.adcc.client.DO.FixmOrder;
import cn.adcc.client.DTO.FixmOrderDto;

import java.util.Map;

public interface FixmOrderService {
    Map<String, FixmOrder> list(String version);

//    Map<String, FixmOrder> get(String version, String xsdnode);

//    FixmOrder get(String version, String xsdnode);

//    List<FixmOrder> getChild(String version, String xsdnodePrefix);

    void save(FixmOrderDto fixmOrderDto);

    void updateXsdnode(FixmOrderDto fixmOrderDto);

    void del(String version, String xsdnode);

    void deleteByVersion(String version);
}
