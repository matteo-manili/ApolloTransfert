package com.apollon.service;

import com.apollon.model.TipoRuoli;

import java.util.List;

/**
 * Business Service Interface to handle communication between web and
 * persistence layer.
 *
 * @author <a href="mailto:dan@getrolling.com">Dan Kibler </a>
 */
public interface TipoRuoliManager extends GenericManager<TipoRuoli, Long> {
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("rawtypes")
	List getTipoRuolis(TipoRuoli tipoRuoli);

    /**
     * {@inheritDoc}
     */
    TipoRuoli getTipoRuoliByName(String tipoRuoliname);

    /**
     * {@inheritDoc}
     */
    TipoRuoli saveTipoRuoli(TipoRuoli tipoRuoli);

    /**
     * {@inheritDoc}
     */
    void removeTipoRuoli(String tipoRuoliname);
}
