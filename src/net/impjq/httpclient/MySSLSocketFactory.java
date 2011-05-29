
package net.impjq.httpclient;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * The SSLSocketFactory,it extends SSLSocketFactory. It is used to trust the
 * self-signed KeyStore while connection to the secure server.
 * <p>
 * Example: <br>
 * You can just use the static method to create the new
 * {@link DefaultHttpClient}.
 * 
 * <pre>
 * HttpClient httpClient = MySSLSocketFactory.createNewHttpClient();
 * HttpGet httpget = new HttpGet(&quot;https://172.21.30.117/index.html&quot;);
 * HttpResponse response = httpClient.execute(httpget);
 * </pre>
 * 
 * Or you can use it as the {@link SSLSocketFactory} to create the customer
 * https scheme
 * 
 * <pre>
 * public void registerHttpsScheme(HttpClient httpClient) {
 *     SSLSocketFactory factory;
 *     try {
 *         factory = new SSLSocketFactory(null);
 *         KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
 *         trustStore.load(null, null);
 *         factory = new MySSLSocketFactory(trustStore);
 *         factory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
 *         Scheme https = new Scheme(&quot;https&quot;, factory, 443);
 *         httpClient.getConnectionManager().getSchemeRegistry().register(https);
 *     } catch (KeyManagementException e) {
 *         // TODO Auto-generated catch block
 *         e.printStackTrace();
 *     } catch (NoSuchAlgorithmException e) {
 *         // TODO Auto-generated catch block
 *         e.printStackTrace();
 *     } catch (KeyStoreException e) {
 *         // TODO Auto-generated catch block
 *         e.printStackTrace();
 *     } catch (UnrecoverableKeyException e) {
 *         // TODO Auto-generated catch block
 *         e.printStackTrace();
 *     } catch (CertificateException e) {
 *         // TODO Auto-generated catch block
 *         e.printStackTrace();
 *     } catch (IOException e) {
 *         // TODO Auto-generated catch block
 *         e.printStackTrace();
 *     }
 * }
 * </pre>
 * 
 * </p>
 * 
 * @author pjq0274@arcsoft.com
 * @date 2011-4-21
 */

public class MySSLSocketFactory extends SSLSocketFactory {
    SSLContext sslContext = SSLContext.getInstance("TLS");

    public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException,
            KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        super(truststore);

        TrustManager tm = new X509TrustManager() {

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
                // TODO Auto-generated method stub

            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
                // TODO Auto-generated method stub

            }
        };

        sslContext.init(null, new TrustManager[] {
                tm
        }, null);
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
            throws IOException, UnknownHostException {
        return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    @Override
    public Socket createSocket() throws IOException {
        return sslContext.getSocketFactory().createSocket();
    }

    /**
     * Create the new {@link DefaultHttpClient} with the special registed
     * http/https schemes.
     * 
     * @return {@link DefaultHttpClient}
     */
    public  HttpClient createNewDefaultHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }
}
