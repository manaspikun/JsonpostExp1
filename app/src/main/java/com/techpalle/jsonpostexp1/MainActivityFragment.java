package com.techpalle.jsonpostexp1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
EditText et1,et2,et3;
    Button b1,b2;
    TextView tv;
    //create a dynamic receiver
    public class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle=intent.getExtras();
            String result=bundle.getString("result");
            tv.setText("server status"+result);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyReceiver myReceiver=new MyReceiver();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("TASK_DONE");
        getActivity().registerReceiver(myReceiver,intentFilter);
    }

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_main, container, false);
        et1= (EditText) v.findViewById(R.id.edittext1);
        et2= (EditText) v.findViewById(R.id.edittext2);
        et3= (EditText) v.findViewById(R.id.edittext3);
        b1= (Button) v.findViewById(R.id.button1);
        b2= (Button) v.findViewById(R.id.button2);
        tv= (TextView) v.findViewById(R.id.textview1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //we will start service,for postion data immideatlly
                Intent intent=new Intent(getActivity(),MyService.class);
                intent.putExtra("name",et1.getText().toString());
                intent.putExtra("country",et2.getText().toString());
                intent.putExtra("twitter",et3.getText().toString());
                getActivity().startService(intent);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //post after some time 1 minute-using alarm
                AlarmManager alarmManager= (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                Intent intent=new Intent(getActivity(),MyService.class);
                intent.putExtra("name",et1.getText().toString());
                intent.putExtra("country",et2.getText().toString());
                intent.putExtra("twitter",et3.getText().toString());
                PendingIntent pendingIntent=PendingIntent.getService(getActivity(),0,intent,0);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+60000,60000,pendingIntent);
               // alarmManager.set(AlarmManager.RTC_WAKEUP,60000,pendingIntent);
                //alarmManager.cancel(pendingIntent);
                Toast.makeText(getActivity(), "alaram started", Toast.LENGTH_SHORT).show();

            }
        });
        return v;
    }
}
