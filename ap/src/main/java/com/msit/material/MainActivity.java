package com.msit.material;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    ListView Contact_listview;
    ArrayList<Profile> contact_data = new ArrayList<>();
    Profile_Adapter cAdapter;
    DBHandler dbHandler;
    private int mLastCorrectPosition = -1;
    private int mButtonPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_appbar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        try {
            Contact_listview = (ListView) findViewById(R.id.list);
            Contact_listview.setItemsCanFocus(false);
            Contact_listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            Set_Refresh_Data();

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("some error", "" + e);
        }



        NavigationDrawerFragment drawerFragment;
        drawerFragment = (NavigationDrawerFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer,(DrawerLayout)findViewById(R.id.drawer_layout), toolbar);

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.ic_action_new);

        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(imageView)
                .setBackgroundDrawable(R.drawable.button_action_blue_touch)
                .build();

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,SubActivity.class);
                i.putExtra("called", "add");
               // i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
       // int id = item.getItemId();


      /*  if(id == R.id.add){

            startActivity(new Intent(this,SubActivity.class));
        }*/
        return super.onOptionsItemSelected(item);
    }
    public void Set_Refresh_Data() {
        contact_data.clear();
        dbHandler = new DBHandler(this);
        ArrayList<Profile> profile_array_from_db = dbHandler.Get_Profiles();

        for (int i = 0; i < profile_array_from_db.size(); i++) {

            String pname = profile_array_from_db.get(i).getName();
            String pring = profile_array_from_db.get(i).getRing();
            String pmedi = profile_array_from_db.get(i).getMedia();
            String pala = profile_array_from_db.get(i).getAlarm();
            Profile cnt = new Profile();
            cnt.setName(pname);
            cnt.setRing(pring);
            cnt.setMedia(pmedi);
            cnt.setAlarm(pala);

            contact_data.add(cnt);
        }
        dbHandler.close();
        cAdapter = new Profile_Adapter(MainActivity.this, R.layout.listview_row,contact_data);
        Contact_listview.setAdapter(cAdapter);
        cAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Set_Refresh_Data();
    }

    public class Profile_Adapter extends ArrayAdapter<Profile> {
        Activity activity;
        int layoutResourceId;
        Profile user;
        ArrayList<Profile> data = new ArrayList<>();
        private RadioButton mSelectedRB;
        private int mSelectedPosition = -1;

        public Profile_Adapter(Activity act, int layoutResourceId,
                               ArrayList<Profile> data) {
            super(act, layoutResourceId, data);
            this.layoutResourceId = layoutResourceId;
            this.activity = act;
            this.data = data;
            notifyDataSetChanged();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View row = convertView;
            UserHolder holder;

            if (row == null) {
                LayoutInflater inflater = LayoutInflater.from(activity);

                row = inflater.inflate(layoutResourceId, parent, false);
                holder = new UserHolder();
                holder.name = (TextView) row.findViewById(R.id.user_name_txt);

                holder.edit = (Button) row.findViewById(R.id.btn_update);
                holder.delete = (Button) row.findViewById(R.id.btn_delete);
                holder.activate=(RadioButton) row.findViewById(R.id.radioButton);
                row.setTag(holder);
            } else {
                holder = (UserHolder) row.getTag();
            }
            user = data.get(position);
            holder.edit.setTag(user.getName());
            holder.delete.setTag(user.getName());
            holder.name.setText(user.getName());

            //final int pos=position;

            holder.activate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(position!= mSelectedPosition && mSelectedRB != null){
                        mSelectedRB.setChecked(false);
                    }

                    mSelectedPosition = position;
                    mSelectedRB = (RadioButton)v;

                    Profile c = dbHandler.Get_Profile(user.getName());

                    int ring=Integer.parseInt(c.getRing());
                    int media=Integer.parseInt(c.getMedia());
                    int alarm=Integer.parseInt(c.getAlarm());

                    AudioManager myManger = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    if (media>=0) {

                        myManger.setStreamVolume(AudioManager.STREAM_MUSIC,media, 0);

                    }
                    if (ring>=0) {

                        myManger.setStreamVolume(AudioManager.STREAM_RING,ring, 0);
                    }
                    if (alarm>=0) {

                        myManger.setStreamVolume(AudioManager.STREAM_ALARM,alarm, 0);
                    }

                    Toast.makeText(getBaseContext(),"Profile Changed...",Toast.LENGTH_LONG).show();
                }
            });


            if(mSelectedPosition != position){
                holder.activate.setChecked(false);
            }else{
                holder.activate.setChecked(true);
                if(mSelectedRB != null && holder.activate != mSelectedRB){
                    mSelectedRB = holder.activate;
                }
            }




            holder.edit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Log.i("Edit Button Clicked", "**********");

                    Intent update_user = new Intent(activity,SubActivity.class);
                    update_user.putExtra("called", "update");
                    update_user.putExtra("name", v.getTag().toString());
                    activity.startActivity(update_user);

                }
            });
            holder.delete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub

                    // show a message while loader is loading

                    AlertDialog.Builder adb = new AlertDialog.Builder(activity);
                    adb.setTitle("Delete?");
                    adb.setMessage("Are you sure you want to delete ");
                    final String user_id = (v.getTag().toString());
                    adb.setNegativeButton("Cancel", null);
                    adb.setPositiveButton("Ok",
                            new AlertDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // MyDataObject.remove(positionToRemove);
                                    DBHandler dBHandler = new DBHandler(
                                            activity.getApplicationContext());
                                    dBHandler.Delete_Profile(user_id);
                                    MainActivity.this.onResume();

                                }
                            });
                    adb.show();
                }

            });
            return row;

        }

        class UserHolder {

            TextView name;
            Button edit;
            Button delete;
            RadioButton activate;
        }
    }
}
