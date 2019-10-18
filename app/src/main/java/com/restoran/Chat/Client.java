package com.restoran.Chat;

import android.os.AsyncTask;

import com.restoran.Interfase.IResive;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

public class Client extends AsyncTask<Void, String, String> {

    private String dstAddress;
    private int dstPort;
    private String response = "";
    private IResive iR;
    private Socket socket = null;
    private static Client client = null;


    public static Client getInstance(String addr, int port, IResive iResive) {
        if (client != null) {
            return client;
        }
        client = new Client(addr, port, iResive);
        client.execute();
        return client;
    }

    public static Client getInstance() {
        if (client != null) {
            return client;
        }
        return null;
    }

    private Client(String addr, int port, IResive iResive) {
        dstAddress = addr;
        dstPort = port;
        iR = iResive;
    }

    @Override
    public String doInBackground(Void... arg0) {
        try {
            socket = new Socket(dstAddress, dstPort);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
                    1024);
            byte[] buffer = new byte[1024];

            int bytesRead;
            InputStream inputStream = socket.getInputStream();
            publishProgress("con", "-1");
            while ((bytesRead = inputStream.read(buffer)) != -1) {

                byteArrayOutputStream.write(buffer, 0, bytesRead);

                publishProgress(byteArrayOutputStream.toString("UTF-8"), "0");
                byteArrayOutputStream.reset();
            }
            publishProgress("dis", "2");

        } catch (UnknownHostException e) {
            e.printStackTrace();
            publishProgress("UnknownHostException: " + e.toString(), "1");
        } catch (IOException e) {
            e.printStackTrace();
            publishProgress("IOException: " + e.toString(), "1");
        }
        return response;
    }

    public void Send(String msg) {
        try {
            if (socket != null) {
                new PrintWriter(
                        new BufferedWriter(
                                new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8"))), true).println(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onProgressUpdate(String... values) {
        switch (values[1]) {
            case "-1":
                iR.OnConnected();
                break;
            case "0":
                iR.OnResive(values[0]);
                break;
            case "1":
                client = null;
                iR.OnError(values[0]);
                break;
            case "2":
                client = null;
                iR.OnDisconnected();
                break;
        }
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }

}