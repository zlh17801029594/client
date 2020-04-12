package cn.adcc.client.DO;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "ms_apply_details")
public class MSApplyDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /*申请表id*/
    private Long msApplyId;
    private String msApiName;
    private String msApiDescription;
    private String msApiUrl;
    private String msApiHttpMethod;
    private String msApiOtherInfo;

}
