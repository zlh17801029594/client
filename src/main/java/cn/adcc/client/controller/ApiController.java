package cn.adcc.client.controller;

import cn.adcc.client.DTO.MSApiDto;
import cn.adcc.client.service.MSApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@CrossOrigin
@RestController
public class ApiController {
    @Autowired
    private MSApiService MSApiService;

    @RequestMapping("/servicex")
    public List<MSApiDto> servicex() throws Exception {
        String url = "localhost:9005/api1?url=http://192.168.243.87:8080/v2/api-docs";
        MSApiDto msApiDto = MSApiService.buildMSApiDto(url);
        MSApiService.updateMSApi(Arrays.asList(msApiDto));
        return MSApiService.findMSApi();
    }

    @RequestMapping("/service")
    public String service() {
        return "{\n" +
                "\"code\": 20000,\n" +
                "\"msg\": \"\",\n" +
                "\"data\": [{\n" +
                "\"host\": \"192.168.204.71:8720\",\n" +
                "\"basePath\": \"/gateway/user-permission-service\",\n" +
                "\"label\": \"用户权限服务 API\",\n" +
                "\"children\": [{\n" +
                "\"uri\": \"/permission/getPermission/{name}\",\n" +
                "\"method\": \"get\",\n" +
                "\"controllers\": [\n" +
                "\"permission-controller\"\n" +
                "],\n" +
                "\"operationId\": \"getPermissionUsingGET\",\n" +
                "\"consumes\": [\n" +
                "\"*/*\"\n" +
                "],\n" +
                "\"produces\": [\n" +
                "\"application/xml\",\n" +
                "\"application/json\"\n" +
                "],\n" +
                "\"parameters\": [{\n" +
                "\"name\": \"name\",\n" +
                "\"in\": \"path\",\n" +
                "\"description\": \"name\",\n" +
                "\"required\": true,\n" +
                "\"type\": \"string\",\n" +
                "\"allowEmptyValue\": false,\n" +
                "\"example\": null\n" +
                "}],\n" +
                "\"result\": {\n" +
                "\"name\": \"string\",\n" +
                "\"id\": 0,\n" +
                "\"url\": \"string\"\n" +
                "},\n" +
                "\"responses\": [{\n" +
                "\"code\": \"200\",\n" +
                "\"description\": \"请求成功\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"401\",\n" +
                "\"description\": \"未认证\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"403\",\n" +
                "\"description\": \"权限不足\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"404\",\n" +
                "\"description\": \"未找到\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"500\",\n" +
                "\"description\": \"请求失败\"\n" +
                "}\n" +
                "],\n" +
                "\"deprecated\": false,\n" +
                "\"label\": \"根据权限名查询对应权限\"\n" +
                "},\n" +
                "{\n" +
                "\"uri\": \"/permission/getRolePermission/{roleId}\",\n" +
                "\"method\": \"get\",\n" +
                "\"controllers\": [\n" +
                "\"permission-controller\"\n" +
                "],\n" +
                "\"operationId\": \"getRolePermissionUsingGET\",\n" +
                "\"consumes\": [\n" +
                "\"*/*\"\n" +
                "],\n" +
                "\"produces\": [\n" +
                "\"application/xml\",\n" +
                "\"application/json\"\n" +
                "],\n" +
                "\"parameters\": [{\n" +
                "\"name\": \"roleId\",\n" +
                "\"in\": \"path\",\n" +
                "\"description\": \"roleId\",\n" +
                "\"required\": true,\n" +
                "\"type\": \"integer\",\n" +
                "\"allowEmptyValue\": false,\n" +
                "\"example\": null\n" +
                "}],\n" +
                "\"result\": [{\n" +
                "\"name\": \"string\",\n" +
                "\"id\": 0,\n" +
                "\"url\": \"string\"\n" +
                "}],\n" +
                "\"responses\": [{\n" +
                "\"code\": \"200\",\n" +
                "\"description\": \"请求成功\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"401\",\n" +
                "\"description\": \"未认证\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"403\",\n" +
                "\"description\": \"权限不足\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"404\",\n" +
                "\"description\": \"未找到\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"500\",\n" +
                "\"description\": \"请求失败\"\n" +
                "}\n" +
                "],\n" +
                "\"deprecated\": false,\n" +
                "\"label\": \"根据角色ID查询对应权限\"\n" +
                "},\n" +
                "{\n" +
                "\"uri\": \"/role/getRoleByUserId/{userId}\",\n" +
                "\"method\": \"get\",\n" +
                "\"controllers\": [\n" +
                "\"role-controller\"\n" +
                "],\n" +
                "\"operationId\": \"getRoleByUserIdUsingGET\",\n" +
                "\"consumes\": [\n" +
                "\"*/*\"\n" +
                "],\n" +
                "\"produces\": [\n" +
                "\"application/xml\",\n" +
                "\"application/json\"\n" +
                "],\n" +
                "\"parameters\": [{\n" +
                "\"name\": \"userId\",\n" +
                "\"in\": \"path\",\n" +
                "\"description\": \"userId\",\n" +
                "\"required\": true,\n" +
                "\"type\": \"integer\",\n" +
                "\"allowEmptyValue\": false,\n" +
                "\"example\": null\n" +
                "}],\n" +
                "\"result\": [{\n" +
                "\"createTime\": \"2020-03-24T04:44:17.770+0000\",\n" +
                "\"name\": \"string\",\n" +
                "\"description\": \"string\",\n" +
                "\"id\": 0,\n" +
                "\"lastUpdateTime\": \"2020-03-24T04:44:17.770+0000\",\n" +
                "\"status\": 0\n" +
                "}],\n" +
                "\"responses\": [{\n" +
                "\"code\": \"200\",\n" +
                "\"description\": \"请求成功\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"401\",\n" +
                "\"description\": \"未认证\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"403\",\n" +
                "\"description\": \"权限不足\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"404\",\n" +
                "\"description\": \"未找到\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"500\",\n" +
                "\"description\": \"请求失败\"\n" +
                "}\n" +
                "],\n" +
                "\"deprecated\": false,\n" +
                "\"label\": \"根据用户ID查询对应角色\"\n" +
                "},\n" +
                "{\n" +
                "\"uri\": \"/user/findByUsername/{username}\",\n" +
                "\"method\": \"get\",\n" +
                "\"controllers\": [\n" +
                "\"user-controller\"\n" +
                "],\n" +
                "\"operationId\": \"findByUsernameUsingGET\",\n" +
                "\"consumes\": [\n" +
                "\"*/*\"\n" +
                "],\n" +
                "\"produces\": [\n" +
                "\"application/xml\",\n" +
                "\"application/json\"\n" +
                "],\n" +
                "\"parameters\": [{\n" +
                "\"name\": \"username\",\n" +
                "\"in\": \"path\",\n" +
                "\"description\": \"username\",\n" +
                "\"required\": true,\n" +
                "\"type\": \"string\",\n" +
                "\"allowEmptyValue\": false,\n" +
                "\"example\": null\n" +
                "}],\n" +
                "\"result\": {\n" +
                "\"accountLocked\": true,\n" +
                "\"password\": \"string\",\n" +
                "\"accountExpired\": true,\n" +
                "\"id\": 0,\n" +
                "\"credentialsExpired\": true,\n" +
                "\"enabled\": true,\n" +
                "\"username\": \"string\"\n" +
                "},\n" +
                "\"responses\": [{\n" +
                "\"code\": \"200\",\n" +
                "\"description\": \"请求成功\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"401\",\n" +
                "\"description\": \"未认证\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"403\",\n" +
                "\"description\": \"权限不足\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"404\",\n" +
                "\"description\": \"未找到\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"500\",\n" +
                "\"description\": \"请求失败\"\n" +
                "}\n" +
                "],\n" +
                "\"deprecated\": false,\n" +
                "\"label\": \"根据用户名查询用户\"\n" +
                "}\n" +
                "]\n" +
                "},\n" +
                "{\n" +
                "\"host\": \"192.168.204.71:8720\",\n" +
                "\"basePath\": \"/gateway/flightinfo-service\",\n" +
                "\"label\": \"FlightInfo航班信息服务 API\",\n" +
                "\"children\": [{\n" +
                "\"uri\": \"/flightinfo/getCloseDoorOverTimMinutes\",\n" +
                "\"method\": \"get\",\n" +
                "\"controllers\": [\n" +
                "\"flight-info-controller\"\n" +
                "],\n" +
                "\"operationId\": \"getCloseDoorOverTimMinutesUsingGET\",\n" +
                "\"consumes\": [\n" +
                "\"*/*\"\n" +
                "],\n" +
                "\"produces\": [\n" +
                "\"application/xml\",\n" +
                "\"application/json\"\n" +
                "],\n" +
                "\"parameters\": null,\n" +
                "\"result\": {\n" +
                "\"overtime\": 0\n" +
                "},\n" +
                "\"responses\": [{\n" +
                "\"code\": \"200\",\n" +
                "\"description\": \"请求成功\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"401\",\n" +
                "\"description\": \"未认证\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"403\",\n" +
                "\"description\": \"权限不足\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"404\",\n" +
                "\"description\": \"未找到\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"500\",\n" +
                "\"description\": \"请求失败\"\n" +
                "}\n" +
                "],\n" +
                "\"deprecated\": false,\n" +
                "\"label\": \"关舱门超时分钟数\"\n" +
                "},\n" +
                "{\n" +
                "\"uri\": \"/flightinfo/getCloseDoorOverTimeFlightInfo\",\n" +
                "\"method\": \"get\",\n" +
                "\"controllers\": [\n" +
                "\"flight-info-controller\"\n" +
                "],\n" +
                "\"operationId\": \"getCloseDoorOverTimeFlightInfoUsingGET\",\n" +
                "\"consumes\": [\n" +
                "\"*/*\"\n" +
                "],\n" +
                "\"produces\": [\n" +
                "\"application/xml\",\n" +
                "\"application/json\"\n" +
                "],\n" +
                "\"parameters\": [{\n" +
                "\"name\": \"airport\",\n" +
                "\"in\": \"query\",\n" +
                "\"description\": \"起飞机场四码\",\n" +
                "\"required\": true,\n" +
                "\"type\": \"string\",\n" +
                "\"allowEmptyValue\": false,\n" +
                "\"example\": \"ZUUU\"\n" +
                "},\n" +
                "{\n" +
                "\"name\": \"from\",\n" +
                "\"in\": \"query\",\n" +
                "\"description\": \"计划起飞时间查询起始时间\",\n" +
                "\"required\": true,\n" +
                "\"type\": \"string\",\n" +
                "\"allowEmptyValue\": false,\n" +
                "\"example\": \"2018-11-04T00:00:00Z\"\n" +
                "},\n" +
                "{\n" +
                "\"name\": \"to\",\n" +
                "\"in\": \"query\",\n" +
                "\"description\": \"计划起飞时间查询结束时间\",\n" +
                "\"required\": true,\n" +
                "\"type\": \"string\",\n" +
                "\"allowEmptyValue\": false,\n" +
                "\"example\": \"2018-11-05T00:00:00Z\"\n" +
                "}\n" +
                "],\n" +
                "\"result\": [{\n" +
                "\"acid\": \"string\",\n" +
                "\"eobt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"atot\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"overtimeMinutes\": 0,\n" +
                "\"agct\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"sobt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"dest\": \"string\",\n" +
                "\"sibt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"dep\": \"string\"\n" +
                "}],\n" +
                "\"responses\": [{\n" +
                "\"code\": \"200\",\n" +
                "\"description\": \"请求成功\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"401\",\n" +
                "\"description\": \"未认证\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"403\",\n" +
                "\"description\": \"权限不足\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"404\",\n" +
                "\"description\": \"未找到\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"500\",\n" +
                "\"description\": \"请求失败\"\n" +
                "}\n" +
                "],\n" +
                "\"deprecated\": false,\n" +
                "\"label\": \"关舱门超时航班信息\"\n" +
                "},\n" +
                "{\n" +
                "\"uri\": \"/flightinfo/getFormerIntegrateFlightInfoBySobtAndAcid\",\n" +
                "\"method\": \"get\",\n" +
                "\"controllers\": [\n" +
                "\"flight-info-controller\"\n" +
                "],\n" +
                "\"operationId\": \"getFormerIntegrateFlightInfoBySobtAndAcidUsingGET\",\n" +
                "\"consumes\": [\n" +
                "\"*/*\"\n" +
                "],\n" +
                "\"produces\": [\n" +
                "\"application/xml\",\n" +
                "\"application/json\"\n" +
                "],\n" +
                "\"parameters\": [{\n" +
                "\"name\": \"acid\",\n" +
                "\"in\": \"query\",\n" +
                "\"description\": \"航班号\",\n" +
                "\"required\": true,\n" +
                "\"type\": \"string\",\n" +
                "\"allowEmptyValue\": false,\n" +
                "\"example\": \"CSC8923\"\n" +
                "},\n" +
                "{\n" +
                "\"name\": \"from\",\n" +
                "\"in\": \"query\",\n" +
                "\"description\": \"计划起飞时间查询起始时间\",\n" +
                "\"required\": true,\n" +
                "\"type\": \"string\",\n" +
                "\"allowEmptyValue\": false,\n" +
                "\"example\": \"2018-11-04T00:00:00Z\"\n" +
                "},\n" +
                "{\n" +
                "\"name\": \"to\",\n" +
                "\"in\": \"query\",\n" +
                "\"description\": \"计划起飞时间查询结束时间\",\n" +
                "\"required\": true,\n" +
                "\"type\": \"string\",\n" +
                "\"allowEmptyValue\": false,\n" +
                "\"example\": \"2018-11-05T00:00:00Z\"\n" +
                "}\n" +
                "],\n" +
                "\"result\": [{\n" +
                "\"obt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"executedate\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"fltHeight\": \"string\",\n" +
                "\"rfpt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"dep\": \"string\",\n" +
                "\"fltNum\": \"string\",\n" +
                "\"ssrCode\": \"string\",\n" +
                "\"tipt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"deicingPad\": \"string\",\n" +
                "\"fix\": \"string\",\n" +
                "\"isAbs\": \"string\",\n" +
                "\"majorCarrier\": \"string\",\n" +
                "\"beggate\": \"string\",\n" +
                "\"originFlag\": 0,\n" +
                "\"planId\": 0,\n" +
                "\"id\": 0,\n" +
                "\"endgate\": \"string\",\n" +
                "\"contrlPoint\": \"string\",\n" +
                "\"planEtd\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"planEta\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"taxi\": 0,\n" +
                "\"asbt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"priority\": 0,\n" +
                "\"aobt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"planDate\": \"string\",\n" +
                "\"formerId\": 0,\n" +
                "\"deparrflag\": \"string\",\n" +
                "\"pustime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"cdt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"route\": \"string\",\n" +
                "\"sts\": \"string\",\n" +
                "\"ctot\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"planRoute\": \"string\",\n" +
                "\"zbApproval\": \"string\",\n" +
                "\"agct\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"dof\": \"string\",\n" +
                "\"tobt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"odt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"outTime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"turb\": \"string\",\n" +
                "\"inHeight\": \"string\",\n" +
                "\"cplGroup18\": \"string\",\n" +
                "\"lintime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"boardingTime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"fltState\": 0,\n" +
                "\"eobt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"aibt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"nonskedPlanRef\": 0,\n" +
                "\"acType\": \"string\",\n" +
                "\"focusflag\": \"string\",\n" +
                "\"arr\": \"string\",\n" +
                "\"lndtime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"star\": \"string\",\n" +
                "\"group10b\": \"string\",\n" +
                "\"ofTime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"group10a\": \"string\",\n" +
                "\"hitFlowcontrols\": \"string\",\n" +
                "\"atot\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"formerIdReal\": 0,\n" +
                "\"fplArr\": \"string\",\n" +
                "\"sfl\": \"string\",\n" +
                "\"boardingGate\": \"string\",\n" +
                "\"integrateDelFlag\": 0,\n" +
                "\"ibt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"linTime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"taxTime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"ofPoint\": \"string\",\n" +
                "\"dist\": \"string\",\n" +
                "\"outPoint\": \"string\",\n" +
                "\"realArr\": \"string\",\n" +
                "\"ata\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"frequency\": \"string\",\n" +
                "\"atd\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"cobt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"fplAcType\": \"string\",\n" +
                "\"outHeight\": \"string\",\n" +
                "\"eta\": \"string\",\n" +
                "\"group16c1\": \"string\",\n" +
                "\"skedPlanRef\": 0,\n" +
                "\"deicing\": 0,\n" +
                "\"group16c2\": \"string\",\n" +
                "\"hitControlflags\": \"string\",\n" +
                "\"taxRoute\": \"string\",\n" +
                "\"rnav5\": \"string\",\n" +
                "\"sector\": \"string\",\n" +
                "\"weekdays\": \"string\",\n" +
                "\"fplDep\": \"string\",\n" +
                "\"hobt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"runway\": \"string\",\n" +
                "\"rvsm\": \"string\",\n" +
                "\"relateDeps\": \"string\",\n" +
                "\"isNotifyAf\": 0,\n" +
                "\"vipflag\": \"string\",\n" +
                "\"atis\": \"string\",\n" +
                "\"task\": \"string\",\n" +
                "\"cobtType\": \"string\",\n" +
                "\"delDeps\": \"string\",\n" +
                "\"delayDesc\": \"string\",\n" +
                "\"rfl\": \"string\",\n" +
                "\"position\": \"string\",\n" +
                "\"pbn\": \"string\",\n" +
                "\"ssrCallback\": 0,\n" +
                "\"userMemo\": \"string\",\n" +
                "\"ssrMode\": \"string\",\n" +
                "\"unconfirmed\": 0,\n" +
                "\"gatePosition\": \"string\",\n" +
                "\"afpt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"distance\": \"string\",\n" +
                "\"sysMemo\": \"string\",\n" +
                "\"asat\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"delaytime\": 0,\n" +
                "\"fltStatus\": \"string\",\n" +
                "\"dynaEtd\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"efpsAta\": \"string\",\n" +
                "\"remark\": \"string\",\n" +
                "\"dynaEta\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"speed\": \"string\",\n" +
                "\"conjunctionId\": \"string\",\n" +
                "\"g18\": \"string\",\n" +
                "\"sid\": \"string\",\n" +
                "\"inPoint\": \"string\",\n" +
                "\"rwy\": \"string\",\n" +
                "\"syncflag\": \"string\",\n" +
                "\"closeTime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"asrt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"rdytime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"executionRate\": \"string\",\n" +
                "\"flowTime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"inTime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"statusChangedTime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"fplRoute\": \"string\",\n" +
                "\"fltNormal\": \"string\",\n" +
                "\"formerFltArrTime\": \"string\",\n" +
                "\"fplEet\": \"string\",\n" +
                "\"contrlStatus\": \"string\",\n" +
                "\"fltId\": \"string\",\n" +
                "\"realDep\": \"string\",\n" +
                "\"arrAp\": \"string\",\n" +
                "\"arwy\": \"string\",\n" +
                "\"cdmDestAp\": \"string\",\n" +
                "\"deptView\": \"string\"\n" +
                "}],\n" +
                "\"responses\": [{\n" +
                "\"code\": \"200\",\n" +
                "\"description\": \"请求成功\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"401\",\n" +
                "\"description\": \"未认证\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"403\",\n" +
                "\"description\": \"权限不足\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"404\",\n" +
                "\"description\": \"未找到\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"500\",\n" +
                "\"description\": \"请求失败\"\n" +
                "}\n" +
                "],\n" +
                "\"deprecated\": false,\n" +
                "\"label\": \"查询前序航班信息\"\n" +
                "},\n" +
                "{\n" +
                "\"uri\": \"/flightinfo/getIntegrateFlightInfoBySobtAndAcid\",\n" +
                "\"method\": \"get\",\n" +
                "\"controllers\": [\n" +
                "\"flight-info-controller\"\n" +
                "],\n" +
                "\"operationId\": \"getIntegrateFlightInfoBySobtAndAcidUsingGET\",\n" +
                "\"consumes\": [\n" +
                "\"*/*\"\n" +
                "],\n" +
                "\"produces\": [\n" +
                "\"application/xml\",\n" +
                "\"application/json\"\n" +
                "],\n" +
                "\"parameters\": [{\n" +
                "\"name\": \"acid\",\n" +
                "\"in\": \"query\",\n" +
                "\"description\": \"航班号\",\n" +
                "\"required\": true,\n" +
                "\"type\": \"string\",\n" +
                "\"allowEmptyValue\": false,\n" +
                "\"example\": \"CSC8923\"\n" +
                "},\n" +
                "{\n" +
                "\"name\": \"from\",\n" +
                "\"in\": \"query\",\n" +
                "\"description\": \"计划起飞时间查询起始时间\",\n" +
                "\"required\": true,\n" +
                "\"type\": \"string\",\n" +
                "\"allowEmptyValue\": false,\n" +
                "\"example\": \"2018-11-04T00:00:00Z\"\n" +
                "},\n" +
                "{\n" +
                "\"name\": \"to\",\n" +
                "\"in\": \"query\",\n" +
                "\"description\": \"计划起飞时间查询结束时间\",\n" +
                "\"required\": true,\n" +
                "\"type\": \"string\",\n" +
                "\"allowEmptyValue\": false,\n" +
                "\"example\": \"2018-11-05T00:00:00Z\"\n" +
                "}\n" +
                "],\n" +
                "\"result\": [{\n" +
                "\"obt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"executedate\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"fltHeight\": \"string\",\n" +
                "\"rfpt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"dep\": \"string\",\n" +
                "\"fltNum\": \"string\",\n" +
                "\"ssrCode\": \"string\",\n" +
                "\"tipt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"deicingPad\": \"string\",\n" +
                "\"fix\": \"string\",\n" +
                "\"isAbs\": \"string\",\n" +
                "\"majorCarrier\": \"string\",\n" +
                "\"beggate\": \"string\",\n" +
                "\"originFlag\": 0,\n" +
                "\"planId\": 0,\n" +
                "\"id\": 0,\n" +
                "\"endgate\": \"string\",\n" +
                "\"contrlPoint\": \"string\",\n" +
                "\"planEtd\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"planEta\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"taxi\": 0,\n" +
                "\"asbt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"priority\": 0,\n" +
                "\"aobt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"planDate\": \"string\",\n" +
                "\"formerId\": 0,\n" +
                "\"deparrflag\": \"string\",\n" +
                "\"pustime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"cdt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"route\": \"string\",\n" +
                "\"sts\": \"string\",\n" +
                "\"ctot\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"planRoute\": \"string\",\n" +
                "\"zbApproval\": \"string\",\n" +
                "\"agct\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"dof\": \"string\",\n" +
                "\"tobt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"odt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"outTime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"turb\": \"string\",\n" +
                "\"inHeight\": \"string\",\n" +
                "\"cplGroup18\": \"string\",\n" +
                "\"lintime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"boardingTime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"fltState\": 0,\n" +
                "\"eobt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"aibt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"nonskedPlanRef\": 0,\n" +
                "\"acType\": \"string\",\n" +
                "\"focusflag\": \"string\",\n" +
                "\"arr\": \"string\",\n" +
                "\"lndtime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"star\": \"string\",\n" +
                "\"group10b\": \"string\",\n" +
                "\"ofTime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"group10a\": \"string\",\n" +
                "\"hitFlowcontrols\": \"string\",\n" +
                "\"atot\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"formerIdReal\": 0,\n" +
                "\"fplArr\": \"string\",\n" +
                "\"sfl\": \"string\",\n" +
                "\"boardingGate\": \"string\",\n" +
                "\"integrateDelFlag\": 0,\n" +
                "\"ibt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"linTime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"taxTime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"ofPoint\": \"string\",\n" +
                "\"dist\": \"string\",\n" +
                "\"outPoint\": \"string\",\n" +
                "\"realArr\": \"string\",\n" +
                "\"ata\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"frequency\": \"string\",\n" +
                "\"atd\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"cobt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"fplAcType\": \"string\",\n" +
                "\"outHeight\": \"string\",\n" +
                "\"eta\": \"string\",\n" +
                "\"group16c1\": \"string\",\n" +
                "\"skedPlanRef\": 0,\n" +
                "\"deicing\": 0,\n" +
                "\"group16c2\": \"string\",\n" +
                "\"hitControlflags\": \"string\",\n" +
                "\"taxRoute\": \"string\",\n" +
                "\"rnav5\": \"string\",\n" +
                "\"sector\": \"string\",\n" +
                "\"weekdays\": \"string\",\n" +
                "\"fplDep\": \"string\",\n" +
                "\"hobt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"runway\": \"string\",\n" +
                "\"rvsm\": \"string\",\n" +
                "\"relateDeps\": \"string\",\n" +
                "\"isNotifyAf\": 0,\n" +
                "\"vipflag\": \"string\",\n" +
                "\"atis\": \"string\",\n" +
                "\"task\": \"string\",\n" +
                "\"cobtType\": \"string\",\n" +
                "\"delDeps\": \"string\",\n" +
                "\"delayDesc\": \"string\",\n" +
                "\"rfl\": \"string\",\n" +
                "\"position\": \"string\",\n" +
                "\"pbn\": \"string\",\n" +
                "\"ssrCallback\": 0,\n" +
                "\"userMemo\": \"string\",\n" +
                "\"ssrMode\": \"string\",\n" +
                "\"unconfirmed\": 0,\n" +
                "\"gatePosition\": \"string\",\n" +
                "\"afpt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"distance\": \"string\",\n" +
                "\"sysMemo\": \"string\",\n" +
                "\"asat\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"delaytime\": 0,\n" +
                "\"fltStatus\": \"string\",\n" +
                "\"dynaEtd\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"efpsAta\": \"string\",\n" +
                "\"remark\": \"string\",\n" +
                "\"dynaEta\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"speed\": \"string\",\n" +
                "\"conjunctionId\": \"string\",\n" +
                "\"g18\": \"string\",\n" +
                "\"sid\": \"string\",\n" +
                "\"inPoint\": \"string\",\n" +
                "\"rwy\": \"string\",\n" +
                "\"syncflag\": \"string\",\n" +
                "\"closeTime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"asrt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"rdytime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"executionRate\": \"string\",\n" +
                "\"flowTime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"inTime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"statusChangedTime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"fplRoute\": \"string\",\n" +
                "\"fltNormal\": \"string\",\n" +
                "\"formerFltArrTime\": \"string\",\n" +
                "\"fplEet\": \"string\",\n" +
                "\"contrlStatus\": \"string\",\n" +
                "\"fltId\": \"string\",\n" +
                "\"realDep\": \"string\",\n" +
                "\"arrAp\": \"string\",\n" +
                "\"arwy\": \"string\",\n" +
                "\"cdmDestAp\": \"string\",\n" +
                "\"deptView\": \"string\"\n" +
                "}],\n" +
                "\"responses\": [{\n" +
                "\"code\": \"200\",\n" +
                "\"description\": \"请求成功\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"401\",\n" +
                "\"description\": \"未认证\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"403\",\n" +
                "\"description\": \"权限不足\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"404\",\n" +
                "\"description\": \"未找到\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"500\",\n" +
                "\"description\": \"请求失败\"\n" +
                "}\n" +
                "],\n" +
                "\"deprecated\": false,\n" +
                "\"label\": \"航班信息\"\n" +
                "},\n" +
                "{\n" +
                "\"uri\": \"/flightinfo/getIntegrateFlightInfoBySobtAndArr\",\n" +
                "\"method\": \"get\",\n" +
                "\"controllers\": [\n" +
                "\"flight-info-controller\"\n" +
                "],\n" +
                "\"operationId\": \"getIntegrateFlightInfoBySobtAndArrUsingGET\",\n" +
                "\"consumes\": [\n" +
                "\"*/*\"\n" +
                "],\n" +
                "\"produces\": [\n" +
                "\"application/xml\",\n" +
                "\"application/json\"\n" +
                "],\n" +
                "\"parameters\": [{\n" +
                "\"name\": \"arr\",\n" +
                "\"in\": \"query\",\n" +
                "\"description\": \"降落机场四码\",\n" +
                "\"required\": true,\n" +
                "\"type\": \"string\",\n" +
                "\"allowEmptyValue\": false,\n" +
                "\"example\": \"ZUUU\"\n" +
                "},\n" +
                "{\n" +
                "\"name\": \"from\",\n" +
                "\"in\": \"query\",\n" +
                "\"description\": \"计划起飞时间查询起始时间\",\n" +
                "\"required\": true,\n" +
                "\"type\": \"string\",\n" +
                "\"allowEmptyValue\": false,\n" +
                "\"example\": \"2018-11-04T00:00:00Z\"\n" +
                "},\n" +
                "{\n" +
                "\"name\": \"to\",\n" +
                "\"in\": \"query\",\n" +
                "\"description\": \"计划起飞时间查询结束时间\",\n" +
                "\"required\": true,\n" +
                "\"type\": \"string\",\n" +
                "\"allowEmptyValue\": false,\n" +
                "\"example\": \"2018-11-05T00:00:00Z\"\n" +
                "}\n" +
                "],\n" +
                "\"result\": [{\n" +
                "\"obt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"executedate\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"fltHeight\": \"string\",\n" +
                "\"rfpt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"dep\": \"string\",\n" +
                "\"fltNum\": \"string\",\n" +
                "\"ssrCode\": \"string\",\n" +
                "\"tipt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"deicingPad\": \"string\",\n" +
                "\"fix\": \"string\",\n" +
                "\"isAbs\": \"string\",\n" +
                "\"majorCarrier\": \"string\",\n" +
                "\"beggate\": \"string\",\n" +
                "\"originFlag\": 0,\n" +
                "\"planId\": 0,\n" +
                "\"id\": 0,\n" +
                "\"endgate\": \"string\",\n" +
                "\"contrlPoint\": \"string\",\n" +
                "\"planEtd\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"planEta\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"taxi\": 0,\n" +
                "\"asbt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"priority\": 0,\n" +
                "\"aobt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"planDate\": \"string\",\n" +
                "\"formerId\": 0,\n" +
                "\"deparrflag\": \"string\",\n" +
                "\"pustime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"cdt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"route\": \"string\",\n" +
                "\"sts\": \"string\",\n" +
                "\"ctot\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"planRoute\": \"string\",\n" +
                "\"zbApproval\": \"string\",\n" +
                "\"agct\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"dof\": \"string\",\n" +
                "\"tobt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"odt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"outTime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"turb\": \"string\",\n" +
                "\"inHeight\": \"string\",\n" +
                "\"cplGroup18\": \"string\",\n" +
                "\"lintime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"boardingTime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"fltState\": 0,\n" +
                "\"eobt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"aibt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"nonskedPlanRef\": 0,\n" +
                "\"acType\": \"string\",\n" +
                "\"focusflag\": \"string\",\n" +
                "\"arr\": \"string\",\n" +
                "\"lndtime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"star\": \"string\",\n" +
                "\"group10b\": \"string\",\n" +
                "\"ofTime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"group10a\": \"string\",\n" +
                "\"hitFlowcontrols\": \"string\",\n" +
                "\"atot\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"formerIdReal\": 0,\n" +
                "\"fplArr\": \"string\",\n" +
                "\"sfl\": \"string\",\n" +
                "\"boardingGate\": \"string\",\n" +
                "\"integrateDelFlag\": 0,\n" +
                "\"ibt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"linTime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"taxTime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"ofPoint\": \"string\",\n" +
                "\"dist\": \"string\",\n" +
                "\"outPoint\": \"string\",\n" +
                "\"realArr\": \"string\",\n" +
                "\"ata\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"frequency\": \"string\",\n" +
                "\"atd\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"cobt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"fplAcType\": \"string\",\n" +
                "\"outHeight\": \"string\",\n" +
                "\"eta\": \"string\",\n" +
                "\"group16c1\": \"string\",\n" +
                "\"skedPlanRef\": 0,\n" +
                "\"deicing\": 0,\n" +
                "\"group16c2\": \"string\",\n" +
                "\"hitControlflags\": \"string\",\n" +
                "\"taxRoute\": \"string\",\n" +
                "\"rnav5\": \"string\",\n" +
                "\"sector\": \"string\",\n" +
                "\"weekdays\": \"string\",\n" +
                "\"fplDep\": \"string\",\n" +
                "\"hobt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"runway\": \"string\",\n" +
                "\"rvsm\": \"string\",\n" +
                "\"relateDeps\": \"string\",\n" +
                "\"isNotifyAf\": 0,\n" +
                "\"vipflag\": \"string\",\n" +
                "\"atis\": \"string\",\n" +
                "\"task\": \"string\",\n" +
                "\"cobtType\": \"string\",\n" +
                "\"delDeps\": \"string\",\n" +
                "\"delayDesc\": \"string\",\n" +
                "\"rfl\": \"string\",\n" +
                "\"position\": \"string\",\n" +
                "\"pbn\": \"string\",\n" +
                "\"ssrCallback\": 0,\n" +
                "\"userMemo\": \"string\",\n" +
                "\"ssrMode\": \"string\",\n" +
                "\"unconfirmed\": 0,\n" +
                "\"gatePosition\": \"string\",\n" +
                "\"afpt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"distance\": \"string\",\n" +
                "\"sysMemo\": \"string\",\n" +
                "\"asat\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"delaytime\": 0,\n" +
                "\"fltStatus\": \"string\",\n" +
                "\"dynaEtd\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"efpsAta\": \"string\",\n" +
                "\"remark\": \"string\",\n" +
                "\"dynaEta\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"speed\": \"string\",\n" +
                "\"conjunctionId\": \"string\",\n" +
                "\"g18\": \"string\",\n" +
                "\"sid\": \"string\",\n" +
                "\"inPoint\": \"string\",\n" +
                "\"rwy\": \"string\",\n" +
                "\"syncflag\": \"string\",\n" +
                "\"closeTime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"asrt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"rdytime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"executionRate\": \"string\",\n" +
                "\"flowTime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"inTime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"statusChangedTime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"fplRoute\": \"string\",\n" +
                "\"fltNormal\": \"string\",\n" +
                "\"formerFltArrTime\": \"string\",\n" +
                "\"fplEet\": \"string\",\n" +
                "\"contrlStatus\": \"string\",\n" +
                "\"fltId\": \"string\",\n" +
                "\"realDep\": \"string\",\n" +
                "\"arrAp\": \"string\",\n" +
                "\"arwy\": \"string\",\n" +
                "\"cdmDestAp\": \"string\",\n" +
                "\"deptView\": \"string\"\n" +
                "}],\n" +
                "\"responses\": [{\n" +
                "\"code\": \"200\",\n" +
                "\"description\": \"请求成功\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"401\",\n" +
                "\"description\": \"未认证\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"403\",\n" +
                "\"description\": \"权限不足\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"404\",\n" +
                "\"description\": \"未找到\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"500\",\n" +
                "\"description\": \"请求失败\"\n" +
                "}\n" +
                "],\n" +
                "\"deprecated\": false,\n" +
                "\"label\": \"航班信息\"\n" +
                "},\n" +
                "{\n" +
                "\"uri\": \"/flightinfo/getIntegrateFlightInfoBySobtAndDep\",\n" +
                "\"method\": \"get\",\n" +
                "\"controllers\": [\n" +
                "\"flight-info-controller\"\n" +
                "],\n" +
                "\"operationId\": \"getIntegrateFlightInfoBySobtAndDepUsingGET\",\n" +
                "\"consumes\": [\n" +
                "\"*/*\"\n" +
                "],\n" +
                "\"produces\": [\n" +
                "\"application/xml\",\n" +
                "\"application/json\"\n" +
                "],\n" +
                "\"parameters\": [{\n" +
                "\"name\": \"dep\",\n" +
                "\"in\": \"query\",\n" +
                "\"description\": \"起飞机场四码\",\n" +
                "\"required\": true,\n" +
                "\"type\": \"string\",\n" +
                "\"allowEmptyValue\": false,\n" +
                "\"example\": \"ZUUU\"\n" +
                "},\n" +
                "{\n" +
                "\"name\": \"from\",\n" +
                "\"in\": \"query\",\n" +
                "\"description\": \"计划起飞时间查询起始时间\",\n" +
                "\"required\": true,\n" +
                "\"type\": \"string\",\n" +
                "\"allowEmptyValue\": false,\n" +
                "\"example\": \"2018-11-04T00:00:00Z\"\n" +
                "},\n" +
                "{\n" +
                "\"name\": \"to\",\n" +
                "\"in\": \"query\",\n" +
                "\"description\": \"计划起飞时间查询结束时间\",\n" +
                "\"required\": true,\n" +
                "\"type\": \"string\",\n" +
                "\"allowEmptyValue\": false,\n" +
                "\"example\": \"2018-11-05T00:00:00Z\"\n" +
                "}\n" +
                "],\n" +
                "\"result\": [{\n" +
                "\"obt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"executedate\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"fltHeight\": \"string\",\n" +
                "\"rfpt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"dep\": \"string\",\n" +
                "\"fltNum\": \"string\",\n" +
                "\"ssrCode\": \"string\",\n" +
                "\"tipt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"deicingPad\": \"string\",\n" +
                "\"fix\": \"string\",\n" +
                "\"isAbs\": \"string\",\n" +
                "\"majorCarrier\": \"string\",\n" +
                "\"beggate\": \"string\",\n" +
                "\"originFlag\": 0,\n" +
                "\"planId\": 0,\n" +
                "\"id\": 0,\n" +
                "\"endgate\": \"string\",\n" +
                "\"contrlPoint\": \"string\",\n" +
                "\"planEtd\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"planEta\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"taxi\": 0,\n" +
                "\"asbt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"priority\": 0,\n" +
                "\"aobt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"planDate\": \"string\",\n" +
                "\"formerId\": 0,\n" +
                "\"deparrflag\": \"string\",\n" +
                "\"pustime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"cdt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"route\": \"string\",\n" +
                "\"sts\": \"string\",\n" +
                "\"ctot\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"planRoute\": \"string\",\n" +
                "\"zbApproval\": \"string\",\n" +
                "\"agct\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"dof\": \"string\",\n" +
                "\"tobt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"odt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"outTime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"turb\": \"string\",\n" +
                "\"inHeight\": \"string\",\n" +
                "\"cplGroup18\": \"string\",\n" +
                "\"lintime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"boardingTime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"fltState\": 0,\n" +
                "\"eobt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"aibt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"nonskedPlanRef\": 0,\n" +
                "\"acType\": \"string\",\n" +
                "\"focusflag\": \"string\",\n" +
                "\"arr\": \"string\",\n" +
                "\"lndtime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"star\": \"string\",\n" +
                "\"group10b\": \"string\",\n" +
                "\"ofTime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"group10a\": \"string\",\n" +
                "\"hitFlowcontrols\": \"string\",\n" +
                "\"atot\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"formerIdReal\": 0,\n" +
                "\"fplArr\": \"string\",\n" +
                "\"sfl\": \"string\",\n" +
                "\"boardingGate\": \"string\",\n" +
                "\"integrateDelFlag\": 0,\n" +
                "\"ibt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"linTime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"taxTime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"ofPoint\": \"string\",\n" +
                "\"dist\": \"string\",\n" +
                "\"outPoint\": \"string\",\n" +
                "\"realArr\": \"string\",\n" +
                "\"ata\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"frequency\": \"string\",\n" +
                "\"atd\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"cobt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"fplAcType\": \"string\",\n" +
                "\"outHeight\": \"string\",\n" +
                "\"eta\": \"string\",\n" +
                "\"group16c1\": \"string\",\n" +
                "\"skedPlanRef\": 0,\n" +
                "\"deicing\": 0,\n" +
                "\"group16c2\": \"string\",\n" +
                "\"hitControlflags\": \"string\",\n" +
                "\"taxRoute\": \"string\",\n" +
                "\"rnav5\": \"string\",\n" +
                "\"sector\": \"string\",\n" +
                "\"weekdays\": \"string\",\n" +
                "\"fplDep\": \"string\",\n" +
                "\"hobt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"runway\": \"string\",\n" +
                "\"rvsm\": \"string\",\n" +
                "\"relateDeps\": \"string\",\n" +
                "\"isNotifyAf\": 0,\n" +
                "\"vipflag\": \"string\",\n" +
                "\"atis\": \"string\",\n" +
                "\"task\": \"string\",\n" +
                "\"cobtType\": \"string\",\n" +
                "\"delDeps\": \"string\",\n" +
                "\"delayDesc\": \"string\",\n" +
                "\"rfl\": \"string\",\n" +
                "\"position\": \"string\",\n" +
                "\"pbn\": \"string\",\n" +
                "\"ssrCallback\": 0,\n" +
                "\"userMemo\": \"string\",\n" +
                "\"ssrMode\": \"string\",\n" +
                "\"unconfirmed\": 0,\n" +
                "\"gatePosition\": \"string\",\n" +
                "\"afpt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"distance\": \"string\",\n" +
                "\"sysMemo\": \"string\",\n" +
                "\"asat\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"delaytime\": 0,\n" +
                "\"fltStatus\": \"string\",\n" +
                "\"dynaEtd\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"efpsAta\": \"string\",\n" +
                "\"remark\": \"string\",\n" +
                "\"dynaEta\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"speed\": \"string\",\n" +
                "\"conjunctionId\": \"string\",\n" +
                "\"g18\": \"string\",\n" +
                "\"sid\": \"string\",\n" +
                "\"inPoint\": \"string\",\n" +
                "\"rwy\": \"string\",\n" +
                "\"syncflag\": \"string\",\n" +
                "\"closeTime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"asrt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"rdytime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"executionRate\": \"string\",\n" +
                "\"flowTime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"inTime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"statusChangedTime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"fplRoute\": \"string\",\n" +
                "\"fltNormal\": \"string\",\n" +
                "\"formerFltArrTime\": \"string\",\n" +
                "\"fplEet\": \"string\",\n" +
                "\"contrlStatus\": \"string\",\n" +
                "\"fltId\": \"string\",\n" +
                "\"realDep\": \"string\",\n" +
                "\"arrAp\": \"string\",\n" +
                "\"arwy\": \"string\",\n" +
                "\"cdmDestAp\": \"string\",\n" +
                "\"deptView\": \"string\"\n" +
                "}],\n" +
                "\"responses\": [{\n" +
                "\"code\": \"200\",\n" +
                "\"description\": \"请求成功\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"401\",\n" +
                "\"description\": \"未认证\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"403\",\n" +
                "\"description\": \"权限不足\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"404\",\n" +
                "\"description\": \"未找到\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"500\",\n" +
                "\"description\": \"请求失败\"\n" +
                "}\n" +
                "],\n" +
                "\"deprecated\": false,\n" +
                "\"label\": \"航班信息\"\n" +
                "},\n" +
                "{\n" +
                "\"uri\": \"/flightinfo/getIntegrateFlightInfoBySobtAndDepAndArrAndAcid\",\n" +
                "\"method\": \"get\",\n" +
                "\"controllers\": [\n" +
                "\"flight-info-controller\"\n" +
                "],\n" +
                "\"operationId\": \"getIntegrateFlightInfoBySobtAndDepAndArrAndAcidUsingGET\",\n" +
                "\"consumes\": [\n" +
                "\"*/*\"\n" +
                "],\n" +
                "\"produces\": [\n" +
                "\"application/xml\",\n" +
                "\"application/json\"\n" +
                "],\n" +
                "\"parameters\": [{\n" +
                "\"name\": \"acid\",\n" +
                "\"in\": \"query\",\n" +
                "\"description\": \"航班号\",\n" +
                "\"required\": true,\n" +
                "\"type\": \"string\",\n" +
                "\"allowEmptyValue\": false,\n" +
                "\"example\": \"CSC8923\"\n" +
                "},\n" +
                "{\n" +
                "\"name\": \"arr\",\n" +
                "\"in\": \"query\",\n" +
                "\"description\": \"降落机场四码\",\n" +
                "\"required\": true,\n" +
                "\"type\": \"string\",\n" +
                "\"allowEmptyValue\": false,\n" +
                "\"example\": \"ZSNJ\"\n" +
                "},\n" +
                "{\n" +
                "\"name\": \"dep\",\n" +
                "\"in\": \"query\",\n" +
                "\"description\": \"起飞机场四码\",\n" +
                "\"required\": true,\n" +
                "\"type\": \"string\",\n" +
                "\"allowEmptyValue\": false,\n" +
                "\"example\": \"ZUUU\"\n" +
                "},\n" +
                "{\n" +
                "\"name\": \"from\",\n" +
                "\"in\": \"query\",\n" +
                "\"description\": \"计划起飞时间查询起始时间\",\n" +
                "\"required\": true,\n" +
                "\"type\": \"string\",\n" +
                "\"allowEmptyValue\": false,\n" +
                "\"example\": \"2018-11-04T00:00:00Z\"\n" +
                "},\n" +
                "{\n" +
                "\"name\": \"to\",\n" +
                "\"in\": \"query\",\n" +
                "\"description\": \"计划起飞时间查询结束时间\",\n" +
                "\"required\": true,\n" +
                "\"type\": \"string\",\n" +
                "\"allowEmptyValue\": false,\n" +
                "\"example\": \"2018-11-05T00:00:00Z\"\n" +
                "}\n" +
                "],\n" +
                "\"result\": [{\n" +
                "\"obt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"executedate\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"fltHeight\": \"string\",\n" +
                "\"rfpt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"dep\": \"string\",\n" +
                "\"fltNum\": \"string\",\n" +
                "\"ssrCode\": \"string\",\n" +
                "\"tipt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"deicingPad\": \"string\",\n" +
                "\"fix\": \"string\",\n" +
                "\"isAbs\": \"string\",\n" +
                "\"majorCarrier\": \"string\",\n" +
                "\"beggate\": \"string\",\n" +
                "\"originFlag\": 0,\n" +
                "\"planId\": 0,\n" +
                "\"id\": 0,\n" +
                "\"endgate\": \"string\",\n" +
                "\"contrlPoint\": \"string\",\n" +
                "\"planEtd\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"planEta\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"taxi\": 0,\n" +
                "\"asbt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"priority\": 0,\n" +
                "\"aobt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"planDate\": \"string\",\n" +
                "\"formerId\": 0,\n" +
                "\"deparrflag\": \"string\",\n" +
                "\"pustime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"cdt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"route\": \"string\",\n" +
                "\"sts\": \"string\",\n" +
                "\"ctot\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"planRoute\": \"string\",\n" +
                "\"zbApproval\": \"string\",\n" +
                "\"agct\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"dof\": \"string\",\n" +
                "\"tobt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"odt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"outTime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"turb\": \"string\",\n" +
                "\"inHeight\": \"string\",\n" +
                "\"cplGroup18\": \"string\",\n" +
                "\"lintime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"boardingTime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"fltState\": 0,\n" +
                "\"eobt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"aibt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"nonskedPlanRef\": 0,\n" +
                "\"acType\": \"string\",\n" +
                "\"focusflag\": \"string\",\n" +
                "\"arr\": \"string\",\n" +
                "\"lndtime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"star\": \"string\",\n" +
                "\"group10b\": \"string\",\n" +
                "\"ofTime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"group10a\": \"string\",\n" +
                "\"hitFlowcontrols\": \"string\",\n" +
                "\"atot\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"formerIdReal\": 0,\n" +
                "\"fplArr\": \"string\",\n" +
                "\"sfl\": \"string\",\n" +
                "\"boardingGate\": \"string\",\n" +
                "\"integrateDelFlag\": 0,\n" +
                "\"ibt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"linTime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"taxTime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"ofPoint\": \"string\",\n" +
                "\"dist\": \"string\",\n" +
                "\"outPoint\": \"string\",\n" +
                "\"realArr\": \"string\",\n" +
                "\"ata\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"frequency\": \"string\",\n" +
                "\"atd\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"cobt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"fplAcType\": \"string\",\n" +
                "\"outHeight\": \"string\",\n" +
                "\"eta\": \"string\",\n" +
                "\"group16c1\": \"string\",\n" +
                "\"skedPlanRef\": 0,\n" +
                "\"deicing\": 0,\n" +
                "\"group16c2\": \"string\",\n" +
                "\"hitControlflags\": \"string\",\n" +
                "\"taxRoute\": \"string\",\n" +
                "\"rnav5\": \"string\",\n" +
                "\"sector\": \"string\",\n" +
                "\"weekdays\": \"string\",\n" +
                "\"fplDep\": \"string\",\n" +
                "\"hobt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"runway\": \"string\",\n" +
                "\"rvsm\": \"string\",\n" +
                "\"relateDeps\": \"string\",\n" +
                "\"isNotifyAf\": 0,\n" +
                "\"vipflag\": \"string\",\n" +
                "\"atis\": \"string\",\n" +
                "\"task\": \"string\",\n" +
                "\"cobtType\": \"string\",\n" +
                "\"delDeps\": \"string\",\n" +
                "\"delayDesc\": \"string\",\n" +
                "\"rfl\": \"string\",\n" +
                "\"position\": \"string\",\n" +
                "\"pbn\": \"string\",\n" +
                "\"ssrCallback\": 0,\n" +
                "\"userMemo\": \"string\",\n" +
                "\"ssrMode\": \"string\",\n" +
                "\"unconfirmed\": 0,\n" +
                "\"gatePosition\": \"string\",\n" +
                "\"afpt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"distance\": \"string\",\n" +
                "\"sysMemo\": \"string\",\n" +
                "\"asat\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"delaytime\": 0,\n" +
                "\"fltStatus\": \"string\",\n" +
                "\"dynaEtd\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"efpsAta\": \"string\",\n" +
                "\"remark\": \"string\",\n" +
                "\"dynaEta\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"speed\": \"string\",\n" +
                "\"conjunctionId\": \"string\",\n" +
                "\"g18\": \"string\",\n" +
                "\"sid\": \"string\",\n" +
                "\"inPoint\": \"string\",\n" +
                "\"rwy\": \"string\",\n" +
                "\"syncflag\": \"string\",\n" +
                "\"closeTime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"asrt\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"rdytime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"executionRate\": \"string\",\n" +
                "\"flowTime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"inTime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"statusChangedTime\": \"2020-03-24T04:44:17.802+0000\",\n" +
                "\"fplRoute\": \"string\",\n" +
                "\"fltNormal\": \"string\",\n" +
                "\"formerFltArrTime\": \"string\",\n" +
                "\"fplEet\": \"string\",\n" +
                "\"contrlStatus\": \"string\",\n" +
                "\"fltId\": \"string\",\n" +
                "\"realDep\": \"string\",\n" +
                "\"arrAp\": \"string\",\n" +
                "\"arwy\": \"string\",\n" +
                "\"cdmDestAp\": \"string\",\n" +
                "\"deptView\": \"string\"\n" +
                "}],\n" +
                "\"responses\": [{\n" +
                "\"code\": \"200\",\n" +
                "\"description\": \"请求成功\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"401\",\n" +
                "\"description\": \"未认证\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"403\",\n" +
                "\"description\": \"权限不足\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"404\",\n" +
                "\"description\": \"未找到\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"500\",\n" +
                "\"description\": \"请求失败\"\n" +
                "}\n" +
                "],\n" +
                "\"deprecated\": false,\n" +
                "\"label\": \"航班信息\"\n" +
                "}\n" +
                "]\n" +
                "},\n" +
                "{\n" +
                "\"host\": \"192.168.204.71:8720\",\n" +
                "\"basePath\": \"/gateway/fdexm-service\",\n" +
                "\"label\": \"FDEXM飞行数据交换信息服务 API\",\n" +
                "\"children\": [{\n" +
                "\"uri\": \"/fdexm/get\",\n" +
                "\"method\": \"get\",\n" +
                "\"controllers\": [\n" +
                "\"fdexm-controller\"\n" +
                "],\n" +
                "\"operationId\": \"getMessagesUsingGET\",\n" +
                "\"consumes\": [\n" +
                "\"*/*\"\n" +
                "],\n" +
                "\"produces\": [\n" +
                "\"application/xml\",\n" +
                "\"application/json\"\n" +
                "],\n" +
                "\"parameters\": [{\n" +
                "\"name\": \"airport\",\n" +
                "\"in\": \"query\",\n" +
                "\"description\": \"机场四码\",\n" +
                "\"required\": true,\n" +
                "\"type\": \"string\",\n" +
                "\"allowEmptyValue\": false,\n" +
                "\"example\": \"ZUGY\"\n" +
                "},\n" +
                "{\n" +
                "\"name\": \"from\",\n" +
                "\"in\": \"query\",\n" +
                "\"description\": \"查询起始时间\",\n" +
                "\"required\": true,\n" +
                "\"type\": \"string\",\n" +
                "\"allowEmptyValue\": false,\n" +
                "\"example\": \"2018-10-02\"\n" +
                "},\n" +
                "{\n" +
                "\"name\": \"key\",\n" +
                "\"in\": \"query\",\n" +
                "\"description\": \"报文关键字\",\n" +
                "\"required\": true,\n" +
                "\"type\": \"string\",\n" +
                "\"allowEmptyValue\": false,\n" +
                "\"example\": \"HOST ACSDD4\"\n" +
                "},\n" +
                "{\n" +
                "\"name\": \"to\",\n" +
                "\"in\": \"query\",\n" +
                "\"description\": \"查询结束时间\",\n" +
                "\"required\": true,\n" +
                "\"type\": \"string\",\n" +
                "\"allowEmptyValue\": false,\n" +
                "\"example\": \"2018-10-03\"\n" +
                "},\n" +
                "{\n" +
                "\"name\": \"type\",\n" +
                "\"in\": \"query\",\n" +
                "\"description\": \"报文类型\",\n" +
                "\"required\": true,\n" +
                "\"type\": \"string\",\n" +
                "\"allowEmptyValue\": false,\n" +
                "\"example\": \"BCWP\"\n" +
                "}\n" +
                "],\n" +
                "\"result\": [{\n" +
                "\"msg\": \"string\",\n" +
                "\"id\": 0,\n" +
                "\"rcvTime\": \"2020-03-24T04:44:17.829+0000\"\n" +
                "}],\n" +
                "\"responses\": [{\n" +
                "\"code\": \"200\",\n" +
                "\"description\": \"请求成功\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"401\",\n" +
                "\"description\": \"未认证\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"403\",\n" +
                "\"description\": \"权限不足\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"404\",\n" +
                "\"description\": \"未找到\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"500\",\n" +
                "\"description\": \"请求失败\"\n" +
                "}\n" +
                "],\n" +
                "\"deprecated\": false,\n" +
                "\"label\": \"报文类型和关键字查询\"\n" +
                "}]\n" +
                "}\n" +
                "]\n" +
                "}";
    }
    
    @RequestMapping("/service1")
    public String service1() {
        return "{\n" +
                "\t\"code\": 20000,\n" +
                "\t\"msg\": \"\",\n" +
                "\t\"data\": [{\n" +
                "\t\t\t\"host\": \"192.168.204.71:8720\",\n" +
                "\t\t\t\"basePath\": \"/gateway/user-permission-service\",\n" +
                "\t\t\t\"label\": \"用户权限服务 API\",\n" +
                "\t\t\t\"children\": [{\n" +
                "\t\t\t\t\t\"name\": \"permission-controller\",\n" +
                "\t\t\t\t\t\"label\": \"权限信息服务\",\n" +
                "\t\t\t\t\t\"children\": [{\n" +
                "\t\t\t\t\t\t\t\"uri\": \"/permission/getPermission/{name}\",\n" +
                "\t\t\t\t\t\t\t\"method\": \"get\",\n" +
                "\t\t\t\t\t\t\t\"controllers\": [\n" +
                "\t\t\t\t\t\t\t\t\"permission-controller\"\n" +
                "\t\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\t\"operationId\": \"getPermissionUsingGET\",\n" +
                "\t\t\t\t\t\t\t\"consumes\": [\n" +
                "\t\t\t\t\t\t\t\t\"*/*\"\n" +
                "\t\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\t\"produces\": [\n" +
                "\t\t\t\t\t\t\t\t\"application/xml\",\n" +
                "\t\t\t\t\t\t\t\t\"application/json\"\n" +
                "\t\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\t\"parameters\": [{\n" +
                "\t\t\t\t\t\t\t\t\"name\": \"name\",\n" +
                "\t\t\t\t\t\t\t\t\"in\": \"path\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"name\",\n" +
                "\t\t\t\t\t\t\t\t\"required\": true,\n" +
                "\t\t\t\t\t\t\t\t\"type\": \"string\",\n" +
                "\t\t\t\t\t\t\t\t\"allowEmptyValue\": false,\n" +
                "\t\t\t\t\t\t\t\t\"example\": null\n" +
                "\t\t\t\t\t\t\t}],\n" +
                "\t\t\t\t\t\t\t\"result\": {\n" +
                "\t\t\t\t\t\t\t\t\"name\": \"string\",\n" +
                "\t\t\t\t\t\t\t\t\"id\": 0,\n" +
                "\t\t\t\t\t\t\t\t\"url\": \"string\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\"responses\": [{\n" +
                "\t\t\t\t\t\t\t\t\t\"code\": \"200\",\n" +
                "\t\t\t\t\t\t\t\t\t\"description\": \"请求成功\"\n" +
                "\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\t\"code\": \"401\",\n" +
                "\t\t\t\t\t\t\t\t\t\"description\": \"未认证\"\n" +
                "\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\t\"code\": \"403\",\n" +
                "\t\t\t\t\t\t\t\t\t\"description\": \"权限不足\"\n" +
                "\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\t\"code\": \"404\",\n" +
                "\t\t\t\t\t\t\t\t\t\"description\": \"未找到\"\n" +
                "\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\t\"code\": \"500\",\n" +
                "\t\t\t\t\t\t\t\t\t\"description\": \"请求失败\"\n" +
                "\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\t\"deprecated\": false,\n" +
                "\t\t\t\t\t\t\t\"label\": \"根据权限名查询对应权限\"\n" +
                "\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\"uri\": \"/permission/getRolePermission/{roleId}\",\n" +
                "\t\t\t\t\t\t\t\"method\": \"get\",\n" +
                "\t\t\t\t\t\t\t\"controllers\": [\n" +
                "\t\t\t\t\t\t\t\t\"permission-controller\"\n" +
                "\t\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\t\"operationId\": \"getRolePermissionUsingGET\",\n" +
                "\t\t\t\t\t\t\t\"consumes\": [\n" +
                "\t\t\t\t\t\t\t\t\"*/*\"\n" +
                "\t\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\t\"produces\": [\n" +
                "\t\t\t\t\t\t\t\t\"application/xml\",\n" +
                "\t\t\t\t\t\t\t\t\"application/json\"\n" +
                "\t\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\t\"parameters\": [{\n" +
                "\t\t\t\t\t\t\t\t\"name\": \"roleId\",\n" +
                "\t\t\t\t\t\t\t\t\"in\": \"path\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"roleId\",\n" +
                "\t\t\t\t\t\t\t\t\"required\": true,\n" +
                "\t\t\t\t\t\t\t\t\"type\": \"integer\",\n" +
                "\t\t\t\t\t\t\t\t\"allowEmptyValue\": false,\n" +
                "\t\t\t\t\t\t\t\t\"example\": null\n" +
                "\t\t\t\t\t\t\t}],\n" +
                "\t\t\t\t\t\t\t\"result\": [{\n" +
                "\t\t\t\t\t\t\t\t\"name\": \"string\",\n" +
                "\t\t\t\t\t\t\t\t\"id\": 0,\n" +
                "\t\t\t\t\t\t\t\t\"url\": \"string\"\n" +
                "\t\t\t\t\t\t\t}],\n" +
                "\t\t\t\t\t\t\t\"responses\": [{\n" +
                "\t\t\t\t\t\t\t\t\t\"code\": \"200\",\n" +
                "\t\t\t\t\t\t\t\t\t\"description\": \"请求成功\"\n" +
                "\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\t\"code\": \"401\",\n" +
                "\t\t\t\t\t\t\t\t\t\"description\": \"未认证\"\n" +
                "\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\t\"code\": \"403\",\n" +
                "\t\t\t\t\t\t\t\t\t\"description\": \"权限不足\"\n" +
                "\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\t\"code\": \"404\",\n" +
                "\t\t\t\t\t\t\t\t\t\"description\": \"未找到\"\n" +
                "\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\t\"code\": \"500\",\n" +
                "\t\t\t\t\t\t\t\t\t\"description\": \"请求失败\"\n" +
                "\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\t\"deprecated\": false,\n" +
                "\t\t\t\t\t\t\t\"label\": \"根据角色ID查询对应权限\"\n" +
                "\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"name\": \"role-controller\",\n" +
                "\t\t\t\t\t\"label\": \"角色信息服务\",\n" +
                "\t\t\t\t\t\"children\": [{\n" +
                "\t\t\t\t\t\t\"uri\": \"/role/getRoleByUserId/{userId}\",\n" +
                "\t\t\t\t\t\t\"method\": \"get\",\n" +
                "\t\t\t\t\t\t\"controllers\": [\n" +
                "\t\t\t\t\t\t\t\"role-controller\"\n" +
                "\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\"operationId\": \"getRoleByUserIdUsingGET\",\n" +
                "\t\t\t\t\t\t\"consumes\": [\n" +
                "\t\t\t\t\t\t\t\"*/*\"\n" +
                "\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\"produces\": [\n" +
                "\t\t\t\t\t\t\t\"application/xml\",\n" +
                "\t\t\t\t\t\t\t\"application/json\"\n" +
                "\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\"parameters\": [{\n" +
                "\t\t\t\t\t\t\t\"name\": \"userId\",\n" +
                "\t\t\t\t\t\t\t\"in\": \"path\",\n" +
                "\t\t\t\t\t\t\t\"description\": \"userId\",\n" +
                "\t\t\t\t\t\t\t\"required\": true,\n" +
                "\t\t\t\t\t\t\t\"type\": \"integer\",\n" +
                "\t\t\t\t\t\t\t\"allowEmptyValue\": false,\n" +
                "\t\t\t\t\t\t\t\"example\": null\n" +
                "\t\t\t\t\t\t}],\n" +
                "\t\t\t\t\t\t\"result\": [{\n" +
                "\t\t\t\t\t\t\t\"createTime\": \"2020-03-23T06:18:25.038+0000\",\n" +
                "\t\t\t\t\t\t\t\"name\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"description\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"id\": 0,\n" +
                "\t\t\t\t\t\t\t\"lastUpdateTime\": \"2020-03-23T06:18:25.038+0000\",\n" +
                "\t\t\t\t\t\t\t\"status\": 0\n" +
                "\t\t\t\t\t\t}],\n" +
                "\t\t\t\t\t\t\"responses\": [{\n" +
                "\t\t\t\t\t\t\t\t\"code\": \"200\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"请求成功\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\"code\": \"401\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"未认证\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\"code\": \"403\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"权限不足\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\"code\": \"404\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"未找到\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\"code\": \"500\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"请求失败\"\n" +
                "\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\"deprecated\": false,\n" +
                "\t\t\t\t\t\t\"label\": \"根据用户ID查询对应角色\"\n" +
                "\t\t\t\t\t}]\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"name\": \"user-controller\",\n" +
                "\t\t\t\t\t\"label\": \"用户信息服务\",\n" +
                "\t\t\t\t\t\"children\": [{\n" +
                "\t\t\t\t\t\t\"uri\": \"/user/findByUsername/{username}\",\n" +
                "\t\t\t\t\t\t\"method\": \"get\",\n" +
                "\t\t\t\t\t\t\"controllers\": [\n" +
                "\t\t\t\t\t\t\t\"user-controller\"\n" +
                "\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\"operationId\": \"findByUsernameUsingGET\",\n" +
                "\t\t\t\t\t\t\"consumes\": [\n" +
                "\t\t\t\t\t\t\t\"*/*\"\n" +
                "\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\"produces\": [\n" +
                "\t\t\t\t\t\t\t\"application/xml\",\n" +
                "\t\t\t\t\t\t\t\"application/json\"\n" +
                "\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\"parameters\": [{\n" +
                "\t\t\t\t\t\t\t\"name\": \"username\",\n" +
                "\t\t\t\t\t\t\t\"in\": \"path\",\n" +
                "\t\t\t\t\t\t\t\"description\": \"username\",\n" +
                "\t\t\t\t\t\t\t\"required\": true,\n" +
                "\t\t\t\t\t\t\t\"type\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"allowEmptyValue\": false,\n" +
                "\t\t\t\t\t\t\t\"example\": null\n" +
                "\t\t\t\t\t\t}],\n" +
                "\t\t\t\t\t\t\"result\": {\n" +
                "\t\t\t\t\t\t\t\"accountLocked\": true,\n" +
                "\t\t\t\t\t\t\t\"password\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"accountExpired\": true,\n" +
                "\t\t\t\t\t\t\t\"id\": 0,\n" +
                "\t\t\t\t\t\t\t\"credentialsExpired\": true,\n" +
                "\t\t\t\t\t\t\t\"enabled\": true,\n" +
                "\t\t\t\t\t\t\t\"username\": \"string\"\n" +
                "\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\"responses\": [{\n" +
                "\t\t\t\t\t\t\t\t\"code\": \"200\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"请求成功\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\"code\": \"401\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"未认证\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\"code\": \"403\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"权限不足\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\"code\": \"404\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"未找到\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\"code\": \"500\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"请求失败\"\n" +
                "\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\"deprecated\": false,\n" +
                "\t\t\t\t\t\t\"label\": \"根据用户名查询用户\"\n" +
                "\t\t\t\t\t}]\n" +
                "\t\t\t\t}\n" +
                "\t\t\t]\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"host\": \"192.168.204.71:8720\",\n" +
                "\t\t\t\"basePath\": \"/gateway/flightinfo-service\",\n" +
                "\t\t\t\"label\": \"FlightInfo航班信息服务 API\",\n" +
                "\t\t\t\"children\": [{\n" +
                "\t\t\t\t\"name\": \"flight-info-controller\",\n" +
                "\t\t\t\t\"label\": \"FlightInfo航班信息服务\",\n" +
                "\t\t\t\t\"children\": [{\n" +
                "\t\t\t\t\t\t\"uri\": \"/flightinfo/getCloseDoorOverTimMinutes\",\n" +
                "\t\t\t\t\t\t\"method\": \"get\",\n" +
                "\t\t\t\t\t\t\"controllers\": [\n" +
                "\t\t\t\t\t\t\t\"flight-info-controller\"\n" +
                "\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\"operationId\": \"getCloseDoorOverTimMinutesUsingGET\",\n" +
                "\t\t\t\t\t\t\"consumes\": [\n" +
                "\t\t\t\t\t\t\t\"*/*\"\n" +
                "\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\"produces\": [\n" +
                "\t\t\t\t\t\t\t\"application/xml\",\n" +
                "\t\t\t\t\t\t\t\"application/json\"\n" +
                "\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\"parameters\": null,\n" +
                "\t\t\t\t\t\t\"result\": {\n" +
                "\t\t\t\t\t\t\t\"overtime\": 0\n" +
                "\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\"responses\": [{\n" +
                "\t\t\t\t\t\t\t\t\"code\": \"200\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"请求成功\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\"code\": \"401\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"未认证\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\"code\": \"403\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"权限不足\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\"code\": \"404\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"未找到\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\"code\": \"500\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"请求失败\"\n" +
                "\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\"deprecated\": false,\n" +
                "\t\t\t\t\t\t\"label\": \"关舱门超时分钟数\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"uri\": \"/flightinfo/getCloseDoorOverTimeFlightInfo\",\n" +
                "\t\t\t\t\t\t\"method\": \"get\",\n" +
                "\t\t\t\t\t\t\"controllers\": [\n" +
                "\t\t\t\t\t\t\t\"flight-info-controller\"\n" +
                "\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\"operationId\": \"getCloseDoorOverTimeFlightInfoUsingGET\",\n" +
                "\t\t\t\t\t\t\"consumes\": [\n" +
                "\t\t\t\t\t\t\t\"*/*\"\n" +
                "\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\"produces\": [\n" +
                "\t\t\t\t\t\t\t\"application/xml\",\n" +
                "\t\t\t\t\t\t\t\"application/json\"\n" +
                "\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\"parameters\": [{\n" +
                "\t\t\t\t\t\t\t\t\"name\": \"airport\",\n" +
                "\t\t\t\t\t\t\t\t\"in\": \"query\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"起飞机场四码\",\n" +
                "\t\t\t\t\t\t\t\t\"required\": true,\n" +
                "\t\t\t\t\t\t\t\t\"type\": \"string\",\n" +
                "\t\t\t\t\t\t\t\t\"allowEmptyValue\": false,\n" +
                "\t\t\t\t\t\t\t\t\"example\": \"ZUUU\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\"name\": \"from\",\n" +
                "\t\t\t\t\t\t\t\t\"in\": \"query\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"计划起飞时间查询起始时间\",\n" +
                "\t\t\t\t\t\t\t\t\"required\": true,\n" +
                "\t\t\t\t\t\t\t\t\"type\": \"string\",\n" +
                "\t\t\t\t\t\t\t\t\"allowEmptyValue\": false,\n" +
                "\t\t\t\t\t\t\t\t\"example\": \"2018-11-04T00:00:00Z\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\"name\": \"to\",\n" +
                "\t\t\t\t\t\t\t\t\"in\": \"query\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"计划起飞时间查询结束时间\",\n" +
                "\t\t\t\t\t\t\t\t\"required\": true,\n" +
                "\t\t\t\t\t\t\t\t\"type\": \"string\",\n" +
                "\t\t\t\t\t\t\t\t\"allowEmptyValue\": false,\n" +
                "\t\t\t\t\t\t\t\t\"example\": \"2018-11-05T00:00:00Z\"\n" +
                "\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\"result\": [{\n" +
                "\t\t\t\t\t\t\t\"acid\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"eobt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"atot\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"overtimeMinutes\": 0,\n" +
                "\t\t\t\t\t\t\t\"agct\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"sobt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"dest\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"sibt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"dep\": \"string\"\n" +
                "\t\t\t\t\t\t}],\n" +
                "\t\t\t\t\t\t\"responses\": [{\n" +
                "\t\t\t\t\t\t\t\t\"code\": \"200\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"请求成功\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\"code\": \"401\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"未认证\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\"code\": \"403\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"权限不足\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\"code\": \"404\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"未找到\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\"code\": \"500\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"请求失败\"\n" +
                "\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\"deprecated\": false,\n" +
                "\t\t\t\t\t\t\"label\": \"关舱门超时航班信息\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"uri\": \"/flightinfo/getFormerIntegrateFlightInfoBySobtAndAcid\",\n" +
                "\t\t\t\t\t\t\"method\": \"get\",\n" +
                "\t\t\t\t\t\t\"controllers\": [\n" +
                "\t\t\t\t\t\t\t\"flight-info-controller\"\n" +
                "\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\"operationId\": \"getFormerIntegrateFlightInfoBySobtAndAcidUsingGET\",\n" +
                "\t\t\t\t\t\t\"consumes\": [\n" +
                "\t\t\t\t\t\t\t\"*/*\"\n" +
                "\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\"produces\": [\n" +
                "\t\t\t\t\t\t\t\"application/xml\",\n" +
                "\t\t\t\t\t\t\t\"application/json\"\n" +
                "\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\"parameters\": [{\n" +
                "\t\t\t\t\t\t\t\t\"name\": \"acid\",\n" +
                "\t\t\t\t\t\t\t\t\"in\": \"query\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"航班号\",\n" +
                "\t\t\t\t\t\t\t\t\"required\": true,\n" +
                "\t\t\t\t\t\t\t\t\"type\": \"string\",\n" +
                "\t\t\t\t\t\t\t\t\"allowEmptyValue\": false,\n" +
                "\t\t\t\t\t\t\t\t\"example\": \"CSC8923\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\"name\": \"from\",\n" +
                "\t\t\t\t\t\t\t\t\"in\": \"query\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"计划起飞时间查询起始时间\",\n" +
                "\t\t\t\t\t\t\t\t\"required\": true,\n" +
                "\t\t\t\t\t\t\t\t\"type\": \"string\",\n" +
                "\t\t\t\t\t\t\t\t\"allowEmptyValue\": false,\n" +
                "\t\t\t\t\t\t\t\t\"example\": \"2018-11-04T00:00:00Z\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\"name\": \"to\",\n" +
                "\t\t\t\t\t\t\t\t\"in\": \"query\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"计划起飞时间查询结束时间\",\n" +
                "\t\t\t\t\t\t\t\t\"required\": true,\n" +
                "\t\t\t\t\t\t\t\t\"type\": \"string\",\n" +
                "\t\t\t\t\t\t\t\t\"allowEmptyValue\": false,\n" +
                "\t\t\t\t\t\t\t\t\"example\": \"2018-11-05T00:00:00Z\"\n" +
                "\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\"result\": [{\n" +
                "\t\t\t\t\t\t\t\"obt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"executedate\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"fltHeight\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"rfpt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"dep\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"fltNum\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"ssrCode\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"tipt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"deicingPad\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"fix\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"isAbs\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"majorCarrier\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"beggate\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"originFlag\": 0,\n" +
                "\t\t\t\t\t\t\t\"planId\": 0,\n" +
                "\t\t\t\t\t\t\t\"id\": 0,\n" +
                "\t\t\t\t\t\t\t\"endgate\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"contrlPoint\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"planEtd\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"planEta\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"taxi\": 0,\n" +
                "\t\t\t\t\t\t\t\"asbt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"priority\": 0,\n" +
                "\t\t\t\t\t\t\t\"aobt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"planDate\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"formerId\": 0,\n" +
                "\t\t\t\t\t\t\t\"deparrflag\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"pustime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"cdt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"route\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"sts\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"ctot\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"planRoute\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"zbApproval\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"agct\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"dof\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"tobt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"odt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"outTime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"turb\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"inHeight\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"cplGroup18\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"lintime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"boardingTime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"fltState\": 0,\n" +
                "\t\t\t\t\t\t\t\"eobt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"aibt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"nonskedPlanRef\": 0,\n" +
                "\t\t\t\t\t\t\t\"acType\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"focusflag\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"arr\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"lndtime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"star\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"group10b\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"ofTime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"group10a\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"hitFlowcontrols\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"atot\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"formerIdReal\": 0,\n" +
                "\t\t\t\t\t\t\t\"fplArr\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"sfl\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"boardingGate\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"integrateDelFlag\": 0,\n" +
                "\t\t\t\t\t\t\t\"ibt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"linTime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"taxTime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"ofPoint\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"dist\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"outPoint\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"realArr\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"ata\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"frequency\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"atd\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"cobt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"fplAcType\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"outHeight\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"eta\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"group16c1\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"skedPlanRef\": 0,\n" +
                "\t\t\t\t\t\t\t\"deicing\": 0,\n" +
                "\t\t\t\t\t\t\t\"group16c2\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"hitControlflags\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"taxRoute\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"rnav5\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"sector\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"weekdays\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"fplDep\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"hobt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"runway\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"rvsm\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"relateDeps\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"isNotifyAf\": 0,\n" +
                "\t\t\t\t\t\t\t\"vipflag\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"atis\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"task\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"cobtType\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"delDeps\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"delayDesc\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"rfl\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"position\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"pbn\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"ssrCallback\": 0,\n" +
                "\t\t\t\t\t\t\t\"userMemo\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"ssrMode\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"unconfirmed\": 0,\n" +
                "\t\t\t\t\t\t\t\"gatePosition\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"afpt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"distance\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"sysMemo\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"asat\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"delaytime\": 0,\n" +
                "\t\t\t\t\t\t\t\"fltStatus\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"dynaEtd\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"efpsAta\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"remark\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"dynaEta\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"speed\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"conjunctionId\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"g18\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"sid\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"inPoint\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"rwy\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"syncflag\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"closeTime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"asrt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"rdytime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"executionRate\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"flowTime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"inTime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"statusChangedTime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"fplRoute\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"fltNormal\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"formerFltArrTime\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"fplEet\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"contrlStatus\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"fltId\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"realDep\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"arrAp\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"arwy\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"cdmDestAp\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"deptView\": \"string\"\n" +
                "\t\t\t\t\t\t}],\n" +
                "\t\t\t\t\t\t\"responses\": [{\n" +
                "\t\t\t\t\t\t\t\t\"code\": \"200\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"请求成功\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\"code\": \"401\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"未认证\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\"code\": \"403\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"权限不足\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\"code\": \"404\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"未找到\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\"code\": \"500\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"请求失败\"\n" +
                "\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\"deprecated\": false,\n" +
                "\t\t\t\t\t\t\"label\": \"查询前序航班信息\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"uri\": \"/flightinfo/getIntegrateFlightInfoBySobtAndAcid\",\n" +
                "\t\t\t\t\t\t\"method\": \"get\",\n" +
                "\t\t\t\t\t\t\"controllers\": [\n" +
                "\t\t\t\t\t\t\t\"flight-info-controller\"\n" +
                "\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\"operationId\": \"getIntegrateFlightInfoBySobtAndAcidUsingGET\",\n" +
                "\t\t\t\t\t\t\"consumes\": [\n" +
                "\t\t\t\t\t\t\t\"*/*\"\n" +
                "\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\"produces\": [\n" +
                "\t\t\t\t\t\t\t\"application/xml\",\n" +
                "\t\t\t\t\t\t\t\"application/json\"\n" +
                "\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\"parameters\": [{\n" +
                "\t\t\t\t\t\t\t\t\"name\": \"acid\",\n" +
                "\t\t\t\t\t\t\t\t\"in\": \"query\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"航班号\",\n" +
                "\t\t\t\t\t\t\t\t\"required\": true,\n" +
                "\t\t\t\t\t\t\t\t\"type\": \"string\",\n" +
                "\t\t\t\t\t\t\t\t\"allowEmptyValue\": false,\n" +
                "\t\t\t\t\t\t\t\t\"example\": \"CSC8923\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\"name\": \"from\",\n" +
                "\t\t\t\t\t\t\t\t\"in\": \"query\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"计划起飞时间查询起始时间\",\n" +
                "\t\t\t\t\t\t\t\t\"required\": true,\n" +
                "\t\t\t\t\t\t\t\t\"type\": \"string\",\n" +
                "\t\t\t\t\t\t\t\t\"allowEmptyValue\": false,\n" +
                "\t\t\t\t\t\t\t\t\"example\": \"2018-11-04T00:00:00Z\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\"name\": \"to\",\n" +
                "\t\t\t\t\t\t\t\t\"in\": \"query\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"计划起飞时间查询结束时间\",\n" +
                "\t\t\t\t\t\t\t\t\"required\": true,\n" +
                "\t\t\t\t\t\t\t\t\"type\": \"string\",\n" +
                "\t\t\t\t\t\t\t\t\"allowEmptyValue\": false,\n" +
                "\t\t\t\t\t\t\t\t\"example\": \"2018-11-05T00:00:00Z\"\n" +
                "\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\"result\": [{\n" +
                "\t\t\t\t\t\t\t\"obt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"executedate\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"fltHeight\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"rfpt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"dep\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"fltNum\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"ssrCode\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"tipt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"deicingPad\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"fix\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"isAbs\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"majorCarrier\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"beggate\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"originFlag\": 0,\n" +
                "\t\t\t\t\t\t\t\"planId\": 0,\n" +
                "\t\t\t\t\t\t\t\"id\": 0,\n" +
                "\t\t\t\t\t\t\t\"endgate\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"contrlPoint\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"planEtd\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"planEta\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"taxi\": 0,\n" +
                "\t\t\t\t\t\t\t\"asbt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"priority\": 0,\n" +
                "\t\t\t\t\t\t\t\"aobt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"planDate\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"formerId\": 0,\n" +
                "\t\t\t\t\t\t\t\"deparrflag\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"pustime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"cdt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"route\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"sts\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"ctot\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"planRoute\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"zbApproval\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"agct\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"dof\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"tobt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"odt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"outTime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"turb\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"inHeight\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"cplGroup18\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"lintime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"boardingTime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"fltState\": 0,\n" +
                "\t\t\t\t\t\t\t\"eobt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"aibt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"nonskedPlanRef\": 0,\n" +
                "\t\t\t\t\t\t\t\"acType\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"focusflag\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"arr\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"lndtime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"star\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"group10b\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"ofTime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"group10a\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"hitFlowcontrols\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"atot\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"formerIdReal\": 0,\n" +
                "\t\t\t\t\t\t\t\"fplArr\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"sfl\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"boardingGate\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"integrateDelFlag\": 0,\n" +
                "\t\t\t\t\t\t\t\"ibt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"linTime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"taxTime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"ofPoint\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"dist\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"outPoint\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"realArr\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"ata\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"frequency\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"atd\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"cobt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"fplAcType\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"outHeight\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"eta\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"group16c1\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"skedPlanRef\": 0,\n" +
                "\t\t\t\t\t\t\t\"deicing\": 0,\n" +
                "\t\t\t\t\t\t\t\"group16c2\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"hitControlflags\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"taxRoute\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"rnav5\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"sector\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"weekdays\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"fplDep\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"hobt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"runway\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"rvsm\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"relateDeps\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"isNotifyAf\": 0,\n" +
                "\t\t\t\t\t\t\t\"vipflag\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"atis\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"task\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"cobtType\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"delDeps\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"delayDesc\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"rfl\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"position\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"pbn\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"ssrCallback\": 0,\n" +
                "\t\t\t\t\t\t\t\"userMemo\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"ssrMode\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"unconfirmed\": 0,\n" +
                "\t\t\t\t\t\t\t\"gatePosition\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"afpt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"distance\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"sysMemo\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"asat\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"delaytime\": 0,\n" +
                "\t\t\t\t\t\t\t\"fltStatus\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"dynaEtd\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"efpsAta\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"remark\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"dynaEta\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"speed\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"conjunctionId\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"g18\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"sid\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"inPoint\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"rwy\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"syncflag\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"closeTime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"asrt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"rdytime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"executionRate\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"flowTime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"inTime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"statusChangedTime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"fplRoute\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"fltNormal\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"formerFltArrTime\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"fplEet\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"contrlStatus\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"fltId\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"realDep\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"arrAp\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"arwy\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"cdmDestAp\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"deptView\": \"string\"\n" +
                "\t\t\t\t\t\t}],\n" +
                "\t\t\t\t\t\t\"responses\": [{\n" +
                "\t\t\t\t\t\t\t\t\"code\": \"200\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"请求成功\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\"code\": \"401\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"未认证\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\"code\": \"403\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"权限不足\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\"code\": \"404\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"未找到\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\"code\": \"500\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"请求失败\"\n" +
                "\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\"deprecated\": false,\n" +
                "\t\t\t\t\t\t\"label\": \"航班信息\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"uri\": \"/flightinfo/getIntegrateFlightInfoBySobtAndArr\",\n" +
                "\t\t\t\t\t\t\"method\": \"get\",\n" +
                "\t\t\t\t\t\t\"controllers\": [\n" +
                "\t\t\t\t\t\t\t\"flight-info-controller\"\n" +
                "\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\"operationId\": \"getIntegrateFlightInfoBySobtAndArrUsingGET\",\n" +
                "\t\t\t\t\t\t\"consumes\": [\n" +
                "\t\t\t\t\t\t\t\"*/*\"\n" +
                "\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\"produces\": [\n" +
                "\t\t\t\t\t\t\t\"application/xml\",\n" +
                "\t\t\t\t\t\t\t\"application/json\"\n" +
                "\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\"parameters\": [{\n" +
                "\t\t\t\t\t\t\t\t\"name\": \"arr\",\n" +
                "\t\t\t\t\t\t\t\t\"in\": \"query\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"降落机场四码\",\n" +
                "\t\t\t\t\t\t\t\t\"required\": true,\n" +
                "\t\t\t\t\t\t\t\t\"type\": \"string\",\n" +
                "\t\t\t\t\t\t\t\t\"allowEmptyValue\": false,\n" +
                "\t\t\t\t\t\t\t\t\"example\": \"ZUUU\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\"name\": \"from\",\n" +
                "\t\t\t\t\t\t\t\t\"in\": \"query\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"计划起飞时间查询起始时间\",\n" +
                "\t\t\t\t\t\t\t\t\"required\": true,\n" +
                "\t\t\t\t\t\t\t\t\"type\": \"string\",\n" +
                "\t\t\t\t\t\t\t\t\"allowEmptyValue\": false,\n" +
                "\t\t\t\t\t\t\t\t\"example\": \"2018-11-04T00:00:00Z\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\"name\": \"to\",\n" +
                "\t\t\t\t\t\t\t\t\"in\": \"query\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"计划起飞时间查询结束时间\",\n" +
                "\t\t\t\t\t\t\t\t\"required\": true,\n" +
                "\t\t\t\t\t\t\t\t\"type\": \"string\",\n" +
                "\t\t\t\t\t\t\t\t\"allowEmptyValue\": false,\n" +
                "\t\t\t\t\t\t\t\t\"example\": \"2018-11-05T00:00:00Z\"\n" +
                "\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\"result\": [{\n" +
                "\t\t\t\t\t\t\t\"obt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"executedate\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"fltHeight\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"rfpt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"dep\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"fltNum\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"ssrCode\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"tipt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"deicingPad\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"fix\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"isAbs\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"majorCarrier\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"beggate\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"originFlag\": 0,\n" +
                "\t\t\t\t\t\t\t\"planId\": 0,\n" +
                "\t\t\t\t\t\t\t\"id\": 0,\n" +
                "\t\t\t\t\t\t\t\"endgate\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"contrlPoint\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"planEtd\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"planEta\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"taxi\": 0,\n" +
                "\t\t\t\t\t\t\t\"asbt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"priority\": 0,\n" +
                "\t\t\t\t\t\t\t\"aobt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"planDate\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"formerId\": 0,\n" +
                "\t\t\t\t\t\t\t\"deparrflag\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"pustime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"cdt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"route\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"sts\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"ctot\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"planRoute\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"zbApproval\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"agct\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"dof\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"tobt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"odt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"outTime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"turb\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"inHeight\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"cplGroup18\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"lintime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"boardingTime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"fltState\": 0,\n" +
                "\t\t\t\t\t\t\t\"eobt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"aibt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"nonskedPlanRef\": 0,\n" +
                "\t\t\t\t\t\t\t\"acType\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"focusflag\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"arr\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"lndtime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"star\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"group10b\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"ofTime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"group10a\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"hitFlowcontrols\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"atot\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"formerIdReal\": 0,\n" +
                "\t\t\t\t\t\t\t\"fplArr\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"sfl\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"boardingGate\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"integrateDelFlag\": 0,\n" +
                "\t\t\t\t\t\t\t\"ibt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"linTime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"taxTime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"ofPoint\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"dist\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"outPoint\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"realArr\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"ata\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"frequency\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"atd\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"cobt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"fplAcType\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"outHeight\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"eta\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"group16c1\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"skedPlanRef\": 0,\n" +
                "\t\t\t\t\t\t\t\"deicing\": 0,\n" +
                "\t\t\t\t\t\t\t\"group16c2\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"hitControlflags\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"taxRoute\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"rnav5\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"sector\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"weekdays\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"fplDep\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"hobt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"runway\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"rvsm\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"relateDeps\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"isNotifyAf\": 0,\n" +
                "\t\t\t\t\t\t\t\"vipflag\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"atis\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"task\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"cobtType\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"delDeps\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"delayDesc\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"rfl\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"position\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"pbn\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"ssrCallback\": 0,\n" +
                "\t\t\t\t\t\t\t\"userMemo\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"ssrMode\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"unconfirmed\": 0,\n" +
                "\t\t\t\t\t\t\t\"gatePosition\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"afpt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"distance\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"sysMemo\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"asat\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"delaytime\": 0,\n" +
                "\t\t\t\t\t\t\t\"fltStatus\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"dynaEtd\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"efpsAta\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"remark\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"dynaEta\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"speed\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"conjunctionId\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"g18\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"sid\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"inPoint\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"rwy\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"syncflag\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"closeTime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"asrt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"rdytime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"executionRate\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"flowTime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"inTime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"statusChangedTime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"fplRoute\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"fltNormal\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"formerFltArrTime\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"fplEet\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"contrlStatus\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"fltId\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"realDep\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"arrAp\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"arwy\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"cdmDestAp\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"deptView\": \"string\"\n" +
                "\t\t\t\t\t\t}],\n" +
                "\t\t\t\t\t\t\"responses\": [{\n" +
                "\t\t\t\t\t\t\t\t\"code\": \"200\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"请求成功\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\"code\": \"401\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"未认证\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\"code\": \"403\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"权限不足\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\"code\": \"404\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"未找到\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\"code\": \"500\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"请求失败\"\n" +
                "\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\"deprecated\": false,\n" +
                "\t\t\t\t\t\t\"label\": \"航班信息\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"uri\": \"/flightinfo/getIntegrateFlightInfoBySobtAndDep\",\n" +
                "\t\t\t\t\t\t\"method\": \"get\",\n" +
                "\t\t\t\t\t\t\"controllers\": [\n" +
                "\t\t\t\t\t\t\t\"flight-info-controller\"\n" +
                "\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\"operationId\": \"getIntegrateFlightInfoBySobtAndDepUsingGET\",\n" +
                "\t\t\t\t\t\t\"consumes\": [\n" +
                "\t\t\t\t\t\t\t\"*/*\"\n" +
                "\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\"produces\": [\n" +
                "\t\t\t\t\t\t\t\"application/xml\",\n" +
                "\t\t\t\t\t\t\t\"application/json\"\n" +
                "\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\"parameters\": [{\n" +
                "\t\t\t\t\t\t\t\t\"name\": \"dep\",\n" +
                "\t\t\t\t\t\t\t\t\"in\": \"query\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"起飞机场四码\",\n" +
                "\t\t\t\t\t\t\t\t\"required\": true,\n" +
                "\t\t\t\t\t\t\t\t\"type\": \"string\",\n" +
                "\t\t\t\t\t\t\t\t\"allowEmptyValue\": false,\n" +
                "\t\t\t\t\t\t\t\t\"example\": \"ZUUU\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\"name\": \"from\",\n" +
                "\t\t\t\t\t\t\t\t\"in\": \"query\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"计划起飞时间查询起始时间\",\n" +
                "\t\t\t\t\t\t\t\t\"required\": true,\n" +
                "\t\t\t\t\t\t\t\t\"type\": \"string\",\n" +
                "\t\t\t\t\t\t\t\t\"allowEmptyValue\": false,\n" +
                "\t\t\t\t\t\t\t\t\"example\": \"2018-11-04T00:00:00Z\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\"name\": \"to\",\n" +
                "\t\t\t\t\t\t\t\t\"in\": \"query\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"计划起飞时间查询结束时间\",\n" +
                "\t\t\t\t\t\t\t\t\"required\": true,\n" +
                "\t\t\t\t\t\t\t\t\"type\": \"string\",\n" +
                "\t\t\t\t\t\t\t\t\"allowEmptyValue\": false,\n" +
                "\t\t\t\t\t\t\t\t\"example\": \"2018-11-05T00:00:00Z\"\n" +
                "\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\"result\": [{\n" +
                "\t\t\t\t\t\t\t\"obt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"executedate\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"fltHeight\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"rfpt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"dep\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"fltNum\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"ssrCode\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"tipt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"deicingPad\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"fix\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"isAbs\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"majorCarrier\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"beggate\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"originFlag\": 0,\n" +
                "\t\t\t\t\t\t\t\"planId\": 0,\n" +
                "\t\t\t\t\t\t\t\"id\": 0,\n" +
                "\t\t\t\t\t\t\t\"endgate\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"contrlPoint\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"planEtd\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"planEta\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"taxi\": 0,\n" +
                "\t\t\t\t\t\t\t\"asbt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"priority\": 0,\n" +
                "\t\t\t\t\t\t\t\"aobt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"planDate\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"formerId\": 0,\n" +
                "\t\t\t\t\t\t\t\"deparrflag\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"pustime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"cdt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"route\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"sts\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"ctot\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"planRoute\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"zbApproval\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"agct\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"dof\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"tobt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"odt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"outTime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"turb\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"inHeight\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"cplGroup18\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"lintime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"boardingTime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"fltState\": 0,\n" +
                "\t\t\t\t\t\t\t\"eobt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"aibt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"nonskedPlanRef\": 0,\n" +
                "\t\t\t\t\t\t\t\"acType\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"focusflag\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"arr\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"lndtime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"star\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"group10b\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"ofTime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"group10a\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"hitFlowcontrols\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"atot\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"formerIdReal\": 0,\n" +
                "\t\t\t\t\t\t\t\"fplArr\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"sfl\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"boardingGate\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"integrateDelFlag\": 0,\n" +
                "\t\t\t\t\t\t\t\"ibt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"linTime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"taxTime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"ofPoint\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"dist\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"outPoint\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"realArr\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"ata\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"frequency\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"atd\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"cobt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"fplAcType\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"outHeight\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"eta\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"group16c1\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"skedPlanRef\": 0,\n" +
                "\t\t\t\t\t\t\t\"deicing\": 0,\n" +
                "\t\t\t\t\t\t\t\"group16c2\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"hitControlflags\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"taxRoute\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"rnav5\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"sector\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"weekdays\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"fplDep\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"hobt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"runway\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"rvsm\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"relateDeps\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"isNotifyAf\": 0,\n" +
                "\t\t\t\t\t\t\t\"vipflag\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"atis\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"task\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"cobtType\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"delDeps\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"delayDesc\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"rfl\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"position\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"pbn\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"ssrCallback\": 0,\n" +
                "\t\t\t\t\t\t\t\"userMemo\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"ssrMode\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"unconfirmed\": 0,\n" +
                "\t\t\t\t\t\t\t\"gatePosition\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"afpt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"distance\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"sysMemo\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"asat\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"delaytime\": 0,\n" +
                "\t\t\t\t\t\t\t\"fltStatus\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"dynaEtd\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"efpsAta\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"remark\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"dynaEta\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"speed\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"conjunctionId\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"g18\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"sid\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"inPoint\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"rwy\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"syncflag\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"closeTime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"asrt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"rdytime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"executionRate\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"flowTime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"inTime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"statusChangedTime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"fplRoute\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"fltNormal\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"formerFltArrTime\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"fplEet\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"contrlStatus\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"fltId\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"realDep\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"arrAp\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"arwy\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"cdmDestAp\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"deptView\": \"string\"\n" +
                "\t\t\t\t\t\t}],\n" +
                "\t\t\t\t\t\t\"responses\": [{\n" +
                "\t\t\t\t\t\t\t\t\"code\": \"200\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"请求成功\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\"code\": \"401\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"未认证\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\"code\": \"403\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"权限不足\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\"code\": \"404\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"未找到\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\"code\": \"500\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"请求失败\"\n" +
                "\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\"deprecated\": false,\n" +
                "\t\t\t\t\t\t\"label\": \"航班信息\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"uri\": \"/flightinfo/getIntegrateFlightInfoBySobtAndDepAndArrAndAcid\",\n" +
                "\t\t\t\t\t\t\"method\": \"get\",\n" +
                "\t\t\t\t\t\t\"controllers\": [\n" +
                "\t\t\t\t\t\t\t\"flight-info-controller\"\n" +
                "\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\"operationId\": \"getIntegrateFlightInfoBySobtAndDepAndArrAndAcidUsingGET\",\n" +
                "\t\t\t\t\t\t\"consumes\": [\n" +
                "\t\t\t\t\t\t\t\"*/*\"\n" +
                "\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\"produces\": [\n" +
                "\t\t\t\t\t\t\t\"application/xml\",\n" +
                "\t\t\t\t\t\t\t\"application/json\"\n" +
                "\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\"parameters\": [{\n" +
                "\t\t\t\t\t\t\t\t\"name\": \"acid\",\n" +
                "\t\t\t\t\t\t\t\t\"in\": \"query\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"航班号\",\n" +
                "\t\t\t\t\t\t\t\t\"required\": true,\n" +
                "\t\t\t\t\t\t\t\t\"type\": \"string\",\n" +
                "\t\t\t\t\t\t\t\t\"allowEmptyValue\": false,\n" +
                "\t\t\t\t\t\t\t\t\"example\": \"CSC8923\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\"name\": \"arr\",\n" +
                "\t\t\t\t\t\t\t\t\"in\": \"query\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"降落机场四码\",\n" +
                "\t\t\t\t\t\t\t\t\"required\": true,\n" +
                "\t\t\t\t\t\t\t\t\"type\": \"string\",\n" +
                "\t\t\t\t\t\t\t\t\"allowEmptyValue\": false,\n" +
                "\t\t\t\t\t\t\t\t\"example\": \"ZSNJ\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\"name\": \"dep\",\n" +
                "\t\t\t\t\t\t\t\t\"in\": \"query\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"起飞机场四码\",\n" +
                "\t\t\t\t\t\t\t\t\"required\": true,\n" +
                "\t\t\t\t\t\t\t\t\"type\": \"string\",\n" +
                "\t\t\t\t\t\t\t\t\"allowEmptyValue\": false,\n" +
                "\t\t\t\t\t\t\t\t\"example\": \"ZUUU\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\"name\": \"from\",\n" +
                "\t\t\t\t\t\t\t\t\"in\": \"query\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"计划起飞时间查询起始时间\",\n" +
                "\t\t\t\t\t\t\t\t\"required\": true,\n" +
                "\t\t\t\t\t\t\t\t\"type\": \"string\",\n" +
                "\t\t\t\t\t\t\t\t\"allowEmptyValue\": false,\n" +
                "\t\t\t\t\t\t\t\t\"example\": \"2018-11-04T00:00:00Z\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\"name\": \"to\",\n" +
                "\t\t\t\t\t\t\t\t\"in\": \"query\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"计划起飞时间查询结束时间\",\n" +
                "\t\t\t\t\t\t\t\t\"required\": true,\n" +
                "\t\t\t\t\t\t\t\t\"type\": \"string\",\n" +
                "\t\t\t\t\t\t\t\t\"allowEmptyValue\": false,\n" +
                "\t\t\t\t\t\t\t\t\"example\": \"2018-11-05T00:00:00Z\"\n" +
                "\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\"result\": [{\n" +
                "\t\t\t\t\t\t\t\"obt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"executedate\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"fltHeight\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"rfpt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"dep\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"fltNum\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"ssrCode\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"tipt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"deicingPad\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"fix\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"isAbs\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"majorCarrier\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"beggate\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"originFlag\": 0,\n" +
                "\t\t\t\t\t\t\t\"planId\": 0,\n" +
                "\t\t\t\t\t\t\t\"id\": 0,\n" +
                "\t\t\t\t\t\t\t\"endgate\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"contrlPoint\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"planEtd\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"planEta\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"taxi\": 0,\n" +
                "\t\t\t\t\t\t\t\"asbt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"priority\": 0,\n" +
                "\t\t\t\t\t\t\t\"aobt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"planDate\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"formerId\": 0,\n" +
                "\t\t\t\t\t\t\t\"deparrflag\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"pustime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"cdt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"route\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"sts\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"ctot\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"planRoute\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"zbApproval\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"agct\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"dof\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"tobt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"odt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"outTime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"turb\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"inHeight\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"cplGroup18\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"lintime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"boardingTime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"fltState\": 0,\n" +
                "\t\t\t\t\t\t\t\"eobt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"aibt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"nonskedPlanRef\": 0,\n" +
                "\t\t\t\t\t\t\t\"acType\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"focusflag\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"arr\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"lndtime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"star\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"group10b\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"ofTime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"group10a\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"hitFlowcontrols\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"atot\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"formerIdReal\": 0,\n" +
                "\t\t\t\t\t\t\t\"fplArr\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"sfl\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"boardingGate\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"integrateDelFlag\": 0,\n" +
                "\t\t\t\t\t\t\t\"ibt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"linTime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"taxTime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"ofPoint\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"dist\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"outPoint\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"realArr\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"ata\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"frequency\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"atd\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"cobt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"fplAcType\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"outHeight\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"eta\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"group16c1\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"skedPlanRef\": 0,\n" +
                "\t\t\t\t\t\t\t\"deicing\": 0,\n" +
                "\t\t\t\t\t\t\t\"group16c2\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"hitControlflags\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"taxRoute\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"rnav5\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"sector\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"weekdays\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"fplDep\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"hobt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"runway\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"rvsm\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"relateDeps\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"isNotifyAf\": 0,\n" +
                "\t\t\t\t\t\t\t\"vipflag\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"atis\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"task\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"cobtType\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"delDeps\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"delayDesc\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"rfl\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"position\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"pbn\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"ssrCallback\": 0,\n" +
                "\t\t\t\t\t\t\t\"userMemo\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"ssrMode\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"unconfirmed\": 0,\n" +
                "\t\t\t\t\t\t\t\"gatePosition\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"afpt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"distance\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"sysMemo\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"asat\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"delaytime\": 0,\n" +
                "\t\t\t\t\t\t\t\"fltStatus\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"dynaEtd\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"efpsAta\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"remark\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"dynaEta\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"speed\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"conjunctionId\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"g18\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"sid\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"inPoint\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"rwy\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"syncflag\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"closeTime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"asrt\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"rdytime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"executionRate\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"flowTime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"inTime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"statusChangedTime\": \"2020-03-23T06:18:25.068+0000\",\n" +
                "\t\t\t\t\t\t\t\"fplRoute\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"fltNormal\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"formerFltArrTime\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"fplEet\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"contrlStatus\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"fltId\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"realDep\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"arrAp\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"arwy\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"cdmDestAp\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"deptView\": \"string\"\n" +
                "\t\t\t\t\t\t}],\n" +
                "\t\t\t\t\t\t\"responses\": [{\n" +
                "\t\t\t\t\t\t\t\t\"code\": \"200\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"请求成功\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\"code\": \"401\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"未认证\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\"code\": \"403\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"权限不足\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\"code\": \"404\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"未找到\"\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\"code\": \"500\",\n" +
                "\t\t\t\t\t\t\t\t\"description\": \"请求失败\"\n" +
                "\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t],\n" +
                "\t\t\t\t\t\t\"deprecated\": false,\n" +
                "\t\t\t\t\t\t\"label\": \"航班信息\"\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t]\n" +
                "\t\t\t}]\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"host\": \"192.168.204.71:8720\",\n" +
                "\t\t\t\"basePath\": \"/gateway/fdexm-service\",\n" +
                "\t\t\t\"label\": \"FDEXM飞行数据交换信息服务 API\",\n" +
                "\t\t\t\"children\": [{\n" +
                "\t\t\t\t\"name\": \"fdexm-controller\",\n" +
                "\t\t\t\t\"label\": \"FDEXM飞行数据交换信息服务\",\n" +
                "\t\t\t\t\"children\": [{\n" +
                "\t\t\t\t\t\"uri\": \"/fdexm/get\",\n" +
                "\t\t\t\t\t\"method\": \"get\",\n" +
                "\t\t\t\t\t\"controllers\": [\n" +
                "\t\t\t\t\t\t\"fdexm-controller\"\n" +
                "\t\t\t\t\t],\n" +
                "\t\t\t\t\t\"operationId\": \"getMessagesUsingGET\",\n" +
                "\t\t\t\t\t\"consumes\": [\n" +
                "\t\t\t\t\t\t\"*/*\"\n" +
                "\t\t\t\t\t],\n" +
                "\t\t\t\t\t\"produces\": [\n" +
                "\t\t\t\t\t\t\"application/xml\",\n" +
                "\t\t\t\t\t\t\"application/json\"\n" +
                "\t\t\t\t\t],\n" +
                "\t\t\t\t\t\"parameters\": [{\n" +
                "\t\t\t\t\t\t\t\"name\": \"airport\",\n" +
                "\t\t\t\t\t\t\t\"in\": \"query\",\n" +
                "\t\t\t\t\t\t\t\"description\": \"机场四码\",\n" +
                "\t\t\t\t\t\t\t\"required\": true,\n" +
                "\t\t\t\t\t\t\t\"type\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"allowEmptyValue\": false,\n" +
                "\t\t\t\t\t\t\t\"example\": \"ZUGY\"\n" +
                "\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\"name\": \"from\",\n" +
                "\t\t\t\t\t\t\t\"in\": \"query\",\n" +
                "\t\t\t\t\t\t\t\"description\": \"查询起始时间\",\n" +
                "\t\t\t\t\t\t\t\"required\": true,\n" +
                "\t\t\t\t\t\t\t\"type\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"allowEmptyValue\": false,\n" +
                "\t\t\t\t\t\t\t\"example\": \"2018-10-02\"\n" +
                "\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\"name\": \"key\",\n" +
                "\t\t\t\t\t\t\t\"in\": \"query\",\n" +
                "\t\t\t\t\t\t\t\"description\": \"报文关键字\",\n" +
                "\t\t\t\t\t\t\t\"required\": true,\n" +
                "\t\t\t\t\t\t\t\"type\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"allowEmptyValue\": false,\n" +
                "\t\t\t\t\t\t\t\"example\": \"HOST ACSDD4\"\n" +
                "\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\"name\": \"to\",\n" +
                "\t\t\t\t\t\t\t\"in\": \"query\",\n" +
                "\t\t\t\t\t\t\t\"description\": \"查询结束时间\",\n" +
                "\t\t\t\t\t\t\t\"required\": true,\n" +
                "\t\t\t\t\t\t\t\"type\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"allowEmptyValue\": false,\n" +
                "\t\t\t\t\t\t\t\"example\": \"2018-10-03\"\n" +
                "\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\"name\": \"type\",\n" +
                "\t\t\t\t\t\t\t\"in\": \"query\",\n" +
                "\t\t\t\t\t\t\t\"description\": \"报文类型\",\n" +
                "\t\t\t\t\t\t\t\"required\": true,\n" +
                "\t\t\t\t\t\t\t\"type\": \"string\",\n" +
                "\t\t\t\t\t\t\t\"allowEmptyValue\": false,\n" +
                "\t\t\t\t\t\t\t\"example\": \"BCWP\"\n" +
                "\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t],\n" +
                "\t\t\t\t\t\"result\": [{\n" +
                "\t\t\t\t\t\t\"msg\": \"string\",\n" +
                "\t\t\t\t\t\t\"id\": 0,\n" +
                "\t\t\t\t\t\t\"rcvTime\": \"2020-03-23T06:18:25.100+0000\"\n" +
                "\t\t\t\t\t}],\n" +
                "\t\t\t\t\t\"responses\": [{\n" +
                "\t\t\t\t\t\t\t\"code\": \"200\",\n" +
                "\t\t\t\t\t\t\t\"description\": \"请求成功\"\n" +
                "\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\"code\": \"401\",\n" +
                "\t\t\t\t\t\t\t\"description\": \"未认证\"\n" +
                "\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\"code\": \"403\",\n" +
                "\t\t\t\t\t\t\t\"description\": \"权限不足\"\n" +
                "\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\"code\": \"404\",\n" +
                "\t\t\t\t\t\t\t\"description\": \"未找到\"\n" +
                "\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\"code\": \"500\",\n" +
                "\t\t\t\t\t\t\t\"description\": \"请求失败\"\n" +
                "\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t],\n" +
                "\t\t\t\t\t\"deprecated\": false,\n" +
                "\t\t\t\t\t\"label\": \"报文类型和关键字查询\"\n" +
                "\t\t\t\t}]\n" +
                "\t\t\t}]\n" +
                "\t\t}\n" +
                "\t]\n" +
                "}";
    }
}
