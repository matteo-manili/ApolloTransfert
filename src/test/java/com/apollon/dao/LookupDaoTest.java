package com.apollon.dao;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * This class tests the current LookupDao implementation class
 * @author mraible
 */
@Ignore
public class LookupDaoTest extends BaseDaoTestCase {
    @Autowired
    LookupDao lookupDao;

    @SuppressWarnings("rawtypes")
	@Test
    public void testGetRoles() {
        List roles = lookupDao.getRoles();
        log.debug(roles);
        assertTrue(roles.size() > 0);
    }
}
