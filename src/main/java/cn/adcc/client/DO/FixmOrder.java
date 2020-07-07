package cn.adcc.client.DO;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
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
