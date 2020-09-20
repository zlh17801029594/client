package cn.adcc.client.service;

import cn.adcc.client.ClientApplicationTests;
import cn.adcc.client.utils.ColumnField;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
class FixmLogicServiceTest extends ClientApplicationTests {
    @Autowired
    private FixmLogicService fixmLogicService;

    @Test
    void convert2Tree() {
        fixmLogicService.list2tree("core4.1");
    }

    @Test
    void add() {
        /*FixmLogicVO fixmLogicVO = new FixmLogicVO();
        fixmLogicVO.setFatherXsdnode("fx:Flight->fx:aircraft->fx:aircraftAddress");
        fixmLogicVO.setIsnode(false);
        fixmLogicVO.setName("ms:z&m");
        fixmLogicVO.setVersion("core4.2");
        fixmLogicVO.setIsvalid(true);
        FixmLogicDto fixmLogicDto = fixmLogicService.add(fixmLogicVO);
        System.out.println(fixmLogicDto);*/
    }

    @Test
    void columnField() {
        List<ColumnField> flightInfoColumnsDesc = fixmLogicService.findFlightInfoColumnsDesc();
        System.out.println(flightInfoColumnsDesc);
    }

    @Test
    void updateSubversion() {
        String version = "zlh";
        String subversion = "m";
        List<String> xsdnodes = new ArrayList<>();
//        xsdnodes.add("zhoudaye->lixiaolong->huangfeihong->minglaoda->yuzhaoqian->chaiwochaibuchai");
        xsdnodes.add("zhoudaye->lixiaolong->huangfeihong->minglaoda->yuzhaoqian->chaiwochaibuchai->xxx->sasf->bbbaasfd");
        xsdnodes.add("zhoudaye->lixiaolong->huangfeihong->minglaoda->yuzhaoqian->chaiwochaibuchai->xxx->xxssdf->sfasf");
        xsdnodes.add("zhoudaye->lixiaolong->huangfeihong->minglaoda->yuzhaoqian->chaiwochaibuchai->xxx->xxssdf->sfasf->aaa->sdf->sdfsdf->eeeeef->dsfa");
//        fixmLogicService.updateSubversion(version, subversion, xsdnodes);
    }
}