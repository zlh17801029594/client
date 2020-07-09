package cn.adcc.client.DO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@ToString(exclude = {"userApis", "applies"})
@JsonIgnoreProperties(value = {"userApis", "applies"})
@Entity
@Table(catalog = "privilege", name = "user")
@SQLDelete(sql = "update user set del_flag = 1 where id = ?")
@Where(clause = "del_flag != 1")
@DynamicInsert
@DynamicUpdate
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /*用户名*/
    private String username;
    /*用户敏感级别*/
    private Integer sensitiveNum;
    /*伪删除字段*/
    private Boolean delFlag;
    /**
     * 关联用户接口表
     * 1.CascadeType: 使用级联删除(remove), 用户移除时，用户接口关系没有保留价值
     * CascadeType 总结：
     * 1.用户 vs 用户接口关系。 保存用户无需同时保存用户接口关系，因为用户接口关系随时变动，因此可以不级联保存(persist)，使用级联删除就行(remove)。先保存用户、再保存用户接口关系即可
     * 2.申请 vs 申请详情。保存申请时需要同时保存申请详情，后期不会单独更新申请详情。因此使用级联保存(persisit)。
     * 3.接口 vs 接口详情。保存接口时需要同时保存接口详情，并且同步微服务接口时也需要同时更新接口详情。因此使用级联保存、级联更新(persist、merge)
     */
    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE})
    private Set<UserApi> userApis;
    /**
     * 关联申请表
     * 1.使用@JoinColumn 主控外键，当删除user表数据时，apply表移除user表外键(置user_id为null)
     */
    @OneToMany
    @JoinColumn(name = "userId")
    private Set<Apply> applies;
}
