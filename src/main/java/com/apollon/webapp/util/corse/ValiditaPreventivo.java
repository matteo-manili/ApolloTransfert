package com.apollon.webapp.util.corse;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.json.JSONArray;

import com.apollon.model.RicercaTransfert;

public class ValiditaPreventivo {
	
	public static JSONArray DammiJsonTabellaPeriodioValiditaPreventivo_JSONArray() {
		return new JSONArray( DammiJsonTabellaPeriodioValiditaPreventivo() );
	}
	
	/**
	 * Mi ritorna la Lista PeriodoValidita ma senza gli emenenti che superano la data di prelevamento Transfert
	 * @param ricercaTransfert
	 * @return
	 */
	public static JSONArray DammiJsonTabellaPeriodioValiditaPreventivo_JSONArray(RicercaTransfert ricercaTransfert) {
		if( ricercaTransfert != null ) {
			List<PeriodoValidita> listPeriodoValidita_RicercaTransfert = new ArrayList<PeriodoValidita>();
			for(PeriodoValidita ite: DammiJsonTabellaPeriodioValiditaPreventivo() ) {
				Calendar calendar = Calendar.getInstance();
				calendar.add(ite.getTipoPeriodoCalendar(), ite.getValorePeriodoCalendar());
				if( ricercaTransfert.getDataOraPrelevamentoDate().getTime() > calendar.getTime().getTime() ) {
					listPeriodoValidita_RicercaTransfert.add(ite);
				}
			}
			return new JSONArray( listPeriodoValidita_RicercaTransfert );
		}else {
			return new JSONArray( DammiJsonTabellaPeriodioValiditaPreventivo() );
		}
	}
	

	public static PeriodoValidita DammiPeriodo_by_Id(int idPeriodo) {
		for(PeriodoValidita ite: DammiJsonTabellaPeriodioValiditaPreventivo()) {
			if(ite.getIdPeriodo() == idPeriodo) {
				return ite;
			}
		}
		return null;
	}
	
	private static List<PeriodoValidita> DammiJsonTabellaPeriodioValiditaPreventivo() {
		List<PeriodoValidita> list = new ArrayList<PeriodoValidita>();
		list.add( new PeriodoValidita(10, Calendar.HOUR_OF_DAY, 1, "1 ora") );
		list.add( new PeriodoValidita(20, Calendar.HOUR_OF_DAY, 5, "5 ore") );
		list.add( new PeriodoValidita(30, Calendar.HOUR_OF_DAY, 10, "10 ore") );
		list.add( new PeriodoValidita(40, Calendar.HOUR_OF_DAY, 24, "1 giorno") );
		list.add( new PeriodoValidita(45, Calendar.HOUR_OF_DAY, 36, "1 giorno e mezzo") );
		list.add( new PeriodoValidita(50, Calendar.DAY_OF_MONTH, 2, "2 giorni") );
		list.add( new PeriodoValidita(60, Calendar.DAY_OF_MONTH, 3, "3 giorni") );
		list.add( new PeriodoValidita(70, Calendar.DAY_OF_MONTH, 5, "5 giorni") );
		list.add( new PeriodoValidita(80, Calendar.WEEK_OF_YEAR, 1, "1 settimana") );
		list.add( new PeriodoValidita(90, Calendar.WEEK_OF_YEAR, 2, "2 settimane") );
		list.add( new PeriodoValidita(100, Calendar.MONTH, 1, "1 mese") );
		list.add( new PeriodoValidita(110, Calendar.MONTH, 2, "2 mesi") );
		return list;
	}


	public static class PeriodoValidita {
		private int idPeriodo;
		private int tipoPeriodoCalendar;
		private int valorePeriodoCalendar;
		private String testoPeriodo;
		
		public PeriodoValidita(int idPeriodo, int tipoPeriodoCalendar, int valorePeriodoCalendar, String testoPeriodo) {
			super();
			this.idPeriodo = idPeriodo;
			this.tipoPeriodoCalendar = tipoPeriodoCalendar;
			this.valorePeriodoCalendar = valorePeriodoCalendar;
			this.testoPeriodo = testoPeriodo;
		}

		public int getIdPeriodo() {
			return idPeriodo;
		}
		public void setIdPeriodo(int idPeriodo) {
			this.idPeriodo = idPeriodo;
		}
		public int getTipoPeriodoCalendar() {
			return tipoPeriodoCalendar;
		}
		public void setTipoPeriodoCalendar(int tipoPeriodoCalendar) {
			this.tipoPeriodoCalendar = tipoPeriodoCalendar;
		}
		public int getValorePeriodoCalendar() {
			return valorePeriodoCalendar;
		}
		public void setValorePeriodoCalendar(int valorePeriodoCalendar) {
			this.valorePeriodoCalendar = valorePeriodoCalendar;
		}
		public String getTestoPeriodo() {
			return testoPeriodo;
		}
		public void setTestoPeriodo(String testoPeriodo) {
			this.testoPeriodo = testoPeriodo;
		}
	}
	
	
} // fine classe
