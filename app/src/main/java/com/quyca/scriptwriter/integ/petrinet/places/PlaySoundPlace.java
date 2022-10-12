package com.quyca.scriptwriter.integ.petrinet.places;

import android.media.MediaPlayer;

import com.quyca.scriptwriter.integ.utils.UIBundle;
import com.quyca.scriptwriter.model.Playable;
import com.quyca.scriptwriter.model.QuycaCommandState;
import com.quyca.scriptwriter.model.SoundAction;

import java.io.FileDescriptor;
import java.io.IOException;

/**
 * The type Play sound place.
 */
public class PlaySoundPlace extends PlayViewPlace {

    /**
     * Instantiates a new Play sound place. It extends the petrinet lifecycle to sound related actions and integrates android media player with the place status.
     *
     * @param playable the playable
     * @param bundle   the bundle
     */
    public PlaySoundPlace(Playable playable, UIBundle bundle) {
        super(playable, bundle);
    }

    @Override
    public void run() {
        SoundAction sAction = (SoundAction) playable;
        MediaPlayer player = new MediaPlayer();
        try {
            FileDescriptor file = sAction.getSoundFile(context);
            player.setDataSource(file);
            player.setOnCompletionListener(mp -> {
                fireTransitions();
            });
            player.prepare();
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            player.reset();
            player.release();
        }
    }



}
