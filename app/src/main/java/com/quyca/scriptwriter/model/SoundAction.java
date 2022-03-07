package com.quyca.scriptwriter.model;

import android.content.Context;
import android.media.MediaPlayer;

import androidx.documentfile.provider.DocumentFile;

import com.google.gson.annotations.Expose;
import com.quyca.scriptwriter.R;
import com.quyca.scriptwriter.integ.network.QuycaMessageCreator;
import com.quyca.scriptwriter.integ.network.QuycaSender;
import com.quyca.scriptwriter.utils.AudioRepository;
import com.quyca.scriptwriter.utils.FileRepository;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SoundAction extends Action {

    private FileDescriptor sound;
    @Expose
    private String name;
    @Expose
    private boolean saved;


    public SoundAction() {
        super();
        saved = false;
    }

    @Override
    public boolean play(QuycaMessageCreator msgCreator, QuycaSender msgSender, Context context) {
        MediaPlayer player = new MediaPlayer();
        try {
            FileDescriptor file = getSoundFile(context);
            player.setDataSource(file);
            player.prepare();
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            setSound(null);
            player.reset();
            player.release();
        }
        return true;
    }

    private FileDescriptor getSoundFile(Context context) throws FileNotFoundException {
        return AudioRepository.getAudio(this, context);
    }

    public FileDescriptor getSound() {
        return sound;
    }

    public void setSound(FileDescriptor sound) {
        this.sound = sound;
    }

    public String getName() {
        return name;
    }

    public String getNameWithoutPrefix() {
        return name.split(".mp4")[0];
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

}
