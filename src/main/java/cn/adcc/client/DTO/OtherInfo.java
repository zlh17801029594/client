package cn.adcc.client.DTO;

import cn.adcc.client.DTOImport.ApiParameterDetails;
import cn.adcc.client.DTOImport.Schema;
import lombok.Data;

import java.util.List;

@Data
public class OtherInfo {
    private List<String> consumes;
    private List<String> produces;
    private List<ApiParameterDetails> parameters;
    private Schema result;
    private List<ResponseDesc> responses;
}
