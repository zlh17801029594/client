package cn.adcc.client.repository;

import cn.adcc.client.ClientApplicationTests;
import cn.adcc.fixm.convertinterface.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import java.util.List;
import java.util.Map;

@Component
class FixmLogicRepositoryTest extends ClientApplicationTests {
    @Autowired
    private FixmLogicRepository fixmLogicRepository;

    @Test
    void findIntegrateFlightInfoTest() throws SAXException {
        /*List<String> list = fixmLogicRepository.findIntegrateFlightInfoColumns();
        System.out.println(list);
        Map<String, String> map = fixmLogicRepository.findFirstIntegrateFlightInfo();
        System.out.println(map);*/
        Validator validator = new Validator();
        List<String> gen = validator.gen("core4.1", "INTEGRATE", "INTEGRATE_FLIGHT_INFO_TEST");
        if (gen != null && !gen.isEmpty()) {
            String xml = gen.get(0);
            // xsd_file_path: xsd文件路径
            boolean flag = false;
            try {
                flag = validator.validate_xml("./xsd/core4.1/Fixm.xsd", xml);
            } catch (Exception e) {
                e.printStackTrace();
                String errorMessage = e.getMessage();
                System.out.println(errorMessage);
            }
            System.out.println(flag);
        }
        System.out.println(gen);
    }

    @Test
    void testA() {
        Object a = null;
        String b = (String) a;
        System.out.println(b);
    }
}