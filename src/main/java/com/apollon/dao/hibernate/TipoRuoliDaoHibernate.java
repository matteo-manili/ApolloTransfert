package com.apollon.dao.hibernate;

import com.apollon.dao.TipoRuoliDao;
import com.apollon.model.TipoRuoli;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * This class interacts with hibernate session to save/delete and
 * retrieve TipoRuoli objects.
 *
 * @author <a href="mailto:bwnoll@gmail.com">Bryan Noll</a>
 * @author jgarcia (updated to hibernate 4)
 */
@EnableTransactionManagement
@Repository("TipoRuoliDao")
public class TipoRuoliDaoHibernate extends GenericDaoHibernate<TipoRuoli, Long> implements TipoRuoliDao {

    /**
     * Constructor to create a Generics-based version using TipoRuoli as the entity
     */
    public TipoRuoliDaoHibernate() {
        super(TipoRuoli.class);
    }

    /**
     * {@inheritDoc}
     */

	@Transactional(readOnly = true)
    public TipoRuoli getTipoRuoliByName(String tipoRuoliname) {
        List<?> tipoRuolis = getSession().createCriteria(TipoRuoli.class).add(Restrictions.eq("name", tipoRuoliname)).list();
        if (tipoRuolis.isEmpty()) {
            return null;
        } else {
            return (TipoRuoli) tipoRuolis.get(0);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void removeTipoRuoli(String tipoRuoliname) {
        Object tipoRuoli = getTipoRuoliByName(tipoRuoliname);
        Session session = getSessionFactory().getCurrentSession();
        session.delete(tipoRuoli);
    }
}
