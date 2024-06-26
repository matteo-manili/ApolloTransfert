package com.apollon.service.impl;

import com.apollon.dao.TipoRuoliDao;
import com.apollon.model.TipoRuoli;
import com.apollon.service.TipoRuoliManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of TipoRuoliManager interface.
 *
 * @author <a href="mailto:dan@getrolling.com">Dan Kibler</a>
 */
@Service("tipoRuoliManager")
public class TipoRuoliManagerImpl extends GenericManagerImpl<TipoRuoli, Long> implements TipoRuoliManager {
    TipoRuoliDao tipoRuoliDao;

    @Autowired
    public TipoRuoliManagerImpl(TipoRuoliDao tipoRuoliDao) {
        super(tipoRuoliDao);
        this.tipoRuoliDao = tipoRuoliDao;
    }

    /**
     * {@inheritDoc}
     */
    public List<TipoRuoli> getTipoRuolis(TipoRuoli tipoRuoli) {
        return dao.getAll();
    }

    /**
     * {@inheritDoc}
     */
    public TipoRuoli getTipoRuoliByName(String tipoRuoliname) {
        return tipoRuoliDao.getTipoRuoliByName(tipoRuoliname);
    }

    /**
     * {@inheritDoc}
     */
    public TipoRuoli saveTipoRuoli(TipoRuoli tipoRuoli) {
        return dao.save(tipoRuoli);
    }

    /**
     * {@inheritDoc}
     */
    public void removeTipoRuoli(String tipoRuoliname) {
        tipoRuoliDao.removeTipoRuoli(tipoRuoliname);
    }
}