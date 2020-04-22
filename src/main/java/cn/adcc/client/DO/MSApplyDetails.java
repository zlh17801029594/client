package cn.adcc.client.DO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

//@Data
@Getter
@Setter
@Entity
@Table(name = "ms_apply_details")
@DynamicInsert
@DynamicUpdate
public class MSApplyDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    /*(fetch = FetchType.LAZY) 避免重复加载父表*/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ms_apply_id")
    private MSApply msApply;
    private String msApiName;
    private String msApiDescription;
    private String msApiUrl;
    private String msApiHttpMethod;
    private String msApiOtherInfo;

}
