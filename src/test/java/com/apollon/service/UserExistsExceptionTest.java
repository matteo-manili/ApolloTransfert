package com.apollon.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.apollon.model.User;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations = {"/applicationContext-service.xml",
                     "/applicationContext-resources.xml",
                     "classpath:/applicationContext-dao.xml"})

@Ignore
public class UserExistsExceptionTest {
    @Autowired
    private UserManager manager;
    private Log log = LogFactory.getLog(UserExistsExceptionTest.class);


    public void testAddExistingUser() throws Exception {
        log.debug("entered 'testAddExistingUser' method");
        assertNotNull(manager);

        User user = manager.getUser("-1");
        user.setPhoneNumber("+393289126444");
        
        // create new object with null id - Hibernate doesn't like setId(null)
        User user2 = new User();
        BeanUtils.copyProperties(user, user2);
        user2.setId(null);
        user2.setVersion(null);
        user2.setRoles(null);

        // try saving as new user, this should fail UserExistsException b/c of unique keys
        manager.saveUser(user2);
    }
}
