package com.apollon.webapp.util.bean;

import java.math.BigDecimal;

/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class InfoPaymentProvider {
	
	private String paymentProviderId;
	private String paymentProviderTipo;
	
	private String nomeCliente;
	private BigDecimal refund;
	private BigDecimal amount;
	private BigDecimal fee;
	private String messaggioErrore;
	
	public InfoPaymentProvider(String paymentProviderId, String paymentProviderTipo, String nomeCliente, BigDecimal refund, BigDecimal amount, BigDecimal fee) {
		super();
		this.paymentProviderId = paymentProviderId;
		this.paymentProviderTipo = paymentProviderTipo;
		this.nomeCliente = nomeCliente;
		this.refund = refund;
		this.amount = amount;
		this.fee = fee;
	}

	public InfoPaymentProvider() {
		super();
		// TODO Auto-generated constructor stub
	}


	public String getPaymentProviderId() {
		return paymentProviderId;
	}
	public void setPaymentProviderId(String paymentProviderId) {
		this.paymentProviderId = paymentProviderId;
	}
	public String getPaymentProviderTipo() {
		return paymentProviderTipo;
	}
	public void setPaymentProviderTipo(String paymentProviderTipo) {
		this.paymentProviderTipo = paymentProviderTipo;
	}
	public String getNomeCliente() {
		return nomeCliente;
	}
	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}
	public BigDecimal getRefund() {
		return refund;
	}
	public void setRefund(BigDecimal refund) {
		this.refund = refund;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getFee() {
		return fee;
	}
	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
	public String getMessaggioErrore() {
		return messaggioErrore;
	}
	public void setMessaggioErrore(String messaggioErrore) {
		this.messaggioErrore = messaggioErrore;
	}



}
