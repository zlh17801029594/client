package cn.adcc.client.VO;

import lombok.Data;

@Data
public class PageQuery {
    private Integer page;
    private Integer limit;
    private Integer status;
}
