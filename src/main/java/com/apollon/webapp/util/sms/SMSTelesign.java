package com.apollon.webapp.util.sms;

import com.telesign.MessagingClient;
import com.telesign.RestClient;
import com.telesign.Util;
import com.telesign.VoiceClient;
/**
 *
 * VEDERE:
 * 
 * 
 * 	Invio sms e voice
	https://portal.telesign.com/portal/dashboard
	https://portal.telesign.com/portal/pricing
	
	
	Da qui c'è il servizio di rapidapi.com che fornise il solo servizio di invio sms per verifica, cioè invia il messaggio col solo codice senza poter aggiungere il testo.
	E ha prezzi molto più bassi dell'inviare l'SMS da telesign.com 
	https://rapidapi.com/telesign/api/telesign-sms-verify/pricing
 * 
 * 
 * @author Matteo
  */
public class SMSTelesign {

	
	public static void InviaSMSTest() {
		
	
		 String customerId = "8D8B1E9B-EAD7-4E03-810B-1D9BB3C831B0";
	     String apiKey = "cmjuG2m4WSaXp2kCQPUoarSiD1F+fYk7zhnAlyCg4LVhu+n8g+l0Q0XDd9iqENAhI36ZmPjrvmbbKa7nm7x3DQ==";
	     String phoneNumber = "393289126869";
	     String verifyCode = Util.randomWithNDigits(5);
	     String message = String.format("Your code is %s", verifyCode);
	     String messageType = "OTP";
	
	     try {
	         MessagingClient messagingClient = new MessagingClient(customerId, apiKey);
	         RestClient.TelesignResponse telesignResponse = messagingClient.message(phoneNumber, message, messageType, null);
	     } catch (Exception e) {
	         e.printStackTrace();
	     }
     
	}
	
	
	public static void Invia_VOICE_Test() {

        String customerId = "8D8B1E9B-EAD7-4E03-810B-1D9BB3C831B0";
        String apiKey = "cmjuG2m4WSaXp2kCQPUoarSiD1F+fYk7zhnAlyCg4LVhu+n8g+l0Q0XDd9iqENAhI36ZmPjrvmbbKa7nm7x3DQ==";
        String phoneNumber = "393289126869"; // matteo "393289126869"; // silvia "393934040437"
        String verifyCode = Util.randomWithNDigits(5);
        String message = String.format("Silvia, non fare la zoccola e cucina bene stasera, Skiava!", verifyCode, verifyCode);
        String messageType = "OTP";

        try {
            VoiceClient voiceClient = new VoiceClient(customerId, apiKey);
            RestClient.TelesignResponse telesignResponse = voiceClient.call(phoneNumber, message, messageType, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	
}
