package cn.adcc.client.service;

import cn.adcc.client.ClientApplicationTests;
import cn.adcc.client.DTO.FixmLogicDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
}