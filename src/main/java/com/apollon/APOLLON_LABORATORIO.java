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
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
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
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import org.joda.time.DateTime;
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
import com.apollon.dao.CoordGeoProvinceDao;
import com.apollon.dao.FattureDao;
import com.apollon.dao.ModelloAutoNumeroPostiDao;
import com.apollon.dao.ModelloAutoScoutDao;
import com.apollon.dao.NumeroPostiAutoDao;
import com.apollon.dao.RichiestaAutistaMedioDao;
import com.apollon.dao.AeroportiDao;
import com.apollon.dao.AgA_AutoveicoloModelliGiornateDao;
import com.apollon.dao.AgA_AutoveicoloModelliTariffariDao;
import com.apollon.dao.AgA_GiornateDao;
import com.apollon.dao.AgA_ModelliGiornateDao;
import com.apollon.dao.AgA_ModelliTariffariDao;
import com.apollon.dao.AgA_TariffariDao;
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
import com.apollon.dao.NazioniDao;
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
import com.apollon.model.AgA_AutoveicoloModelliGiornate;
import com.apollon.model.AgA_AutoveicoloModelliTariffari;
import com.apollon.model.AgA_Giornate;
import com.apollon.model.AgA_ModelliGiornate;
import com.apollon.model.AgA_ModelliTariffari;
import com.apollon.model.AgA_Tariffari;
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
import com.apollon.model.CoordGeoProvince;
import com.apollon.model.ListaInvioEmailSms;
import com.apollon.model.MacroRegioni;
import com.apollon.model.ModelloAutoNumeroPosti;
import com.apollon.model.ModelloAutoScout;
import com.apollon.model.Musei;
import com.apollon.model.Nazioni;
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
import com.apollon.util.CatturaAvvocati;
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
import com.apollon.util.DammiTempoOperazione;
import com.apollon.util.DateUtil;
import com.apollon.util.Telefono_Prefisso_e_Formato;
import com.apollon.util.LeggiFileExcel;
import com.apollon.util.Mailin;
import com.apollon.util.NumberUtil;
import com.apollon.util.PropertiesFileUtil;
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
import com.apollon.webapp.rest.AgA_Calendario;
import com.apollon.webapp.rest.AgA_General;
import com.apollon.webapp.rest.AgA_Giornata;
import com.apollon.webapp.rest.AgA_ModelTariffario;
import com.apollon.webapp.rest.AgA_Tariffario;
import com.apollon.webapp.rest.AgA_Calendario.Calendario_FrontEnd;
import com.apollon.webapp.rest.AgA_Calendario.Calendario_FrontEnd.GiorniMeseCalendario;
import com.apollon.webapp.rest.AgA_Giornata_Bean.TabellaCalendario;
import com.apollon.webapp.rest.AgA_Giornata_Bean.TabellaGiornata;
import com.apollon.webapp.rest.AgA_Giornata_Bean.TabellaGiornataTariffario;
import com.apollon.webapp.rest.AgA_Giornata_Bean.TabellaModelloGiornata;
import com.apollon.webapp.rest.AgA_Giornata_Bean.TabellaModelloTariffario;
import com.apollon.webapp.rest.AgA_Giornata_Bean.TabellaTariffario;
import com.apollon.webapp.rest.AgA_ModelTariffario.TabellaTariffarioAutista;
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
import com.apollon.webapp.util.bean.AgendaAutista_Autista;
import com.apollon.webapp.util.bean.AutistaTerritorio;
import com.apollon.webapp.util.bean.GestioneCorseMedieAdmin;
import com.apollon.webapp.util.bean.InfoPaymentProvider;
import com.apollon.webapp.util.bean.ResultRicerca_Autista_Tariffe;
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
import com.apollon.webapp.util.controller.home.CalcoloTariffe;
import com.apollon.webapp.util.controller.home.CalcoloTariffe_Base;
import com.apollon.webapp.util.controller.home.HomeUtil;
import com.apollon.webapp.util.controller.home.HomeUtil_Sms_Email;
import com.apollon.webapp.util.controller.rimborsi.RimborsiUtil;
import com.apollon.webapp.util.controller.tariffe.TariffeUtil;
import com.apollon.webapp.util.controller.tariffe.MenuTariffeTransfer;
import com.apollon.webapp.util.controller.tariffe.MenuTariffeTransfer.MenuTerrTariffeTransfer;
import com.apollon.webapp.util.controller.tariffe.MenuTariffeTransfer.MenuTerrTariffeTransfer.MenuTerrTariffeTransferElement;
import com.apollon.webapp.util.corse.PanelCorseTemplateUtil;
import com.apollon.webapp.util.corse.ValiditaPreventivo.PeriodoValidita;
import com.apollon.webapp.util.email.InviaEmail;
import com.apollon.webapp.util.fatturazione.Fatturazione;
import com.apollon.webapp.util.email.EmailMarketing_ClientiRecencioneSconto;
import com.apollon.webapp.util.email.EmailMarketing_AgenzieViaggi;
import com.apollon.webapp.util.email.EmailMarketing_Autisti_Comunicazioni;
import com.apollon.webapp.util.geogoogle.GMaps_Api;
import com.apollon.webapp.util.geogoogle.RicercaTransfert_GoogleMaps_Info;
import com.apollon.webapp.util.sms.InvioSms;
import com.apollon.webapp.util.sms.Invio_Email_Sms_UTIL;
import com.apollon.webapp.util.sms.SMSTelesign;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.common.collect.ImmutableList;
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
import com.stripe.Stripe;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Balance;
import com.stripe.model.BalanceTransaction;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.telesign.MessagingClient;
import com.telesign.RestClient;
import com.telesign.Util;

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

import java.io.IOException;

import org.json.JSONObject;

import com.paypal.http.serializer.Json;
import com.paypal.orders.Order;
import com.paypal.orders.OrdersGetRequest;
import com.paypal.base.rest.APIContext;
import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import com.paypal.payments.CapturesRefundRequest;
import com.paypal.payments.LinkDescription;
import com.paypal.payments.Money;
import com.paypal.payments.RefundRequest;


import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

/**
 * 
 * 
 * @author Matteo - matteo.manili@gmail.com
 * 
 * La classe è schedulata per avviarsi ogni 4 ore dal momento dello start dell'applicaton server. 
 * Raccoglie gli annunci dal sito www.ilcercapadrone.it e li memorizza.
 *
 */
public class APOLLON_LABORATORIO extends ApplicationUtils implements Runnable {
	private final transient static Log log = LogFactory.getLog(APOLLON_LABORATORIO.class);
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
	private static AgA_AutoveicoloModelliGiornateDao agA_AutoveicoloModelliGiornateDao = (AgA_AutoveicoloModelliGiornateDao) contextDao.getBean("AgA_AutoveicoloModelliGiornateDao");
	private static AgA_AutoveicoloModelliTariffariDao agA_AutoveicoloModelliTariffariDao = (AgA_AutoveicoloModelliTariffariDao) contextDao.getBean("AgA_AutoveicoloModelliTariffariDao");
	private static AgA_ModelliTariffariDao agA_ModelliTariffariDao = (AgA_ModelliTariffariDao) contextDao.getBean("AgA_ModelliTariffariDao");
	private static AgA_ModelliGiornateDao agA_ModelliGiornateDao = (AgA_ModelliGiornateDao) contextDao.getBean("AgA_ModelliGiornateDao");
	private static AgA_TariffariDao agA_TariffariDao = (AgA_TariffariDao) contextDao.getBean("AgA_TariffariDao");
	private static AgA_GiornateDao agA_GiornateDao = (AgA_GiornateDao) contextDao.getBean("AgA_GiornateDao");
	private static NazioniDao nazioniDao = (NazioniDao) contextDao.getBean("NazioniDao");
	private static CoordGeoProvinceDao coordGeoProvinceDao = (CoordGeoProvinceDao) contextDao.getBean("CoordGeoProvinceDao");
	
	public static void main(String[] args) { DAJE_PORCO_DIO(); }
	/*
	public static String round(double value) {
	    BigDecimal bd = BigDecimal.valueOf(value);
	    bd = bd.setScale(1, RoundingMode.HALF_UP);
	    return bd.toString().replace(".", ",") ;
	}
	*/
	
	// InviaEmail_SMS_GATEWAY( ApplicationMessagesUtil.DammiMessageSource("cellulare.matteo", null), "dai cazzo" );
	
	@SuppressWarnings("unchecked")
	public static void DAJE_PORCO_DIO() {
		long startTime = System.nanoTime(); 
		
		try {
			long idAutoveicolo = 2l;
			String meseAnno = "01/2020";
			String giorno = "09/01/2020";
			
			
			
			CatturaAvvocati.StartScrapingMultiplo(1, 50);
			
			
			//SMSTelesign.Invia_VOICE_Test();
			
			
			
			
			
			
			/*
			List<Object[]> listObject = listaInvioSmsDao.Check_TIPO_MESSAGGIO_SMS_RAPIDO();
			if( listObject != null && listObject.size() > 0 ) {
				Object obj_firstElemnt = (Object) listObject.get(0);
				ListaInvioEmailSms messaggiono_firstElemnt = (ListaInvioEmailSms) obj_firstElemnt;
				if( messaggiono_firstElemnt.getTipoMessaggio() == Invio_Email_Sms_UTIL.TIPO_MESSAGGIO_SMS_RAPIDO ) {
					InvioSms.Lancia_SMS_Gateway(null, messaggiono_firstElemnt);
				}
			}
			*/
			
			
			
			
			//ricercaTransfertDao.Recencioni_Approvate(false);
			
			
			/*
			MenuTerrTariffeTransfer menuTerrTariffeTransfer = MenuTariffeTransfer.DammiMenuItems_MacroRegioni_Regioni(Constants.Locale_IT, null, regioniDao.MenuTariffe_Province());
			
			for(MenuTerrTariffeTransferElement ite: menuTerrTariffeTransfer.getMacroRegioni() ) {
				System.out.println(ite.getUrl());
			}
			for(MenuTerrTariffeTransferElement ite: menuTerrTariffeTransfer.getRegioni() ) {
				System.out.println(ite.getUrl());
			}
			for(MenuTerrTariffeTransferElement ite: menuTerrTariffeTransfer.getProvince() ) {
				System.out.println(ite.getUrl());
			}
			*/
			
			//regioniDao.MenuTariffe_MacroRegioni_e_Regioni();
			//regioniDao.Menu_Lista_ProvinceItalianeOrderByAbitanti_MaxResult(1, Constants.MAX_NUMERO_SETTIMANE__OLD_DATA_GOOGLE_MAPS_REQUEST_DISTANCE);
			
			//regioniDao.Menu_Lista_MacroRegioneOrderByAbitanti_MaxResult(1l, 1, Constants.MAX_NUMERO_SETTIMANE__OLD_DATA_GOOGLE_MAPS_REQUEST_DISTANCE);

			//regioniDao.Menu_Lista_RegioneOrderByAbitanti_MaxResult(9l, 2, Constants.MAX_NUMERO_SETTIMANE__OLD_DATA_GOOGLE_MAPS_REQUEST_DISTANCE);
			
			//regioniDao.Menu_Lista_ProvinciaOrderByAbitanti_MaxResult(33l, Constants.MAX_NUMERO_SETTIMANE__OLD_DATA_GOOGLE_MAPS_REQUEST_DISTANCE);
			
			
			/*
			PropertiesFileUtil mpc = new PropertiesFileUtil();
	        Set<Object> keys = mpc.getAllKeys();
	        System.out.println("keys.size(): "+keys.size());
	        for(int conta = 0; conta <= keys.size(); conta++) {
	        	for(Object k: keys) {
		            String key = (String)k;
		            if( key.startsWith( ("consigli.post.url."+conta+".").toString() ) ) {
		            	System.out.println(key+": "+mpc.getPropertyValue(key)); 
		            }
		        }
	        }
	        */
	        
			
			
			//CoordGeoProvince coord = coordGeoProvinceDao.getCoordGeoProvince_by_LatLng( NumberUtil.round(41.9028702, 2), NumberUtil.round(12.4964693, 2), 2 );
			//System.out.println( coord );
			
			
			// con parametri 3 e 50 ci mette 6 ore
			//CatturaEmailSitiGoogleEngine.StartScrapingMultiplo(3, 50);
			
			//CatturaEmailMarketing_UTIL.EseguiScriptAggiornamentiSconti();
			
			
			//agA_GiornateDao.PuliziaDatabase_GiornateTariffari();
			
			//EmailMarketing_AgenzieViaggi.inviaEmailMarketing_AgenzieViaggi_FieraMilano(null, null, 0 );
			
			/*
			Autista autista = autistaDao.get(2l); // Ciro
			List<Autoveicolo> autoList = autoveicoloDao.getAutoveicolo();
			for(Autoveicolo ite: autoList) {
				if( agA_GiornateDao.AutoveicoloDisponbileVendita(ite.getId()) ) {
					//ite.setAutista(autista);
					//autoveicoloDao.saveAutoveicolo(ite);
					System.out.println( ite.getInfo() );
				}
			}
			*/
			
			
			
			//richiestaMediaDao.PuliziaDataBase_RichiestaMedia();
			
			/*
			// CORSA ACQUISTATA ST 13481 PARTENZA 2020-03-15 18:15:00 RITORNO 2020-03-30 18:15:00
			// CORSA ACQUISTATA PART 13460 PARTENZA 2020-03-27 18:15:00
			
			// 13477l PARTENZA 2020-03-25 18:10:00
			// 13476l PARTENZA 2020-03-25 18:30:00  
			// 13479l PARTENZA 2020-03-23 18:30:00 RITORNO 2020-03-25 18:30:00
			
			// 13478l PARTENZA 2020-03-25 18:00:00
			
			RicercaTransfert ricTransfert = ricercaTransfertDao.get( 13479l );
			CalcoloTariffe CalcoloTariffe = new CalcoloTariffe();
			CalcoloTariffe.CalcoloTariffe_Main(ricTransfert);
			*/
			
			
			/*
			List<AutistaZone > autistaList = autistaZoneDao.getAutistaZone();
			
			for(AutistaZone ite : autistaList) {
				
				boolean servizioAttivo = ite.isServizioAttivo();
				boolean tuttaItaia  = ite.isTuttaItalia();
				Autista autista = ite.getAutista();
				
				Comuni comune = ite.getComuni();
				if( comune != null && ite.getProvince() == null) {
					autistaZoneDao.remove( ite.getId() );
					try {
						AutistaZone AutistaZoneNew = new AutistaZone();
						AutistaZoneNew.setServizioAttivo(servizioAttivo);
						AutistaZoneNew.setTuttaItalia(tuttaItaia);
						AutistaZoneNew.setProvince(comune.getProvince());
						AutistaZoneNew.setAutista(autista);
						autistaZoneDao.saveAutistaZone(AutistaZoneNew);
					}catch( DataIntegrityViolationException ss ) {
						System.out.println("DataIntegrityViolationException");
					}
				}
			}
			 */
			
			/*
			List<Autoveicolo> listAuto = autoveicoloDao.getAutoveicolo();
			for(Autoveicolo ite: listAuto  ) {
				if (agA_GiornateDao.DisponbileVendita(ite.getId()) ) {
					System.out.println( ite.getId() );
				}
			}
			*/

				
			
			/*
			int conta = 0;
			for(RicercaTransfert ite :  ricercaTransfertDao.getRicercaTransfert_idPaymentProvider_NotNull()  ) {
				JSONObject infoDatiPasseggero = HomeUtil.GetInfoDatiPasseggero(ite);
				if( ite.getIdPaymentProvider().contains("PAY") ) {
					System.out.println(ite.getIdPaymentProvider());
					conta++;
					infoDatiPasseggero.put(Constants.PaymentProviderIdJSON, ite.getIdPaymentProvider());
					infoDatiPasseggero.put(Constants.PaymentProviderTipoJSON, Constants.TIPO_PAYMENT_PAYPAL_1);

				}else if( ite.getIdPaymentProvider().contains("ch_") ) {
					System.out.println(ite.getIdPaymentProvider());
					conta++;
					infoDatiPasseggero.put(Constants.PaymentProviderIdJSON, ite.getIdPaymentProvider());
					infoDatiPasseggero.put(Constants.PaymentProviderTipoJSON, Constants.TIPO_PAYMENT_STRIPE_1);
				}
					
				ite.setInfoPasseggero(infoDatiPasseggero.toString());
				ricercaTransfertDao.saveRicercaTransfert( ite );
			}
			System.out.println(conta);
			*/
			
			
			//autoveicoloDao.Result_AgendaAutista_TEST(ricTransfert.getDataOraPrelevamentoDate(), ricTransfert.getDurataConTrafficoValue());

			/*
			List<AgendaAutista_Autista> aa = autoveicoloDao.Result_AgendaAutista(15l, ricTransfert.getNumeroPasseggeri(), ricTransfert.getDataOraPrelevamentoDate(), ricTransfert.getDistanzaValue(), 
					ricTransfert.getLat_Partenza(), ricTransfert.getLng_Partenza(), ricTransfert.getLat_Arrivo(), ricTransfert.getLng_Arrivo());
			if(ricTransfert.isRitorno()) {
				System.out.println("--------------------------------------");
				//autoveicoloDao.Result_AgendaAutista(15l, ricTransfert.getNumeroPasseggeri(), ricTransfert.getDataOraRitornoDate(), ricTransfert.getDistanzaValueRitorno(), 
					//	ricTransfert.getLat_Arrivo(), ricTransfert.getLng_Arrivo(), ricTransfert.getLat_Partenza(), ricTransfert.getLng_Partenza() );
			}
			*/
			
			
			
			
			
			
			
			
			//List<RicercaTransfert> aa = ricercaTransfertDao.getCorseClienteAgendaAutista(1114l, null);
			//List<RicercaTransfert> aa = ricercaTransfertDao.getCorseAutistaAgendaAutista(-3l);
			
			

			
			
			/*
			String CredenzialiPayPal = GestioneApplicazioneUtil.CredenzialiRESTPayPal();

			PayPalEnvironment environment = new PayPalEnvironment.Sandbox(CredenzialiPayPal.split("#")[0], CredenzialiPayPal.split("#")[1]);
			PayPalHttpClient client = new PayPalHttpClient(environment);
			
			InfoPaymentProvider info = ricTransfert.getProviderPagamentoInfo();
			System.out.println(info);

			String aaa = RimborsiUtil.EseguiRimborso(new BigDecimal(2), ricTransfert, null);
			System.out.println(aaa);
			
			info = ricTransfert.getProviderPagamentoInfo();
			System.out.println(info);
			*/

			//System.out.println("CredenzialiPayPal: "+CredenzialiPayPal);
			//APIContext apiContext = new APIContext(CredenzialiPayPal.split("#")[0], CredenzialiPayPal.split("#")[1], CredenzialiPayPal.split("#")[2]);
			//Capture capture = Capture.get(apiContext, "7RH14535U29775711");
			

			/*
			
			String CODICE_PAYPAL = "94L33358VU509130W";
			RimborsiUtil.Retrive_Amount_Rimborso_NomeCliente(ricTransfert);
			OrdersGetRequest request = new OrdersGetRequest( CODICE_PAYPAL );
			com.paypal.http.HttpResponse<Order> response = client.execute(request);
			//response.result().purchaseUnits().get(0).
			//4. Save the transaction in your database. Implement logic to save transaction to your database for future reference.
		    System.out.println("Full response body:");
		    System.out.println(new JSONObject(new Json().serialize(response.result())).toString(4));
			*/
			
			/* // NON FUNZIONAAAAAAAAAAAA
			CapturesRefundRequest captRefRequest = new CapturesRefundRequest("7RH14535U29775711");
			RefundRequest refundRequest = new RefundRequest();
			Money money = new Money();
		    money.currencyCode("USD");
		    money.value("1.00");
		    refundRequest.amount(money);
		    captRefRequest.prefer("return=representation");
		    captRefRequest.requestBody(refundRequest);
		    com.paypal.http.HttpResponse<com.paypal.payments.Refund> response = client.execute(captRefRequest);
		    System.out.println("Status Code: " + response.statusCode());
		    System.out.println("Status: " + response.result().status());
		    System.out.println("Refund Id: " + response.result().id());
		    System.out.println("Links: ");
		    for (LinkDescription link : response.result().links()) {
		        System.out.println("\t" + link.rel() + ": " + link.href() + "\tCall Type: " + link.method());
		      }
		    System.out.println("Full response body:");
		    System.out.println(new JSONObject(new Json().serialize(response.result())).toString(4));    
		    */
		    
		    
		    /*
		    APIContext apiContext = new APIContext(CredenzialiPayPal.split("#")[0], CredenzialiPayPal.split("#")[1], CredenzialiPayPal.split("#")[2]) ;
 			String captureId = "7RH14535U29775711";
 			Capture capture = Capture.get(apiContext, captureId);
 			com.paypal.api.payments.RefundRequest refund = new com.paypal.api.payments.RefundRequest();
 			Amount amount = new Amount();
 			amount.setCurrency("USD").setTotal("0.52");
 			refund.setAmount(amount);
 			DetailedRefund responseRefund = capture.refund(apiContext, refund); //refund(apiContext, refund);
		    System.out.println(responseRefund);
		    */
		    
			//System.out.println( info.getRefund() );
			
			
			// 13101l //è il 19 febbrao (Tariffario)
			// 13102l //è il 20 febbrao (Modello Tariffario)
			
			// 13108l // Ritorno
			
			//HomeUtil_Sms_Email.Security_InviaEmailCorsaAcquistata_Cliente(null, ricTransfert, null);
			
			//InvioSms.Crea_SMS_Gateway(ricTransfert, ApplicationMessagesUtil.DammiMessageSource("cellulare.matteo", null), "wewew ciaoooo", Invio_Email_Sms_UTIL.TIPO_MESSAGGIO_SMS_AVVISO_MATTEO_CORSA_VENDUTA);
			
			/*
			autoveicoloDao.Result_AgendaAutista(15l, ricTransfert.getNumeroPasseggeri(), ricTransfert.getDataOraPrelevamentoDate(), ricTransfert.getDistanzaValue(), 
					ricTransfert.getLat_Partenza(), ricTransfert.getLng_Partenza(), ricTransfert.getLat_Arrivo(), ricTransfert.getLng_Arrivo());
			if(ricTransfert.isRitorno()) {
				System.out.println("--------------------------------------");
				autoveicoloDao.Result_AgendaAutista(15l, ricTransfert.getNumeroPasseggeri(), ricTransfert.getDataOraRitornoDate(), ricTransfert.getDistanzaValueRitorno(), 
						ricTransfert.getLat_Arrivo(), ricTransfert.getLng_Arrivo(), ricTransfert.getLat_Partenza(), ricTransfert.getLng_Partenza() );
			}
			*/
			
			//agA_AutoveicoloModelliTariffariDao.EliminaModelliTariffari(1l);
			
			/*
			AgA_AutoveicoloModelliTariffari AutoModTariff = agA_AutoveicoloModelliTariffariDao.get(1l);
			for(int ite = 1; ite <= 628; ite++) {
				AgA_ModelliTariffari modTariff = new AgA_ModelliTariffari(ite, true, new BigDecimal(33), 33, AutoModTariff);
				agA_ModelliTariffariDao.saveAgA_ModelliTariffari(modTariff);
			}
			AgA_AutoveicoloModelliGiornate AutoModGiorn = agA_AutoveicoloModelliGiornateDao.get(1l);
			for(int ite = 0; ite <= 23; ite++) {
				AgA_ModelliGiornate modGiorn = new AgA_ModelliGiornate(ite, true, AutoModTariff, AutoModGiorn);
				agA_ModelliGiornateDao.saveAgA_ModelliGiornate(modGiorn);
			}
			*/
			
			
			/*
			Calendario_FrontEnd calendario_FrontEnd = AgA_Calendario.DammiMeseCalendario(meseAnno);
			List<Object> listObject = agA_GiornateDao.ListaTariffariCalendario_ListaModelliTariffari(calendario_FrontEnd, idAutoveicolo);
			TabellaCalendario TabellaCalendario = AgA_Giornata.Dammi_TabellaCalendario(calendario_FrontEnd, 
					(List<TabellaGiornataTariffario>) listObject.get(0), (List<TabellaModelloTariffario>) listObject.get(1) );
			List<TabellaModelloGiornata> tabellaModelloGiornataList = agA_GiornateDao.ListaModelliGiornata_OrariGiornataIdModelliTariffari(idAutoveicolo);
			AgA_Calendario.InserisciGiornate_in_MeseCalendario(calendario_FrontEnd, TabellaCalendario, tabellaModelloGiornataList);
			*/
			
			
			//System.out.println( AgA_Calendario.DammiPrimeTreLettere("Ciaoooo") );;
			
			/*
			int ore = 75;
			String aa = new DecimalFormat("#.#").format(ore/24);
			System.out.println( round(ore/24) );
			*/
			
			//EmailMarketing_Autisti_Comunicazioni.inviaEmailMarketing_Autisti(null, null, 0, Constants.VM_COMUNICAZIONE_AUTISTI_AGENDA_AUTISTA);
			
			//agA_GiornateDao.DisponbileVendita(2l);
			
			//AgA_General.GetValues_AutoClearProssimeOreGiornate(null);
			
			/*
			Calendario_FrontEnd tabellaMeseCalendario = AgA_Calendario.DammiGiornoCalendario(giorno);
			List<AgA_Giornate> agA_GiornateList = agA_GiornateDao.ListaGiornata_DisponbileVendita_Mese(tabellaMeseCalendario, idAutoveicolo);
			tabellaMeseCalendario = AgA_Calendario.InserisciGiornate_in_MeseCalendario(tabellaMeseCalendario, agA_GiornateList);
			
			JSONObject mainObj = new JSONObject();
			
			mainObj.put(AgA_General.JN_meseAnno, tabellaMeseCalendario.getMeseAnno());
			mainObj.put(AgA_General.JN_Calendario_NomeMese_Anno, tabellaMeseCalendario.getNomeMese_Anno());
			JSONArray jArray = new JSONArray();
    		for(GiorniMeseCalendario ite: tabellaMeseCalendario.getGiorniMeseCalendarioList()) {
    			JSONObject json = new JSONObject();
    			json.put(AgA_General.JN_Calendario_NumeroGiorno, ite.getNumeroGiorno());
    			json.put(AgA_General.JN_Calendario_GiornoColore, ite.getColore());
    			json.put(AgA_General.JN_giorno, DateUtil.FormatoData_1.format(ite.getGiorno()));
    			jArray.put(json);
    		}
    		mainObj.put(AgA_General.JN_Calendario_GiorniMeseCalendario, jArray);
			System.out.println(mainObj.toString());
			*/
			
			
			//agA_GiornateDao.Menu_Data(idAutoveicolo);
			
			/*
			Calendario_FrontEnd tabellaMeseCalendario = AgA_Calendario.DammiMeseCalendario(meseAnno);
			List<AgA_Giornate> agA_GiornateList = agA_GiornateDao.ListaGiornata_DisponbileVendita_Mese(tabellaMeseCalendario, idAutoveicolo);
			tabellaMeseCalendario = AgA_Calendario.InserisciGiornate_in_MeseCalendario(tabellaMeseCalendario, agA_GiornateList);
			*/
			
		    //int delta = -now.get(GregorianCalendar.DAY_OF_WEEK) + 2; //add 2 if your week start on monday
		    //now.add(Calendar.DAY_OF_MONTH, delta );
		    
		    /*
			Calendar start = Calendar.getInstance();
			start.set(Calendar.MONTH, month - 1);  // month is 0 based on calendar
			start.set(Calendar.YEAR, year);
			start.set(Calendar.DAY_OF_MONTH, 1);
			start.getTime();   // to avoid problems getTime make set changes apply
			start.set(Calendar.DAY_OF_WEEK, SUNDAY);
			if (start.get(Calendar.MONTH) <= (month - 1))  // check if sunday is in same month!
			    start.add(Calendar.DATE, -7);
			*/

			/*
			List<AgA_AutoveicoloModelliGiornate> tabellaModelloGiornataList_ExistsTariffari = 
					agA_AutoveicoloModelliGiornateDao.AutoveicoloModelliGiornate_ExistsTariffari_by_IdAutoveicolo(2l);
			for(AgA_AutoveicoloModelliGiornate ite : tabellaModelloGiornataList_ExistsTariffari) {
				System.out.println("aaa: "+ ite.getId()+" | "+ ite.getNomeGiornata() );
			}
			*/
			
			/*
			
			String giorno = "14/01/2020";
			List<Object> listObject = agA_GiornateDao.ListaTariffariGiornata_ListaModelliTariffari(AgA_Giornata.convertiDataString_Date_Giorno(giorno), idAutoveicolo);
			List<TabellaGiornata> tabellaGiornataList = 
					AgA_Giornata.Dammi_TabellaGiornata((List<TabellaGiornataTariffario>) listObject.get(0), (List<TabellaModelloTariffario>) listObject.get(1));
			List<TabellaModelloGiornata> tabellaModelloGiornataList = agA_GiornateDao.ListaModelliGiornata_OrariGiornataIdModelliTariffari(idAutoveicolo);
			List<AgA_AutoveicoloModelliGiornate> agA_AutoveicoloModelliGiornateList = 
					agA_AutoveicoloModelliGiornateDao.AutoveicoloModelliGiornate_ExistsTariffari_by_IdAutoveicolo(idAutoveicolo);
			AgA_Calendario.Dammi_MenuCalendario_Giornata(giorno, tabellaGiornataList, tabellaModelloGiornataList, agA_AutoveicoloModelliGiornateList);
			 */
			
			//String sDate1="03/01/2020";  
		    //Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate1); 
			//int aa =  agA_GiornateDao.Elimina_Giornata_e_ListaTariffari(date1, 1l);
			//System.out.println(aa);
		    //agA_GiornateDao.ListaTariffariGiornataModello_by_IdModelloGiornata(date1, 1l, 1l);
			
			/*
			AgA_Giornate giornata = agA_GiornateDao.get(1l);
			List<AgA_Tariffari> tariffariList = agA_TariffariDao.getAgA_Tariffari_by_idGiornata( giornata.getId() );
			boolean esito = AgA_Tariffario.Controlla_SePresente_AlmenoUn_EseguiCorseTrue(tariffariList);
			System.out.println(esito);
			if( esito == false) {
				giornata.setAttivo(false);
				giornata = agA_GiornateDao.saveAgA_Giornate(giornata);
			}
			*/
			
			
			
			/*
			List<Object> listObject = agA_GiornateDao.ListaTariffariGiornata(AgA_Giornata.convertiDataString_Date_Giorno("01/01/2020"), 1l);
			AgA_Giornata.Dammi_TabellaGiornata((List<TabellaGiornataTariffario>) listObject.get(0), (List<TabellaModelloTariffario>) listObject.get(1));
			System.out.println( ":::::::::::::::::::::::::::::::::" );
			
			Integer orario = 14;
			List<Object> listObject2 = agA_GiornateDao.ListaTariffariGiornataOrario(AgA_Giornata.convertiDataString_Date_Giorno("01/01/2020", orario.toString()), 1l);	
			AgA_Giornata.Dammi_MenuOrarioGiornata(orario, (List<TabellaGiornataTariffario>) listObject2.get(0), (List<TabellaModelloTariffario>) listObject2.get(1));
			*/
			

			/*
			List<TabellaTariffarioAutista> kmTariff_list = AgA_TabellaTariffarioAutista.Dammi_TabellaTariffarioAutista();
    		List<AgA_ModelliTariffari> modelTarifList = agA_ModelliTariffariDao.getAgA_ModelliTariffari_by_idAutoveicoloModelTariff(1l);
    		
    		
    		for(AgA_ModelliTariffari ite_modelTarif: modelTarifList) {
    			System.out.println("KM: "+ite_modelTarif.getKmCorsa()+" | ESEGUI_CORSE: "+ite_modelTarif.isEseguiCorse()
    			+" | PREZZO: "+ite_modelTarif.getPrezzoCorsa()+" | RAGGIO: "+ite_modelTarif.getKmRaggioArea());
    		}
    		System.out.println("==================================================");
    		
    		
    		for(TabellaTariffarioAutista ite_AAA: kmTariff_list) {
    			double RangeKilometri = ite_AAA.getToKm() - ite_AAA.getFromKm() + 1;
    			int NumeroElementi = 0;
    			double TotEseguiCorse = 0;
    			BigDecimal TotEuro = BigDecimal.ZERO;
    			double TotRaggio = 0d;
    			
    			for(AgA_ModelliTariffari ite_modelTarif: modelTarifList) {
        			if( ite_AAA.getFromKm() == 193 ) {
        				System.out.println(193);
        			}
    				if( ite_modelTarif.getKmCorsa() >= ite_AAA.getFromKm() 
    						&& ite_modelTarif.getKmCorsa() <= ite_AAA.getToKm() ) {
    					TotEseguiCorse = TotEseguiCorse + ( ite_modelTarif.isEseguiCorse() ? 1 : 0 );
    					TotEuro = TotEuro.add( ite_modelTarif.getPrezzoCorsa() );
    					TotRaggio = TotRaggio + ite_modelTarif.getKmRaggioArea();
    					NumeroElementi++;
    				}
    			}
				if(NumeroElementi > 0) {
	   				if(TotEseguiCorse > RangeKilometri / 2) {
	   					ite_AAA.setEseguiCorse( true );
    				}else {
    					ite_AAA.setEseguiCorse( false );
    				}
    				ite_AAA.setEuro(TotEuro.divide(new BigDecimal(NumeroElementi)).toString());
    				ite_AAA.setRaggio( TotRaggio / NumeroElementi );
				}
    		}

    		
    		for(TabellaTariffarioAutista ite_AAA: kmTariff_list) {
    			System.out.println("FROM: "+ite_AAA.getFromKm()+" | TO:"+ite_AAA.getToKm()
    			+" | ESEGUI_CORSE: "+ite_AAA.getEseguiCorse()+" | PREZZO: "+ite_AAA.getEuro()+" | RAGGIO: "+ite_AAA.getRaggio());
    			
    		}
			*/
			
			//agA_ModelliTariffariDao.updateKilometriCorsa(24l, 1, 3);
			
			/*
			System.out.println("size: "+IntevalliKilometri_Map.size());
			for(int aa = 0; aa <= IntevalliKilometri_Map.size() - 1; aa++) {
				

				Integer key = IntevalliKilometri_Map.get(IntevalliKilometri_Map.keySet().toArray()[aa]);
				System.out.println(key);
				
			}
			*/
			
			//AgendaAutista_Km_Tariffario.Dammi_Km_Tariffario();
			
			
			// {"ricTransfert_IpAddress":"0:0:0:0:0:0:0:1","ricTransfert_Email":"matteo.manili@gmail.com","ricTransfert_Token":"1059259in2YcZBJksWdT","richiestaAutistaMultiplo_Id":[9520,9521],"ricTransfert_IdUser":-2,"ricTransfert_Nome":"Matteo","ricTransfert_Cognome":"Manili","richiestaPreventivi_Inviata":true}
			// {"ricTransfert_IpAddress":"0:0:0:0:0:0:0:1","ricTransfert_Token":"1059259in2YcZBJksWdT","richiestaAutistaMultiplo_Id":[9520,9521],"ricTransfert_IdUser":-2,"ricTransfert_Nome":"Matteo","ricTransfert_Cognome":"Manili","richiestaPreventivi_Inviata":true}
			//RicercaTransfert ricTransfert = ricercaTransfertDao.get( 10676l );
			
			
			/*
			Locale loc = new Locale("IT", "IT");
			loc.getCountry();
			System.out.println(loc.getDisplayCountry());
			*/
			
			/*
			Stripe.apiKey = Constants.STRIPE_SECRET_KEY_TEST; // LIVE
			
			PaymentIntent intent = PaymentIntent.retrieve( "pi_1ElH0wCLt3TKIzHhinkgSFbs");
			List<Charge> charges = intent.getCharges().getData();
			String id_Charge = charges.get(0).getId();
			
			
			Charge charge = Charge.retrieve("ch_1ElH1YCLt3TKIzHh8K4Itw2K");
			BalanceTransaction balanceTransaction = BalanceTransaction.retrieve( charge.getBalanceTransaction() );
			JSONObject json = charge.getSource() != null ? new JSONObject(charge.getSource()) : new JSONObject() ;
			InfoPaymentProvider infoPay = new InfoPaymentProvider();
		    infoPay.setNomeCliente(  json.has("name")  ? json.getString("name") : null );
			Double amount = (double) charge.getAmount() / 100;
			infoPay.setAmount(new BigDecimal(amount).setScale(2, RoundingMode.HALF_EVEN));
			Double fee = (double) balanceTransaction.getFee() / 100;
			infoPay.setFee(new BigDecimal(fee).setScale(2, RoundingMode.HALF_EVEN));
			Double refund = (double) charge.getAmountRefunded() / 100;
			infoPay.setRefund(new BigDecimal(refund).setScale(2, RoundingMode.HALF_EVEN));
			*/
			
			// PaymentIntent intent = PaymentIntent.retrieve( ricTransfert.getIdPaymentProvider() /*"pi_Aabcxyz01aDfoo"*/);
			/*
			List<Charge> charges = intent.getCharges().getData();
			String aa = intent.getCharges().getData().get(0).getId();
			*/
			
			
			
			/*
			//String aa = new JSONObject(ricTransfert.getInfoPasseggero()).remove(Constants.RicTransfert_Email
			JSONObject testObject = new JSONObject( ricTransfert.getInfoPasseggero() );
            testObject.remove(Constants.RicTransfert_Email);
			System.out.println(testObject.toString());
			*/
			
			/*
			Supplementi aa = supplementiDao.getSupplementoBy_IdRicercaTransfert(10562l);
			System.out.println(aa.getId());
			
			List<Supplementi> bb = supplementiDao.getSupplementi();
			System.out.println(bb.size());
			*/
			
			/*
			List<RichiestaAutistaParticolare> aa = ricercaTransfertDao.getCorseAutistaRichiestaAutistaMultiplo(-3l, null);
			System.out.println("aa: "+aa.size());
			for(RichiestaAutistaParticolare ite: aa) {
				System.out.println("RIC ite: "+ite.getRicercaTransfert().getId() + " PART ite: "+ite.getId() +" AUTO: "+ite.getAutoveicolo().getId());
			}
			*/
			
			//Fatturazione.Informazioni_FatturaCorsa(10274l);
			//InviaEmail.AllegatoFatturaCorsaCliente(ricTransfert, null);
			
			/*
			List<RichiestaAutistaParticolare> ListaDaOrdinare = new ArrayList<RichiestaAutistaParticolare>();
			ListaDaOrdinare.addAll(ricTransfert.getRichiestaAutistaParticolare());
			List<Object[]> ObjectList = richiestaAutistaParticolareDao.InvioSmsRicevuti_Autisti(ricTransfert.getId());
			for(RichiestaAutistaParticolare ite: ListaDaOrdinare) {
				for(Object[] ite_object: ObjectList){
					BigInteger elemt_0 = (BigInteger) ite_object[0];
					BigInteger elemt_1 = (BigInteger) ite_object[1];
					if(ite.getId() == elemt_0.longValue() ) {
						ite.setTotaleSmsRicevutiAutista( elemt_1.intValue() );
						System.out.println(elemt_0+" | "+elemt_1);
					}
				}
			}
			
			Collections.sort( ListaDaOrdinare, new Comparator<RichiestaAutistaParticolare>() {

		        public int compare(RichiestaAutistaParticolare a, RichiestaAutistaParticolare b) {
		        	int c;
		        	Long ValClasseAutoA = a.getAutoveicolo().getClasseAutoveicoloReale().getId();
		        	Long ValClasseAutoB = b.getAutoveicolo().getClasseAutoveicoloReale().getId();
	                c = ValClasseAutoA.compareTo(ValClasseAutoB);
	                if (c == 0){
	                	Integer ValTotaleSmsRicevutiAutistaA = a.getTotaleSmsRicevutiAutista();
			        	Integer ValTotaleSmsRicevutiAutistaB = b.getTotaleSmsRicevutiAutista();
		                c = ValTotaleSmsRicevutiAutistaA.compareTo(ValTotaleSmsRicevutiAutistaB);
		                if (c == 0){
		                	Integer ValNumeroCorseEseguiteA = a.getAutoveicolo().getAutista().getNumCorseEseguite();
				        	Integer ValNumeroCorseEseguiteB = b.getAutoveicolo().getAutista().getNumCorseEseguite();
			                c = ValNumeroCorseEseguiteA.compareTo(ValNumeroCorseEseguiteB);
			                if (c == 0){
				                Date valDataRegistrazioneA = a.getAutoveicolo().getAutista().getUser().getSignupDate();
			                	Date valDateRegistrazioneB = b.getAutoveicolo().getAutista().getUser().getSignupDate();
				                c = valDataRegistrazioneA.compareTo(valDateRegistrazioneB);
			                }
		                }
	                }
	                return c;
		        }
		    });
			int conta = 0; final int NumeroMassimoInvioSmsRichiestaPreventivi = 15; 
			for(RichiestaAutistaParticolare ite: ListaDaOrdinare) {
				System.out.println("classeautoveicolo: "+ite.getClasseAutoveicoloScelta()  +" TOTALE: "+ite.getTotaleSmsRicevutiAutista()); 
				
			}
			
			*/
		
			
			/*
			List<Object[]> ObjectList = ricercaTransfertDao.report_4(); 
			
			for(Object[] ite_object: ObjectList){
			//for(Object ite_object: ObjectList){
			//System.out.println(ite_object);
			
			Object elemt_0 = (Object) ite_object[0];
			Object elemt_1 = (Object) ite_object[1];
			Object elemt_2 = (Object) ite_object[2];
			//Object elemt_3 = (Object) ite_object[3];
			System.out.println(elemt_0+" | "+elemt_1+" | "+elemt_2+" | ");
			}
			*/
			
			/*
			Locale locale = new Locale("it", "IT");
			List<RicercaTransfert> listRicTransfert = ricercaTransfertDao.getRicercaTransfert();
			for(RicercaTransfert ite: listRicTransfert  ) {
				RicercaTransfert_GoogleMaps_Info PARTENZA = null; 
				PARTENZA = GMaps_Api.GoogleMaps_PlaceTextSearch(ite.getPartenzaRequest(), locale.getLanguage());
				RicercaTransfert_GoogleMaps_Info ARRIVO = null; 
				ARRIVO = GMaps_Api.GoogleMaps_PlaceTextSearch(ite.getArrivoRequest(), locale.getLanguage());
				System.out.println("TRANS ID: "+ite.getId() +" | PARTENZA: "+ HomeUtil.CercaComuneGoogleMaps(PARTENZA, locale) +" | ARRIVO: "+ HomeUtil.CercaComuneGoogleMaps(ARRIVO, locale) ); ;
			}
			*/
			
			
			/*
			Date currentDate = new Date();

			Calendar aaa = Calendar.getInstance();
			aaa.setTime(currentDate);
			aaa.add(Calendar.DAY_OF_MONTH, 1);
			System.out.println("aaa: "+ aaa.getTime() );
			
			Calendar bbb = Calendar.getInstance();
			bbb.setTime(currentDate);
			bbb.add(Calendar.DATE, 1);
			System.out.println("bbb: "+ bbb.getTime() );
			
			System.out.println(Calendar.HOUR_OF_DAY);
			System.out.println(Calendar.HOUR_OF_DAY)ò;
			System.out.println(Calendar.DAY_OF_MONTH);
			System.out.println(Calendar.DAY_OF_MONTH);:_ 
			System.out.println(Calendar.MONTH);
			*/
			
			
			/*
			RicercaTransfert ric = ricercaTransfertDao.get( 9934l );
			
			System.out.println( ric.getDataRicerca() );
			System.out.println( ric.getPhoneNumberCustomer() );
			System.out.println( ric.getRicTransfert_Email() );
			System.out.println( ric.getRicTransfert_IdUser() );
			
			ricercaTransfertDao.CheckRichiestaPreventivi_MAX3invii_IN24Ore(ric.getDataRicerca(), ric.getPhoneNumberCustomer(), 
					ric.getRicTransfert_Email(), ric.getRicTransfert_IdUser());
			*/
			
			
			//ricercaTransfertDao.Recencioni_Approvate(false);
			//EmailMarketing_ClientiRecencioneSconto.inviaEmailMarketing_ClientiRecencioneSconto(null, null, 0l );
			
			/*
			List<Object[]> ObjectList = ricercaTransfertDao.getTransferAcquistati_Approvati_Distinct_User();
			System.out.println(ObjectList.size());
			for(Object ite_object: ObjectList){
				User user_ite = (User) ite_object;
				System.out.println( user_ite.getFullName() );
				User user = (User) userDao.loadUserByUsername( user_ite.getUsername() );
				System.out.println("IDUSER: "+user.getId());
				System.out.println(user.getEmail() + user.getFullName()+" | https://localhost:8443/apollon/scrivi-scrivi-recensione?"+RecensioneTransferUtil.URL_PAGE_TOKEN_RECENSIONE+"="+user.getRecensioneTransfer().getUrlTockenPageScriviRecensone() 
						+" | sconto:"+ user.getRecensioneTransfer().getPercentualeSconto()+"%" );
				List<RicercaTransfert> listaTransfr = user.getRecensioneTransfer().getRicercaTransfertList_Approvati();
				for(RicercaTransfert ite_ric: listaTransfr) {
					System.out.println("ID: "+ ite_ric.getId()+ ((ite_ric.isRitorno())?" Andata e Ritorno":" Solo Andata") +" giorno: "+ite_ric.getDataOraPrelevamentoDate() + " Partenza: "+ite_ric.getPartenzaRequest() 
						+" --> "+ite_ric.getArrivoRequest());
				}
				//Invio_Email_Sms_UTIL.Crea_ListaInvioEmailSms_EMAIL_ScriviRecensioneTransfer(listaTransfr.get(0), null);
				System.out.println("-------------------------------------------------------------------");
			}
			*/
			
			
			/*
			List<RicercaTransfert> ricTra = ricercaTransfertDao.getTransferAcquistati_User(0);
			for(RicercaTransfert ite: ricTra) {
				System.out.println(
						ite.getId() +" | "+ ite.getApprovazioneAndata()  +" | "+ ite.getApprovazioneRitorno() 
						+" | "+ ite.getUser().getId() +" | "+ ite .getUser().getFullName()  
						+" | "+ ite.getPartenzaRequest() +" | "+ ite.getArrivoRequest() 
						
						 );
			}
			*/
			
			
			
			/*
			// +61 0406 127 695
			// +39 3289126869
			// +61 406127695
			String telefono = "+61 406127695";
			System.out.println( Telefono_Prefisso_e_Formato.NumeroTelefonicoValido(telefono) );
			*/
			
			/*
			RicercaTransfert ric = ricercaTransfertDao.get(9365l);
			//InvioSms_UTIL.CreaListaAvvisiCorsa_Email_SMS_Cliente_Autista(ric, null);
			RichiestaMediaAutista richMediaAutista = ricercaTransfertDao.getRichiestaMediaAutista_AutistaAssegnatoCorsaMedia(ric.getId());
			System.out.println( richMediaAutista.getTokenAutista() );
			richMediaAutista.getTokenAutista();
			*/
			
			
			/*
			List<Object[]> ObjectList = ricercaTransfertDao.OrdinaPerRecencioniNonApprovate(null);
			
			for(Object[] ite_object: ObjectList){
			//for(Object ite_object: ObjectList){
				BigInteger id = (BigInteger) ite_object[0];
				String aaa = (String) ite_object[1];
				String bbb = (String) ite_object[2];
				String ccc = (String) ite_object[3];
				if(ccc != null ) {
					System.out.println(ccc.getClass());
				}
				
				BigInteger idUser = (BigInteger) ite_object[4];
				//String bbb = (String) ite_object;
				//Object ccc = (Object) ite_object;
				//RicercaTransfert ric_ite = (RicercaTransfert) ite[1];
				System.out.println( id +" "+aaa+" "+bbb+" "+ccc+" "+idUser);
			}
			*/

			
			//ricercaTransfertDao.RecensioneApprovata_CodiceScontoAttivato_User( 384l );
			
			//System.out.println( ricercaTransfertDao.RecensioneApprovata_User( 384l ) );
			

			/*
			for(Object[] ite: AutoveicoliList){
				User user_ite = (User) ite[0];
				RicercaTransfert ric_ite = (RicercaTransfert) ite[1];
				System.out.println( user_ite.getFullName() +" | "+ ric_ite.getId() +" | "+ ric_ite.getApprovazioneAndata()  );
			}
			*/
			
			
			
			
			

			/*
			String textSms = "Hola amigo 1"; String destinatario = "+393289126869";
			FirebaseCloudMessaging.sendCommonMessageSms("Hola amigo 1", "+393289126869", null);
			/*
			
			/*
			ListaInvioSms Sms = InvioSms_UTIL.DammiNuovoSms(textSms, destinatario);
			listaInvioSmsDao.saveListaInvioSms( Sms );
			*/
			
			
			/*
			InvioSms.Invio_SMS(destinatario, testoSms,
			(ApplicationUtils.CheckDomainTranfertClienteVenditore(ritardo.getRicercaTransfert())) ? Constants.SMS_TITLE_NCCTRANSFERONLINE : Constants.SMS_TITLE_APOLLOTRANSFERT, 
					InvioSms.SMS_LENTO);
			*/
			

			
			
			//---------------
			
			/*
			List<AgenzieViaggioBit> agenzieViaggioBitList = agenzieViaggioBitDao.getAgenzie_CodiceScontoUsato();
			for(AgenzieViaggioBit ite: agenzieViaggioBitList) {
				System.out.println(ite.getId()+" | "+ite.getEmail()+" | "+ite.getNome()+" | "+ite.getSitoWeb()+" | "+ite.getCitta_e_indirizzo());
			}
			*/
			
			
			/*
			List<AgenzieViaggioBit> agenzieViaggioBitList = agenzieViaggioBitDao.getAgenzieViaggioBy_LIKE("Splendid");
			for(AgenzieViaggioBit ite: agenzieViaggioBitList) {
				System.out.println(ite.getId()+" | "+ite.getEmail()+" | "+ite.getNome()+" | "+ite.getSitoWeb()+" | "+ite.getCitta_e_indirizzo());
			}
			*/
			
			
			//AgenzieViaggioBit aa = agenzieViaggioBitDao.getEmailAgenzieViaggioBit("filippo.jatta@jfactor.it");
			//System.out.println(aa.getId() +" | EMAIL: "+aa.getEmail());
			//agenzieViaggioBitDao.remove(aa);
			
			/*
			List<AgenzieViaggioBit> agenzieViaggioBitList = agenzieViaggioBitDao.getAgenzieViaggioBit_DESC();
			for(AgenzieViaggioBit ite: agenzieViaggioBitList) {
				ite.setNumeroEmailInviate(0);
				agenzieViaggioBitDao.saveAgenzieViaggioBit(ite);
			}
			*/
			
			/*
			List<AgenzieViaggioBit> agenzieViaggioBitList = agenzieViaggioBitDao.getAgenzieViaggioBit_DESC();
			for(AgenzieViaggioBit ite: agenzieViaggioBitList) {
				if( StringUtils.containsIgnoreCase(ite.getEmail(), "gmail") ) {
					String risultato = "";
					if(ite.getNome() != null && !ite.getNome().equals("") && ite.getNome().length() <= 60 
							&& !StringUtils.containsIgnoreCase(ite.getNome(), "home") && !StringUtils.containsIgnoreCase(ite.getNome(), "news") && !StringUtils.containsIgnoreCase(ite.getNome(), "Chi Siamo")  ) {
						risultato = ite.getNome();
					}else if(ite.getSitoWeb() != null && !ite.getSitoWeb().equals("")){
						try {
							URL url = new URL( ite.getSitoWeb() );
							String host = url.getHost();
							risultato = host;
						}catch(java.net.MalformedURLException aa) {
							risultato = ite.getEmail();
						}
	
					}else if( ite.getNome() != null && !ite.getNome().equals("") ){
						risultato = ite.getNome();
	
					}else {
						risultato = ite.getEmail();
					}
					
					System.out.println("ID: "+ite.getId() +" | NOME: "+risultato +" | NOME OLD: "+ite.getNome() +" | SITOWEB: "+ite.getSitoWeb() +" | EMAIL: "+ite.getEmail());
				}else {
					System.out.println("--------------------------------------");

				}
			}
			*/
			
			//--------------------------------------------------------

			/*
			List<AutistaZone> listAutistaZone = new ArrayList<AutistaZone>();
			
			listAutistaZone = autistaDao.getAutistiNordItalia();
			for(AutistaZone ite: listAutistaZone){
				
				if(ite.getAutista().isAttivo() && ! ite.getAutista().isBannato()  ) {
					System.out.print(ite.getAutista().getUser().getEmail() + "; "); 
				}
			}
			*/

			//RicercaTransfert ricTransfert = ricercaTransfertDao.get(7425l);
			//InviaEmail.InviaEmailCorsaVendutaVenditore(ricTransfert, null, null);
			//HomeUtil_Sms_Email.Invia_Email_CorsaAcquistataCliente(null, ricTransfert, null);
			//Ritardi ritardo = ritardiDao.getRitardoBy_IdRicercaTransfert(ricTransfert.getId());
			//InviaEmail.Invia_Email_SollcecitoRitardoCliente(ritardo, null, null);
			
			// ------------------------
			
			//GestioneApplicazioneUtil.inviaEmailMarketing_AgenzieViaggi_FieraMilano(null, null, 0 );
			
			//HomeUtil.Calcolo_e_Salvataggio_Ricerca(ricercaTransfert);
			//InviaEmailMarketing.inviaEmailMarketingAutisti_InfoMancanti_e_AutistaInfo();
			//CatturaTripAdvisor.Start("VA", 1);

			//DURATA 7-8 ore (3, null);
			//CatturaTripAdvisor.StartScrapingMultiploProvinceDisponibili(3, null);
			//CatturaTripAdvisor.Start("MI", null);
			
			// MILANO TARIFFA BASE: 4.00 (DEVE DINVETAE 4.10)
			// ROMA TARIFFA BASE 3.60 (DEVE DINVETAE 3.70) (ATTENZIONE CHE GIA LA HO ALZATA, POI LA DE ABBASSARE MANUALMENTE)
			// BISOGNA AGGIUNGERE 0.10 A TUTTE LE PROVINCE
			
			/*
			List<Province> porvList = provinceDao.getProvince();
			for(Province ite: porvList){
				ite.setPercentualeServizio(8);
				BigDecimal tarBase = ite.getTariffaBase().add( new BigDecimal("0.10") );
				System.out.println("ORIGINE: "+ite.getTariffaBase()+" MODIF: "+tarBase );
				ite.setTariffaBase( tarBase );
				provinceDao.saveProvince(ite);
				System.out.println("salvato");
			}
			*/
			
			
			/*
			for(AgenzieViaggioBit ite: agenzieViaggioBitDao.getAgenzieViaggioBit()){
				if(ite.getEmail().contains("jkroma.com")){
					System.out.println(ite.getEmail() +"inviate: "+ite.getNumeroEmailInviate() + ite.isUnsubscribe()+ite.getDataInvioLastEmail());
				}
			}
			*/
			
			
			/*
			String LastPAge = "https://www.tripadvisor.it/Hotels-g187849-oa990-Milan_Lombardy-Hotels.html#BODYCON";
			String currentePage = "https://www.tripadvisor.it/Hotels-g187849-Milan_Lombardy-Hotels.html";
			
			// https://www.tripadvisor.it/Hotels-g187849-oa150-Milan_Lombardy-Hotels.html#BODYCON
			// https://www.tripadvisor.it/Hotels-g187849-oa180-Milan_Lombardy-Hotels.html#BODYCON
			
			String FirstPartUrlHotelList = "https://www.tripadvisor.it/Hotels-";
			String LastPartUrlHotelList = "#BODYCON";
			String PrefixCodiceProvincia = "oa";
			
			String[] parts = currentePage.split( FirstPartUrlHotelList );
			String CodiceProvincia = parts[1].split("-")[0];
			String RestOfUrl = parts[1].split(CodiceProvincia)[1];
			String Aggiunta = "30";;
			
			String PaginaNumero = FirstPartUrlHotelList+CodiceProvincia+"-"+PrefixCodiceProvincia+Aggiunta+RestOfUrl+LastPartUrlHotelList;
			System.out.println(PaginaNumero);
			*/		
			
			//System.out.println( TerritorioAutisti.ProvinceAutistiDisponibili_NomeProvincia() ); 


			/*
			// SETTARE URL ALLE MACRO_REGIONI E ALLE REGIONI
			int conta = 0;
			for(AgenzieViaggioBit ite: agenzieViaggioBitDao.getAgenzieViaggioBit()){
				if(ite.getTelefono() != null && ite.getTelefono().equals("www.federweb.com") && ite.getCodiceStand() == 200l){
					String[] parts = ite.getCitta_e_indirizzo().split(" - ");
					String PROV = UtilString.RimuoviTuttiGliSpazi(parts[parts.length - 2]);
					String NewIndirizzo = ite.getCitta_e_indirizzo().replace(PROV, "("+PROV+")" );
					System.out.println("PROV: "+PROV);
					ite.setCitta_e_indirizzo(NewIndirizzo);
					try{
					agenzieViaggioBitDao.saveAgenzieViaggioBit(ite);
					conta++;
					}catch(ArrayIndexOutOfBoundsException aa){
						System.out.println(aa.getMessage());
					}
				}
			}
			System.out.println(conta);
		*/	
			

			/*
			// ANALISI ORARIO E GIORNI CORSE VENDUTE, PER IMPOSTARE MEGLIO LA CAMPGNA ADWORDS
			List<RicercaTransfert> ricercaTransfertLis = ricercaTransfertDao.getRicercaTransfertSoloRicercheEseguiteCliente_PROVA_TEST(200, 0);
			for(RicercaTransfert ric_ite: ricercaTransfertLis){
				ResultRicerca_Autista_Tariffe res = ric_ite.getResultAutistaTariffePrezzo(); //CalcoloTariffe.CalcoloTariffe_Main(ric_ite);
				int kilometriDistanza = (int)(long)ric_ite.getDistanzaValue()/1000;


				System.out.println(ric_ite.getComune_Partenza()+" -> "+ric_ite.getComune_Arrivo()+" | "+kilometriDistanza +" kilometri");
				//System.out.println(ite.getClasseAutoveicolo().getNome());
				//System.out.println("getMaggiorazioneNotturna: "+ite.getMaggiorazioneNotturna());

				float valore = DammiTarrifaKilometricaFasce(Fasce, kilometriDistanza).getValue();

				System.out.println("getDataRicerca: "+ DateUtil.FORMATO_GIORNO_MESE_ORA_ESTESO(new Locale("IT")).format(ric_ite.getDataRicerca()));
				System.out.println("--------------------------------------------------------");
			}
			*/
			
			
			/*
			Autoveicolo autoveicolo_ite = autoveicoloDao.get(342l);
			ClasseAutoveicolo classeAutov2 = autoveicolo_ite.getClasseAutoveicoloReale();
			//ClasseAutoveicolo classeAutov = AutoveicoloUtil.DammiAutoClasseReale(autoveicolo_ite.getClasseAutoveicoloReale(), autoveicolo_ite.getAnnoImmatricolazione());
			System.out.print(" | "+  classeAutov2.getDescription());
			*/
			
			
			/*
			String result = "";
			while( !result.equals("success") ){
	        	result = "success";
	        	if(result.equals("success")){

	        	}else if( !ApplicationUtils.CheckAmbienteProduzione() ){

	        	}
	        }
			*/
			
			
			/*
			RicercaTransfert ricTransfert = ricercaTransfertDao.get( Long.parseLong("5304") );
			InfoPaymentProvider infoPay = RimborsiUtil.Retrive_Amount_Rimborso_NomeCliente(ricTransfert);
			*/
			
			//LeggiFileExcel.InserisciFrazioni();
			/*
			// INSERISCO I COMUNI NON PRESENTI DALLA TABELLA 
			System.out.println("-------------------------------");
			List<Object> comuniList = comuniDao.getComuni_Altri(); 
			for(Object ite: comuniList){
				Object[] we = (Object[]) ite;
				String NOME_COMUNE = UtilString.ReplaceAccenti(we[0].toString()); 
				String SIGLA_PROVINCIA = we[1].toString();
				String NOME_REGIONE = we[2].toString();
				Comuni newComune = new Comuni();
				newComune.setIsola(false);
				newComune.setNomeComune( NOME_COMUNE );
				Province newProv = provinceDao.getProvinciaBy_SiglaProvincia(SIGLA_PROVINCIA);
				newComune.setProvince( newProv );
				newComune.setRegioni( newProv.getRegioni() );
				System.out.println(NOME_COMUNE +" | "+ SIGLA_PROVINCIA +" | "+ NOME_REGIONE);
				try{
					// controllo che il comune già non esiste
					if( comuniDao.getComuniByNomeComune_Equal(NOME_COMUNE, newProv.getSiglaProvincia()) == null ){
						Comuni SalvatoComune = comuniDao.saveComuni(newComune);
						System.out.println(NOME_COMUNE +" | "+ SIGLA_PROVINCIA +" | "+ NOME_REGIONE);
						System.out.println("comune salvato: "+SalvatoComune.getNomeComune());
					}
				}catch(DataIntegrityViolationException sasa){
					System.out.println(sasa.getMessage());
				}
			}
			*/
			
			
			/*
			// INSERIRE CAP CON UPDATE
			System.out.println("-------------------------------");
			//pulisco i valori catasto
			comuniDao.Update_Clear_Column_Catasto();
			List<Object> comuniListTutti = comuniDao.getComuni_Altri_Tutti(); 
			for(Object ite: comuniListTutti){
				Object[] we = (Object[]) ite;
				String NOME_COMUNE = UtilString.ReplaceAccenti(we[0].toString()); 
				String SIGLA_PROVINCIA = we[1].toString();
				String NOME_REGIONE = we[2].toString();
				String NOME_CAP = we[3].toString();
				String NOME_ISTAT = we[4].toString();
				Comuni comuneUpdate = comuniDao.getComuniByNomeComune_Equal(NOME_COMUNE, SIGLA_PROVINCIA);
				if(comuneUpdate != null){
					comuneUpdate.setCatasto( NOME_CAP +" | "+ NOME_ISTAT );
					Comuni SalvatoComune = comuniDao.saveComuni(comuneUpdate);
					System.out.println("comune salvato: "+SalvatoComune.getNomeComune());
				}
			}
			*/
			
			
			/*
			//VISUALIZZO I COMUNI DUPLICATI
			System.out.println("-------------------------------");
			comuniDao.getComuni_Duplicati();
			*/
			
			
			/*
			List<Object[]> autistiList = autistaDao.getDocumentiAutisti_da_Approvare(999999,0); 
			int conta = 0;
			for(Object[] ite: autistiList){
				Autista autista = (Autista) ite[0];
				Object object = ite[1];
				System.out.println("POSIZ: "+ conta++ + " ID: "+autista.getId() +" "+ autista.getUser().getFullName() +" | VAOLORE: "+ object);
			}
			System.out.println( "Count Autiti AAA: "+autistiList.size() );
			System.out.println( "Count Autiti BBB: "+autistaDao.getCountAutista() );
			*/
			
			/*
			List<Autoveicolo> autoList = autoveicoloDao.getAutovecoloList_like_NomeModello("2");
			for(Autoveicolo auto_ite: autoList){
				System.out.println(auto_ite.getModelloAutoNumeroPosti().getModelloAutoScout().getMarcaAutoScout().getName() +" "+ 
						auto_ite.getModelloAutoNumeroPosti().getModelloAutoScout().getName());
			}
			*/

			/*
			 * TODO RIEMPIRE LA TABELLA ModelloAutoNumeroPosti
			 * 
			 * QUESTO CODICE FUNZIONA SOLAMENTE SE IN AUTOVEICOLO CI SONO QUESTI CAMPI
			 * 
			 * 
			 //-------------------- TIPO AUTOVEICOLO --------------------------
			 
			 // TODO REINSERICO TEMPORANEAMENTE QUESTO CAMPO PER FARE L'INSERT DEI DATI, POI DEVO TOGLIERLO !!!!
			@Deprecated 
			private int tipoAutoveicoloEx;
			@Column(name = "id_tipo_autoveicolo")
			public int getTipoAutoveicoloEx() {
				return tipoAutoveicoloEx; 
			}
			public void setTipoAutoveicoloEx(int tipoAutoveicoloEx) {
				this.tipoAutoveicoloEx = tipoAutoveicoloEx;
			}
		
			//-------------------- MODELLO AUTOSCOUT --------------------------
		
			 // TODO REINSERICO TEMPORANEAMENTE QUESTO CAMPO PER FARE L'INSERT DEI DATI, POI DEVO TOGLIERLO !!!!
			@Deprecated 
			private ModelloAutoScout modelloAutoScoutXX;
			@ManyToOne(fetch = FetchType.EAGER)
			@JoinColumn(name = "id_modello_autoscout", unique = false, nullable = true)
			public ModelloAutoScout getModelloAutoScoutXX() {
				return modelloAutoScoutXX;
			}
			public void setModelloAutoScoutXX(ModelloAutoScout modelloAutoScoutXX) {
				this.modelloAutoScoutXX = modelloAutoScoutXX;
			}
			 */
			
			/*
			final List<ModelloAutoScout> ModelliAutoScout_TUTTI = modelloAutoScoutDao.getModelloAutoScout();
			final List<Autoveicolo> AutoveicoloList_USATI = autoveicoloDao.getAutoveicolo();
			
			// prova
			List<ModelloAutoNumeroPosti> aa = new ArrayList<ModelloAutoNumeroPosti>();
			aa.addAll(modelloAutoScoutDao.get( 563l ).getModelloAutoNumeroPosti());
			for(ModelloAutoNumeroPosti aa_ite: aa){
				System.out.println( aa_ite.getNumeroPostiAuto().getNumero() );
			}
			
			List<ModelloAutoScout> modelliAutoScout_NON_USATI = new ArrayList<ModelloAutoScout>();
			for(ModelloAutoScout modello_ite: ModelliAutoScout_TUTTI){
				List<Autoveicolo> listAutov = autoveicoloDao.getAutoveicoloBy_IdModelloAutoScout(modello_ite.getId());
				if(listAutov.size() == 0 ){
					modelliAutoScout_NON_USATI.add(modello_ite);
				}
			}
			
			System.out.println("ModelliAutoScout_TUTTI: "+ModelliAutoScout_TUTTI.size());
			System.out.println("modelliAutoScout_NON_USATI: "+modelliAutoScout_NON_USATI.size());
			System.out.println("distinct: "+modelloAutoScoutDao.getModelliAutoScout_by_UtilizzatiDagliAutisti().size());
			for(Autoveicolo autov_ite: AutoveicoloList_USATI){
				if(autov_ite.getModelloAutoScoutXX() != null){
					try{
						if(autov_ite.getTipoAutoveicoloEx() == 1){
							ModelloAutoNumeroPosti modAutoNumeroPosti = new ModelloAutoNumeroPosti();
							modAutoNumeroPosti.setModelloAutoScout( autov_ite.getModelloAutoScoutXX() );
							modAutoNumeroPosti.setNumeroPostiAuto( numeroPostiAutoDao.get(5l) );
							modelloAutoNumeroPostiDao.saveModelloAutoNumeroPosti(modAutoNumeroPosti);
							System.out.println("salvato");
							
						}else if(autov_ite.getTipoAutoveicoloEx() == 2 || autov_ite.getTipoAutoveicoloEx() == 3){
							ModelloAutoNumeroPosti modAutoNumeroPosti7 = new ModelloAutoNumeroPosti();
							modAutoNumeroPosti7.setModelloAutoScout( autov_ite.getModelloAutoScoutXX() );
							modAutoNumeroPosti7.setNumeroPostiAuto( numeroPostiAutoDao.get(7l) );
							modelloAutoNumeroPostiDao.saveModelloAutoNumeroPosti(modAutoNumeroPosti7);
							System.out.println("salvato");
							ModelloAutoNumeroPosti modAutoNumeroPosti8 = new ModelloAutoNumeroPosti();
							modAutoNumeroPosti8.setModelloAutoScout( autov_ite.getModelloAutoScoutXX() );
							modAutoNumeroPosti8.setNumeroPostiAuto( numeroPostiAutoDao.get(8l) );
							modelloAutoNumeroPostiDao.saveModelloAutoNumeroPosti(modAutoNumeroPosti8);
							System.out.println("salvato");
							ModelloAutoNumeroPosti modAutoNumeroPosti9 = new ModelloAutoNumeroPosti();
							modAutoNumeroPosti9.setModelloAutoScout( autov_ite.getModelloAutoScoutXX() );
							modAutoNumeroPosti9.setNumeroPostiAuto( numeroPostiAutoDao.get(9l) );
							modelloAutoNumeroPostiDao.saveModelloAutoNumeroPosti(modAutoNumeroPosti9);
							System.out.println("salvato");
						}
					}catch(DataIntegrityViolationException bb){
						System.out.println(bb.getMessage());
					}
					
				}
			}
			
			for(Autoveicolo autov_ite: AutoveicoloList_USATI){
				if(autov_ite.getModelloAutoScoutXX() != null){

					Long NumeroPosti = 0l;
					if(autov_ite.getTipoAutoveicoloEx() == 1){
						NumeroPosti = 5l;
					}else if(autov_ite.getTipoAutoveicoloEx() == 2){
						NumeroPosti = 7l;
					}else if(autov_ite.getTipoAutoveicoloEx() == 3){
						NumeroPosti = 8l;
					}
					
					ModelloAutoNumeroPosti modelloNumeroPosti = modelloAutoNumeroPostiDao.
							getModelloAutoNumeroPosti_By_ModelloAutoScout_NumeroPosti(autov_ite.getModelloAutoScoutXX().getId(), NumeroPosti);
					if(modelloNumeroPosti != null){
						System.out.println( modelloNumeroPosti.getId() );
						autov_ite.setModelloAutoNumeroPosti(modelloNumeroPosti);
	
						autoveicoloDao.saveAutoveicolo(autov_ite);
						System.out.println("salvato");
					}else{
						System.out.println("non salvato");
					}
				}
			}
			*/
			
			
			//CatturaAutoScout.CatturaMarche();
			//CatturaAutoScout.CatturaModelli();
			
			// 3886l meno di 20 km
			//RicercaTransfert ricercaTransfert = ricercaTransfertDao.get( 3926l );
			//CalcoloTariffe.CalcoloTariffe_Main(ricercaTransfert);
			
			
			//InviaEmailMarketing.inviaEmailMarketingAutisti_InfoMancanti_e_AutistaInfo();
			//InviaSMSMarketing.inviaSMSAutisti_InfoMancanti();

			/*
			try{
				// PAYPAL - RIMBORSO
				RicercaTransfert ricTransfert = ricercaTransfertDao.get( 3565l );
				final String paymentPayPal_ID = ricTransfert.getIdPaymentProvider();
				APIContext apiContext = new APIContext(Constants.PAYPAL_APOLLOTRANSFERT_SANDBOX_ID, Constants.PAYPAL_APOLLOTRANSFERT_SANDBOX_SECRET, "sandbox") ;
				Payment payment = Payment.get(apiContext, paymentPayPal_ID);
				
				RefundRequest refund = new RefundRequest();
				refund.setDescription("rimborso id corsa: "+ricTransfert.getId());
				Amount amount = new Amount();
				amount.setTotal("44.01");
				amount.setCurrency("EUR");
				refund.setAmount(amount);
				
				final String saleId = payment.getTransactions().get(0).getRelatedResources().get(0).getSale().getId();
				Sale sale = new Sale();
				sale.setId(saleId);
				// Refund sale
				sale.refund(apiContext, refund);
				
				// ATTENZIONE PER LEGGERE IL RIMBORSO BISOGNA SCORRERE DI 1 L'ARRAY .getRelatedResources().get(1)
				payment = Payment.get(apiContext, paymentPayPal_ID); // riaggiorno la chiamata
				System.out.println( payment.getTransactions().get(0).getRelatedResources().get(1)
						.getRefund().getAmount().getTotal()); // TODO QUESTO LO SALVO NEL CAMPO rimborsoCliente
				
			} catch (PayPalRESTException e) {
				// rimborso non riuscito
				System.out.println("PayPalRESTException");
				System.out.println(e.getMessage());
				//e.printStackTrace();
			}
			*/

			///---------------------------------------------
			
			/*
			// STRIPE - RIMBORSO
			RicercaTransfert ricTransfert = ricercaTransfertDao.get( 3568l );
			Stripe.apiKey = Constants.STRIPE_SECRET_KEY_TEST; // TEST
			try{
				Map<String, Object> refundParams = new HashMap<String, Object>();
				refundParams.put("charge", ricTransfert.getIdPaymentProvider() );
				
				Map<String, String> initialMetadata = new HashMap<String, String>();
				initialMetadata.put("Rich Autist Medio RicercaTransfert id:", ricTransfert.getId().toString() );
				refundParams.put("metadata", initialMetadata);
				
				refundParams.put("amount", "1000");
				refundParams.put("reason", "requested_by_customer"); // Reason for the refund. If set, possible values are duplicate, fraudulent, and requested_by_customer.
				
				// eseguo il rimborso
				Refund rf = Refund.create(refundParams);
				// mi ritorna il rimborso appena fatto
				//System.out.println("Rimborso eseguito: "+Refund.retrieve(rf.getId()).getAmount() );
				
				
				// mi ritorna il Totale Rimborsi eseguiti
				Charge charge = Charge.retrieve(ricTransfert.getIdPaymentProvider()); // riaggiorno la chiamata
				System.out.println("Totale Rimborsi eseguiti: "+charge.getAmountRefunded() ); // TODO QUESTO LO SALVO NEL CAMPO rimborsoCliente
				
			}catch(InvalidRequestException irE){
				System.out.println(irE.getMessage());
			}
			*/
			
			
			// -------------------------------------------------------------------------------------
			
			//RichiestaMediaAutista aa = ricercaTransfertDao.getRichiestaMediaAutista_AutistaAssegnatoCorsaMedia(3218l);			
			//System.out.println(aa.getAutista().getUser().getFullName());
			
			//List <GestioneCorseMedieAdmin> aa = ricercaTransfertDao.getCorseAutistaRichiestaAutistaMedioALL(true, true, true, null, null, null);
			//System.out.println(aa.size());
			
			
			/*
			List<RicercaTransfert> aa = ricercaTransfertDao.getRicercaTransfertSoloRicercheEseguiteCliente();
			for(RicercaTransfert ite: aa){
				System.out.println( ite.getRichiestaMedia().getPrezzoTotaleCliente() );
				ite.getId();
			}
			*/
			
			
			// torino 2814 malpensa 2815
			//2806	Firenze, FI, Italia	- Aeroporto di Pisa-San Giusto "Galileo Galilei"
			
			// 2747l - roma firenze
			// 3040l - casalotti bracciano
			// 2239l - roma milanoTrento
			// 3046l - tricase (salento) Aosta
			// 2921l - Milano Centrale, Milano, MI, Italia - Genova Piazza Principe, Genova, GE, Italia
			// 2917l - Latina, LT, Italia - Gallipoli, LE, Italia
			// 3111l - torino - venezia
			// 3130l - livorno - la pila (isola d'elba)
			//RicercaTransfert ricercaTransfert = ricercaTransfertDao.get( 3137l );
			//CalcoloTariffe.CalcoloTariffe_Main(ricercaTransfert);
			
			
			
			
			//Locale locale = new Locale.Builder().setLanguage("it").setRegion("IT").build();
			//RicercaTransfert_GoogleMaps_Connect googleMaps = new RicercaTransfert_GoogleMaps_Connect();
			//googleMaps.dammiInfoGoogleDirections_by_Query(ricercaTransfert, locale.getLanguage());
			
			
			

			//TerritorioAutisti.ProvinceAutistiDisponibili();
			//TerritorioAutisti.ReportGenerale();
			//TerritorioAutisti.ProvinciaAutistaList();
			//TerritorioAutisti.RegioneAutistaList();
			/*
			List<AutistiProvincia> provAutistilist = new ArrayList<AutistiProvincia>();
			provAutistilist = TerritorioAutisti.ProvinciaAutistaList().getAutistiProvincia();
			int totaleConferma = 0;
			for(AutistiProvincia provAutistilist_ite: provAutistilist){
				if(provAutistilist_ite.getAutisti().size() >= 3){
					provAutistilist_ite.getNomeProvincia();
					System.out.println( "---------"+provAutistilist_ite.getNomeProvincia().toUpperCase() );
					System.out.println(provAutistilist_ite.getAutisti().size());
					totaleConferma = totaleConferma + provAutistilist_ite.getAutisti().size();
					for(Long autista_ite: provAutistilist_ite.getAutisti()){
						//System.out.println(autistaDao.get(autista_ite).getUser().getFullName());
					}
				}
			}
			System.out.println("TOTALE CONFERMA PROV: "+totaleConferma); //58 //275
			
			List<Autista> listAutist = autistaDao.getAutistiList();
			int contA = 0;
			for(Autista aa: listAutist){
				
				if(aa.getAutistaDocumento().isApprovatoGenerale()){
					contA++;
				}
			}
			System.out.println("contA: "+contA);
			*/
			
			
	    	
	    	/*
	    	List<Autista> listAutisti = autistaDao.getAutistiList();
	    	for(Autista listAutisti_ite: listAutisti){
	    		System.out.println(listAutisti_ite.getId() +" "+listAutisti_ite.getUser().getFullName());
	    		
	    		List<AutistaZone> autistaZone_list = autistaZoneDao.getAutistaZoneByAutista(listAutisti_ite.getId());
				for(AutistaZone autistaZona_ite: autistaZone_list){
					//if(autistaZone_list.size() == 0 ){
						if(autistaZona_ite.getComuni() != null){
							//System.out.println("------ " +"comune: "+autistaZona_ite.getComuni().getNomeComune());
						}
						if(autistaZona_ite.getProvince() != null){
							System.out.println("------ " +"provincia: "+autistaZona_ite.getProvince().getNomeProvincia());
						}
						if(autistaZona_ite.getRegioni() != null){
							//System.out.println("------ " +"regione: "+autistaZona_ite.getRegioni().getNomeRegione());
						}
					//}
				}
	    	}
			*/
			
			//InviaEmailMarketing.inviaEmailAutisti_InfoMancanti();

			
			/*
			Map<String, Object> model = new HashMap<String, Object>();
	        model.put("nomeCognome", "Mario Rossi");
	        model.put("user", "user 222");
	        model.put("password", "password 333");
	        model.put("applicationURL", "applicationURL 444");
	        model.put(Constants.VM_MESSAGE_SOURCE, messageSource);
	        

	        String xhtml = VelocityEngineUtils.mergeTemplateIntoString(
	        		new VelocityEngine(), "src/main/resources/"+"tmp_email_scrittura_privata_autista.vm", Constants.ENCODING_UTF_8, model);
			
			System.out.println("htmlBody: "+xhtml);
			
			ITextRenderer renderer = new ITextRenderer();

			
			xhtml = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
					+ "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><title>Title of document</title></head>"
					+ "<body style=\"font-family:Minion Web;\">Con la presente scrittura privata, valevole a tutti gli effetti di legge, tra le due parti si"
					+ "</body></html>";
			

			// if you have html source in hand, use it to generate document object
			renderer.setDocumentFromString( xhtml );
			renderer.layout();
			
			String fileNameWithPath = "C:\\aaa_test\\" + "scrittura_privata_autista.pdf";
			FileOutputStream fos = new FileOutputStream( fileNameWithPath );
			renderer.createPDF( fos );
			fos.close();
			System.out.println( "File 2: '" + fileNameWithPath + "' created." );
			*/
			
			/*
			System.out.println("COUNT: "+autistaDao.getCountAutista());
			List<Autista> aa = autistaDao.getAutistaTable_2_limit(20,0);
			for(Autista aa_ite : aa){
				Autista autista = aa_ite;
				System.out.println(autista.getId());
			}
			*/

			//documentiPatenteDao.remove(2l);
			
			//DocumentiPatente aa = documentiPatenteDao.get(2l);
			//System.out.println(aa.getNomeFilePatenteFronte());
			
			//List<Object> autoList = autoveicoloDao.getAutoveicoliCalcoloTariffe_percentualeAutistaList( -2 );
			//System.out.println(autoList.size() );
			
			//Autoveicolo aa = autoveicoloDao.getConInfoAutista( 16l );
			//System.out.println( aa.getAutista().getUser().getFirstName() );
			
			//System.out.println(autoList.get(0).getAutista().getPercentualeServizio() );
			
			//System.out.println( AutistaSottoAutisti.class.getName() );
			
			//https://localhost:8443/apollon/getFileAmazonStore?objectModel=AutistaLicenzeNcc&objectModelId=48&objectModelFileName=nomeFileDocumentoLicenza
			
			//if(AutistaSottoAutisti.class.getName().equals("AutistaSottoAutisti")){
				//System.out.println("OKKKKK");
			//}
			
			//AmazonStorageFiles.ListaPrintTuttiFileBucket( Constants.AMAZON_STORE_BUCKET_TEST_NAME );
			
			
			
			/*
			//Autista autista = autistaDao.get(-3l);
			// 24 mi da null
			boolean max = autistaZoneDao.ControllaServiziAttivi(3);

			//List<TipoRuoli> tipoRuoliList = new ArrayList<TipoRuoli>();
			//tipoRuoliList.addAll( user.getTipoRuoli() );
			System.out.println(max);
			 */
			
			//List<Tariffe_Zone> tariffe_Zone_List = TariffeUtil.getTariffe_Zone_List(aa, bb, cc);
			//List<Tariffe> aa = new ArrayList<Tariffe>(autista.getTariffe());
			
			//System.out.println( tariffe_Zone_List.size() );

			
			//List<Tariffe> tariffe = tariffeDao.getTariffeByIdAutista(autista.getId());
	        //List<AutistaZone> autistaZone = autistaZoneDao.getAutistaZoneByAutista(autista.getId());
	        
	        //List<AutistaAeroporti> autistaAeroporti = autistaAeroportiDao.getAutistaAeroportiByIdAutista(autista.getId());
	        //List<AutistaPortiNavali> autistaPortiNavali = autistaPortiNavaliDao.getAutistaPortiNavaliByIdAutista(autista.getId());
	        
	        //List<Autoveicolo> autoveicoloList = new ArrayList<Autoveicolo>(autista.getAutoveicolo());
	        
	        //List<Tariffe_Zone> tariffe_Zone_List = TariffeUtil.getTariffe_Zone_List(tariffe, autistaZone, autoveicoloList);
			

			
			//System.out.println( tariffe_Zone_List.size() );
			
			/*
			List<Autoveicolo> autoveicoliList = autoveicoloDao.getAutoveicolo();
			CatturaTargheMinisteroEntrate.CatturaDati_by_Targa(autoveicoliList);
			
			for(Autoveicolo autoveicolo_ite: autoveicoliList){
				
				Autoveicolo auto = autoveicoloDao.get( autoveicolo_ite.getId() );
				if(auto.getAnnoImmatricolazione() != null ){
					String aa = autoveicolo_ite.getAnnoImmatricolazione().substring(6);
					auto.setAnnoImmatricolazione( aa );
					autoveicoloDao.saveAutoveicolo(auto);
				}
			}
			*/
			
			
			//UtilBukowski.CalcolaTariffas(80556l, new BigDecimal("120.00"));

			//RicercaTransfert ricercaTransfert = ricercaTransfertDao.get( 1858l );
			//CalcoloTariffe.CalcoloTariffe_Main(ricercaTransfert);

			/*
			ComunicazioniUser ee = emailAutistiMarketingDao.getComunicazioniUserByEmail("m.ventura@inwind.it");
			System.out.println(ee.getEmail());
			List<ComunicazioniUser> indirizziEmail = new ArrayList<ComunicazioniUser>();
			
			
			ComunicazioniUser aa = new ComunicazioniUser();
			aa.setEmail( "matteo.manili@gmail.com");
			indirizziEmail.add(aa);
			
			aa = new ComunicazioniUser();
			aa.setEmail( "matteo.manili@tiscali.it");
			indirizziEmail.add(aa);
			
			aa = new ComunicazioniUser();
			aa.setEmail( "uidjsuddffkddh@gmail.com");
			indirizziEmail.add(aa);
			*/
			
			//List<ComunicazioniUser> indirizziEmail = new ArrayList<ComunicazioniUser>();
			//indirizziEmail = emailAutistiMarketingDao.getComunicazioniUser();
			//InviaEmailMarketingAutisti.inviaEmail(indirizziEmail);
			
			//CatturaEmailAutisti_assotaxi_it.CatturaAnnunci();

			/*
			RicercaTransfert ricercaTransfert = ricercaTransfertDao.get( 1618l );
			//CalcoloTariffe.CalcoloTariffe_Main(ricercaTransfert);
			
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
	        String dateInString = "12-03-2017 22:26";
	        Date prelevametoDate = formatter.parse(dateInString);
			
			System.out.println("GIORNO ORA PRELEVAMENTO: "+prelevametoDate);

			
			//Calendar cal = Calendar.getInstance(); // creates calendar
		    //cal.setTime(new Date()); // sets calendar time/date
		    //cal.add(Calendar.HOUR_OF_DAY, 12); // adds one hour
		    
		    //System.out.println("GIORNO ORA PRELEVAMENTO: "+cal.getTime());
			//ControlloDateRicerca.ControlloDataPrelevamentoSuperioreTotOraDaAdesso(prelevametoDate, Constants.CORSA_MEDIA);
			 */
			
			
			
			//RicercaTransfert eliminaRic =  ricercaTransfertDao .get( 1479l );
			//Object object = fattureDao.getObjectFatturaBy_IdricercaTransfert(1465l);

			/*
			for(Tariffe tariffe_ite: tariffeDao.getTariffe() ){
				if(tariffe_ite.getTariffeValori().getTariffaLP() != null && tariffe_ite.getTariffeValori().getTariffaLP(). compareTo( new BigDecimal("1.20") ) <= 0 &&
						tariffe_ite.getTariffeValori().getTariffaLP() != null && tariffe_ite.getTariffeValori().getTariffaLP(). compareTo( new BigDecimal("0.00") ) > 0 
						){
					System.out.println( tariffe_ite.getTariffeValori().getTariffaLP() );

					tariffe_ite.getTariffeValori().setTariffaLP(new BigDecimal("1.20"));
					tariffeDao.saveTariffe(tariffe_ite);
				}
			}
			 */
			
			/*
			List<RicercaTransfert> listautista = ricercaTransfertDao.getRicercaTransfertSoloRicercheEseguiteCliente();
			for(RicercaTransfert ite: listautista){
				
				ite.getId();
				System.out.println( ite.getId() );
				System.out.println( ite.getUser().getFirstName() );
				//System.out.println(ite.getTariffe().size());
				
			}
			*/
			
			//System.out.println(object);
			
			
			/*
			// PULISCE TUTTE LE CORSE SENZA COLLEGAMENTO
			List<RicercaTransfert> listaTotaleRicherche = ricercaTransfertDao.getAll();
			System.out.println( listaTotaleRicherche.size() );
			
			for(RicercaTransfert ite: listaTotaleRicherche){
				
				try{
					ricercaTransfertDao.deleteRicercaTransfert ( ite.getId() );
				}catch(ConstraintViolationException zz){
					System.out.println("aaa");
					System.out.println( zz.getMessage() );
				}catch(InvalidDataAccessApiUsageException aa){
					System.out.println("bbb");
					System.out.println( aa.getMessage() );
				}catch(ObjectDeletedException bb){
					System.out.println("ccc");
					System.out.println( bb.getMessage() );
				}catch(Exception cc){
					System.out.println("ddd");
					System.out.println( cc.getMessage() );
				}
			}
			*/
			
			
			
			
			
			
			//System.out.println(  fattureDao.dammiNumeroProgressivoFattura() );
			
			
			
			//Fatture fatturaPart = fattureDao.getFatturaBy_IdCorsaParticolare( 483l );
			
			//System.out.println(  fattureDao.dammiNumeroProgressivoFattura() );
			
			
			
			//List <GestioneCorseMedieAdmin> aa = ricercaTransfertDao.getCorseAutistaRichiestaAutistaMedioALL(true, true, true, null, null, 18l);
			
			//System.out.println(aa.size());

			
			/*
			StringBuilder sb = new StringBuilder("https://www.sandbox.paypal.com/it_IT/signin/authorize");
			sb.append("?client_id=" + Constants.PAYPAL_APOLLOTRANSFERT_SANDBOX_ID);
			
			sb.append("&response_type=" + "token");
			//sb.append("&scope=" + "profile+email+address+phone+https%3A%2F%2Furi.paypal.com%2Fservices%2Fpaypalattributes");
			//sb.append("&redirect_uri=" + "http://example.com/myapp/return.php");
			
			System.out.println("URL="+sb);
			System.out.println( UrlConnection.HttpConnection( sb ) );
			//JSONObject obj = new JSONObject( UrlConnection.HttpConnection( sb ) );
			//String status = Integer.toString( obj.getInt("geoplugin_status") );
			*/
			
			
			
			
			
			/*
			BraintreeGateway gateway = new BraintreeGateway(
					Environment.PRODUCTION,
					Constants.BT_MERCHANT_ID,
					Constants.BT_PUBLIC_KEY,
					Constants.BT_PRIVATE_KEY );
			
			
			String aaa = gateway.clientToken().generate();
			System.out.println("aaa: "+aaa);
			*/
			//ClientTokenRequest clientTokenRequest = new ClientTokenRequest().customerId(aaa);
			
			//System.out.println("bbb: "+clientTokenRequest.getCustomerId());
			
			
			/*
			List<RichiestaAutistaMedio> corseDisponibiliMedioList = ricercaTransfertDao.getCorseClienteRichiestaAutistaMedio(-2);
			for(RichiestaAutistaMedio corseDisponibiliMedio_ite: corseDisponibiliMedioList){
				System.out.println(corseDisponibiliMedio_ite.getId());
			}
			*/
			
			/*
			RichiestaAutistaParticolare aa = ricercaTransfertDao.getCorsaAutRicTransfPartPrenotazione("parOX6LD7sjab");
			System.out.println(aa.getId());
			*/
			
			//DateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm");
			/*
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	        String dateInString = "27-11-2016";
	        String dateOutString = "29-12-2016";
	        
	        Date dateFrom = formatter.parse(dateInString);
			System.out.println(dateFrom);
			System.out.println(formatter.format(dateFrom));
			
			Date dateTo = formatter.parse(dateOutString);
			dateFrom = null;
			dateTo = null;
			
			boolean aa = richiestaAutistaMedioDao.getInfoCliente_by_AutistaPanel(84l, 1l);
			
			System.out.println(aa);
			*/
			
			//int aa = autistaDao.getCalcolaNumeroCorseApprovate(2l);
			//System.out.println(aa);
			//DateUtils.addHours(oldDate, 3);
			
			/*
			List<RichiestaAutistaParticolare> aa = ricercaTransfertDao.getCorseAutistaRichiestaAutistaParticolare(-3l);
			for(RichiestaAutistaParticolare ricPart_ite: aa){
				
				if(ricPart_ite.getRicercaTransfert().getId() == 78){
				
					System.out.println("------------------: "+ricPart_ite.getRicercaTransfert().getId());
				
					if( ricPart_ite.getRicercaTransfert().getDataOraPrelevamentoDate().getTime() > new Date().getTime() ){
						System.out.println("CORSA DA ESEGUIRE");
					}
					
					if( ricPart_ite.getRicercaTransfert().getDataOraPrelevamentoDate().getTime() < new Date().getTime() &&
							ricPart_ite.getRicercaTransfert().getDataOraPrelevamentoDate().getTime() + 
								TimeUnit.SECONDS.toMillis( ricPart_ite.getRicercaTransfert().getDurataConTrafficoValue()) > new Date().getTime()){
						System.out.println("CORSA IN ESECUZIONE");
					}
					
					if( ricPart_ite.getRicercaTransfert().getDataOraPrelevamentoDate().getTime() < new Date().getTime() &&
							ricPart_ite.getRicercaTransfert().getDataOraPrelevamentoDate().getTime() + 
								TimeUnit.SECONDS.toMillis( ricPart_ite.getRicercaTransfert().getDurataConTrafficoValue()) < new Date().getTime()){
						System.out.println("CORSA ESEGUITA");
					}
					
					
					if( ricPart_ite.getRicercaTransfert().getDataOraPrelevamentoDate().getTime() < new Date().getTime() && 
							ricPart_ite.getRicercaTransfert().getDataOraPrelevamentoDate().getTime() + 
								TimeUnit.SECONDS.toMillis( ricPart_ite.getRicercaTransfert().getDurataConTrafficoValue()) < new Date().getTime()
							){
						System.out.println("CORSA ESEGUITA");
					}
					if( ricPart_ite.getRicercaTransfert().getDataOraPrelevamentoDate().getTime() > new Date().getTime() && 
								ricPart_ite.getRicercaTransfert().getDataOraPrelevamentoDate().getTime() + 
									TimeUnit.SECONDS.toMillis( ricPart_ite.getRicercaTransfert().getDurataConTrafficoValue()) > new Date().getTime() ){
						System.out.println("CORSA IN ESECUZIONE");
					}
					else{
						System.out.println("CORSA DA ESEGUIRE");
					}
				}
			}
			*/
			
			
			/*
			List<RichiestaAutistaParticolare> aa = ricercaTransfertDao.getCorseAutistaRichiestaAutistaParticolareALL(true, false, false);
			
			for(RichiestaAutistaParticolare aa_ite: aa){
				System.out.println(aa_ite.getRicercaTransfert().getId());
			}
			 */
			
			/*
			User user = (User) userDao. loadUserByTelephone ( "+393289898585" );
			if(!user.getTipoRuoli().contains( tipoRuoliDao.getTipoRuoliByName(Constants.AUTISTA) ) ){
				System.out.println("ENTRO");
			}
			*/
			
			/*
			RicercaTransfert ricTransfert = ricercaTransfertDao.get(76l);
			Autista autista = autistaDao.get(1l);
			String prezzoTot = "100";
			
			prezzoTot = prezzoTot.replace(",", ".");
			BigDecimal prezzo = new BigDecimal(prezzoTot);
			BigDecimal tariffaServzioST;
			BigDecimal tariffaServzioLP;
			
			// TARIFFA PIU' PERCENTUALE SERVIZIO AUTISTA
			BigDecimal tariffa = util_BUKOWSKI.CalcolaTariffas(ricTransfert.getDistanzaValue(), prezzo, autista.getPercentualeServizio());
			
			System.out.println("--------------------------------------------------");
			
			// PERCENTUALE DIFFERENZA ST - LP
			BigDecimal differenzaPercetuale = util_BUKOWSKI.calcolaPercentuale(tariffa, (int)(long)gestioneApplicazioneDao.getName( "PERCENTUALE_DIFFERENZA_TARIFFA_SERV_ST_LP" ).getValueNumber() );
			System.out.println("DIFFERENZA PERCENTUALE: "+differenzaPercetuale);
			
			differenzaPercetuale = differenzaPercetuale.setScale(2, RoundingMode.HALF_EVEN);
			
			System.out.println("tariffa: "+tariffa);
			
			BigDecimal metri = new BigDecimal(ricTransfert.getDistanzaValue());
			BigDecimal km = metri.divide(new BigDecimal("1000"), 2, BigDecimal.ROUND_HALF_UP);
			
			BigDecimal risultatoFinale = tariffaServzioLP.multiply( km );
			 
			System.out.println("RISULTATO FINALE: "+ risultatoFinale);
			*/

			
			/*
			Autista aa = autistaDao.get(7l);
			
			String ibanString = "IT56K0100335000ee0000002029";
			
			IBANCheckDigit ibanCheckDigit = new IBANCheckDigit();
			
			
			if(ibanCheckDigit.isValid( ibanString )){
				System.out.println("IBAN VALIDO");
			}else{
				System.out.println("IBAN NON VALIDO");
			}
			*/
			
			
			
			/*
			List<Tariffe_Zone> tariffe_Zone_List = autistaDao.get(8l).getTariffe_Zone_List();
			for(Tariffe_Zone tariffeZone_ite: tariffe_Zone_List){
				
				List<Tariffe_AutoveicoloTariffa> Tariffe_AutoveicoloTariffaList = tariffeZone_ite.getTariffe_AutoveicoliTariffeList();
				

				
				System.out.println("weweweW:"+Tariffe_AutoveicoloTariffaList.size());
				
				for(Tariffe_AutoveicoloTariffa Tariffe_AutoveicoloTariffaList_ite: Tariffe_AutoveicoloTariffaList){
					Tariffe_AutoveicoloTariffaList_ite.getTariffaST();
					System.out.println("sasasasassa"+Tariffe_AutoveicoloTariffaList_ite.getTariffaST());

					
				}
				
				
			}
			*/
			

			

			
			
			/*
			RicercaTransfert ricTransfert = ricercaTransfertDao.get(5l);
			
			RisultatoRicerca_Autista_Tariffe aa = CalcoloTariffe.CalcoloTariffe_Main(ricTransfert, new Locale( "it" ));
			
			for(RisultatoAutistaParticolareAutoveicolo bb_ite: aa.getResultParticolareAutistaAutoveicoloTariffaPrezzo()){
				
				System.out.println("AUTOVEICOLO: "+bb_ite.getAutoveicolo().getMarca() +" TARIFFA PART: "+bb_ite.getTariffa());
				
			}
			*/
			

			/* ESEMPIO DI ARRAY LIST SET CONTAINS CON OBJECT 
			User user = (User) userDao. loadUserByTelephone ( "+393289126333" );
			
			Set<TipoRuoli> tipoRuoliUser = user.getTipoRuoli();
			
			
			if(tipoRuoliUser.contains( tipoRuoliDao.getTipoRuoliByName( Constants.CLIENTE_ROLE ) )){
				System.out.println("OKKKKKK");
			}else{
				System.out.println("NOOOOOOOOOOO");
			}
			*/
			
			
			
			
			
			/*
			List<Long> s = new ArrayList<Long>();
			s.add(6L);
			s.add(2L);
			s.add(6L);
			s.add(7L);
			
			

			s = util_BUKOWSKI.removeDuplicatesLong( s );
			
			
			for(Long tariffe_ite : s){

				System.out.println("AUTISTI AAAAA:"+tariffe_ite);

			}
			*/
			
			/*
			if(s.contains(3L)){
				System.out.println("SI CONTIENE");
			}else{
				System.out.println("NO NON CONTIENE");
			}
			*/
			
			
	/*
			 //qui devo verificare se l'autista è occupato in altre corse che interferiscono con le date della corsa ricercata 
			 // e quindi l'autista deve escuderlo dagli autisti offerti

			
			// ricTransfertDaControllare
			RicercaTransfert CorsaTest = ricercaTransfertDao.get(183l);

			// CONTROLLA DATA PRELEVAMENTO
			long AustistaID = 3l;

			ControlloDateRicerca.ControlloAutistaCorseSovrapposte_MAIN(CorsaTest, AustistaID);
		*/	
			
			/*
			// CONTROLLA DATA RITRONO
			if(CorsaTest.isRitorno() && CorsaTest.getDataOraRitornoDate() != null){
				System.out.println("--------------------------------------------------------------------------------------");
				System.out.println("----------- controllo ritorno --------------------------------------------------------");
				System.out.println("--------------------------------------------------------------------------------------");
				SECONDI = CorsaTest.getDurataConTrafficoValueRitorno();
				datasommata =  CorsaTest.getDataOraRitornoDate().getTime() + TimeUnit.SECONDS.toMillis( SECONDI );
				dataArrivoPrevisto = new Date( datasommata );
				System.out.println("PRELEVAMENTO: "+CorsaTest.getDataOraRitornoDate());
				System.out.println("ARRIVO PREVISTO FINE PRIMA CORSA: "+dataArrivoPrevisto);
				
				ControlloNumero1(AustistaID, CorsaTest, dataArrivoPrevisto, CorsaTest.getDataOraRitornoDate(), true);
			}
			*/
			
			
			
			
			
			
			
			
			
			
			/*
			RicercaTransfert ricTransfert = ricercaTransfertDao.get( 53l );
			
			 // controllo data ritorno superiore alla data di arrivo prevista

			if(ricTransfert.isRitorno() && ricTransfert.getDataOraRitorno() != null && !ricTransfert.getDataOraRitorno().equals("")){
				
				Date dataPrelevamento = ControlloDateRicerca.FormatParseDateRicerca( ricTransfert.getDataOraPrelevamento() );
				System.out.println("PRELEVAMENTO: "+dataPrelevamento);
				long SECONDI = ricTransfert.getDurataConTrafficoValue();
				
				long datasommata =  dataPrelevamento.getTime() + TimeUnit.SECONDS.toMillis( SECONDI );
				Date dataArrivoPrevisto = new Date( datasommata );
				System.out.println("ARRIVO PREVISTO: "+dataArrivoPrevisto);
				
				Date dataRitorno = ControlloDateRicerca.FormatParseDateRicerca( ricTransfert.getDataOraRitorno() );
				System.out.println("RITORNO: "+dataRitorno);
				
				if(dataRitorno.getTime() <= dataArrivoPrevisto.getTime() ){
					String formattedDate = new SimpleDateFormat("HH:mm").format(dataArrivoPrevisto);
					System.out.println( "Alert! la data di ritorno deve essere superiore alle "+ formattedDate );
					
				}
			}
			*/
			
			/*

			String KEY_GOOGLE_API = "AIzaSyC1cPK5XE6nDVlJif24p_c5PBZ8lcRTgwo";
			
			String TYPE_SEARCH = "/place/textsearch";
	        String TYPE_AUTOCOMPLETE = "/place/autocomplete";
			String TYPE_QUERY_AUTOCOMPLETE = "/place/queryautocomplete";
	        String OUT_XML = "/xml";
			String OUT_JSON = "/json";
			
			//https://www.googleapis.com/geolocation/v1/geolocate?key=YOUR_API_KEY
			
			StringBuilder sb = new StringBuilder( "https://www.googleapis.com/geolocation/v1/geolocate" );
			sb.append("?key=" + KEY_GOOGLE_API);

			
			// Roma - specificando queste coordinate le ricerche saranno circoscritte da questo punto al raggio del radious
			// ho messo roma perché si trova nel centro dell'italia.

			System.out.println("-----parsing---------");
			UrlConnection url = new UrlConnection();
	        JSONObject jb = new JSONObject(url.HttpConnection( sb ));
	        String status = (String) jb.get("status");
	        if(status.equals("OK")){
	        	
	        }
			*/
			
			
			
			
	        
			/*
			String IpAddess = "151.57.72.211";
			//StringBuilder sb = new StringBuilder("https://tools.keycdn.com/geo.json?host=" + IpAddess +"/geo"  );
			//StringBuilder sb = new StringBuilder( "http://ipinfo.io/"+IpAddess+"/geo"  );
			
			//StringBuilder sb = new StringBuilder("http://geoip.nekudo.com/api/151.57.24.126"  );
			//StringBuilder sb = new StringBuilder("https://ipapi.co/151.57.24.126/json/"  );
			StringBuilder sb = new StringBuilder("http://ip-api.com/json/37.227.188.159");
			
			
			System.out.println("URL="+sb);

			JSONObject obj = new JSONObject( UrlConnection.HttpConnection( sb ) );
			String status =  obj.getString("city") ;

*/
			
			
			
			
			
			
			
			
			
			
			
			/*
			// CONTROLLARE CHE LA DATA CHIAMATA PRENOTATA NON SIA PIU' VECCHIA DI 6 ore E CHE LA DATA PRELEVAMENTO NON SIA INFERIORE DA 3 ORE DA ADESSO
			// ALTRIMENTI FARE UN REDIRECT: APOLLOTRANSFERT.COM/?course=numRicercaTrans
			// METTENDO ricercaTransfert.resultAutistaTariffePrezzo.tariffeTrovate A FALSE
			
			DateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.ITALIAN);
			
			String DATE_ADESSO = "10/08/2016 20:00";
			String dateRicerca = "09/08/2016 19:00";
			String datePrelevamento = "20/08/2016 00:02";
			String dateChiamataPrenotata = "10/08/2016 10:00";
			
			Date DATA_ADESSO = format.parse(DATE_ADESSO);
			Date data_Ricerca = format.parse(dateRicerca);
			Date data_Prelevamento = format.parse(datePrelevamento);
			Date data_ChiamataPrenotata = format.parse(dateChiamataPrenotata);
			
			Calendar CALENDAR_ADESSO = Calendar.getInstance(); CALENDAR_ADESSO.setTime( DATA_ADESSO );
			Calendar calendar_Ricerca = Calendar.getInstance(); calendar_Ricerca.setTime( data_Ricerca );
			Calendar calendar_Prelevamento = Calendar.getInstance(); calendar_Prelevamento.setTime( data_Prelevamento );
			Calendar calendar_ChiamataPrenotata = Calendar.getInstance(); calendar_ChiamataPrenotata.setTime( data_ChiamataPrenotata );
			
			System.out.println("ADESSO "+CALENDAR_ADESSO.getTime());
			//System.out.println("dateRicerca "+calendar_Ricerca.getTime());
			System.out.println("datePrelevamento "+calendar_Prelevamento.getTime());
			//System.out.println("dateChiamataPrenotata "+calendar_ChiamataPrenotata.getTime());

			
			calendar_Prelevamento = Calendar.getInstance(); calendar_Prelevamento.setTime( data_Prelevamento );

			if( calendar_Prelevamento.getTime().before(Calendar.getInstance().getTime()) ){
				System.out.println("data prelevamento non valida");
				System.out.println("FALSE");
			}else{
				System.out.println("data prelevamento valida");
				System.out.println("TRUE");
			}
			*/
			
			
			// ------------
			
			
			//System.out.println(calendar_Prelevamento.getTime() );

			
			//status=success&remaining_sms=130

			
			//Autoveicolo auto = autoveicoloDao.get(1l);
			//System.out.println( auto.getMarca() );
			

			//Stripe.apiKey = "sk_test_Sob1p6jEVeztQZOVAT66dxzP"; // TEST
			
			// lista dei clienti
			//Map<String, Object> customerParams = new HashMap<String, Object>();
			//customerParams.put("limit", 3);

			//System.out.println( Customer.list(customerParams) );
			
			
			// BALANCE (il saldo)
			//System.out.println( Balance.retrieve() );
			//System.out.println( BalanceTransaction.retrieve("txn_18fpMUCLt3TKIzHhmDtaahT7") );
			
			
			//Retrieve a charge
			//System.out.println( Charge.retrieve("ch_18frtJCLt3TKIzHhbqvGzIDN") );
			
			
			// questo serve a catturare un pagamento in sospeso
			//Charge ch = Charge.retrieve("ch_18frtJCLt3TKIzHhbqvGzIDN");
			//ch.capture();
			
			
			// dettagli cliente
			//System.out.println( Customer.retrieve("cus_8xnUYbXEzgKYSe") );
			
			
			// Retrieve a dispute (se ci sono)
			//Dispute.retrieve("dp_18frwWCLt3TKIzHhUfhtPOW3");
			
			// List all disputes
			//Map<String, Object> disputeParams = new HashMap<String, Object>();
			//disputeParams.put("limit", 3);
			//Dispute.list(disputeParams);
			//System.out.println( Dispute.list(disputeParams) );
			
			// List all events
			//Map<String, Object> eventParams = new HashMap<String, Object>();
			//eventParams.put("limit", 3);
			//Event.list(eventParams);
			//System.out.println( Event.list(eventParams) );
			

			// Refunds
			// nota: ho eseguito due volte questo codice: la prima volta il 08-08-2016 e la seconda il 09-08-2016.
			// cioè significa che dal pagamento iniziale di 50 euro, successivamente ho rimborsato due volte di 10 euro. 
			// quindi nella lista operazioni della carta prepagata vedrò due accrediti di 10 euro.
			/*
			Map<String, Object> refundParams = new HashMap<String, Object>();
			refundParams.put("charge", "ch_18gG0lCLt3TKIzHh3WpV4N96");
			
			Map<String, String> initialMetadata = new HashMap<String, String>();
			initialMetadata.put("RicercaTransfert_id", "123");
			refundParams.put("metadata", initialMetadata);
			
			refundParams.put("amount", "1000");
			refundParams.put("reason", "requested_by_customer"); // Reason for the refund. If set, possible values are duplicate, fraudulent, and requested_by_customer.

			Refund rf = Refund.create(refundParams);
			
			System.out.println("faccio il retrive......");
			
			System.out.println( Refund.retrieve( rf.getId() ) );
			*/
			
			
			
			
			// POSSO INVIARE PAGAMENTI AGLI ALTRI UTENTI STRIPE CHE SI SONO CLLEGATI AL MIO: VEDERE LA CLASSE StripeConnectController.java
			
			/*
			Map<String, Object> transferParams = new HashMap<String, Object>();
			transferParams.put("amount", 0500);
			transferParams.put("currency", "eur");
			transferParams.put("destination", "acct_ ID ACCOUNT CONNECT (VEDERE: https://connect.stripe.com/test/applications/users/overview) ");
			transferParams.put("description", "Transfer for matteo.manili@tiscali.it");

			Transfer.create(transferParams);
			*/
			
			/*
			RequestOptions requestOptions = RequestOptions.builder().setStripeAccount("acct_18bMnKCLt3TKIzHh").build();

			Map<String, Object> transferParams = new HashMap<String, Object>();
			transferParams.put("amount", 2000);
			transferParams.put("currency", "eur");
			transferParams.put("destination", "default_for_currency");

			Transfer.create(transferParams, requestOptions);
			*/
			

			
			/*
			RicercaTransfert_MaxMind_Connect ricTrans_MaxMind = new RicercaTransfert_MaxMind_Connect();
    		RicercaTransfert_MaxMind_Info result = new RicercaTransfert_MaxMind_Info();
    		*/
    		//result = ricTrans_MaxMind.dammiINFO_MaxMind_Plugin( /* request.getRemoteAddr() //(lavorando in localHost non ritorna l'IP)*/ "94.37.246.57" );
    		
			

			
			/*
			File db = new File("src/main/resources/GeoLiteCity.dat");

			LookupService lookup = new LookupService(db, LookupService.GEOIP_MEMORY_CACHE);
			
			com.maxmind.geoip.Location locationServices = lookup.getLocation("94.37.246.57");
			
			
			System.out.println(locationServices.longitude);
			
			*/
			
			
			
			/*
			RichiestaAutistaParticolare richAutPart = new RichiestaAutistaParticolare();
			richAutPart = richiestaAutistaParticolareDao.getRichiestaAutista_by_IdRicerca_and_IdAuto(30l, 5l);
			
			System.out.println("prezzo: "+richAutPart.getPrezzo());
			*/
			/*
			int tentativi = 0;
			
			long t= System.currentTimeMillis();
    	    long end = t+10000;
    	    while(System.currentTimeMillis() < end) {
    	      // do something
    	      // pause to avoid churning
    	      Thread.sleep( 3000 );
    	      
    	      System.out.println("ciaooooo"+ ++tentativi);
    	    }
			*/
			
			
			
			// SMS SKEBBY
	        //String [] destinatario = new String[]{"393289126869",""};
	        //String [] destinatario = new String[]{"58 426 46 34 139",""};
	        //String testoSms = "ciao ciao ciao ciao";

	        // [send_sms_basic] con il basic mi fa vedere ogni volta un numero diverso (lasciare null gli ultimi due parametri)
	        // [send_sms_classic] con il classic mi fa vedere il mio numero di telefono (lasciare null gli ultimi due parametri altrimenti mi fa vedere un altro numero, ma sempre lo stesso)
	        // il penultimo e l'ultimo parametro sono per i clienti bisness (con partita iva) servono a personalizzare il mittente o con numero o lettere
	        //String result = SmsSkebby.SkebbyGatewaySendSMS(Constants.USERNAME_SKEBBY, Constants.PASSWORD_SKEBBY, destinatario, testoSms, "send_sms_classic", null, null);
	        //System.out.println("result: "+result);
	        
	        // SMS Basic dispatch ("send_sms_basic" costa di meno ma visualizza un numero di telefono farlocco: 393664610123)
		    // String result = skebbyGatewaySendSMS(username, password, recipients, "Hi Mike, how are you?", "send_sms_basic", null, null);
		     
		    // SMS CLASSIC dispatch with custom numeric sender ("send_sms_classic" costa di più  ma visualizza il mio numero di telefono 393289126869)
		    // String result = skebbyGatewaySendSMS(username, password, recipients, "Hi Mike, how are you?", "send_sms_classic", "", null);
	         
	        // SMS CLASSIC PLUS dispatch (with delivery report) with custom alphanumeric sender
	        // String result = skebbyGatewaySendSMS(username, password, recipients, "Hi Mike, how are you?", "send_sms_classic_report", null, "John");

	        // SMS CLASSIC PLUS dispatch (with delivery report) with custom numeric sender
	        // String result = skebbyGatewaySendSMS(username, password, recipients, "Hi Mike, how are you?", "send_sms_classic_report", "393471234567", null);

			// ------------------------------------------------------------------
			// Check the complete documentation at http://www.skebby.com/business/index/send-docs/
			// ------------------------------------------------------------------
			// For eventual errors see http:#www.skebby.com/business/index/send-docs/#errorCodesSection
			// WARNING: in case of error DON'T retry the sending, since they are blocking errors
			// ------------------------------------------------------------------	
	        
			
			
			
			// SMS VIANETT
			
				//new SmsVianettSender();
			
			

			// SMS CKIKATELL gli sms costano cari ed è confusa la vendita (ci sono di mezzo i crediti)
			// FUNZIONANO INSTALLANDO UN SOFTWARE IN LOCALE E CON UNA SIM
			//ClickatellHttp click = new ClickatellHttp("matteo1981", "3610855", "HPQRLFAbJAQTSF");
			//ClickatellHttp.Message response = click.sendMessage("+393289126869", "ciao porca madonna!");
			/*
	        String recipient = "+393289126869";
	        String message = "Hello World";
	        String username = "admin";
	        String password = "abc123";
	        String originator = "+393289126869";
	
	        String requestUrl  = "http://127.0.0.1:9501/api?action=sendmessage&" +
		    "username=" + URLEncoder.encode(username, Constants.ENCODING_UTF_8) +
		    "&password=" + URLEncoder.encode(password, Constants.ENCODING_UTF_8) +
		    "&recipient=" + URLEncoder.encode(recipient, Constants.ENCODING_UTF_8) +
		    "&messagetype=SMS:TEXT" +
		    "&messagedata=" + URLEncoder.encode(message, Constants.ENCODING_UTF_8) +
		    "&originator=" + URLEncoder.encode(originator, Constants.ENCODING_UTF_8) +
		    "&serviceprovider=GSMModem1" +
		    "&responseformat=html";
	
	        URL url = new URL(requestUrl);
	        HttpURLConnection uc = (HttpURLConnection)url.openConnection();
	        System.out.println(uc.getResponseMessage());
	        uc.disconnect();
			 */

			
			
			
			
			
			
			
			/*
			if (obj.getClass().equals(String.class)) {
				
			}
			*/
			
			
			/*
			String mylatPart = "41.88879379999999";
			String mylongPart = "12.5367552";
			String mylatArriv = "41.7998868";
			String mylongArriv = "12.2462384";
			*/
			
			/*
			String mylatPart = "41.9176778";
			String mylongPart = "12.3853809";
			String mylatArriv = "41.89864";
			String mylongArriv = "12.43222";
			
			
			RicercaTransfert RicTrans_info = new RicercaTransfert();
			RicTrans_info.setLat_Partenza( Double.parseDouble(mylatPart) );
			RicTrans_info.setLng_Partenza( Double.parseDouble(mylongPart) );
			RicTrans_info.setDataOraPrelevamento( "25/06/2016 00:33" );
			//43025: 45 min: 00
			//50,9 km: 44 min: 19
			
			RicTrans_info.setLat_Arrivo( Double.parseDouble(mylatArriv) );
			RicTrans_info.setLng_Arrivo( Double.parseDouble(mylongArriv) );
			
			RicercaTransfert_GoogleMaps_Connect aa = new RicercaTransfert_GoogleMaps_Connect();
			aa.dammiDistanzaMatrix_LAT_LNG(RicTrans_info, "it");
			*/
			
			
			/*
			String mylat = "43.75060450000001";
			String mylong = "11.7239661";

			RicercaTransfert_GoogleMaps_Info RicTrans_info = new RicercaTransfert_GoogleMaps_Info();
			RicTrans_info.setLat( Double.parseDouble(mylat) );
			RicTrans_info.setLng( Double.parseDouble(mylong) );
			
			RicercaTransfert_GoogleMaps_Connect aa = new RicercaTransfert_GoogleMaps_Connect();
			RicercaTransfert_GoogleMaps_Info RicTrans = aa.dammiInfoGoogle_by_LatLng(RicTrans_info);
			*/
			

			/*
			//var local = '${pageContext.request.locale.language}';
			Locale locale1 = new Locale("en", "US", "WIN");
			
			//System.out.println(locale1.getla getDisplayLanguage(new Locale("en")));
			
			String term = "La";
			List<Map<String, Serializable>> list = new LinkedList<Map<String, Serializable>>();
    		Map<String, Serializable> valueJson = null;
    		
			Iterator<Regioni> regioni_ite = regioniDao.getNomeRegioneBy_Like(term) .iterator();
			while (regioni_ite.hasNext()) {
				Regioni regione = regioni_ite.next();
				
				valueJson = new HashMap<String, Serializable>();
	    		valueJson.put("value", regione.getId() );
	    		valueJson.put("text", regione.getNomeRegione());
	    		list.add(valueJson);
			}
			
			String stringJson2 = new Gson().toJson( list );
			System.out.println(stringJson2);
			*/
			
			
			
			
			/*
			//leonardo da vinci: (41.7998868, 12.2462384);
			//aeroporto di olbia: (40.9022918, 9.515457499999998);
			// Aeroporto Federico Fellini (Rimini): 44.02288799999999 12.618819
			// Aeroporto regionale della Valle d'Aosta "Corrado Gex": (45.73885480000001, 7.366692000000001);
			List<Aeroporti> listAeroporti = ricercaTransfertDao.OrdinaAeroportiBy_Lat_Lng(45.73885480000001, 7.366692000000001);
			for(Aeroporti listAeroporti_ite: listAeroporti){
				Aeroporti aero = aeroportiDao.get( listAeroporti_ite.getId() );
				//System.out.println(aero.getNomeAeroporto());
				System.out.println(aero.getNomeAeroporto() +" "+ aero.getComuni().getProvince().getNomeProvincia() +" "+aero.getComuni().getRegioni().getNomeRegione());

	        }
			*/

			
			/*
			RicercaTransfert_GoogleMapsConnect googleMapsConn = new RicercaTransfert_GoogleMapsConnect();
			//List<Aeroporti> listAeroporti = aeroportiDao.getAeroportiBy_LIKE("Leonardo");
			List<Musei> listAeroporti = museiDao.getMusei();
			for(Musei aero_ite: listAeroporti){
				if( aero_ite.getLat() <= 0 && aero_ite.getLng() <= 0 ){
					String place_id = aero_ite.getPlaceId();
					place_id = place_id.replaceAll("\\s+","");
					
					RicercaTransfert_GoogleMapsPlace ric_GMP = new RicercaTransfert_GoogleMapsPlace();
					ric_GMP.setPlace_id( place_id );
					
					//RicercaTransfert_GoogleMapsPlace ris = new RicercaTransfert_GoogleMapsPlace();
					ric_GMP = googleMapsConn.dammiINFO_PlaceID_Details_Google_LAT_LNG( ric_GMP );
					
					System.out.println(aero_ite.getNomeMuseo());
					System.out.println("LAT: "+ric_GMP.getLat());
					System.out.println("LNG: "+ric_GMP.getLng());
					aero_ite.setLat( ric_GMP.getLat() );
					aero_ite.setLng( ric_GMP.getLng() );
					aero_ite.setPlaceId( place_id );
					museiDao.saveMusei( aero_ite );
				}
			}
			*/
		
			
			/*
			Locale locale = new Locale("en", "EN", "WIN");
			DateFormat formatter = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
			
			String datePattern = DateTimeFormatterBuilder.getLocalizedDateTimePattern(
					  FormatStyle.MEDIUM, FormatStyle.SHORT, IsoChronology.INSTANCE, 
					  locale); // or whatever Locale
			
			
			String pattern       = ((SimpleDateFormat)formatter).toPattern();
			String localPattern  = ((SimpleDateFormat)formatter).toLocalizedPattern();
			System.out.println(pattern);
			System.out.println(localPattern);
			System.out.println(datePattern);
			*/
			
			
			/*
			DateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm");
			Date data = format.parse( "25/06/2016 12:00" );
			List<Date> dateList = new ArrayList<Date>();
			dateList.add(data);
			
			
			List<Long> listIdZone = new ArrayList<Long>();
			listIdZone.add(5l);
			
			List<Long> listTipiAutoveicoli = new ArrayList<Long>();
			listTipiAutoveicoli.add(1l);
			
			boolean autoDisponibile = ricercaTransfertDao.Autoveicolo_Disponibe(4l, dateList );
			
			if(autoDisponibile){
				System.out.println("DISPONIBILE");
			}else{
				System.out.println("NON DISPONIBILE");
			}
			*/

			
			
			

			/*
			long metriLong = 26670l;
			BigDecimal metri = new BigDecimal(metriLong);
			BigDecimal tariffa = new BigDecimal("2.5");
			BigDecimal result = metri.multiply(tariffa);
			result = result.divide(new BigDecimal("1000"));
			result = result.setScale(2, RoundingMode.HALF_EVEN);
			System.out.println(result);
			*/
			
			/*
			String place_id_partenza = ""; String place_id_arrivo = "";
			String comune_partenza = "Empoli";
			String comune_arrivo = "Genova";

			//place_id_partenza = "ChIJBw4QAaRg0hIRNergiR_1JkY";
			//place_id_arrivo= "ChIJJeB4y5hA0xIRSZthX8u1H4M";
			
			//place_id_partenza = "ChIJBw4QAaRg0hIRNergiR_1JkY"; 
			place_id_arrivo= "ChIJp0gTOWhB0xIRnPYZsmXcAlc"; //porto di genova
			
			boolean andataRitorno = false;
			long idTipoAutoveicolo = 1l;
			int totaleKilometri = 700;
			
			Comuni comunePartenza = comuniDao.getComuniByNomeComune_Equal(comune_partenza);
			long idComPartenza = comunePartenza.getId();
			long idProvPartenza = comunePartenza.getProvince().getId();
			long idRegPartenza = comunePartenza.getRegioni().getId();
			
			Comuni comuneArrivo = comuniDao.getComuniByNomeComune_Equal(comune_arrivo);
			long idComArrivo = comuneArrivo.getId();
			long idProvArrivo = comuneArrivo.getProvince().getId();
			long idRegArrivo = comuneArrivo.getRegioni().getId();
			
			//INFRASTRUTTURE AEROPORTI e PORTI
			Aeroporti aeroportoPartenza = null;
			Aeroporti aeroportoArrivo = null;
			PortiNavali portoPartenza = null;
			PortiNavali portoArrivo = null;
			
			aeroportoPartenza = aeroportiDao.getAeroportoBy_PlaceId(place_id_partenza);
			aeroportoArrivo = aeroportiDao.getAeroportoBy_PlaceId(place_id_arrivo);
			portoPartenza = portiNavaliDao.getPortiNavaliBy_PlaceId(place_id_partenza);
			portoArrivo = portiNavaliDao.getPortiNavaliBy_PlaceId(place_id_arrivo);
			
			System.out.println(comune_partenza +" id comune partenza "+idComPartenza +" idProvPartenza "+idProvPartenza+" idRegPartenza "+idRegPartenza);
			System.out.println(comune_arrivo +" id comune arrivo "+idComArrivo + " idProvArrivo "+idProvArrivo+" idRegArrivo "+idRegArrivo);
			
			if(aeroportoPartenza!=null)
				System.out.println("partenza aeroporto trovato: "+aeroportoPartenza.getNomeAeroporto() +" id aeroporto:" +aeroportoPartenza.getId());
			if(aeroportoArrivo!=null)
				System.out.println("arrivo aeroporto trovato: "+aeroportoArrivo.getNomeAeroporto() +" id aeroporto:" +aeroportoArrivo.getId());
			if(portoPartenza != null)
				System.out.println("partenza porto navale trovato: "+portoPartenza.getNomePorto());
			if(portoArrivo != null)
				System.out.println("arrivo porto navale trovato: "+portoArrivo.getNomePorto());


			System.out.println("------------- RICERCA ZONE E INFRASTRUTTURE ------------------");
			List<BigDecimal> tariffaListBD = new ArrayList<BigDecimal>();
			List<AutistaAeroporti> autistiAeroportiList_partenza_e_arrivo = new ArrayList<AutistaAeroporti>();
			List<AutistaPortiNavali> autistiPortiNavaliList_partenza_e_arrivo = new ArrayList<AutistaPortiNavali>();
			if(aeroportoPartenza != null){
				List<AutistaAeroporti> listAutistaAero = ricercaTransfertDao.Ricerca_Autisti_ServizioStandard_AEROPORTI(aeroportoPartenza.getId());
				autistiAeroportiList_partenza_e_arrivo.addAll(listAutistaAero);
			}
			if(aeroportoArrivo != null){
				List<AutistaAeroporti> listAutistaAero = ricercaTransfertDao.Ricerca_Autisti_ServizioStandard_AEROPORTI(aeroportoArrivo.getId());
				autistiAeroportiList_partenza_e_arrivo.addAll(listAutistaAero);
			}
			if(portoPartenza != null){
				List<AutistaPortiNavali> listAutistaPorto = ricercaTransfertDao.Ricerca_Autisti_ServizioStandard_PORTINAVALI(portoPartenza.getId());
				autistiPortiNavaliList_partenza_e_arrivo.addAll(listAutistaPorto);
			}
			if(portoArrivo != null){
				List<AutistaPortiNavali> listAutistaPorto = ricercaTransfertDao.Ricerca_Autisti_ServizioStandard_PORTINAVALI(portoArrivo.getId());
				autistiPortiNavaliList_partenza_e_arrivo.addAll(listAutistaPorto);
			}
			
			List<AutistaZone> autistiZonaList_partenza_e_arrivo = new ArrayList<AutistaZone>();
			if(aeroportoPartenza == null && portoPartenza == null){
				System.out.println("ZONE PARTENZA OK");
				autistiZonaList_partenza_e_arrivo.addAll(
					ricercaTransfertDao.Ricerca_Autisti_ServizioStandard_ZONE(idComPartenza, idProvPartenza, idRegPartenza));
			}
			if(aeroportoArrivo == null && portoArrivo == null){
				System.out.println("ZONE ARRIVO OK");
				autistiZonaList_partenza_e_arrivo.addAll(
					ricercaTransfertDao.Ricerca_Autisti_ServizioStandard_ZONE(idComArrivo, idProvArrivo, idRegArrivo));
			}
			

			List<Tariffe> listTariffe = new ArrayList<Tariffe>();
			
			//zone
			if(!autistiZonaList_partenza_e_arrivo.isEmpty()){
				List<Long> listId_zone = new ArrayList<Long>();
				for(AutistaZone ite_zone: autistiZonaList_partenza_e_arrivo ){
					//System.out.println(ite_zone.getId());
					listId_zone.add(ite_zone.getId());
				}
				listTariffe.addAll(ricercaTransfertDao.Ricerca_Tariffe_AUTISTA_ID_ZONE(listId_zone));
			}
			//aeroporti
			if(!autistiAeroportiList_partenza_e_arrivo.isEmpty()){
				List<Long> listId_aeroporti = new ArrayList<Long>();
				for(AutistaAeroporti ite_aeroporto: autistiAeroportiList_partenza_e_arrivo ){
					//System.out.println(ite_zone.getId());
					listId_aeroporti.add(ite_aeroporto.getId());
				}
				listTariffe.addAll(ricercaTransfertDao.Ricerca_Tariffe_AUTISTA_ID_AEROPORTI(listId_aeroporti));
			}
			//porti
			if(!autistiPortiNavaliList_partenza_e_arrivo.isEmpty()){
				List<Long> listId_porti = new ArrayList<Long>();
				for(AutistaPortiNavali ite_porto: autistiPortiNavaliList_partenza_e_arrivo ){
					//System.out.println(ite_zone.getId());
					listId_porti.add(ite_porto.getId());
				}
				listTariffe.addAll(ricercaTransfertDao.Ricerca_Tariffe_AUTISTA_ID_PORTINAVALI(listId_porti));
			}

			
			
			System.out.println("------------- TARIFFA MEDIA TRANSFERT  ------------------");
			if(!listTariffe.isEmpty()){
				RisultatoRicerca_Autista_Tariffe risAutistaTariffe = new RisultatoRicerca_Autista_Tariffe();
				
				List<AutistaAutoveicolo> autistaAutoveic_list = new ArrayList<AutistaAutoveicolo>();
				List<AutistaAutoveicoloPrezzo> autistaAutoveicPrezzo_list = new ArrayList<AutistaAutoveicoloPrezzo>();
				
				for(Tariffe tariffe_ite : listTariffe){
					AutistaAutoveicolo autistaAutoveic = new AutistaAutoveicolo();

					System.out.print("nome autista: "+tariffe_ite.getAutista().getUser().getFirstName()
							+" autoveicolo: "+tariffe_ite.getAutoveicolo().getMarca() +" "+tariffe_ite.getAutoveicolo().getNomeModello()
							+" ID autoveicolo: "+tariffe_ite.getAutoveicolo().getId());
					if(tariffe_ite.getTariffeValori().getTariffaST() != null){
						tariffaListBD.add(tariffe_ite.getTariffeValori().getTariffaST());
						System.out.print(" tariffa: "+tariffe_ite.getTariffeValori().getTariffaST());
					}
					if(tariffe_ite.getTariffeValori().getTariffaAERO() != null){
						tariffaListBD.add(tariffe_ite.getTariffeValori().getTariffaAERO());
						System.out.print(" tariffa: "+tariffe_ite.getTariffeValori().getTariffaAERO());
					}
					if(tariffe_ite.getTariffeValori().getTariffaPORTO() != null){
						tariffaListBD.add(tariffe_ite.getTariffeValori().getTariffaPORTO());
						System.out.print(" tariffa: "+tariffe_ite.getTariffeValori().getTariffaPORTO());	
					}
					System.out.println("");
					
					
					autistaAutoveic.setAutista( tariffe_ite.getAutista() );
					autistaAutoveic.setAutoveicolo( tariffe_ite.getAutoveicolo() );
					
					autistaAutoveic_list.add( autistaAutoveic );
					
				}
				
				risAutistaTariffe.setResultMedioAutistaAutoveicoloPrezzo( util_BUKOWSKI.dammiMediaBigDecimal(tariffaListBD));
				risAutistaTariffe.setResultMedioAutistaAutoveicolo(autistaAutoveic_list);

			
			
				System.out.println("------------- TARIFFA PARTICOLARE TRANSFERT ------------------");
				
				
				List<Long> taroffaAutoID_list = new ArrayList<Long>();
				for(Tariffe tariffe_ite : listTariffe){
					taroffaAutoID_list.add(tariffe_ite.getAutoveicolo().getId());
				}
				
				
				List<Long> idAutoDuplicate_list = util_BUKOWSKI.DammiDuplicati(taroffaAutoID_list);
				
				// auto duplicate in result ricerca
				for(Long auto_ite : idAutoDuplicate_list){
					System.out.println("AUTO DUPLICATA: "+auto_ite);
					AutistaAutoveicoloPrezzo autistaAutoveicPrezzo = new AutistaAutoveicoloPrezzo();
					Autoveicolo auto = autoveicoloDao.get(auto_ite);
					System.out.println("- nome autista: "+auto.getAutista().getUser().getFirstName()
							+" autoveicolo: "+auto.getMarca() +" "+auto.getNomeModello()
							+" ID autoveicolo: "+auto.getId());
					
					List<BigDecimal> tariffaListAutoDuplicata = new ArrayList<BigDecimal>();
		
					for(Tariffe tariffe_ite : listTariffe){
						
						if(tariffe_ite.getAutoveicolo().getId() == auto_ite){
							if(tariffe_ite.getTariffeValori().getTariffaST() != null){
								tariffaListAutoDuplicata.add(tariffe_ite.getTariffeValori().getTariffaST());
								System.out.println("tariffa: "+tariffe_ite.getTariffeValori().getTariffaST());
							}
							if(tariffe_ite.getTariffeValori().getTariffaAERO() != null){
								tariffaListAutoDuplicata.add(tariffe_ite.getTariffeValori().getTariffaAERO());
								System.out.println("tariffa: "+tariffe_ite.getTariffeValori().getTariffaAERO());
							}
							if(tariffe_ite.getTariffeValori().getTariffaPORTO() != null){
								tariffaListAutoDuplicata.add(tariffe_ite.getTariffeValori().getTariffaPORTO());
								System.out.println("tariffa: "+tariffe_ite.getTariffeValori().getTariffaPORTO());	
							}
						}
						
						
					}
					
					autistaAutoveicPrezzo.setAutista( auto.getAutista() );
					autistaAutoveicPrezzo.setAutoveicolo( auto );
					autistaAutoveicPrezzo.setPrezzo( util_BUKOWSKI.dammiMediaBigDecimal(tariffaListAutoDuplicata) );
					
					autistaAutoveicPrezzo_list.add( autistaAutoveicPrezzo );
				}
				
				
			
				// auto non duplicate in result ricerca
				for(Tariffe tariffe_ite : listTariffe){
					if(!idAutoDuplicate_list.contains( tariffe_ite.getAutoveicolo().getId() ) ){
						AutistaAutoveicoloPrezzo autistaAutoveicPrezzo = new AutistaAutoveicoloPrezzo();
						System.out.println("- nome autista: "+tariffe_ite.getAutista().getUser().getFirstName()
								+" autoveicolo: "+tariffe_ite.getAutoveicolo().getMarca() +" "+tariffe_ite.getAutoveicolo().getNomeModello()
								+" ID autoveicolo: "+tariffe_ite.getAutoveicolo().getId());
						
						BigDecimal tariffa =  BigDecimal.ZERO;
						
						if(tariffe_ite.getTariffeValori().getTariffaST() != null){
							System.out.println("tariffa: "+tariffe_ite.getTariffeValori().getTariffaST());
							tariffa = tariffe_ite.getTariffeValori().getTariffaST();
						}
						if(tariffe_ite.getTariffeValori().getTariffaAERO() != null){
							System.out.println("tariffa: "+tariffe_ite.getTariffeValori().getTariffaAERO());
							tariffa = tariffe_ite.getTariffeValori().getTariffaAERO();
						}
						if(tariffe_ite.getTariffeValori().getTariffaPORTO() != null){
							System.out.println("tariffa: "+tariffe_ite.getTariffeValori().getTariffaPORTO());
							tariffa = tariffe_ite.getTariffeValori().getTariffaPORTO();
						}
						
						autistaAutoveicPrezzo.setAutista( tariffe_ite.getAutista() );
						autistaAutoveicPrezzo.setAutoveicolo( tariffe_ite.getAutoveicolo() );
						autistaAutoveicPrezzo.setPrezzo( tariffa );
			
						autistaAutoveicPrezzo_list.add( autistaAutoveicPrezzo );
					}
				}
				
				risAutistaTariffe.setResultParticolareAutistaAutoveicoloPrezzo( autistaAutoveicPrezzo_list );
				
				
			}
			*/
	
			
			
			

			
			/*
			------------------------------------------------------------------------------------------------
			WordUtils.capitalize("aa");
			String strin = "l'";
			List <Comuni> comuniList =  comuniDao.getNomeComuneBy_Like(strin);
			for(Comuni comune : comuniList){
					System.out.println(comune.getNomeComune());
					//comune.setNomeComune( comune.getNomeComune().replace("u'", "ù") );
					//String salvata = comuniDao.saveComuni(comune).getNomeComune();
					//System.out.println("----- ok salvato: "+salvata);
			}
			
			//List <Comuni> comuniList = comuniDao.getComuni();
			
			//for(Comuni comune : comuniList){
				
				//comune.setNomeComune( WordUtils.capitalize( comune.getNomeComune() ) );
				//comuniDao.saveComuni(comune);
				//System.out.println("ok salvato");
			//}
			*/

			/*
			HashMap<String, Object> JSONROOT = new HashMap<String, Object>();
			List<Aeroporti> aeroportiList = new ArrayList<Aeroporti>();
			
			// sorting: nomeAeroporto ASC
			aeroportiList = aeroportiDao.getAeroporti();
			JSONROOT .put("Result", "OK");
			JSONROOT.put("Records", aeroportiList);
			
			// Convert Java Object to Json
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			//------------------
			
			List<JSONObject> wee = new ArrayList<JSONObject>();
		    for(Aeroporti p : aeroportiList) {
		        JSONObject formDetailsJson = new JSONObject();
		        
		        formDetailsJson.put("id", p.getNomeAeroporto());
		        formDetailsJson.put("name", p.getComuni().getNomeComune());
		        wee.add(formDetailsJson);
		    }
		    
		    //responseDetailsJson.put("forms", jsonArray);
			System.out.println(wee);
			*/
			
			
			/*
			
			String language = "it";
			String input_luogo_partenza = "benevento";
			String input_luogo_arrivo = "frosinone";
			
			String input_luogo = "VIa Tora, 00021 Affile RM, Italia";
			
			
			System.out.println("-------------------- GooglePlaces ----------------------------");
			*/
			/*
			GooglePlaces client = new GooglePlaces( Constants.API_KEY );
			List<Place> places = client.getPlacesByQuery(input_luogo, GooglePlaces.MAXIMUM_RESULTS);
			for (Place place : places) {
				System.out.println(place.getName());
				}
			*/


			/*
			System.out.println("-------------------- Geocoder ----------------------------");
			
			String nomeCOMUNE = null;
			
			final Geocoder geocoder = new Geocoder();
			GeocoderRequest geocoderRequest = new GeocoderRequestBuilder() .setRegion("it").setLanguage("en") .setAddress(input_luogo).getGeocoderRequest();
			GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);

			if (geocoderResponse != null){
				for(GeocoderResult bbITE  : geocoderResponse.getResults() ){
					
					List<GeocoderAddressComponent> addressCompontent = bbITE. getAddressComponents();
					System.out.println("aa="+bbITE.getFormattedAddress());
					System.out.println("aa="+bbITE.getGeometry().getLocation().getLat());
					System.out.println("aa="+bbITE.getGeometry().getLocation().getLng());
					
					for(GeocoderAddressComponent bb : addressCompontent ){
						
						//bb.get
						
						System.out.println("  bb="+bb.getLongName());
						System.out.println("  bb="+bb.getShortName());

						
						List<String> cc = bb.getTypes();
						for(String typesString : cc ){
							System.out.println("    cc="+typesString);
						}
					}
				}
			}
			*/
			
			
			/*
			Geocoder geocoder = new Geocoder();
			GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(input_luogo).getGeocoderRequest();
			GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
			
			String nomeCOMUNE = null;
			List<String> types = geocoderResponse.getResults().get(0).getAddressComponents().get(0).getTypes();
			for (String type : types) {
				if ("locality".equals(type) ) {
					nomeCOMUNE = geocoderResponse.getResults().get(0).getAddressComponents().get(0).getLongName();
					//System.out.println(city);
				}
			}
			System.out.println("nomeCOMUNE="+nomeCOMUNE);
			*/
			
			/*
			List<String> types = geocoderResponse.getResults().get(0).getAddressComponents().get(0).getTypes();
			for (String type : types) {
				if ("locality".equals(type) ) {
					System.out.println(type);
				}
			}
			*/
			
			
			/*
			System.out.println("-------------------- GeoApiContext ----------------------------");
			
			GeoApiContext context = new GeoApiContext().setApiKey( Constants.API_KEY );
			GeocodingResult[] results =  GeocodingApi.geocode(context, input_luogo).region("IT"). await();
			
			for(GeocodingResult aa  : results){
				AddressComponent[] addressCompontent = aa.addressComponents;
				for(AddressComponent bb : addressCompontent ){
					//System.out.println(bb.longName);
					AddressComponentType[] cc = bb.types;
					for(AddressComponentType typesString : cc ){
						//System.out.println("types= "+typesString);
					}
				}
			}
			*/
			
			/*
			System.out.println("-------------------- GeoApiContext 2 ----------------------------");
			
			GeoApiContext context = new GeoApiContext().setApiKey( Constants.API_KEY );
			GeocodingApiRequest req = GeocodingApi. newRequest(context).address(input_luogo);

			GeocodingResult[] results2 = req.region("IT").await();
			
			for(GeocodingResult aa  : results2){
				AddressComponent[] addressCompontent = aa.addressComponents;
				for(AddressComponent bb : addressCompontent ){
					//System.out.println(bb.longName);
					AddressComponentType[] cc = bb.types;
					for(AddressComponentType typesString : cc ){
						//System.out.println("types= "+typesString);
					}
				}
			}
			*/
			
			
			/*
			Disponibilita disponibilita = disponibilitaDao.getDisponibilitaByAutoveicolo(1l);
			String date = "18-05-2016, 19-05-2016, 20-05-2016, 21-05-2016";
			
			for (DisponibilitaDate lisDispDateITE : disponibilita.getDisponibilitaDate()) {
				if ( !ListDateNuoveLong(date).contains( lisDispDateITE.getData().getTime() ) ){
					System.out.println("NON PRESENTE ELIMINO"+lisDispDateITE.getData());
					//ELIMINO
				}
			}
			
			System.out.println("------------------");
			
			Set<DisponibilitaDate> dateDispDate = disponibilita.getDisponibilitaDate();

			DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
			String[] parts = date.split(",");
			for( String dataString : parts ) {
				Date data = format.parse(dataString);
				if(!ListDateEsistentiLong(dateDispDate).contains( data.getTime() )){
					//System.out.println("DATA NON PRESENTE AGGIUNGO="+data);
					System.out.println(data.getTime() );
					// AGGIUGO
				}
				
			}
			*/
			
			
			
			
			
			
			
			
			
			/*
			DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
			
			DisponibilitaDate disDate = new DisponibilitaDate();
			
			Date data = format.parse("24-01-1981");
			disDate.setData(data);
			disDate.setDisponibilita(disponibilita);
			SalvaDate(disDate);
			
			data = format.parse("25-01-1981");
			disDate.setData(data);
			disDate.setDisponibilita(disponibilita);
			SalvaDate(disDate);
			*/
			
			
			
				
			
			//List<DisponibilitaDate> dateHasSet = new ArrayList<DisponibilitaDate>();
			
			//Date data1 = format.parse("20-01-1981");
			//dateHasSet.add( new DisponibilitaDate(data1, disponibilita )); 
			
			//Date data2 = format.parse("21-01-1981");
			//dateHasSet.add(new DisponibilitaDate(data2, disponibilita )); 
			
			/*
			Date data3 = format.parse("24-01-1981");
			disponibilita.addDisponibilitaDate(new DisponibilitaDate(data3, disponibilita )); 
			SalvaDate(disponibilita);
			
			Date data4 = format.parse("25-01-1981");
			disponibilita.addDisponibilitaDate(new DisponibilitaDate(data4, disponibilita )); 
			SalvaDate(disponibilita);
			*/
			
			
			/*
			Date data = format.parse("23-01-1981");
			disponibilita.addDisponibilitaDate( new DisponibilitaDate(data, disponibilita ) );
			SalvaDate(disponibilita);
			
			data = format.parse("24-01-1981");
			disponibilita.addDisponibilitaDate( new DisponibilitaDate(data, disponibilita ) );
			SalvaDate(disponibilita);
			
			data = format.parse("25-01-1981");
			disponibilita.addDisponibilitaDate( new DisponibilitaDate(data, disponibilita ) );
			SalvaDate(disponibilita);
			
			data = format.parse("27-01-1981");
			disponibilita.addDisponibilitaDate( new DisponibilitaDate(data, disponibilita ) );
			SalvaDate(disponibilita);
			*/
			
			
			/*
			try{
				String aa = "111..537";
				aa = aa.replace(",", ".");
				
				BigDecimal payment = new BigDecimal(aa);
				System.out.println(payment);
				
			}catch(NumberFormatException nfe){
				System.out.println("NumberFormatException nfe");
				System.out.println(new BigDecimal("0"));
			}
			*/

			
			/*
			IntegerValidator aa  = new IntegerValidator();
			
			
			String bb = "";
			
			
			int ok = aa.validate(bb);
			
			System.out.println(ok);
			*/
			
			/*
			List<Autista> autitaList = autistaDao.getAutistaByAutistaZone(1l);

			Iterator<Autista> autista_ite = autitaList.iterator();
			while(autista_ite.hasNext()){
				Autista autista = autista_ite.next();
				
				List <Tariffe_Zone> zzzz = autista.getTariffe_Zone_List();
				Iterator<Tariffe_Zone> zzz_ite = zzzz.iterator();
				while(zzz_ite.hasNext()){
					Tariffe_Zone oggettone = zzz_ite.next();
					AutistaZone zone = oggettone.getAutistaZona();
					AutistaAeroporti autistaAero = oggettone.getAutistaAeroporto();
					
					if(zone != null)
						System.out.println("ZONA COMUNE="+zone.getId());
					
					if(autistaAero.getAeroporti() != null)
						System.out.println(autistaAero.getAeroporti().getNomeAeroporto() );
					
					List <Tariffe_AutoveicoloTariffa> autoveicoloTariffaList = oggettone.getTariffe_AutoveicoliTariffeList();
					System.out.println("AUTO TOT="+autoveicoloTariffaList.size());
					Iterator<Tariffe_AutoveicoloTariffa> autoveic_tariffe_ite = autoveicoloTariffaList.iterator();
					while(autoveic_tariffe_ite.hasNext()){
						Tariffe_AutoveicoloTariffa autoTariffa = autoveic_tariffe_ite.next();
						
						//System.out.println( zone.getId() );
						System.out.println( autoTariffa.getAutoveicolo().getMarca() );
						System.out.println(autoTariffa.getTariffaST());
						System.out.println(autoTariffa.getTariffaLP());
						
						
						System.out.println(autoTariffa.getTariffaAERO());
					}
					*/
					
					
					
					/*
					AutistaAeroporti aero = oggettone.getAutistaAeroporto();
					System.out.println("ZONA COMUNE="+zone.getId());
					
					autoveicoloTariffaList = oggettone.getTariffe_AutoveicoliTariffeList();
					System.out.println("AUTO TOT="+autoveicoloTariffaList.size());
					autoveic_tariffe_ite = autoveicoloTariffaList.iterator();
					while(autoveic_tariffe_ite.hasNext()){
						Tariffe_AutoveicoloTariffa autoTariffa = autoveic_tariffe_ite.next();
						
						//System.out.println( zone.getId() );
						System.out.println( autoTariffa.getAutoveicolo().getMarca() );
						System.out.println(autoTariffa.getTariffaST());
						System.out.println(autoTariffa.getTariffaLP());

					}

				}

			}
			*/	
			
			
			/*
			List<Autista> autitaList = autistaDao.getAutistaByAutistaZone(1l);

			Iterator<Autista> autista_ite = autitaList.iterator();
			while(autista_ite.hasNext()){
				Autista autista = autista_ite.next();
				
				List <AutistaZoneAutoveicoloTariffe> zzzz = autista.getAutistaZoneAutoveicoloTariffe();
				Iterator<AutistaZoneAutoveicoloTariffe> zzz_ite = zzzz.iterator();
				while(zzz_ite.hasNext()){
					
					AutistaZoneAutoveicoloTariffe oggettone = zzz_ite.next();
					
					AutistaZone zone = oggettone.getAutistaZone();
					System.out.println("ZONA COMUNE="+zone.getComuni().getNomeComune());
					
					List <Autoveicolo> autoveicoloList = oggettone.getAutoveicoloList();
					System.out.println("AUTO TOT="+autoveicoloList.size());
					Iterator<Autoveicolo> autoveic_ite = autoveicoloList.iterator();
					while(autoveic_ite.hasNext()){
						Autoveicolo auto = autoveic_ite.next();
						
						System.out.println(auto.getMarca());

					}
				}

			}
			
			*/
			

			
			
			/*
			GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyBNMf1MX3XWwW48V76Ov6i8edNvGRzm_uY");
			GeocodingResult[] results =  GeocodingApi.geocode(context,
			    "prenestina, roma").await();
			
			
			String[] origins = new String[]{
			        "leonardo da vinci"
			    };
			    String[] destinations = new String[]{
			        "roma"
			    };
			
			    
			DistanceMatrix aa = DistanceMatrixApi.newRequest(context).origins(new LatLng(-31.9522, 115.8589), new LatLng(-37.8136, 144.9631))
				.destinations(new LatLng(-25.344677, 131.036692),new LatLng(-13.092297, 132.394057)).awaitIgnoreError();
			
			DistanceMatrix matrix =
			        DistanceMatrixApi.getDistanceMatrix(context, origins, destinations).await();
			

			
			System.out.println(results[0].formattedAddress);
			
			System.out.println(matrix.rows.length);
			
			System.out.println(matrix.rows[0].elements.length);
			
			
			
			
			
			
			long metri = matrix.rows[0].elements[0].distance.inMeters;
			
			double distDouble = (double)metri;
			
					
			distDouble = distDouble / 1000;
			
			
			distDouble = Math.round(distDouble);
			
			System.out.println(distDouble);
			*/
			
			
			
			/*
			Iterator<Aeroporti> aroporti_ite = aeroportiDao.getAeroporti().iterator();
			while(aroporti_ite.hasNext()){
				Aeroporti aero = aroporti_ite.next();
				
				String aeroNuovo = "Aeroporto di "+aero.getNomeAeroporto();
				
				aero.setNomeAeroporto(aeroNuovo);
				
				aeroportiDao.saveAeroporti(aero);
			}
			*/
			
			/*
			Iterator<PortiNavali> portiNavai_ite = portiNavaliDao.getPortiNavali().iterator();
			while(portiNavai_ite.hasNext()){
				PortiNavali porto = portiNavai_ite.next();
				String nomePorto = porto.getSiglaPorto();
				String mystring = nomePorto;
				String arr[] = mystring.split(" ", 2);
				String firstWord = arr[0]; 
				Comuni comune = comuniDao.getComuniByNomeComune_Equal("zzzzz");
				//List<Comuni> listComuni = comuniDao.getComuniByNomeComune_Like(nomePorto);
				
				if(comune != null){

					//eroporto.setProvince( listComuni.get(0).getProvince() );
					//eroporto.setProvince(comune.getProvince());
					//eroporto.setComuni( listComuni.get(0) );
					porto.setComuni( comune );
					//portiNavaliDao.savePortiNavali(porto);
					System.out.println("------salvato-----");

				}else{
					
					System.out.println(nomePorto);
					String okString = "";
					//String okString = StringUtils.strip(nomePorto, null);
					
					//okString = StringUtils.replace(nomePorto, ".", "");
					//okString = StringUtils.replace(nomePorto, ",", "");
					
					//System.out.println(okString);
					
					String appo = porto.getSiglaPorto();
					
					
					if(appo.equals("dddd"))
						porto.setSiglaPorto("");
					
					
					
					
					portiNavaliDao.savePortiNavali(porto);
					System.out.println("salvato");


				
				}

			}
			
			*/
			
			
			
			
			
			
			
			
			
			
			/*
			ArrayList<Long> list = new ArrayList<>();
			list.add(1l);
			list.add(3l);
			list.add(3l);
			list.add(5l);
			list.add(2l);
			list.add(1l);

			// Remove duplicates from ArrayList of Strings.
			ArrayList<Long> unique = util_BUKOWSKI.removeDuplicatesLong(list);
			for (Long element : unique) {
			    System.out.println(element);
			}
			*/
			
			/*
			String string = "REG#122";
	        String[] parts = string.split("#");
	        String id = parts[1];
	        
	        
			System.out.println(string.split("#")[0]);
			*/


			
			/*
			List<LabelValue> listZoneMiste = new ArrayList<LabelValue>();
			
			listZoneMiste.add(new LabelValue("ITALIA TUTTA", "ITALIA#0"));

			Iterator<Regioni> regioni_ite = regioniDao.getRegioni().iterator();
			while(regioni_ite.hasNext()){
				Regioni regioni = regioni_ite.next();
				listZoneMiste.add(new LabelValue(regioni.getNomeRegione().toUpperCase(), "REG#"+regioni.getId()));
				
				Iterator<Province> province_ite = provinceDao.getProvinceByIdRegione(regioni.getId()) .iterator();
				while(province_ite.hasNext()){
					Province province = province_ite.next();
					listZoneMiste.add(new LabelValue(province.getNomeProvincia()+" (PROVINCIA)", "PRO#"+province.getId()));
					
					Iterator<Comuni> comuni_ite = comuniDao.getComuniByIdProvincia(province.getId()).iterator();
					while(comuni_ite.hasNext()){
						Comuni comuni = comuni_ite.next();
						if(!province.getNomeProvincia().equals(comuni.getNomeComune())){
							listZoneMiste.add(new LabelValue( WordUtils.capitalize(comuni.getNomeComune()) , "COM#"+comuni.getId()));
						}
					}
				}
			}
			

			
			
			
			
			System.out.println("........RISULTATO........");
			Iterator<LabelValue> listZoneMiste_ite = listZoneMiste.iterator();
			while(listZoneMiste_ite.hasNext()){
				LabelValue labelValue = listZoneMiste_ite.next();
				
				System.out.println(labelValue.getValue() +" "+labelValue.getLabel());
			}
				
			*/
				
				
				
				
			/*
			Iterator<Aeroporti> aeroporti_ite = aeroportiDao.getAeroporti().iterator();
			while(aeroporti_ite.hasNext()){
				Aeroporti eroporto = aeroporti_ite.next();
				String nomeAeroporto = eroporto.getNomeAeroporto();
				String mystring = nomeAeroporto;
				String arr[] = mystring.split(" ", 2);
				String firstWord = arr[0]; 
				Comuni comune = comuniDao.getComuniByNomeComune_Equal(nomeAeroporto);
				//List<Comuni> listComuni = comuniDao.getComuniByNomeComune_Like(firstWord);
				if(comune != null){
					if(eroporto.getComuni() == null ){
						//eroporto.setProvince( listComuni.get(0).getProvince() );
						//eroporto.setProvince(comune.getProvince());
						//eroporto.setComuni( listComuni.get(0) );
						eroporto.setComuni( comune );
						aeroportiDao.saveAeroporti(eroporto);
						System.out.println("salvato");
					}
				}else{
					//eroporto.setProvince( provinceDao.get(1l) );
					//aeroportiDao.saveAeroporti(eroporto);
				}
				
				//System.out.println(eroporto.getNomeAeroporto());

			}
			*/
			
			
			/*
			//List <AutistaZone> autistaZone = autistaZoneDao.getAutistaZoneByAutista(1l);
			AutistaZone aa = autistaZoneDao.getAutistaZoneBy_Autista_e_Provincia(1l, 72l);
			System.out.println(aa.getProvince().getNomeProvincia());
			
			
			//List <Autista> ccccc =  autistaDao.getAutistaZoneByAutistaZone(1l);
			
			//List <AutistaZone> autistaZone = new ArrayList<AutistaZone>();
					
			Autista bb = autistaDao.get(1l);
			
			System.out.println(bb.getNumeroPatende());
			
			Iterator<AutistaZone> autistaZone_ite = bb.getAutistaZone().iterator();
			while(autistaZone_ite.hasNext()){
				String ss = autistaZone_ite.next().getProvince().getNomeProvincia();
				System.out.println(ss);
				
				
			}
			
			*/
			
			/*
			Province prov = provinceDao.get(59l);
			prov.setNomeProvincia("Forlì Cesena");
			Province provNuova = provinceDao.saveProvince(prov);
			System.out.println(provNuova.getNomeProvincia());
			Iterator<Province> province_ite = provinceDao.getProvinceOrdineNomeRegioneNomeProvincia_perSelectChoseBootstrap().iterator();
			while (province_ite.hasNext()) {
				Province obj = province_ite.next();
				String modello = obj.getNomeProvincia();
				String id = obj.getSiglaProvincia();
				System.out.println(id +" "+modello);
			}
			*/
			
			/*
			MarcaAutoveicolo marca = marcaAutoveicoloDao.getNomeMarcaAutoveicolo("ACurA");
			System.out.println(marca.getTitle());
			
			
			ModelloAutoveicolo Modello = modelloAutoveicoloDao.getNomeModelloAutoveicolo("tiburon", 28l);
			System.out.println(Modello.getTitle());
			System.out.println(Modello.getCode());
			*/
			
/*
			Iterator<ModelloAutoveicolo> annunci_adozione_ite = modelloAutoveicoloDao.getModelloAutoveicoloDescrizione("spi", "Alfa Romeo").iterator();
			while (annunci_adozione_ite.hasNext()) {
				ModelloAutoveicolo objModello = annunci_adozione_ite.next();
				
				
				String modello = objModello.getTitle();
				String id = objModello.getMarcaAutoveicolo().getId().toString();
				
				System.out.println(id +" "+modello);

			}
	*/		
			
			
			//List<ModelloAutoveicolo> listaModello = modelloAutoveicoloDao.getModelloAutoveicoloDescrizione("525");
			/*
			Iterator<ModelloAutoveicolo> annunci_adozione_ite = listaMarca.iterator();
			while (annunci_adozione_ite.hasNext()) {
				ModelloAutoveicolo objModello = annunci_adozione_ite.next();
				
				String modello = objModello.getTitle();
				String marca = objModello.getMarcaAutoveicolo().getTitle();

				System.out.println(marca+ " " +modello);

			}
			*/
			
			DammiTempoOperazione.DammiSecondi(startTime, "FINE_LAB- ");
		}catch(Exception ex) {
			System.out.println("Exception PROVE_PORCO_DIO");
			ex.printStackTrace();
		}
	}


	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}




	
	
	
}
