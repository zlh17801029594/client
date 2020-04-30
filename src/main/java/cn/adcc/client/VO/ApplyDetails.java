package cn.adcc.client.VO;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ApplyDetails {
    private List<Long> ids;
    private Date expireTime;
}
