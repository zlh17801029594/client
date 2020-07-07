package cn.adcc.client.DTO;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data

public class OtherInfoView {
    private List<String> consumes;
    private List<String> produces;
    private List<ApiParaDetails> parameters;
    // 原json
    private Object result1;
    // example样例json
    private Object result;
    // description描述map
    private Map<String, String> resultDesc;
    private List<ResponseDesc> responses;
}
