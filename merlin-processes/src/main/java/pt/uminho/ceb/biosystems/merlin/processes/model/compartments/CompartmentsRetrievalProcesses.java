package pt.uminho.ceb.biosystems.merlin.processes.model.compartments;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

public class CompartmentsRetrievalProcesses {
	
	/**
	 * Method to retrieve information from loctree results link.
	 * @param url
	 */
	public static BufferedReader retrieveDataFromURL(String link){

		BufferedReader in = null;
		
		try{
			doTrustToCertificates();
			URL url = new URL(link);
			in = new BufferedReader(new InputStreamReader(url.openStream()));
		}
		catch(Exception e){
			e.printStackTrace();
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
