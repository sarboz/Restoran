package com.restoran.Chat;

import android.os.AsyncTask;

import com.restoran.Interfase.IResive;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

public class Client extends AsyncTask<Void, String, String> {

    private String dstAddress;
    private int dstPort;
    private String response = "";
    private  static IResive iR;
    private static Socket socket = null;

    public Client(String addr, int port, IResive iResive) {
        dstAddress = addr;
        dstPort = port;
        iR = iResive;
    }

    @Override
    protected String doInBackground(Void... arg0) {

        try {
            socket = new Socket(dstAddress, dstPort);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
                    1024);
            byte[] buffer = new byte[1024];

            int bytesRead;
            InputStream inputStream = socket.getInputStream();

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
                //   response += byteArrayOutputStream.toString("UTF-8");
                publishProgress(byteArrayOutputStream.toString("UTF-8"));
                byteArrayOutputStream.reset();
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
            iR.OnError("UnknownHostException: " + e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            iR.OnError("IOException: " + e.toString());
        }
        return response;
    }


    public static void Send(String msg) {
        try {
            if(socket!=null) {
                new PrintWriter(
                        new BufferedWriter(
                                new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8"))), true).println(msg);
            }
            else {
                iR.OnError("Socket is null");
            }
        } catch (IOException e) {
            iR.OnError("On Send "+e.toString());
            e.printStackTrace();

        }
    }

    @Override
    protected void onProgressUpdate(String... values) {
        iR.OnResive(values[0]);
        super.onProgressUpdate(values);
        //textResponse.setText(values[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }

}