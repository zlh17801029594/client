package cn.adcc.client.repository;

import cn.adcc.client.ClientApplicationTests;
import cn.adcc.client.DO.MSApply;
import cn.adcc.client.DO.MSUser;
import cn.adcc.client.DO.MSUserApi;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
class MSUserRepositoryTest extends ClientApplicationTests {
    @Autowired
    private MSUserRepository msUserRepository;
    @Autowired
    private MSApplyRepository msApplyRepository;
    @Autowired
    private MSApiRepository msApiRepository;
    @Autowired
    private MSUserApiRepository msUserApiRepository;


    @Test
    /*CascadeType.Persist*/
    public void testInsert() {
        MSUser msUser = new MSUser();
        msUser.setUsername("admin");
        /*id已存在，查询出来，仅仅修改msUser，保存会报错 detached pass persist*/
        /*MSUser msUser1 = msUserRepository.getOne(1L);
        Set<MSUserApi> msUserApis = msUser1.getMsUserApis();
        msUserApis.forEach(msUserApi -> {
            msUserApi.setMsUser(msUser);
        });*/
        Set<MSUserApi> msUserApis = new HashSet<>();
        MSUserApi msUserApi = new MSUserApi();
        msUserApi.setMsUser(msUser);
        msUserApi.setMsApi(msApiRepository.getOne(320L));
        msUserApi.setApplyTime(new Date());
        msUserApi.setExpireTime(new Date());
        msUserApi.setStatus(0);
        msUserApis.add(msUserApi);
        msUser.setMsUserApis(msUserApis);
        msUserRepository.save(msUser);
    }

    @Test
    /*CascadeType.Persist*/
    /**
     * 级联插入的意思是
     * 1.无子表关联(该项为null，为空集合)，不影响子表
     * 2.有子表关联，子表元素已存在(数据库中查出来的)，子表做更新操作
     * 3.有子表关联，子表元素不存在(通过人为添加的)，子表做插入操作
     */
    public void testInsertUpdate() {
        MSUser msUser = msUserRepository.getOne(34L);
        Set<MSUserApi> msUserApis = new HashSet<>();
        /*一个新的未插入的就算插入，插入过的算更新*/
        /*MSUserApi msUserApi = new MSUserApi();
        msUserApi.setMsUser(msUser);
        msUserApi.setMsApi(msApiRepository.getOne(323L));
        msUserApi.setApplyTime(new Date());
        msUserApi.setExpireTime(new Date());
        msUserApi.setStatus(0);*/
        MSUserApi msUserApi = msUserApiRepository.getOne(15L);
        msUserApis.add(msUserApi);
//        msUser.setMsUserApis(null);
//        msUser.setMsUserApis(new HashSet<>());
        msUser.setMsUserApis(msUserApis);
        msUserRepository.save(msUser);
    }

    @Test
    /*CascadeType.Merge*/
    public void testUpdate() {
        MSUser msUser = msUserRepository.getOne(1L);
        System.out.println(msUser);
        Set<MSUserApi> msUserApis = msUser.getMsUserApis();
        msUserApis.stream()
                .forEach(msUserApi -> {
                    msUserApi.setStatus(-1);
                });
        System.out.println(msUser);
        msUserRepository.save(msUser);
        Assert.notNull(msUser, "yes");
    }

    @Test
    /*CascadeType.Remove*/
    public void testDelete() {
        //msUserRepository.deleteById(32L);
        msApiRepository.deleteById(320L);
    }

    @Test
    public void testMsApply() {
        MSApply msApply = new MSApply();
        msApply.setUsername("xiaozhou");
        msApply.setApplyTime(new Date());
        msApply.setExpireTime(new Date());
        msApply.setStatus(0);
        /*Set<MSApplyDetails> msApplyDetailsSet = new HashSet<>();
        MSApplyDetails msApplyDetails = new MSApplyDetails();
        msApplyDetails.setMsApiUrl("/b");
        msApplyDetailsSet.add(msApplyDetails);
        msApplyDetails.setMsApply(msApply);
        msApply.setMsApplyDetails(msApplyDetailsSet);
        msApplyRepository.save(msApply);*/
    }

    @Test
    public void testMsApplyDetails() {
        /*MSApplyDetails msApplyDetails = new MSApplyDetails();
        msApplyDetails.setMsApiUrl("/c");
        msApplyDetails.setMsApply(msApplyRepository.getOne(1L));
        msApplyDetailsRepository.save(msApplyDetails);*/
    }

    @Test
    void findAllBy() {
        List<MSUser> msUsers = msUserRepository.findMSUsersBy();
        System.out.println(msUsers);
    }

    @Test
    void findMSUserByUsername() {
        MSUser msUser = msUserRepository.findMSUserByUsername("zhoulihuang");
        System.out.println(msUser);
    }
}