package cn.adcc.client.DO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

//@Data
@Getter
@Setter
@Entity
@Table(name = "ms_apply")
@DynamicInsert
@DynamicUpdate
public class MSApply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "ms_user_id")
    private MSUser msUser;
    private Date applyTime;
    private Date expireTime;
    private Integer status;
    private String reason;
    /*表关联条件：有效*/
    //@Where(clause = "del_flag != 1")
    @ManyToMany//(mappedBy = "msApplies")
    @JoinTable(name = "ms_apply_api",
            joinColumns = @JoinColumn(name = "ms_apply_id"),
            inverseJoinColumns = @JoinColumn(name = "ms_api_id"))
    private Set<MSApi> msApis;
}
