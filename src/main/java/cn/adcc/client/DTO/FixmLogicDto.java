package cn.adcc.client.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class FixmLogicDto {
    private Long id;

    @JsonProperty("label")
    private String label;
//    private String xsdnode;

    private String xmlkey;

    private String srcColumn;

    private String explain;

    private String testvalue;

    private Boolean isvalid;

    private Boolean isnode;

    private Boolean isproperty;

    private String fileextension;

    private String convextension;

    private String srcDb;

    private String srcTable;

    private String valueextension;

    private String extensionkey;

    private Boolean islist;

    private Boolean issequence;

    private Boolean containref;

    private String splitsign;

    private String valuesplit;

    private String res1;

    private String res2;

    private String res3;

    private String res4;

    private String version;

    private List<FixmLogicDto> children;
}
