package com.example.doanmonhoc;

import android.content.Context;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import android.util.Log;
import java.security.cert.X509Certificate;
import javax.net.ssl.TrustManager;

public class CustomOkHttpClient {

    public static OkHttpClient getCustomOkHttpClient(Context context) {
        try {
            // Create a TrustManager that trusts all certificates
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[]{};
                        }
                    }
            };

            // Create an SSLContext that uses our TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier((hostname, session) -> true);

            // Add logging interceptor
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor(message -> Log.d("HTTP_LOG", message));
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);

            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
