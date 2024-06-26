package com.apollon;

import static java.lang.System.out;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Properties;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.imageio.ImageIO;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.servlet.http.HttpServletRequest;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.velocity.app.VelocityEngine;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.search.query.dsl.RangeMatchingContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
/*
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Wait;
*/
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.regions.ServiceAbbreviations;
import com.amazonaws.services.codedeploy.model.transform.GenericRevisionInfoJsonUnmarshaller;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetBucketLocationRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.simpleemail.model.SendRawEmailRequest;
import com.apollon.dao.AutistaAeroportiDao;
import com.apollon.dao.AutistaLicenzeNccDao;
import com.apollon.dao.AutistaPortiNavaliDao;
import com.apollon.dao.ClasseAutoveicoloDao;
import com.apollon.dao.DocumentiPatenteDao;
import com.apollon.dao.ComunicazioniUserDao;
import com.apollon.dao.FattureDao;
import com.apollon.dao.ModelloAutoNumeroPostiDao;
import com.apollon.dao.ModelloAutoScoutDao;
import com.apollon.dao.NumeroPostiAutoDao;
import com.apollon.dao.RichiestaAutistaMedioDao;
import com.apollon.dao.AeroportiDao;
import com.apollon.dao.AgenzieViaggioBitDao;
import com.apollon.dao.AutistaDao;
import com.apollon.dao.AutistaZoneDao;
import com.apollon.dao.AutoveicoloDao;
import com.apollon.dao.ComuniDao;
import com.apollon.dao.DisponibilitaDao;
import com.apollon.dao.DisponibilitaDateDao;
import com.apollon.dao.GestioneApplicazioneDao;
import com.apollon.dao.ListaInvioSmsDao;
import com.apollon.dao.MuseiDao;
import com.apollon.dao.PortiNavaliDao;
import com.apollon.dao.ProvinceDao;
import com.apollon.dao.RegioniDao;
import com.apollon.dao.RicercaTransfertDao;
import com.apollon.dao.RichiestaAutistaParticolareDao;
import com.apollon.dao.RichiestaMediaDao;
import com.apollon.dao.RitardiDao;
import com.apollon.dao.SupplementiDao;
import com.apollon.dao.TariffeDao;
import com.apollon.dao.UserDao;
import com.apollon.dao.VenditorePercServProvinciaDao;
import com.apollon.model.Aeroporti;
import com.apollon.model.AgenzieViaggioBit;
import com.apollon.model.Autista;
import com.apollon.model.AutistaAeroporti;
import com.apollon.model.AutistaLicenzeNcc;
import com.apollon.model.AutistaMusei;
import com.apollon.model.AutistaPortiNavali;
import com.apollon.model.AutistaSottoAutisti;
import com.apollon.model.AutistaZone;
import com.apollon.model.Autoveicolo;
import com.apollon.model.ClasseAutoveicolo;
import com.apollon.model.Comuni;
import com.apollon.model.DocumentiPatente;
import com.apollon.model.ComunicazioniUser;
import com.apollon.model.ListaInvioEmailSms;
import com.apollon.model.MacroRegioni;
import com.apollon.model.ModelloAutoNumeroPosti;
import com.apollon.model.ModelloAutoScout;
import com.apollon.model.Musei;
import com.apollon.model.NumeroPostiAuto;
import com.apollon.model.PortiNavali;
import com.apollon.model.Province;
import com.apollon.model.Regioni;
import com.apollon.model.RicercaTransfert;
import com.apollon.model.RichiestaAutistaParticolare;
import com.apollon.model.RichiestaMedia;
import com.apollon.model.RichiestaMediaAutista;
import com.apollon.model.Ritardi;
import com.apollon.model.Supplementi;
import com.apollon.model.Tariffe;
import com.apollon.model.TipoRuoli;
import com.apollon.model.VenditorePercServProvincia;
import com.apollon.service.MailEngine;
import com.apollon.service.UserManager;
import com.apollon.util.CatturaAutoScout;
import com.apollon.util.CatturaEmailMarketing_UTIL;
import com.apollon.util.CatturaEmailSitiGoogleEngine;
import com.apollon.util.CatturaFederWebAziende;
import com.apollon.util.CatturaMastermeeting;
import com.apollon.util.CatturaProvinceWikipedia;
import com.apollon.util.CatturaTargheMinisteroEntrate;
import com.apollon.util.CatturaTripAdvisor;
import com.apollon.util.CatturaTripAdvisor_OLD;
import com.apollon.util.CatturaTripAdvisor;
import com.apollon.util.CreaFriendlyUrl_Slugify;
import com.apollon.util.DateUtil;
import com.apollon.util.Telefono_Prefisso_e_Formato;
import com.apollon.util.LeggiFileExcel;
import com.apollon.util.Mailin;
import com.apollon.util.UrlConnection;
import com.apollon.util.UtilBukowski;
import com.apollon.util.UtilString;
import com.apollon.util.AnalisiTariffe_ValoreAuto.OggettoTest;
import com.apollon.util.firebase.prova6;
import com.apollon.util.firebase.prova5;
import com.apollon.util.firebase.prova4;
import com.apollon.util.firebase.prova3;
import com.apollon.util.firebase.FirebaseCloudMessaging;
import com.apollon.util.firebase.prova2;
import com.apollon.util.firebase.prova1;
import com.apollon.webapp.util.AmazonStorageFiles;
import com.apollon.webapp.util.ApplicationUtils;
import com.apollon.webapp.util.CalcoloPrezzi;
import com.apollon.webapp.util.ControllerUtil;
import com.apollon.webapp.util.ControlloDateRicerca;
import com.apollon.webapp.util.InfoUserConnectAddressMain;
import com.apollon.webapp.util.Sitemap;
import com.apollon.webapp.util.MenuAutistaAttribute;
import com.apollon.webapp.util.SchedulerQuartz;
import com.apollon.webapp.util.RecensioneTransferUtil;
import com.apollon.webapp.util.TerritorioUtil;
import com.apollon.webapp.util.ApplicationUtils.ApplicationMessagesUtil;
import com.apollon.webapp.util.bean.AutistaTerritorio;
import com.apollon.webapp.util.bean.GestioneCorseMedieAdmin;
import com.apollon.webapp.util.bean.InfoPaymentProvider;
import com.apollon.webapp.util.bean.ResultRicerca_Autista_Tariffe;
import com.apollon.webapp.util.bean.ResultRicerca_Autista_Tariffe.AgendaAutistaScelta;
import com.apollon.webapp.util.bean.ResultRicerca_Autista_Tariffe.ResultMedio;
import com.apollon.webapp.util.bean.TabellaMacroRegioniProvinceTariffe;
import com.apollon.webapp.util.bean.TabellaMacroRegioniProvinceTariffe.Regioni_Entity.Province_Entity;
import com.apollon.webapp.util.bean.Tariffe_Zone;
import com.apollon.webapp.util.bean.ZoneAutisti;
import com.apollon.webapp.util.bean.TabellaMacroRegioniProvinceTariffe.Regioni_Entity;
import com.apollon.webapp.util.controller.autoveicolo.AutoveicoloUtil;
import com.apollon.webapp.util.controller.documenti.DocumentiInfoUtil;
import com.apollon.webapp.util.controller.documenti.InsertDocumentiUtil;
import com.apollon.webapp.util.controller.gestioneApplicazione.GestioneApplicazioneUtil;
import com.apollon.webapp.util.controller.home.CalcoloTariffe_Base;
import com.apollon.webapp.util.controller.home.HomeUtil;
import com.apollon.webapp.util.controller.home.HomeUtil_Sms_Email;
import com.apollon.webapp.util.controller.rimborsi.RimborsiUtil;
import com.apollon.webapp.util.controller.tariffe.TariffeUtil;
import com.apollon.webapp.util.corse.PanelCorseTemplateUtil;
import com.apollon.webapp.util.corse.ValiditaPreventivo.PeriodoValidita;
import com.apollon.webapp.util.email.InviaEmail;
import com.apollon.webapp.util.fatturazione.Fatturazione;
import com.apollon.webapp.util.email.EmailMarketing_ClientiRecencioneSconto;
import com.apollon.webapp.util.email.EmailMarketing_AgenzieViaggi;
import com.apollon.webapp.util.geogoogle.GMaps_Api;
import com.apollon.webapp.util.geogoogle.RicercaTransfert_GoogleMaps_Info;
import com.apollon.webapp.util.sms.InvioSms;
import com.apollon.webapp.util.sms.Invio_Email_Sms_UTIL;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.PendingResult;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;
//import com.deathbycaptcha.Captcha;
//import com.deathbycaptcha.SocketClient;
import com.itextpdf.awt.geom.Dimension;
import com.itextpdf.text.log.SysoCounter;
import com.paypal.api.payments.Address;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Authorization;
import com.paypal.api.payments.Capture;
import com.paypal.api.payments.CreditCard;
import com.paypal.api.payments.DetailedRefund;
import com.paypal.api.payments.Details;
import com.paypal.api.payments.FundingInstrument;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.RefundRequest;
import com.paypal.api.payments.Sale;
import com.paypal.api.payments.Transaction;
import com.paypal.base.exception.HttpErrorException;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.stripe.Stripe;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Balance;
import com.stripe.model.BalanceTransaction;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;

import antlr.Version;

//import org.openqa.selenium.support.ui.ExpectedCondition;
//import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.IOException;

/**
 * @author Matteo - matteo.manili@gmail.com
 * 
 * La classe è schedulata per avviarsi ogni 4 ore dal momento dello start dell'applicaton server. 
 * Raccoglie gli annunci dal sito www.ilcercapadrone.it e li memorizza.
 *
 */
public class REPORT extends ApplicationUtils implements Runnable {
	private final transient static Log log = LogFactory.getLog(REPORT.class);
	public static UserDao userDao = (UserDao) contextDao.getBean("userDao");
	public static GestioneApplicazioneDao gestioneApplicazioneDao = (GestioneApplicazioneDao) contextDao.getBean("GestioneApplicazioneDao");
	public static RegioniDao regioniDao = (RegioniDao) contextDao.getBean("RegioniDao");
	public static ProvinceDao provinceDao = (ProvinceDao) contextDao.getBean("ProvinceDao");
	public static ComuniDao comuniDao = (ComuniDao) contextDao.getBean("ComuniDao");
	public static AutistaZoneDao autistaZoneDao = (AutistaZoneDao) contextDao.getBean("AutistaZoneDao");
	public static AutistaDao autistaDao = (AutistaDao) contextDao.getBean("AutistaDao");
	public static AutistaAeroportiDao autistaAeroportiDao = (AutistaAeroportiDao) contextDao.getBean("AutistaAeroportiDao");
	public static AutistaPortiNavaliDao autistaPortiNavaliDao = (AutistaPortiNavaliDao) contextDao.getBean("AutistaPortiNavaliDao");
	public static AeroportiDao aeroportiDao = (AeroportiDao) contextDao.getBean("AeroportiDao");
	public static PortiNavaliDao portiNavaliDao = (PortiNavaliDao) contextDao.getBean("PortiNavaliDao");
	public static MuseiDao museiDao = (MuseiDao) contextDao.getBean("MuseiDao");
	public static DisponibilitaDao disponibilitaDao = (DisponibilitaDao) contextDao.getBean("DisponibilitaDao");
	public static DisponibilitaDateDao disponibilitaDateDao = (DisponibilitaDateDao) contextDao.getBean("DisponibilitaDateDao");
	public static AutoveicoloDao autoveicoloDao = (AutoveicoloDao) contextDao.getBean("AutoveicoloDao");
	public static RicercaTransfertDao ricercaTransfertDao = (RicercaTransfertDao) contextDao.getBean("RicercaTransfertDao");
	public static TariffeDao tariffeDao = (TariffeDao) contextDao.getBean("TariffeDao");
	public static RichiestaAutistaParticolareDao richiestaAutistaParticolareDao = (RichiestaAutistaParticolareDao) contextDao.getBean("RichiestaAutistaParticolareDao");
	public static RichiestaAutistaMedioDao richiestaAutistaMedioDao = (RichiestaAutistaMedioDao) contextDao.getBean("RichiestaAutistaMedioDao");
	public static FattureDao fattureDao = (FattureDao) contextDao.getBean("FattureDao");
	public static ComunicazioniUserDao emailAutistiMarketingDao = (ComunicazioniUserDao) contextDao.getBean("ComunicazioniUserDao");
	public static DocumentiPatenteDao documentiPatenteDao = (DocumentiPatenteDao) contextDao.getBean("DocumentiPatenteDao");
	public static AutistaLicenzeNccDao autistaLicenzeNccDao = (AutistaLicenzeNccDao) contextDao.getBean("AutistaLicenzeNccDao");
	public static RitardiDao ritardiDao = (RitardiDao) contextDao.getBean("RitardiDao");
	public static ModelloAutoScoutDao modelloAutoScoutDao = (ModelloAutoScoutDao) contextDao.getBean("ModelloAutoScoutDao");
	public static NumeroPostiAutoDao numeroPostiAutoDao = (NumeroPostiAutoDao) contextDao.getBean("NumeroPostiAutoDao");
	public static ModelloAutoNumeroPostiDao modelloAutoNumeroPostiDao = (ModelloAutoNumeroPostiDao) contextDao.getBean("ModelloAutoNumeroPostiDao");
	public static RichiestaMediaDao richiestaMediaDao = (RichiestaMediaDao) contextDao.getBean("RichiestaMediaDao");
	public static ClasseAutoveicoloDao classeAutoveicoloDao = (ClasseAutoveicoloDao) contextDao.getBean("ClasseAutoveicoloDao");
	public static AgenzieViaggioBitDao agenzieViaggioBitDao = (AgenzieViaggioBitDao) contextDao.getBean("AgenzieViaggioBitDao");
	private static VenditorePercServProvinciaDao venditorePercServProvinciaDao = (VenditorePercServProvinciaDao) contextDao.getBean("VenditorePercServProvinciaDao");
	private static ListaInvioSmsDao listaInvioSmsDao = (ListaInvioSmsDao) contextDao.getBean("ListaInvioSmsDao");
	private static SupplementiDao supplementiDao = (SupplementiDao) contextDao.getBean("SupplementiDao");
	
	public static void main(String[] args) { DAJE_REPORT(); }

	public static void DAJE_REPORT() {
		try {
			
			/* 
			List<RicercaTransfert> dd = ricercaTransfertDao.report_6();
			//System.out.println(ite.getId());
			
			
			System.out.println("Inizio LAvoro algoritmo");
			
			List<Kilometri_NumeroTransfert> kmNumTrans_list = new ArrayList<Kilometri_NumeroTransfert>();
			
			for(long km=0; km <= 3000000; km=km+1000) {
				int numeroTransfer = 0;
				for(RicercaTransfert ite : dd) {
					if(ite.getDistanzaValue() >= km && ite.getDistanzaValue() < km + 1000){
						numeroTransfer = numeroTransfer + 1;
						//System.out.println(ite.getId()+" "+ite.getSiglaProvicia_Partenza()+" "+ite.getSiglaProvicia_Arrivo()+" "+ite.getDistanzaText());
					}
				}
				kmNumTrans_list.add( new Kilometri_NumeroTransfert(km, numeroTransfer) );
			}
			

			Collections.sort(kmNumTrans_list, new Comparator<Kilometri_NumeroTransfert>() {
				@Override
		        public int compare(Kilometri_NumeroTransfert a, Kilometri_NumeroTransfert b) {
		        	int c;
		        	Integer transA = a.getNumeroTransfert();
		        	Integer transB = b.getNumeroTransfert();
	                c = transB.compareTo(transA);
	                if (c == 0){
	                	Long kimA = a.getKilometri();
	                	Long kimB = b.getKilometri();
		                c = kimB.compareTo(kimA);
	                }
	                return c;
		        }
		    });
			
			for (Kilometri_NumeroTransfert ite: kmNumTrans_list) {
		        System.out.println( "km: "+ite.getKilometri() / 1000 +" numeroTransfer: "+ite.getNumeroTransfert() );
		    }
			*/
			
			
		
			/* 
			List<Object[]> ee = ricercaTransfertDao.report_5();
			for(Object[] ite : ee) {
				System.out.println(ite[0]+" | "+ite[1]+" | "+ite[2]+" | "+ite[3]+" | "+ite[4]+" | "+ite[5]+" | "+ite[6]
						+" | "+ite[7]+" | "+ite[8]+" | "+ite[9]+" | "+ite[10]+" | "+ite[11]+" | "+ite[12]);
			}
			*/

			/*
			List<Object[]> dd = ricercaTransfertDao.report_4();
			for(Object[] ite : dd) {
				System.out.println("Tot Serviz Acquistati: "+ite[1]+" , Tot speso: "+ite[2]+" , Nazionalità: "+ite[3]+" , nome: "+ite[4]+" "+ite[5].toString().charAt(0)
						//+". , Email: "+ NascondiNomeEmail(ite[0].toString()) 
						+". , Email: "+ ite[0].toString()
						+" , id: "+ite[6]);
			}
			*/
	
			// Per csv per invio richiesta recensioni trustpilot clienti
			/*
			for(Object[] ite : dd) {
				if( !ite[0].toString().equals("onofrio.corona@gmail.com") )
					System.out.println(ite[0]+","+ite[4]+" "+ite[5]+","+ite[6]);
			}
			*/
			
			/*
			// Per csv per invio richiesta recensioni trustpilot autisti
			List<Object[]> dd = ricercaTransfertDao.report_7();
			for(Object[] ite : dd) {
					System.out.println(ite[0]+","+ite[1]+" "+ite[2]+","+ite[3]);
			}
			*/
			

			/*
			List<Object[]> cc = ricercaTransfertDao.report_3();
			for(Object ite : cc) {
				System.out.println(ite);
			}
			*/
			
			
			List<Object[]> bb = ricercaTransfertDao.report_2_bis_1();
			Calendar calCurrent = Calendar.getInstance();
			NumberFormat nf = NumberFormat.getNumberInstance(Locale.ITALIAN); DecimalFormat df = (DecimalFormat)nf;
			BigDecimal totaleMese = BigDecimal.ZERO; BigDecimal totaleMeseSupplementoMese = BigDecimal.ZERO;
			for( int contaRic = 0; contaRic < bb.size(); contaRic++ ) {
				RicercaTransfert ric = ricercaTransfertDao.get( Long.parseLong(bb.get(contaRic)[0].toString()) );
				InfoPaymentProvider infoPayProv = null;
				try {
					infoPayProv = ric.getProviderPagamentoInfo();
				}catch(Exception aa) {
					
				}
				BigDecimal CompensoAutista = BigDecimal.ZERO; BigDecimal CommissioneApollo = BigDecimal.ZERO; BigDecimal Supplemento = BigDecimal.ZERO;
				if( bb.get(contaRic)[1].toString().equals(Constants.SERVIZIO_AGENDA_AUTISTA) && infoPayProv != null ) {
					AgendaAutistaScelta agaScelta = ric.getAgendaAutistaScelta();
					CompensoAutista = agaScelta.getPrezzoTotaleAutisti();
					Supplemento = (ric.getSupplementi_Id() != null ? DammiSupplementi(ric.getId()) : BigDecimal.ZERO);
					CommissioneApollo =  infoPayProv.getAmount().subtract(CompensoAutista).add(Supplemento).subtract(infoPayProv.getFee());
				
				}else if( bb.get(contaRic)[1].toString().equals(Constants.SERVIZIO_STANDARD) && infoPayProv != null ) {
					CompensoAutista = ric.getRichiestaMediaAutistaCorsaConfermata().getPrezzoTotaleAutista();
					Supplemento = (ric.getSupplementi_Id() != null ? DammiSupplementi(ric.getId()) : BigDecimal.ZERO);
					CommissioneApollo =  infoPayProv.getAmount().subtract(CompensoAutista).add(Supplemento).subtract(infoPayProv.getFee());
					
				}else if( bb.get(contaRic)[1].toString().equals(Constants.SERVIZIO_PARTICOLARE) && infoPayProv != null ) {
					CompensoAutista = ric.getRichiestaAutistaParticolareAcquistato().getPrezzoTotaleAutista();
					Supplemento = (ric.getSupplementi_Id() != null ? DammiSupplementi(ric.getId()) : BigDecimal.ZERO);
					CommissioneApollo =  infoPayProv.getAmount() != null && infoPayProv.getFee() != null ? infoPayProv.getAmount().subtract(CompensoAutista).add(Supplemento).subtract(infoPayProv.getFee()) : null;
					
				}else if( bb.get(contaRic)[1].toString().equals(Constants.SERVIZIO_MULTIPLO) && infoPayProv != null ) { }
				
				Calendar calRic = Calendar.getInstance();
				calRic.setTime(ric.getDataRicerca());

				while( true ) {
					//System.out.println("while: "+ calRic.get(Calendar.MONTH) +" "+calRic.get(Calendar.YEAR) +" | "+ calCurrent.get(Calendar.MONTH) +" "+calCurrent.get(Calendar.YEAR) );
					if( calRic.get(Calendar.MONTH) == calCurrent.get(Calendar.MONTH) 
							&& calRic.get(Calendar.YEAR) == calCurrent.get(Calendar.YEAR) && infoPayProv != null ) {
						System.out.println("[*] DataTransfer: "+DateUtil.FORMATO_GIORNO_MESE_ANNO_ORA_ESTESO(null).format(ric.getDataRicerca())
								+"; COD.CORSA: "+bb.get(contaRic)[0]
								+"; TIPO CORSA: "+bb.get(contaRic)[1]
								+"; SIGLA PROV PARTENZA: "+ric.getSiglaProvicia_Partenza()+" "+ric.getFormattedAddress_Partenza() 
								+"; SIGLA PROV ARRIVO: "+ric.getSiglaProvicia_Arrivo()+" "+ric.getFormattedAddress_Arrivo()
								
								+"; Amount; " +nf.format(infoPayProv.getAmount().setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue()) 
								+"; Supplemento; " +(Supplemento.compareTo(BigDecimal.ZERO) > 0 ? nf.format(Supplemento.doubleValue()) : 0)
								+"; Sconto; " +(ric.getPercentualeSconto() != null ? ric.getPercentualeSconto().intValue() : 0)+"%"
								+"; Refund; "+nf.format(infoPayProv.getRefund().doubleValue())
								+"; Fee; "+nf.format(infoPayProv.getFee().setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue())
								+"; CompensoAutista; "+nf.format(CompensoAutista.doubleValue())
								+"; CommissioneApollo; "+nf.format(CommissioneApollo.setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue())
								
								+"; Approv.A.: "+ DammiApprovazione(ric.getApprovazioneAndata()) + (ric.isRitorno() ? " | Approv.R.: "+ DammiApprovazione(ric.getApprovazioneAndata()) : "")
								//+"; Cliente: "+ NascondiNomeEmail(ric.getUser().getEmail()) 
								+"; Cliente: "+ ric.getUser().getEmail() 
								);
						
						if( ric.getApprovazioneAndata() == Constants.APPROVATA || ric.getApprovazioneAndata() == Constants.IN_APPROVAZIONE) {
							totaleMese = CommissioneApollo != null ? totaleMese.add( CommissioneApollo ).setScale(2, BigDecimal.ROUND_HALF_EVEN) : null;
						}
						if (Supplemento.compareTo(BigDecimal.ZERO) > 0) {
							totaleMeseSupplementoMese = totaleMeseSupplementoMese.add( Supplemento ).setScale(2, BigDecimal.ROUND_HALF_EVEN);
						}
						break;
					}else {
						calCurrent.add(Calendar.MONTH, -1);
					}
				}

				boolean ultimoRic = contaRic + 1 < bb.size() ? false : true ; 
				Calendar calRic_NEXT = Calendar.getInstance();
				if( ultimoRic == false ) {
					RicercaTransfert ricNext = ricercaTransfertDao.get(Long.parseLong(bb.get(contaRic + 1)[0].toString())); 
					calRic_NEXT.setTime( ricNext.getDataRicerca() );
				}
				//System.out.println("saldo mese: "+ calRic.get(Calendar.MONTH) +" "+calRic.get(Calendar.YEAR) +" | "+ calRic_NEXT.get(Calendar.MONTH) +" "+calRic_NEXT.get(Calendar.YEAR)  );
				if( ultimoRic || (calRic.get(Calendar.MONTH) != calRic_NEXT.get(Calendar.MONTH) || calRic.get(Calendar.YEAR) != calRic_NEXT.get(Calendar.YEAR)) ) {
					System.out.println("############# TOT COMMISSIONI MESE: "+DateUtil.FORMATO_MESE_ESTESO_ANNO(null).format(calCurrent.getTime()).toUpperCase()+": "+totaleMese
							+(totaleMeseSupplementoMese.compareTo(BigDecimal.ZERO) > 0 ? " + TOT SUPPLEMENTI: "+totaleMeseSupplementoMese+" (*accreditati in parte agli autisti)" : ""));
						totaleMese = BigDecimal.ZERO; totaleMeseSupplementoMese = BigDecimal.ZERO;
				}
			}
			
			
			
			/* 
			List<Object[]> bb = ricercaTransfertDao.report_2();
			for(Object ite : bb) {
				System.out.println(ite);
			}
			*/
			
			/* 
			List<Object[]> aa = ricercaTransfertDao.report_1();
			for(Object ite : aa) {
				System.out.println(ite);
			}
			*/
			
			
			
			

		}catch(Exception ex) {
			System.out.println("Exception REPORT");
			ex.printStackTrace();
		}
	}
	
	private static BigDecimal DammiSupplementi(long idRic) {
		List<Supplementi> supplementiList = supplementiDao.getSupplementiBy_IdRicercaTransfert(idRic);
		BigDecimal totSupplementi = BigDecimal.ZERO;
		for(Supplementi ite: supplementiList) {
			totSupplementi = totSupplementi.add(ite.getPrezzo());
		}
		return totSupplementi;
	}

	private static String DammiApprovazione(int aprovazioneCode) {
		if(Constants.IN_APPROVAZIONE == aprovazioneCode) {
			return "IN_APPROVAZIONE";
		}else if(Constants.APPROVATA == aprovazioneCode) {
			return "APPROVATA";
		}else if(Constants.NON_APPROVATA == aprovazioneCode) {
			return "NON_APPROVATA";
		}
		return "???";
	}
	

	private static class Kilometri_NumeroTransfert {
		private Long kilometri;
		private Integer numeroTransfert;
		public Kilometri_NumeroTransfert(Long kilometri, Integer numeroTransfert) {
			super();
			this.kilometri = kilometri;
			this.numeroTransfert = numeroTransfert;
		}
		
		public Long getKilometri() {
			return kilometri;
		}
		public void setKilometri(Long kilometri) {
			this.kilometri = kilometri;
		}
		public Integer getNumeroTransfert() {
			return numeroTransfert;
		}
		public void setNumeroTransfert(Integer numeroTransfert) {
			this.numeroTransfert = numeroTransfert;
		}
	}
	
	private static String NascondiNomeEmail( String email ) {
		//String email = "ciao@gmail.com"; 
		int start = email.indexOf("@");

		if (start < 0) {
		    return "";  // pick your poison for the error condition
		}

		StringBuilder sbEmail = new StringBuilder(email);
		sbEmail.replace(0, start, "******");
		//System.out.println( sbEmail.toString() );
		return sbEmail.toString();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
}
