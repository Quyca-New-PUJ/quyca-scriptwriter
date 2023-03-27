package com.quyca.scriptwriter.integ.petrinet.places;

import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

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
            Uri file = sAction.getSoundFile(context);
            Log.i("SOUNDHEX", ""+file.getPath());
            player.setDataSource(context,file);
            Log.i("SOUNDHEX", file.toString());
            player.setOnCompletionListener(mp -> {
                fireTransitions();
            });
            player.setOnErrorListener((mp, what, extra) -> {
                Log.i("SOUNDHEXERROR", mp+"");
                Log.i("SOUNDHEXERROR", what+"");
                Log.i("SOUNDHEXERROR", extra+"");
                return false;
            });
            player.setOnPreparedListener(mp -> {
                Log.i("SOUNDHEXPREPARED", "file.toString()");
                //mp.start();
                Log.i("SOUNDHEXPREPAREDOUT", "file.toString()");
            });
            Log.i("SOUNDHEX", "PREPARINGIT");

            player.prepareAsync();

        } catch (IOException e) {
            Log.i("SOUNDHEXFAIL", e.toString());
            e.printStackTrace();
        } finally {
            player.reset();
            player.release();
        }
    }



}
