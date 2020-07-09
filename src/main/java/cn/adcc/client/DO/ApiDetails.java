package cn.adcc.client.DO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@ToString(exclude = {"api"})
@JsonIgnoreProperties(value = {"api"})
@Entity
@Table(catalog = "privilege", name = "api_details")
@DynamicInsert
@DynamicUpdate
public class ApiDetails {
    @Id
    /*根据api主键生成主键*/
    @GeneratedValue(generator = "pkGenerator")
    @GenericGenerator(name = "pkGenerator",
            strategy = "foreign",
            parameters = @org.hibernate.annotations.Parameter(name = "property", value = "api"))
    private Long id;
    /**
     * 关联接口表
     */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id")
//    也可以直接使用@PrimaryKeyJoinColumn
//    @PrimaryKeyJoinColumn
    private Api api;
    /*接口其他信息(仅仅用于集体展示的信息进行统一存储)*/
    private String otherInfo;
    /*接口是否已被弃用*/
    private Boolean deprecated;
}
