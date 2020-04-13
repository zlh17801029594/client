package cn.adcc.client.DO;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
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
    @OneToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "ms_apply_id")
    private List<MSApplyDetails> msApplyDetails = new ArrayList<>();

}
