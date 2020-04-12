package cn.adcc.client.DTO;

import cn.adcc.client.DTOImport.ApiParameterDetails;
import cn.adcc.client.DTOImport.Schema;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class OtherInfoView {
    private List<String> consumes;
    private List<String> produces;
    private List<MSApiParaDetails> parameters;
    private Object result;
    private List<ResponseDesc> responses;
}
