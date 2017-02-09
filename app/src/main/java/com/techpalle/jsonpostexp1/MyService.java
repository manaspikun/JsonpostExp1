package com.techpalle.jsonpostexp1;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MyService extends Service {
    private String name,country,twitter;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //step12=read data comming from fragment
        Bundle bundle=intent.getExtras();
        name=bundle.getString("name");
        country=bundle.getString("country");
        twitter=bundle.getString("twitter");
        //step13-start async task and pass url
        MyTask myTask=new MyTask();
        myTask.execute("http://hmkcode.appspot.com/jsonservlet");

        return super.onStartCommand(intent, flags, startId);
    }

    //step 8
    public class MyTask extends AsyncTask<String,Void,String>{
        //declare required variable
        HttpURLConnection httpURLConnection;
        URL myurl;
        OutputStream outputStream;
        OutputStreamWriter outputStreamWriter;
       @Override
       protected String doInBackground(String... strings) {
           try {
               myurl=new URL(strings[0]);
               httpURLConnection= (HttpURLConnection) myurl.openConnection();
               //preliminary code for preparing connection for post request

               httpURLConnection.setRequestMethod("POST");
               httpURLConnection.setRequestProperty("Content-Type","application/json");
               //prepare json data for posting
               JSONObject jsonObject=new JSONObject();//{}
               jsonObject.accumulate("name",name);//{"name":"batch 34"}
               jsonObject.accumulate("country",country);//{"name":batch 34","country":"india"}
               jsonObject.accumulate("twitter",twitter);
               //prepare output stream
               outputStream=httpURLConnection.getOutputStream();
               outputStreamWriter=new OutputStreamWriter(outputStream);

               //write json data into outstreamwriter
               outputStreamWriter.write(jsonObject.toString());
               //forcefully throw everything to server
               outputStreamWriter.flush();
               //here at this point of time-server will start reading
               //letus ask server-for responxe
               int responsecode=httpURLConnection.getResponseCode();
                //return response code to onpost execute
               if (responsecode==HttpURLConnection.HTTP_OK){
                   return "success";

               }else {
                   return "failure";
               }
           } catch (MalformedURLException e) {
               e.printStackTrace();
           } catch (IOException e) {
               e.printStackTrace();
           } catch (JSONException e) {
               e.printStackTrace();
           }finally {
               //close all connections properly here
               if (httpURLConnection!=null){
                   httpURLConnection.disconnect();//closes connection
                   if (outputStream!=null){
                       try {
                           outputStream.close();
                           if (outputStreamWriter!=null){
                               outputStreamWriter.close();
                           }
                       } catch (IOException e) {
                           e.printStackTrace();
                       }
                   }
               }
           }

           return null;
       }

       @Override
       protected void onPostExecute(String s) {
           Toast.makeText(MyService.this, "status:"+s, Toast.LENGTH_SHORT).show();
           //send broadcast to dynamic receiver
           Intent in=new Intent();
           in.setAction("TASK_DONE");
           in.putExtra("result",s);
           sendBroadcast(in);
           super.onPostExecute(s);
       }
   }
    public MyService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
