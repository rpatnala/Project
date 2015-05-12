package com.msit.material;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


public class SubActivity extends ActionBarActivity implements SeekBar.OnSeekBarChangeListener{

    SeekBar media, ringer,alarm;
    String name="",na=null,ri=null, me=null, al=null;
    TextView rin,med,ala,ama,mma,rma;
    EditText nam;
    DBHandler dbHandler = new DBHandler(this);
    int prog = 0;

    LinearLayout add_view, update_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String called_from = getIntent().getExtras().getString("called");
        Set_Add_Update_Screen();
        if (called_from.equals("add")) {
            add_view.setVisibility(View.VISIBLE);
            update_view.setVisibility(View.GONE);
        } else {

            update_view.setVisibility(View.VISIBLE);
            add_view.setVisibility(View.GONE);
            name = (getIntent().getStringExtra("name"));

            Profile c = dbHandler.Get_Profile(name);
            nam.setText(c.getName());
            rin.setText(c.getRing());
            med.setText(c.getMedia());
            ala.setText(c.getAlarm());
            ringer.setProgress(Integer.parseInt(c.getRing()));
            media.setProgress(Integer.parseInt(c.getMedia()));
            alarm.setProgress(Integer.parseInt(c.getAlarm()));
        }

    }

    public void done_save(View view){
        na = nam.getText().toString();
        ri = rin.getText().toString();
        me = med.getText().toString();
        al = ala.getText().toString();
        if (na != null && ri != null && me != null && al!=null
                && na.length() != 0
                && ri.length() != 0
                && me.length() != 0
                && al.length() != 0) {

            dbHandler.Add_Profile(new Profile(na, ri, me, al));
            Toast.makeText(getBaseContext(),"Successfully Registered...", Toast.LENGTH_LONG).show();
            dbHandler.close();
            done_cancel(view);
        }
        else{
            Toast.makeText(getBaseContext(),"else"+na+" "+ri+" "+me+" "+al, Toast.LENGTH_LONG).show();
        }
    }
    public void done_cancel(View view){
        Intent view_user = new Intent(SubActivity.this,  MainActivity.class);
        view_user.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP  | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(view_user);
        finish();
    }

    public void update_save(View view){
        na = nam.getText().toString();
        ri = rin.getText().toString();
        me = med.getText().toString();
        al = ala.getText().toString();

        if (na != null && ri != null && me != null && na.length() != 0 && ri.length() != 0 && me.length() != 0 && al.length() != 0) {

            dbHandler.Update_Profile(new Profile(na,ri,me,al));
            Toast.makeText(getBaseContext(),"Successfully Updated...", Toast.LENGTH_LONG).show();
            dbHandler.close();
            update_cancel(view);
        }
    }
    public void update_cancel(View view){
        Intent view_user = new Intent(SubActivity.this, MainActivity.class);
        view_user.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP   | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(view_user);
        finish();
    }

    public void Set_Add_Update_Screen() {


        AudioManager myManger = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        media = (SeekBar) findViewById(R.id.mseek);
        media.setMax(myManger.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        mma = (TextView) findViewById(R.id.mmax);
        mma.setText("/"+myManger.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        media.setProgress(myManger.getStreamVolume(AudioManager.STREAM_MUSIC));
        media.setOnSeekBarChangeListener(this);
        med = (TextView) findViewById(R.id.med);
        med.setText(media.getProgress()+"");

        ringer = (SeekBar) findViewById(R.id.rseek);
        ringer.setMax(myManger.getStreamMaxVolume(AudioManager.STREAM_RING));
        rma = (TextView) findViewById(R.id.rmax);
        rma.setText("/"+myManger.getStreamMaxVolume(AudioManager.STREAM_RING));
        ringer.setProgress(myManger.getStreamVolume(AudioManager.STREAM_RING));
        ringer.setOnSeekBarChangeListener(this);
        rin = (TextView) findViewById(R.id.rin);
        rin.setText(ringer.getProgress()+"");

        alarm = (SeekBar) findViewById(R.id.aseek);
        alarm.setMax(myManger.getStreamMaxVolume(AudioManager.STREAM_ALARM));
        ama = (TextView) findViewById(R.id.amax);
        ama.setText("/"+myManger.getStreamMaxVolume(AudioManager.STREAM_ALARM));
        alarm.setProgress(myManger.getStreamVolume(AudioManager.STREAM_ALARM));
        alarm.setOnSeekBarChangeListener(this);
        ala = (TextView) findViewById(R.id.ala);
        ala.setText(alarm.getProgress()+"");

        add_view = (LinearLayout) findViewById(R.id.add_view);
        update_view = (LinearLayout) findViewById(R.id.update_view);

        add_view.setVisibility(View.GONE);
        update_view.setVisibility(View.GONE);
        nam = (EditText) findViewById(R.id.pname);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sub, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //  int id = item.getItemId();
        /*
        if(id == android.R.id.home){

            NavUtils.navigateUpFromSameTask(this);
        }*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        prog = progress;
        AudioManager myManger = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (seekBar.findViewById(R.id.mseek) != null && (seekBar.findViewById(R.id.mseek).equals(seekBar))) {
            med.setText(prog+"");
            //myManger.setStreamVolume(AudioManager.STREAM_MUSIC,prog, 0);

        }
        else if (seekBar.findViewById(R.id.rseek) != null && (seekBar.findViewById(R.id.rseek).equals(seekBar))) {
            rin.setText(prog+"");
            //myManger.setStreamVolume(AudioManager.STREAM_RING,             prog, 0);
        }
        else if (seekBar.findViewById(R.id.aseek) != null && (seekBar.findViewById(R.id.aseek).equals(seekBar))) {
            ala.setText(prog+"");
           // myManger.setStreamVolume(AudioManager.STREAM_ALARM,                    prog, 0);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
