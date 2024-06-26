package com.apollon.dao;

import java.util.List;
import com.apollon.model.AgenzieViaggioBit;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface AgenzieViaggioBitDao extends GenericDao<AgenzieViaggioBit, Long> {
	
	AgenzieViaggioBit get(Long id);
	
	AgenzieViaggioBit getEmailAgenzieViaggioBit(String email);
	
	AgenzieViaggioBit getAgenzieViaggioBit_TokenSconto(String token);
	
	List<AgenzieViaggioBit> getAgenzieViaggioBit();
	
	List<AgenzieViaggioBit> getAgenzieViaggioBit_Nome(String term);
	
	AgenzieViaggioBit saveAgenzieViaggioBit(AgenzieViaggioBit agenzieViaggioBit);

	List<AgenzieViaggioBit> getNomeAgenzieViaggioBit(String nome);

	List<AgenzieViaggioBit> getAgenzieViaggioBit_DESC();

	List<AgenzieViaggioBit> getAgenzieViaggioBit_ASC();

	List<AgenzieViaggioBit> getAgenzieViaggioBy_LIKE(String term);

	List<AgenzieViaggioBit> getAgenzieViaggioBit_Unsubscribe();

	List<AgenzieViaggioBit> getAgenzie_CodiceScontoUsato();

	

	


}
