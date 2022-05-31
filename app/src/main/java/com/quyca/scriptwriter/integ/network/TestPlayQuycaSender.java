package com.quyca.scriptwriter.integ.network;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.quyca.scriptwriter.integ.model.QuycaMessage;
import com.quyca.scriptwriter.model.PlayCharacter;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The type Test quyca sender.
 */
public class TestPlayQuycaSender implements QuycaSender{

    private static final int TIME_OUT =1000 ;
    private List<PlayCharacter> characs;
    private Map<String,Socket> sockets;
    private int port;
    private Map<String,BufferedWriter> out;
    private Map<String,DataInputStream> in;

    public TestPlayQuycaSender(List<PlayCharacter> characs, int port) {
        this.characs= characs;
        this.port = port;
    }

    @Override
    public boolean send(List<QuycaMessage> msgs) {
        AtomicBoolean sent= new AtomicBoolean(true);
        msgs.forEach(quycaMessage -> {
            sent.set(sent.get() && send(quycaMessage));
        });

        return sent.get();

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
        for (String s : sockets.keySet()) {
            try {
                Objects.requireNonNull(sockets.get(s)).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean sendSocket(@NonNull QuycaMessage msg) throws IOException {
        //getSockets();
        //initStreams();
        //BufferedWriter output = getOut(msg.getCharName());
        //DataInputStream input = getIn(msg.getCharName());
        String toSend = msg.toMessageString();
        //output.write(toSend);
        Log.i("SENDING1",toSend);
        //output.flush();
        Log.i("WAITING",toSend);
        //int response = input.readInt();
        //Log.i("RECEIVED",response+"");

        //return response==msg.getTimestamp();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void getSockets()  {
        sockets = new HashMap<>();
        characs.forEach(playCharacter -> {
            try {
                Socket socket = new Socket(playCharacter.getIp(), port);
                socket.setSoTimeout(TIME_OUT);
                sockets.put(playCharacter.getName(),socket);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }

    private void initStreams() {
        in = new HashMap<>();
        out = new HashMap<>();
        sockets.forEach((name, socket) -> {
            try {
                out.put(name, new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
                in.put(name, new DataInputStream(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private BufferedWriter getOut(String name){
        return out.get(name);

    }

    private DataInputStream getIn(String name) {
        return in.get(name);
    }

}
