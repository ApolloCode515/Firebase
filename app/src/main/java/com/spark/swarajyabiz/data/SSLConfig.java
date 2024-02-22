package com.spark.swarajyabiz.data;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import okhttp3.OkHttpClient;

public class SSLConfig {

    // Create a custom TrustManager that trusts all certificates
    private static TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                public void checkClientTrusted(
                        X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(
                        X509Certificate[] certs, String authType) {
                }
            }
    };

    // Create a custom HostnameVerifier that trusts all hostnames
    private static HostnameVerifier trustAllHostnames = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    // Initialize SSLContext with the custom TrustManager
    public static SSLContext getSSLContext() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, new SecureRandom());
        return sslContext;
    }

    // Get OkHttpClient with SSL certificate validation disabled
    public static OkHttpClient getUnsafeOkHttpClient() {
        try {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(getSSLContext().getSocketFactory(), (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(trustAllHostnames);
            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
