package cn.adcc.client.DO;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(catalog = "integrate")
public class FixmLogic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String xsdnode;

    private String xmlkey;

    private String srcColumn;

    @Column(name = "`explain`")
    private String explain;

    private String testvalue;

    private Boolean isvalid;

    private Boolean isnode;

    private Boolean isproperty;

    private String fileextension;

    private String convextension;

    private String srcDb;

    private String srcTable;

    private String valueextension;

    private String extensionkey;

    private Boolean islist;

    private Boolean issequence;

    private Boolean containref;

    private String splitsign;

    private String valuesplit;

    private String res1;

    private String res2;

    private String res3;

    private String res4;

    private String version;

    private String childversion;
}
