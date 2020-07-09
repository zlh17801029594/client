package cn.adcc.client.DO;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(catalog = "integrate")
public class FixmOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String xsdnode;

    private Boolean isvalid;

    private String propertyOrder;

    private String nodeOrder;

    private String splitsign;

    private String res1;

    private String res2;

    private String version;
}
