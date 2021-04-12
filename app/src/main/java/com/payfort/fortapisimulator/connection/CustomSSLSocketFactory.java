package com.payfort.fortapisimulator.connection;

import org.apache.http.conn.ssl.SSLSocketFactory;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by gbarham on 3/1/2016.
 */
public class CustomSSLSocketFactory extends SSLSocketFactory {
    SSLContext sslContext = SSLContext.getInstance("TLS");


    public CustomSSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        super(truststore);

        TrustManager tm = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sslContext.init(null, new TrustManager[] { tm }, null);
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
        return enableTLSOnSocket(sslContext.getSocketFactory().createSocket(socket, host, port, autoClose));
    }

    @Override
    public Socket createSocket() throws IOException {
        return enableTLSOnSocket(sslContext.getSocketFactory().createSocket());
    }


    private Socket enableTLSOnSocket(Socket socket) {
        if (socket != null && (socket instanceof SSLSocket)) {
            //support TLSv1.1 and TLSv21.2
            ((SSLSocket) socket).setEnabledProtocols(new String[]{"TLSv1.1", "TLSv1.2"});
            //remove SSLv3
            List<String> enabledProtocols = new ArrayList<String>(Arrays.asList( ((SSLSocket)socket).getEnabledProtocols()));
            if (enabledProtocols.size() > 1) {
                enabledProtocols.remove("SSLv3");
            }
            ((SSLSocket)socket).setEnabledProtocols(enabledProtocols.toArray(new String[enabledProtocols.size()]));
        }
        return socket;
    }


}
