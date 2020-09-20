package cn.adcc.client.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Data
public class FixmLogicDto {
    private String name;

    private String srcColumn;

    private String explain;

    // private Object testvalue;

    private Boolean isvalid;

    private Boolean isnode;

    // 扩展文件名
    private String fileextension;

    // 转换方法
    private String convextension;

    private Set<String> subversions;

    // 查询时使用，新增等无需使用
    private List<FixmLogicDto> children;
}
