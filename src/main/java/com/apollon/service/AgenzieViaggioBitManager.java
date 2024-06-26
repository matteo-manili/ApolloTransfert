package com.apollon.service;

import java.util.List;

import com.apollon.dao.AgenzieViaggioBitDao;
import com.apollon.model.AgenzieViaggioBit;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public interface AgenzieViaggioBitManager extends GenericManager<AgenzieViaggioBit, Long> {
	
	void setAgenzieViaggioBitDao(AgenzieViaggioBitDao agenzieViaggioBitDao);
	
	
	AgenzieViaggioBit get(Long id);
	
	AgenzieViaggioBit getEmailAgenzieViaggioBit(String email);
	
	AgenzieViaggioBit getAgenzieViaggioBit_TokenSconto(String token);
	
	List<AgenzieViaggioBit> getAgenzieViaggioBit();
	
	List<AgenzieViaggioBit> getAgenzieViaggioBit_DESC();
	
	List<AgenzieViaggioBit> getAgenzieViaggioBit_ASC();
	
	List<AgenzieViaggioBit> getAgenzieViaggioBit_Nome(String term);
	
	AgenzieViaggioBit saveAgenzieViaggioBit(AgenzieViaggioBit agenzieViaggioBit) throws Exception;
	
	void removeAgenzieViaggioBit(long userAgenzieViaggioBit);

	List<AgenzieViaggioBit> getNomeAgenzieViaggioBit(String nomeStand);

	List<AgenzieViaggioBit> getAgenzieViaggioBit_Unsubscribe();

	List<AgenzieViaggioBit> getAgenzieViaggioBy_LIKE(String email);


}
