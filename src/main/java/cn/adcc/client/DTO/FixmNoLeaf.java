package cn.adcc.client.DTO;

import lombok.Data;

import java.util.List;

@Data
public class FixmNoLeaf {
    private Integer level;
    private String label;
    private List<Long> ids;
}
