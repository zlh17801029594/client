package cn.adcc.client.VO;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class SubversionCheckVO {
    private List<String> chkXsdnodes = Collections.emptyList();

    private List<String> cancelChkXsdnodes = Collections.emptyList();
}
