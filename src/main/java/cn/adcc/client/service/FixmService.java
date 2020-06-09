package cn.adcc.client.service;

import cn.adcc.client.DO.FixmLogic;
import cn.adcc.client.DTO.FixmLogicDto;
import cn.adcc.client.DTO.FixmNoLeaf;
import cn.adcc.client.DTO.FixmNode;
import cn.adcc.client.repository.FixmLogicRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class FixmService {

    @Autowired
    private FixmLogicRepository fixmLogicRepository;

    private List<String> routes = Arrays.asList(
            "fx:Flight->fx:aircraft->fx:aircraftAddress",
            "fx:Flight->fx:aircraft->fx:aircraftApproachCategory",
            "fx:Flight->fx:aircraft->fx:aircraftType->fx:numberOfAircraft",
            "fx:Flight->fx:aircraft->fx:aircraftType->fx:type->fx:icaoAircraftTypeDesignator",
            "fx:Flight->fx:aircraft->fx:aircraftType->fx:type->fx:otherAircraftType",
            "fx:Flight->fx:aircraft->fx:capabilities->fx:communication->fx:communicationCapabilityCode",
            "fx:Flight->fx:aircraft->fx:capabilities->fx:communication->fx:datalinkCommunicationCapabilityCode",
            "fx:Flight->fx:aircraft->fx:capabilities->fx:communication->fx:otherCommunicationCapabilities",
            "fx:Flight->fx:aircraft->fx:capabilities->fx:communication->fx:otherDatalinkCapabilities",
            "fx:Flight->fx:aircraft->fx:capabilities->fx:communication->fx:selectiveCallingCode",
            "fx:Flight->fx:routeTrajectoryGroup->fx:ranked->fx:routeTrajectory->fx:element->fx:routeChange->fx:cruiseClimbStart->fx:level->fb:flightLevelOrAltitudeRange->fb:upperBound->fb:altitude",
            "fx:Flight->fx:routeTrajectoryGroup->fx:ranked->fx:routeTrajectory->fx:element->fx:routeChange->fx:cruiseClimbStart->fx:level->fb:flightLevelOrAltitudeRange->fb:upperBound->fb:flightLevel",
            "fx:Flight->fx:routeTrajectoryGroup->fx:ranked->fx:routeTrajectory->fx:element->fx:routeChange->fx:cruiseClimbStart->fx:level->fb:flightLevelOrAltitudeValue->fb:altitude",
            "fx:Flight->fx:routeTrajectoryGroup->fx:ranked->fx:routeTrajectory->fx:element->fx:routeChange->fx:cruiseClimbStart->fx:level->fb:flightLevelOrAltitudeValue->fb:flightLevel",
            "fx:Flight->fx:routeTrajectoryGroup->fx:ranked->fx:routeTrajectory->fx:element->fx:routeChange->fx:cruiseClimbStart->fx:speed",
            "fx:Flight->fx:routeTrajectoryGroup->fx:ranked->fx:routeTrajectory->fx:element->fx:routeChange->fx:level->fx:activation",
            "fx:Flight->fx:routeTrajectoryGroup->fx:ranked->fx:routeTrajectory->fx:element->fx:routeChange->fx:level->fx:level->fb:altitude",
            "fx:Flight->fx:routeTrajectoryGroup->fx:ranked->fx:routeTrajectory->fx:element->fx:routeChange->fx:level->fx:level->fb:flightLevel",
            "fx:Flight->fx:routeTrajectoryGroup->fx:ranked->fx:routeTrajectory->fx:element->fx:routeChange->fx:speed->fx:activation",
            "fx:Flight->fx:routeTrajectoryGroup->fx:ranked->fx:routeTrajectory->fx:element->fx:routeChange->fx:speed->fx:speed"
    );

    public List<FixmLogicDto> convert2Tree() {
        List<FixmLogicDto> fixmLogicDtos = new ArrayList<>();
        List<FixmLogic> fixmLogics = fixmLogicRepository.findAll();
        fixmLogics.stream().forEach(fixmLogic -> {
            String route = fixmLogic.getXsdnode();
            this.recursionA(fixmLogicDtos, route, fixmLogic);
        });
//        fixmLogics.stream().map(FixmLogic::getXsdnode).forEach(item -> this.recursion(fixmNodes, item));
//        routes.forEach(item -> this.recursion(fixmNodes, item));
        return fixmLogicDtos;
    }

    private void recursion(List<FixmNode> fixmNodes, String route) {
        System.out.println(route);
        String[] split = route.split("->", 2);
        String currentNodeName = split[0];
        System.out.println(currentNodeName);
        FixmNode fixmNodeItem = null;
        for (FixmNode fixmNode : fixmNodes) {
            if (fixmNode.getLabel().equals(currentNodeName)) {
                fixmNodeItem = fixmNode;
                break;
            }
        }
        if (fixmNodeItem == null) {
            fixmNodeItem = new FixmNode();
            // 加入list
            fixmNodes.add(fixmNodeItem);
            fixmNodeItem.setLabel(currentNodeName);
        }
        if (route.contains("->")) {
            if (fixmNodeItem.getChildren() == null) {
                // 设置子list
                List<FixmNode> fixmNodeChildren = new ArrayList<>();
                fixmNodeItem.setChildren(fixmNodeChildren);
            }
            String otherNodeName = split[1];
            this.recursion(fixmNodeItem.getChildren(), otherNodeName);
        }
    }

    private void recursionA(List<FixmLogicDto> fixmLogicDtos, String route, FixmLogic fixmLogic) {
        System.out.println(route);
        String[] split = route.split("->", 2);
        String currentNodeName = split[0];
        System.out.println(currentNodeName);
        FixmLogicDto fixmLogicDto = null;
        for (FixmLogicDto fixmLogicDto1 : fixmLogicDtos) {
            if (fixmLogicDto1.getLabel().equals(currentNodeName)) {
                fixmLogicDto = fixmLogicDto1;
                break;
            }
        }
        if (fixmLogicDto == null) {
            fixmLogicDto = new FixmLogicDto();
            // 加入list
            fixmLogicDtos.add(fixmLogicDto);
            fixmLogicDto.setLabel(currentNodeName);
        }
        if (route.contains("->")) {
            if (fixmLogicDto.getChildren() == null) {
                // 设置子list
                List<FixmLogicDto> fixmLogicDtoChildren = new ArrayList<>();
                fixmLogicDto.setChildren(fixmLogicDtoChildren);
            }
            String otherNodeName = split[1];
            this.recursionA(fixmLogicDto.getChildren(), otherNodeName, fixmLogic);
        } else {
            BeanUtils.copyProperties(fixmLogic, fixmLogicDto);
        }
    }

    @Transactional
    public void updateLeaf(Long id, FixmLogicDto fixmLogicDto) {
        FixmLogic fixmLogic = fixmLogicRepository.getOne(id);
        String xsdnode = fixmLogic.getXsdnode();
        int i = xsdnode.lastIndexOf("->");
        String concat = xsdnode.substring(0, i).concat("->").concat(fixmLogicDto.getLabel());
        fixmLogic.setXsdnode(concat);
        fixmLogicRepository.save(fixmLogic);
    }

    @Transactional
    public void updateNoLeaf(FixmNoLeaf fixmNoLeaf) {
        int level = fixmNoLeaf.getLevel();
        String label = fixmNoLeaf.getLabel();
        List<Long> ids = fixmNoLeaf.getIds();
        List<FixmLogic> fixmLogics = fixmLogicRepository.findAllById(ids);
        fixmLogics.forEach(fixmLogic -> {
            String xsdnode = fixmLogic.getXsdnode();
            String[] split = xsdnode.split("->");
            StringBuilder route = new StringBuilder();
            for (int i = 0; i < split.length; i++) {
                if (i != level) {
                    route.append(split[i]).append("->");
                } else {
                    route.append(label).append("->");
                }
            }
            String substring = route.substring(0, route.length() - 2);
            fixmLogic.setXsdnode(substring);
        });
        fixmLogicRepository.saveAll(fixmLogics);
    }

    @Transactional
    public void del(List<Long> ids) {
        ids.forEach(id -> fixmLogicRepository.deleteById(id));
    }
}
