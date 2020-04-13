package cn.adcc.client.repository;

import cn.adcc.client.ClientApplicationTests;
import cn.adcc.client.DO.MSUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.*;

@Component
class MSUserRepositoryTest extends ClientApplicationTests {
    @Autowired
    private MSUserRepository msUserRepository;

    @Test
    public void testUser() {
        MSUser msUser = msUserRepository.getOne(1L);
        Assert.notNull(msUser, "yes");
    }

}