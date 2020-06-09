package cn.adcc.client.DTO;

import lombok.Data;

import java.util.List;

@Data
public class FixmNode {
    private Long id;
    private String label;
    private List<FixmNode> children;
}
