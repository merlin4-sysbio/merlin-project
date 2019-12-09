package pt.uminho.ceb.biosystems.merlin.core.utilities.compartments;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

public class RetrieveRemoteResults {
	
	private final static int LIMIT = 5;
	
	/**
	 * Method to retrieve information from web pages.
	 * @param url
	 * @throws InterruptedException 
	 */
	public static BufferedReader retrieveDataFromURL(String link) throws InterruptedException{

		BufferedReader in = null;
		boolean go = false;
		int errorCounter = 0;

		while(!go && errorCounter < LIMIT){

			try{
				
				doTrustToCertificates();
				URL url = new URL(link);
				in = new BufferedReader(new InputStreamReader(url.openStream()));

				go = true;
			}
			catch(SocketException e){

				go = false;
				errorCounter ++;
				TimeUnit.SECONDS.sleep(10);
			}
			catch(Exception e){
				
				in = null;
				go = true;
				e.printStackTrace();
			}
		}
		return in;
	}
	
	private static void doTrustToCertificates() throws Exception {
        
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		
        X509TrustManager[] trustAllCerts = new X509TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                        return;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                        return;
                    }
                }
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        
        HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String urlHostName, SSLSession session) {
                if (!urlHostName.equalsIgnoreCase(session.getPeerHost())) {
                	// code 200 means a successful connection
                    //System.out.println("Warning: URL host '" + urlHostName + "' is different to SSLSession host '" + session.getPeerHost() + "'.");
                }
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
    }
	

}