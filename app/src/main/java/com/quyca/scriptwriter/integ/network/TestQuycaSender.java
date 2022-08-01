package com.quyca.scriptwriter.integ.network;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.quyca.scriptwriter.config.FixedConfiguredAction;
import com.quyca.scriptwriter.integ.model.QuycaMessage;
import com.quyca.scriptwriter.model.Playable;
import com.quyca.scriptwriter.model.QuycaCommandState;
import com.quyca.scriptwriter.ui.execscript.ExecScriptViewModel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Test quyca sender.
 */
public class TestQuycaSender implements QuycaSender {

    private static final int TIME_OUT =10000000 ;
    private Socket socket;
    private final String ip;
    private final int port;
    private BufferedWriter out;
    private BufferedReader in;

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
        closeSender();
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

    @Override
    public boolean closeSender() {
        try {
            closeSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean sendSocket(@NonNull QuycaMessage msg) throws IOException {
        initStreams();
        boolean endedResponse = true;
        String toSend = msg.toMessageString();
        out.write(toSend);
        Log.i("TEST_SENDING1",toSend);
        out.flush();
        Log.i("TEST_WAITING",toSend);
        int response = Integer.parseInt(in.readLine());
        Log.i("TEST_RECEIVED",response+"");
        /*try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        if(!msg.getActionId().equals(FixedConfiguredAction.emotions.name())){
            endedResponse = response==msg.getTimestamp();
        }
        return endedResponse;
    }

    private Socket getSocket() throws IOException {
        if(socket == null){
            socket = new Socket(ip, port);
            socket.setSoTimeout(TIME_OUT);
        }
        return socket;
    }

    private boolean closeSocket() throws IOException {
        if(socket!=null){
            socket.close();
        }
        return true;
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
            in = new BufferedReader( new InputStreamReader((getSocket().getInputStream())));
        }
    }

}
