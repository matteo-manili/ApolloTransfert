package com.apollon.service.impl;

import com.apollon.dao.LookupDao;
import com.apollon.model.LabelValue;
import com.apollon.model.Role;
import com.apollon.model.TipoRuoli;
import com.apollon.service.LookupManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * Implementation of LookupManager interface to talk to the persistence layer.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Service("lookupManager")
public class LookupManagerImpl implements LookupManager {
    @Autowired
    LookupDao dao;

    /**
     * {@inheritDoc}
     */
    public List<LabelValue> getAllRoles() {
        List<Role> roles = dao.getRoles();
        List<LabelValue> list = new ArrayList<LabelValue>();
        for (Role role1 : roles) {
            list.add(new LabelValue(role1.getDescription(), role1.getName()));
        }
        return list;
    }
    
    
    public List<LabelValue> getAllTipoRuoli() {
        List<TipoRuoli> tipoRuoli = dao.getTipoRuoli();
        List<LabelValue> list = new ArrayList<LabelValue>();

        for (TipoRuoli tipoRuoli1 : tipoRuoli) {
            list.add(new LabelValue(tipoRuoli1.getDescription(), tipoRuoli1.getName()));
        }
        return list;
    }
}
