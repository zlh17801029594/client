package cn.adcc.client.DO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

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
    /*申请用户名*/
    private String username;
    private Date applyTime;
    private Date expireTime;
    private Integer status;
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "msApply")
    private Set<MSApplyDetails> msApplyDetails;
}
