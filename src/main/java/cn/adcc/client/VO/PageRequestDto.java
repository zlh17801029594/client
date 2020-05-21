package cn.adcc.client.VO;

import lombok.Data;

import java.util.List;

@Data
public class PageRequestDto<T> {
    private Integer page = 1;
    private Integer limit = 20;
    private T data;
    private List<T> list;
    private Long total;
}
