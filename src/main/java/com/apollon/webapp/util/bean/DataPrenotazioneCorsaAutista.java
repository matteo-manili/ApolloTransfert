package com.apollon.webapp.util.bean;


import java.util.Date;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class DataPrenotazioneCorsaAutista implements Comparable<DataPrenotazioneCorsaAutista> {

	private long idAutista;
	private Date dataPrenotazioneCorsaAutista;

	
	public long getIdAutista() {
		return idAutista;
	}

	public void setIdAutista(long idAutista) {
		this.idAutista = idAutista;
	}

	public Date getDataPrenotazioneCorsaAutista() {
		return dataPrenotazioneCorsaAutista;
	}

	public void setDataPrenotazioneCorsaAutista(Date dataPrenotazioneCorsaAutista) {
		this.dataPrenotazioneCorsaAutista = dataPrenotazioneCorsaAutista;
	}



	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(DataPrenotazioneCorsaAutista o) {
		return getDataPrenotazioneCorsaAutista(). compareTo(o.getDataPrenotazioneCorsaAutista());
	}

	
}
