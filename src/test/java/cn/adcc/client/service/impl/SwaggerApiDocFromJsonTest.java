package cn.adcc.client.service.impl;

import cn.adcc.client.ClientApplicationTests;
import cn.adcc.client.DTOSwagger.SwaggerApiDoc;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.junit.jupiter.api.Assertions.*;

@Component
class SwaggerApiDocFromJsonTest extends ClientApplicationTests {

    @Autowired
    private ObjectMapper objectMapper;
    
    private String json = "{\"swagger\":\"2.0\",\"info\":{\"description\":\"试验项目\",\"version\":\"version 1.0\",\"title\":\"Statistical统计数据信息服务 API\",\"contact\":{\"name\":\"洪毅\",\"url\":\"https://www.adcc.com.cn\",\"email\":\"hongy@adcc.com.cn\"}},\"host\":\"192.168.243.37:8707\",\"basePath\":\"/\",\"tags\":[{\"name\":\"normal-rate-controller\",\"description\":\"统计类-Statistical统计数据信息服务正常率排名\"}],\"paths\":{\"/statistical/getnormalrate/airline\":{\"get\":{\"tags\":[\"normal-rate-controller\"],\"summary\":\"获取航空公司正常率排名\",\"description\":\"获取航空公司正常率排名\",\"operationId\":\"getAirlineUsingGET\",\"consumes\":[\"*/*\"],\"produces\":[\"application/json\",\"text/plain\"],\"parameters\":[{\"name\":\"query_date\",\"in\":\"query\",\"description\":\"查询时间区间某月:2020,某月到某月:202001-202002(年和日以此类推)\",\"required\":true,\"type\":\"string\",\"allowEmptyValue\":false,\"x-example\":\"202001\"},{\"name\":\"query_dep\",\"in\":\"query\",\"description\":\"起飞机场四码\",\"required\":false,\"type\":\"string\",\"allowEmptyValue\":false,\"x-example\":\"ZUUU\"},{\"name\":\"query_threshold\",\"in\":\"query\",\"description\":\"正常率阈值\",\"required\":false,\"type\":\"string\",\"allowEmptyValue\":false,\"x-example\":\"15\"}],\"responses\":{\"200\":{\"description\":\"请求成功\",\"schema\":{\"type\":\"array\",\"items\":{\"$ref\":\"#/definitions/正常率返回值\"}}},\"401\":{\"description\":\"未认证\"},\"403\":{\"description\":\"权限不足\"},\"404\":{\"description\":\"未找到\"},\"500\":{\"description\":\"请求失败\"}},\"deprecated\":false}},\"/statistical/getnormalrate/airport\":{\"get\":{\"tags\":[\"normal-rate-controller\"],\"summary\":\"获取机场正常率排名\",\"description\":\"获取机场正常率排名\",\"operationId\":\"getAirportUsingGET\",\"consumes\":[\"*/*\"],\"produces\":[\"application/json\",\"text/plain\"],\"parameters\":[{\"name\":\"query_date\",\"in\":\"query\",\"description\":\"查询时间区间某月:2020,某月到某月:202001-202002(年和日以此类推)\",\"required\":true,\"type\":\"string\",\"allowEmptyValue\":false,\"x-example\":\"202001\"},{\"name\":\"query_dep\",\"in\":\"query\",\"description\":\"起飞机场四码\",\"required\":false,\"type\":\"string\",\"allowEmptyValue\":false,\"x-example\":\"ZUUU\"},{\"name\":\"query_threshold\",\"in\":\"query\",\"description\":\"正常率阈值\",\"required\":false,\"type\":\"string\",\"allowEmptyValue\":false,\"x-example\":\"15\"}],\"responses\":{\"200\":{\"description\":\"请求成功\",\"schema\":{\"type\":\"array\",\"items\":{\"$ref\":\"#/definitions/正常率返回值\"}}},\"401\":{\"description\":\"未认证\"},\"403\":{\"description\":\"权限不足\"},\"404\":{\"description\":\"未找到\"},\"500\":{\"description\":\"请求失败\"}},\"deprecated\":false}},\"/statistical/getnormalrate/flightid\":{\"get\":{\"tags\":[\"normal-rate-controller\"],\"summary\":\"获取航班正常率排名\",\"description\":\"获取航班正常率排名\",\"operationId\":\"getFlightIdUsingGET\",\"consumes\":[\"*/*\"],\"produces\":[\"application/json\",\"text/plain\"],\"parameters\":[{\"name\":\"query_date\",\"in\":\"query\",\"description\":\"查询时间区间某月:2020,某月到某月:202001-202002(年和日以此类推)\",\"required\":true,\"type\":\"string\",\"allowEmptyValue\":false,\"x-example\":\"202001\"},{\"name\":\"query_dep\",\"in\":\"query\",\"description\":\"起飞机场四码\",\"required\":false,\"type\":\"string\",\"allowEmptyValue\":false,\"x-example\":\"ZUUU\"},{\"name\":\"query_threshold\",\"in\":\"query\",\"description\":\"正常率阈值\",\"required\":false,\"type\":\"string\",\"allowEmptyValue\":false,\"x-example\":\"15\"}],\"responses\":{\"200\":{\"description\":\"请求成功\",\"schema\":{\"type\":\"array\",\"items\":{\"$ref\":\"#/definitions/正常率返回值\"}}},\"401\":{\"description\":\"未认证\"},\"403\":{\"description\":\"权限不足\"},\"404\":{\"description\":\"未找到\"},\"500\":{\"description\":\"请求失败\"}},\"deprecated\":false}}},\"definitions\":{\"正常率返回值\":{\"type\":\"object\",\"properties\":{\"date\":{\"type\":\"string\",\"example\":2020,\"description\":\"某年或某月或某日\"},\"result\":{\"type\":\"array\",\"example\":[{\"A\":\"ARR\",\"RATE\":22.2}],\"description\":\"结果集\",\"items\":{\"type\":\"object\"}}},\"title\":\"正常率返回值\",\"description\":\"统计数据信息服务正常率排名\"}}}";

    @Test
    void testSwagger() throws JsonProcessingException {
        SwaggerApiDoc swaggerApiDoc = objectMapper.readValue(json, SwaggerApiDoc.class);
        System.out.println(swaggerApiDoc);
    }
    
}