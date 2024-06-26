package com.apollon.dao;

import com.apollon.model.TipoRuoli;

/**
 * TipoRuoli Data Access Object (DAO) interface.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public interface TipoRuoliDao extends GenericDao<TipoRuoli, Long> {
    /**
     * Gets tipoRuoli information based on tipoRuoliname
     * @param tipoRuoliname the tipoRuoliname
     * @return populated tipoRuoli object
     */
    TipoRuoli getTipoRuoliByName(String tipoRuoliname);

    /**
     * Removes a tipoRuoli from the database by name
     * @param tipoRuoliname the tipoRuoli's tipoRuoliname
     */
    void removeTipoRuoli(String tipoRuoliname);
}
