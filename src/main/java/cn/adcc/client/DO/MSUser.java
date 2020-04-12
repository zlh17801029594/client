package cn.adcc.client.DO;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "ms_user")
@DynamicInsert
@DynamicUpdate
public class MSUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "ms_user_aply",
            joinColumns = @JoinColumn(name = "ms_user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "ms_apply_id", referencedColumnName = "id"))
    private List<MSApply> msApplies = new ArrayList<>();
}
