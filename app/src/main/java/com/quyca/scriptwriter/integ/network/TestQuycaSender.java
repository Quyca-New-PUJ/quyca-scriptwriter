package com.quyca.scriptwriter.integ.network;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.quyca.scriptwriter.integ.model.QuycaMessage;
import com.quyca.scriptwriter.model.Playable;
import com.quyca.scriptwriter.model.QuycaCommandState;
import com.quyca.scriptwriter.ui.execscript.ExecScriptViewModel;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Test quyca sender.
 */
public class TestQuycaSender implements QuycaSender {

    private static final int TIME_OUT =1000 ;
    private Socket socket;
    private String ip;
    private int port;
    private BufferedWriter out;
    private DataInputStream in;

    /**
     * Instantiates a new Test quyca sender.
     *
     * @param ip   the ip
     * @param port the port
     */

    public TestQuycaSender(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public boolean send(List<QuycaMessage> msgs) {
        msgs.forEach(this::send);

        return false;

    }


    @Override
    public boolean send(QuycaMessage msg) {
        boolean isOk=false;
        try {
            isOk=sendSocket(msg);
        } catch (SocketTimeoutException e) {
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isOk;
    }
    
    private boolean sendSocket(@NonNull QuycaMessage msg) throws IOException {
       // initStreams();
        String toSend = msg.toMessageString();
        //out.write(toSend);
        Log.i("SENDING1",toSend);
        //out.flush();
        Log.i("WAITING",toSend);
        //int response = in.readInt();
        //Log.i("RECEIVED",response+"");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //return response==msg.getTimestamp();
        return true;
    }

    private Socket getSocket() throws IOException {
        if(socket == null){
            socket = new Socket(ip, port);
            socket.setSoTimeout(TIME_OUT);
        }
        return socket;
    }

    private void initStreams() throws IOException {
        getOut();
        getIn();
    }

    private void getOut() throws IOException {
        if(out == null){
            out = new BufferedWriter(new OutputStreamWriter(getSocket().getOutputStream()));
        }
    }

    private void getIn() throws IOException {
        if(in == null){
            in = new DataInputStream(getSocket().getInputStream());
        }
    }

}
