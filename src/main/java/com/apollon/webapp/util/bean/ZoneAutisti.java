package com.apollon.webapp.util.bean;

import java.io.Serializable;
import java.util.List;

import com.apollon.model.AutistaZone;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class ZoneAutisti implements Serializable {

	private static final long serialVersionUID = -8954130825549083655L;

	public ZoneAutisti() { }
	

    public ZoneAutisti(List<AutistaZone> zoneAutistiList) {
        this.zoneAutistiList = zoneAutistiList;
    }
 
    
    
    
    
    private List<AutistaZone> zoneAutistiList = null;

	public List<AutistaZone> getZoneAutistiList() {
		return zoneAutistiList;
	}

	public void setZoneAutistiList(List<AutistaZone> zoneAutistiList) {
		this.zoneAutistiList = zoneAutistiList;
	}
    
    

    
    
    
    
    

}
