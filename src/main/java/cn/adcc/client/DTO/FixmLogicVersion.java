package cn.adcc.client.DTO;

import lombok.Data;

import java.util.Set;

@Data
public class FixmLogicVersion {
    private String name;

    private Set<String> subversions;
}
