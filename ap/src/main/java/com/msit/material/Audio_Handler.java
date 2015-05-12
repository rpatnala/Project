package com.msit.material;

import android.content.Context;
import android.media.AudioManager;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by Sekhar on 5/12/2015.
 */
public class Audio_Handler extends ActionBarActivity{
    DBHandler dbHandler;
    AudioManager myManger;
   public void Set_Volumes(String name)
   {
       Profile c = dbHandler.Get_Profile(name);
       int ring=Integer.parseInt(c.getRing());
       int media=Integer.parseInt(c.getMedia());
       int alarm=Integer.parseInt(c.getAlarm());

       myManger = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
       if (media>=0) {

           myManger.setStreamVolume(AudioManager.STREAM_MUSIC,media, 0);

       }
       if (ring>=0) {

           myManger.setStreamVolume(AudioManager.STREAM_RING,ring, 0);
       }
       if (alarm>=0) {

           myManger.setStreamVolume(AudioManager.STREAM_ALARM,alarm, 0);
       }
   }
}
