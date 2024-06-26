package com.apollon.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.velocity.VelocityEngineUtils;
import com.apollon.Constants;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

/**
 * Class for sending e-mail messages based on Velocity templates
 * or with attachments.
 *
 * @author Matt Raible
 */
public class MailEngine {
    private final Log log = LogFactory.getLog(MailEngine.class);
    private static MailSender mailSender;
    private static VelocityEngine velocityEngine;
    private static String defaultFrom;

    public void setMailSender(MailSender mailSender) {
        MailEngine.mailSender = mailSender;
    }

    public MailSender getMailSender() {
        return mailSender;
    }

    public void setVelocityEngine(VelocityEngine velocityEngine) {
        MailEngine.velocityEngine = velocityEngine;
    }

    public void setFrom(String from) {
        MailEngine.defaultFrom = from;
    }
    
    
    
    protected static void sendMessageHtml(String[] recipients, String sender, String subject, String templateName, Map<String, Object> model) throws MessagingException {
        String htmlBody = null;
    	MimeMessage message = ((JavaMailSenderImpl) mailSender).createMimeMessage();
    	htmlBody = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templateName, Constants.ENCODING_UTF_8, model);
    	MimeMessageHelper helper = new MimeMessageHelper(message);
    	helper.setTo(recipients);
        // use the default sending if no sender specified
        if (sender == null) {
            helper.setFrom(defaultFrom);
        } else {
           helper.setFrom(sender);
        }
    	
        helper.setText(htmlBody, true);
        helper.setSubject(subject);

        
        /*
    	 * non lo uso più vedere email tiket di mondoserver.it
    	 * 
        try {
			message = DKIMSigner.MainIntestazioneMailDKIM(message);
		} catch (Exception e) {
			e.printStackTrace();
			
		}
        */
        message = ((JavaMailSenderImpl) mailSender).createMimeMessage();
        
        ((JavaMailSenderImpl) mailSender).send(message);
    }


    /**
     * Convenience method for sending messages with attachments.
     *
     * @param recipients array of e-mail addresses
     * @param sender e-mail address of sender
     * @param resource attachment from classpath
     * @param bodyText text in e-mail
     * @param subject subject of e-mail
     * @param attachmentName name for attachment
     * @throws MessagingException thrown when can't communicate with SMTP server
     */
    public void sendMessageHtmlAttachment(String[] recipients, String sender, ClassPathResource resource, String bodyText, String subject, 
    		String attachmentName) throws MessagingException {
    	
        MimeMessage message = ((JavaMailSenderImpl) mailSender).createMimeMessage();
        
        // use the true flag to indicate you need a multipart message
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(recipients);

        // use the default sending if no sender specified
        if (sender == null) {
            helper.setFrom(defaultFrom);
        } else {
           helper.setFrom(sender);
        }

        helper.setText(bodyText);
        helper.setSubject(subject);

        helper.addAttachment(attachmentName, resource);


        /*
    	 * non lo uso più vedere email tiket di mondoserver.it
    	 * 
        try {
			message = DKIMSigner.MainIntestazioneMailDKIM(message);
		} catch (Exception e) {
			e.printStackTrace();
			message = ((JavaMailSenderImpl) mailSender).createMimeMessage();
		}
        */
        
        message = ((JavaMailSenderImpl) mailSender).createMimeMessage();
        
        
        ((JavaMailSenderImpl) mailSender).send(message);
    }
    
    
    /**
     * Send a simple message based on a Velocity template.
     * @param msg the message to populate
     * @param templateName the Velocity template to use (relative to classpath)
     * @param model a map containing key/value pairs
     */
    public void sendMessageText(SimpleMailMessage msg, String templateName, Map<String, Object> model) {
        String result = null;
        try {
            result = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templateName, Constants.ENCODING_UTF_8, model);
        } catch (VelocityException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        msg.setText(result);
        sendSimple(msg);
    }
    
    /**
     * Send a simple message with pre-populated values.
     * @param msg the message to send
     * @throws org.springframework.mail.MailException when SMTP server is down
     */
    public void sendSimple(SimpleMailMessage msg) throws MailException {
        try {
            mailSender.send(msg);
        } catch (MailException ex) {
            log.error(ex.getMessage());
            throw ex;
        }
    }
}
