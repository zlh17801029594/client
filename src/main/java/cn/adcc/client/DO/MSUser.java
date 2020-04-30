package cn.adcc.client.DO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//@Data
@Getter
@Setter
@ToString(exclude = {"msUserApis", "msApplies"})
@JsonIgnoreProperties(value = {"msUserApis", "msApplies"})
@Entity
@Table(name = "ms_user")
@DynamicInsert
@DynamicUpdate
public class MSUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private Integer sensitiveNum;
    @OneToMany(mappedBy = "msUser", cascade = {CascadeType.REMOVE})
    //@JoinColumn(name = "ms_user_id")
    private Set<MSUserApi> msUserApis;
    @OneToMany(mappedBy = "msUser", cascade = {CascadeType.REMOVE})
    private Set<MSApply> msApplies;
}
