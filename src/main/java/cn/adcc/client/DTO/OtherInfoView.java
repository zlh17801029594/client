package cn.adcc.client.DTO;

import lombok.Data;

import java.util.List;

@Data

public class OtherInfoView {
    private List<String> consumes;
    private List<String> produces;
    private List<MSApiParaDetails> parameters;
    private Object result;
    private List<ResponseDesc> responses;
}
