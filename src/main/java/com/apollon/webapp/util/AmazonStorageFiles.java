package com.apollon.webapp.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.glacier.transfer.ArchiveTransferManager;
import com.amazonaws.services.glacier.transfer.UploadResult;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.BucketVersioningConfiguration;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.GetBucketLocationRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.SetBucketVersioningConfigurationRequest;
import com.apollon.Constants;
import com.apollon.model.Autista;
import com.apollon.model.AutistaLicenzeNcc;
import com.apollon.model.Autoveicolo;
import com.apollon.model.DocumentiCap;
import com.apollon.model.DocumentiIscrizioneRuolo;
import com.apollon.model.DocumentiPatente;
import com.apollon.util.UtilBukowski;


/**
 * @author Matteo - matteo.manili@gmail.com
 *	
 *	Classe per trattare lo Storage dei dati sui Server di Amazon: Amazon Storage Service (S3) 
 *	utilizzo le api java (Attenzione se utilizzo la versione successiva la 1.11.* mi da errori)
 *	Utilizzare questa:
 *	<dependency>
	    <groupId>com.amazonaws</groupId>
	    <artifactId>aws-java-sdk</artifactId>
	    <version>1.10.77</version>
	</dependency>
 *	
 *	Console file: https://console.aws.amazon.com/s3/home?region=eu-central-1
 *
 *	vedere documetazione: 
 *	http://docs.aws.amazon.com/AmazonS3/latest/dev/UsingBucket.html (contenitore)
 *	http://docs.aws.amazon.com/AmazonS3/latest/dev/UsingObjects.html (gestione dei file)
 *	
 *	vedere prezzi:
 *	https://aws.amazon.com/it/s3/pricing/
 */
public final class AmazonStorageFiles extends ApplicationUtils {
	
	private static final Log log = LogFactory.getLog(AmazonStorageFiles.class);

	/*
	// vecchio codice che non funziona con le ultime versioni
	 * <groupId>com.amazonaws</groupId>
	 * <artifactId>aws-java-sdk</artifactId>
	 * <version>1.11.*</version>
	 * 
	AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();
	This is the same as using standard followed by build.

	AmazonEC2 ec2 = AmazonEC2ClientBuilder.standard()
	                    .build();
	*/
	
	//dynamo.setRegion(Region.getRegion(Regions.US_WEST_2));
	/*
	BasicAWSCredentials creds = new BasicAWSCredentials(AccessKeyID, SecretAccessKey); 
	AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(creds))
			.withRegion( Regions.CA_CENTRAL_1 )
			//.withRegion("<aws-region>")
			.build();
	 */
	
	private static final String NomeFile_AmazonWebService(Object objectModel, String objectModelFileName){
		
		if(objectModel instanceof DocumentiPatente){
			DocumentiPatente documentiPatente = (DocumentiPatente) objectModel;
			String folderFile = documentiPatente.getClass().getName() + "/" + documentiPatente.getId().toString();
			return folderFile +"/"+ objectModelFileName;
		}
		if(objectModel instanceof DocumentiCap){
			DocumentiCap documentiCap = (DocumentiCap) objectModel;
			String folderFile = documentiCap.getClass().getName() + "/" + documentiCap.getId().toString();
			return folderFile +"/"+ objectModelFileName;
		}
		if(objectModel instanceof DocumentiIscrizioneRuolo){
			DocumentiIscrizioneRuolo documentiIscrizioneRuolo = (DocumentiIscrizioneRuolo) objectModel;
			String folderFile = documentiIscrizioneRuolo.getClass().getName() + "/" + documentiIscrizioneRuolo.getId().toString();
			return folderFile +"/"+ objectModelFileName;
		}
		if(objectModel instanceof AutistaLicenzeNcc){
			AutistaLicenzeNcc autistaLicenzeNcc = (AutistaLicenzeNcc) objectModel;
			String folderFile = autistaLicenzeNcc.getClass().getName() + "/" + autistaLicenzeNcc.getId().toString();
			return folderFile +"/"+ objectModelFileName;
		}
		if(objectModel instanceof Autoveicolo){
			Autoveicolo autoveicolo = (Autoveicolo) objectModel;
			String folderFile = autoveicolo.getClass().getName() + "/" + autoveicolo.getId().toString();
			return folderFile +"/"+ objectModelFileName;
		}
		if(objectModel instanceof Autista){
			Autista autista = (Autista) objectModel;
			String folderFile = autista.getClass().getName() + "/" + autista.getId().toString();
			return folderFile +"/"+ objectModelFileName;
		}
		return null;
	}
	
	/**
	 * Mi ritornano le credenziali per lavorare con Amazon Storage Service (S3)
	 * 
	 * Amazon S3 crea Bucket in una regione specificata. È possibile scegliere qualsiasi Regione AWS che è geograficamente vicino a voi per ottimizzare 
	 * la latenza, ridurre al minimo i costi, o di soddisfare i requisiti normativi. Ad esempio, se si risiede in Europa, si potrebbe trovare vantaggioso 
	 * per creare Bucket nella UE (Irlanda) o le regioni dell'Unione europea (Francoforte). Per un elenco delle regioni Amazon S3, andare a 
	 * Regioni ed endpoint in AWS generale di riferimento.
	 */
	private static final AmazonS3 DammiCredenziali_AmazonWebService(){
		try{
			AWSCredentials credentials = new BasicAWSCredentials(Constants.AMAZON_STORE_ACCESS_KEY_ID, Constants.AMAZON_STORE_SECRET_ACCESS_KEY);
			AmazonS3 s3Client = new AmazonS3Client(credentials);
	        // SETTARE LA REGIONE (IL SERVER AMAZON DOVE VANNO TRATTATI I BUCKET e i FILE)
			Region regionS3 = Region.getRegion(Regions.EU_CENTRAL_1);
	        s3Client.setRegion(regionS3);
	        
	        
	        // ATTIVO IL VERSIONAMENTO, CAPIRE COME FUNZIONA, FORSE PUO' ESSERE' UTILE PER MODIFICARE I DOCUMENTI E TENERNE TRACCIA
	        /*
	        BucketVersioningConfiguration configuration = new BucketVersioningConfiguration().withStatus("Enabled");
			SetBucketVersioningConfigurationRequest setBucketVersioningConfigurationRequest = 
					new SetBucketVersioningConfigurationRequest( DammiNomeBucket(), configuration );
			s3Client.setBucketVersioningConfiguration(setBucketVersioningConfigurationRequest);
	        */
	        
	        // SE NON ESISTONO CREO I BUCKET DI TEST E PRODUZIONE
	        if(!(s3Client.doesBucketExist(Constants.AMAZON_STORE_BUCKET_PROD_NAME))) {
	        	// Note that CreateBucketRequest does not specify region. So bucket is 
	        	// created in the region specified in the client.
	        	s3Client.createBucket(new CreateBucketRequest( Constants.AMAZON_STORE_BUCKET_PROD_NAME ));
	        	log.debug("BucketCreato: "+Constants.AMAZON_STORE_BUCKET_PROD_NAME);
	        	log.debug("bucket location = " + s3Client.getBucketLocation(new GetBucketLocationRequest( Constants.AMAZON_STORE_BUCKET_PROD_NAME )));
	        }
			if(!(s3Client.doesBucketExist(Constants.AMAZON_STORE_BUCKET_TEST_NAME))) {
	        	// Note that CreateBucketRequest does not specify region. So bucket is 
	        	// created in the region specified in the client.
	        	s3Client.createBucket(new CreateBucketRequest( Constants.AMAZON_STORE_BUCKET_TEST_NAME ));
	        	log.debug("BucketCreato: "+Constants.AMAZON_STORE_BUCKET_TEST_NAME);
	        	log.debug("bucket location = " + s3Client.getBucketLocation(new GetBucketLocationRequest( Constants.AMAZON_STORE_BUCKET_TEST_NAME )));
	        }
	        
			/*
	        log.debug("lista dei bucket presenti:");
			for (Bucket bucket : s3Client.listBuckets()) {
				log.debug("nomeBucket: "+ bucket.getName());
			}
			*/

	        return s3Client;
        
		}catch(AmazonServiceException ase){
			log.debug("Caught an AmazonServiceException, " + "which means your request made it " + 
	        		"to Amazon S3, but was rejected with an error response " + "for some reason.");
			log.debug("Error Message:    " + ase.getMessage());
			log.debug("HTTP Status Code: " + ase.getStatusCode());
			log.debug("AWS Error Code:   " + ase.getErrorCode());
			log.debug("Error Type:       " + ase.getErrorType());
			log.debug("Request ID:       " + ase.getRequestId());
	        
		} catch (AmazonClientException ace) {
			log.debug("Caught an AmazonClientException, " + "which means the client encountered " + "an internal error while trying to communicate" +
	            " with S3, " + "such as not being able to access the network.");
			log.debug("Error Message: " + ace.getMessage());
		}
		return null;
	}
	
	
	
	// TODO DA FINIRE. VEDERE: https://aws.amazon.com/it/blogs/developer/archiving-and-backing-up-data-with-the-aws-sdk-for-java/
	public static void EseguiBackupBucket(){
		
		try {
			AWSCredentials credentials = new BasicAWSCredentials(Constants.AMAZON_STORE_ACCESS_KEY_ID, Constants.AMAZON_STORE_SECRET_ACCESS_KEY);
			ArchiveTransferManager atm = new ArchiveTransferManager(credentials);
			
			UploadResult uploadResult = atm.upload("myVaultName", "old logs",
				                                       new File("/logs/oldLogs.zip"));
			// later, when you need to retrieve your data
			atm.download("myVaultName", uploadResult.getArchiveId(), 
			             new File("/download/logs.zip"));
		
		} catch (AmazonClientException | FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Ritorna in nome del Bucket in base al Serve name. se sono in "www.apollotransfert.com" o se sono in "localhost"
	 * @throws UnknownHostException 
	 */
	private static String DammiNomeBucket(HttpServletRequest request) throws UnknownHostException{
		if( ApplicationUtils.CheckAmbienteApolloTransfert_Mondoserver(request) ){
			return Constants.AMAZON_STORE_BUCKET_PROD_NAME;
		}else{
			return Constants.AMAZON_STORE_BUCKET_TEST_NAME;
		}
	}
	
	/**
	 * Caricare un file
	 */
	public static void CaricareFile_AmazonWebService(HttpServletRequest request, MultipartFile multipartFile, Object objectModel, String objectModelFileName) throws IOException{
		try{
			AmazonS3 s3Client = DammiCredenziali_AmazonWebService();
			File file = UtilBukowski.convertMultipartFile_to_File(request.getServletContext(), multipartFile);
			log.debug("upload file.length(): "+file.length());
			
			// mettendo autisti/ crea il file dentro una cartella
	        s3Client.putObject(DammiNomeBucket(request), NomeFile_AmazonWebService(objectModel, objectModelFileName), file);

		}catch(AmazonServiceException ase){
			log.debug("Caught an AmazonServiceException, " + "which means your request made it " + 
	        		"to Amazon S3, but was rejected with an error response " + "for some reason.");
			log.debug("Error Message:    " + ase.getMessage());
			log.debug("HTTP Status Code: " + ase.getStatusCode());
			log.debug("AWS Error Code:   " + ase.getErrorCode());
			log.debug("Error Type:       " + ase.getErrorType());
			log.debug("Request ID:       " + ase.getRequestId());
	        
		} catch (AmazonClientException ace) {
			log.debug("Caught an AmazonClientException, " + "which means the client encountered " + "an internal error while trying to communicate" +
	            " with S3, " + "such as not being able to access the network.");
			log.debug("Error Message: " + ace.getMessage());
		}
	}
	
	
	
	/**
	 * Prendere un file
	 */
	public static S3Object GetFile_AmazonWebService(HttpServletRequest request, Object objectModel, String objectModelFileName ) throws IOException {
		try{
			AmazonS3 s3Client = DammiCredenziali_AmazonWebService();
			
	        return s3Client.getObject(new GetObjectRequest(DammiNomeBucket(request), NomeFile_AmazonWebService(objectModel, objectModelFileName)));
	        
			//InputStream objectData = s3object.getObjectContent();
			
			//String NomeFileOriginale = FilenameUtils.getName(s3object.getKey());
			//return IOUtils.toByteArray(objectData);
			
			/*
			final File tempFile = File.createTempFile("mail", "txt");
	        tempFile.deleteOnExit();
	        try (FileOutputStream out = new FileOutputStream(tempFile)) {
	            IOUtils.copy(objectData, out);
	        }
	        log.debug("get file lenth: "+tempFile.length());
			objectData.close();
			*/
		}catch(AmazonServiceException ase){
	        log.debug("Caught an AmazonServiceException, " + "which means your request made it " + 
	        		"to Amazon S3, but was rejected with an error response " + "for some reason.");
	        log.debug("Error Message:    " + ase.getMessage());
	        log.debug("HTTP Status Code: " + ase.getStatusCode());
	        log.debug("AWS Error Code:   " + ase.getErrorCode());
	        log.debug("Error Type:       " + ase.getErrorType());
	        log.debug("Request ID:       " + ase.getRequestId());
	        
		} catch (AmazonClientException ace) {
			log.debug("Caught an AmazonClientException, " + "which means the client encountered " + "an internal error while trying to communicate" +
	            " with S3, " + "such as not being able to access the network.");
			log.debug("Error Message: " + ace.getMessage());
		}
		return null;
	}
	
	
	public static void EliminareFile_AmazonWebService(HttpServletRequest request, Object objectModel, String objectModelFileName ) throws IOException {
		try{
			
			AmazonS3 s3Client = DammiCredenziali_AmazonWebService();
			s3Client.deleteObject(new DeleteObjectRequest(DammiNomeBucket(request), NomeFile_AmazonWebService(objectModel, objectModelFileName)));
			
		}catch(AmazonServiceException ase){
	        log.debug("Caught an AmazonServiceException, " + "which means your request made it " + 
	        		"to Amazon S3, but was rejected with an error response " + "for some reason.");
	        log.debug("Error Message:    " + ase.getMessage());
	        log.debug("HTTP Status Code: " + ase.getStatusCode());
	        log.debug("AWS Error Code:   " + ase.getErrorCode());
	        log.debug("Error Type:       " + ase.getErrorType());
	        log.debug("Request ID:       " + ase.getRequestId());
	        
		} catch (AmazonClientException ace) {
			log.debug("Caught an AmazonClientException, " + "which means the client encountered " + "an internal error while trying to communicate" +
	            " with S3, " + "such as not being able to access the network.");
			log.debug("Error Message: " + ace.getMessage());
		}
	}
	
	/**
	 * vedere: http://docs.aws.amazon.com/AmazonS3/latest/dev/DeletingMultipleObjectsUsingJava.html
	 */
	public static void EliminareMultipleFile_AmazonWebService(HttpServletRequest request, List<KeyVersion> keys ) throws IOException {
		try{
			
			AmazonS3 s3Client = DammiCredenziali_AmazonWebService();
			
			//Multi-object delete by specifying only keys (no version ID).
	        DeleteObjectsRequest multiObjectDeleteRequest = new DeleteObjectsRequest( DammiNomeBucket(request) ).withQuiet(false);
			
	        // Create request that include only object key names.
	        List<KeyVersion> justKeys = new ArrayList<KeyVersion>();
	        for (KeyVersion key : keys) {
	            justKeys.add(new KeyVersion(key.getKey()));
	        }
	        multiObjectDeleteRequest.setKeys(justKeys);
	        
	        s3Client.deleteObjects(multiObjectDeleteRequest);

			
		}catch(AmazonServiceException ase){
			log.debug("Caught an AmazonServiceException, " + "which means your request made it " + 
	        		"to Amazon S3, but was rejected with an error response " + "for some reason.");
	        log.debug("Error Message:    " + ase.getMessage());
	        log.debug("HTTP Status Code: " + ase.getStatusCode());
	        log.debug("AWS Error Code:   " + ase.getErrorCode());
	        log.debug("Error Type:       " + ase.getErrorType());
	        log.debug("Request ID:       " + ase.getRequestId());
	        
		} catch (AmazonClientException ace) {
			log.debug("Caught an AmazonClientException, " + "which means the client encountered " + "an internal error while trying to communicate" +
	            " with S3, " + "such as not being able to access the network.");
			log.debug("Error Message: " + ace.getMessage());
		}
	}
	
	
	public static void ListaPrintTuttiFileBucket(String NomeBucket){
		try{
			AmazonS3 s3Client = DammiCredenziali_AmazonWebService();
			log.debug("Listing objects");
	        final ListObjectsV2Request req = new ListObjectsV2Request().withBucketName( NomeBucket ).withMaxKeys(2);
	        ListObjectsV2Result result;
	        do {
	           result = s3Client.listObjectsV2(req);
	           
	           for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
	        	   log.debug(" - " + objectSummary.getKey() + " " + "(size = " + objectSummary.getSize() + ")");
	           }
	           log.debug("Next Continuation Token : " + result.getNextContinuationToken());
	           req.setContinuationToken(result.getNextContinuationToken());
	        } while(result.isTruncated() == true );
        
		}catch(AmazonServiceException ase){
			log.debug("Caught an AmazonServiceException, " + "which means your request made it " + 
	        		"to Amazon S3, but was rejected with an error response " + "for some reason.");
	        log.debug("Error Message:    " + ase.getMessage());
	        log.debug("HTTP Status Code: " + ase.getStatusCode());
	        log.debug("AWS Error Code:   " + ase.getErrorCode());
	        log.debug("Error Type:       " + ase.getErrorType());
	        log.debug("Request ID:       " + ase.getRequestId());
	        
		} catch (AmazonClientException ace) {
			log.debug("Caught an AmazonClientException, " + "which means the client encountered " + "an internal error while trying to communicate" +
	            " with S3, " + "such as not being able to access the network.");
			log.debug("Error Message: " + ace.getMessage());
		}
	}
	
	public static void VersioneObject(HttpServletRequest request){
        try {
        	AmazonS3 s3Client = DammiCredenziali_AmazonWebService();

            // 1. Enable versioning on the bucket.
        	BucketVersioningConfiguration configuration = 
        			new BucketVersioningConfiguration().withStatus("Enabled");
            
			SetBucketVersioningConfigurationRequest setBucketVersioningConfigurationRequest = 
					new SetBucketVersioningConfigurationRequest( DammiNomeBucket(request), configuration );
			
			s3Client.setBucketVersioningConfiguration(setBucketVersioningConfigurationRequest);
			
			// 2. Get bucket versioning configuration information.
			BucketVersioningConfiguration conf = s3Client.getBucketVersioningConfiguration( DammiNomeBucket(request) );
			log.debug("bucket versioning configuration status:    " + conf.getStatus());

        } catch (AmazonS3Exception amazonS3Exception) {
            System.out.format("An Amazon S3 error occurred. Exception: %s", amazonS3Exception.toString());
        } catch (Exception ex) {
            System.out.format("Exception: %s", ex.toString());
        } 
	}
	
	
	
	
	
}
