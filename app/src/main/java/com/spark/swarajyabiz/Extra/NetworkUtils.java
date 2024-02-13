package com.spark.swarajyabiz.Extra;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class NetworkUtils {

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static class PingTask extends AsyncTask<Void, Void, Boolean> {

        private Context mContext;
        private String mHost;
        private int mPort;
        private int mTimeout;
        private PingCallback mCallback;

        public PingTask(Context context, String host, int port, int timeout, PingCallback callback) {
            mContext = context;
            mHost = host;
            mPort = port;
            mTimeout = timeout;
            mCallback = callback;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(mHost, mPort), mTimeout);
                socket.close();
                return true; // Ping successful
            } catch (IOException e) {
                return false; // Ping failed
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (mCallback != null) {
                mCallback.onPingResult(result);
            }
        }

        public interface PingCallback {
            void onPingResult(boolean success);
        }
    }
}