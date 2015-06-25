package enguix.mulet.taxivalldigna;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import enguix.mulet.taxivalldigna.Entities.CustomAddress;
import enguix.mulet.taxivalldigna.Entities.PushMessage;
import enguix.mulet.taxivalldigna.Entities.TripEntity;
import enguix.mulet.taxivalldigna.Views.MainActivity;
import enguix.mulet.taxivalldigna.Views.TripsActivity;

/**
 * Created by Raul on 23/03/2015.
 */
public class GCMIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    private int nnoti;
    public GCMIntentService() {
        super("GcmIntentService");
        nnoti=0;
    }
    public static final String TAG = "GCM";

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        /**
         *'subtitle'	=> 'This is a subtitle. subtitle',
         'tickerText'	=> 'Ticker text here...Ticker text here...Ticker text here',
         'vibrate'	=> 1,
         'sound'
         */

        //ArrayList<String> msg = new ArrayList<>();


        String message = extras.getString("message");
        String title = extras.getString("title");
        String subtitle =extras.getString("subtitle");
        String ticker = extras.getString("tickerText");
       // String vibrate = extras.getString("vibrate");
       // String sound = extras.getString("sound");
        String type = extras.getString("type");

        if(type.equals("update")){
            String json = extras.getString("json");
            setTrips(json);
        }
        if(type.equals("info")){


            TaxiDataBase db = new TaxiDataBase(getApplicationContext());
            db.updateTableUser();


        UtilsRequest gettrips = new UtilsRequest(this);
        gettrips.getTripsUser();
        }


        PushMessage msg = new PushMessage();

        msg.setMessage(message);
        msg.setTitle(title);
        msg.setSubtitle(subtitle);
        msg.setTicker(ticker);
       // msg.setVibrate(vibrate);
      //  msg.setSound(sound);
        msg.setType(type);
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM will be
             * extended in the future with new message types, just ignore any message types you're
             * not interested in, or that you don't recognize.
             */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                //sendNotification("Send error: ");
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                //sendNotification("Deleted messages on server: ");
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {

                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                // Post notification of received message.
                sendNotification(msg);
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GCMBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification( PushMessage msg) {
        String appname = this.getResources().getString(R.string.app_name);
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent;
        if(msg.getType().equals("info")){
            intent =new Intent(this, TripsActivity.class);
        }else {
           intent =new Intent(this, MainActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.setAction("MESSAGE_PUSH");
        intent.putExtra("messagePush", msg);



        Bitmap launch =  BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);


        PendingIntent contentIntent = PendingIntent.getActivity(this,0 ,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

                        builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(msg.getSmallIcon())
                        .setLargeIcon(launch)
                        .setContentTitle(msg.getTitle())
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg.getMessage()))
                        .setTicker(msg.getTicker())
                        .setSubText(msg.getSubtitle())
                        .setContentText(msg.getMessage())
                        .setContentInfo(appname)

                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setLights(NotificationCompat.DEFAULT_LIGHTS, 2000, 500)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setAutoCancel(true);



        builder.setContentIntent(contentIntent);
        mNotificationManager.notify(msg.numberNotification(), builder.build());
    }

    public void setTrips(String json){

TaskSetTrips task = new TaskSetTrips();
        try {
            JSONObject object = new JSONObject(json);
            task.execute(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private class TaskSetTrips extends AsyncTask<JSONObject,Void,ArrayList<TripEntity>>{

        @Override
        protected ArrayList<TripEntity> doInBackground(JSONObject... params) {

            ArrayList<TripEntity> journeys = new ArrayList<>();
            try {
                JSONArray trips = params[0].getJSONArray("trips");

                for (int i = 0; i < trips.length(); i++) {

                    JSONObject tripObj = trips.getJSONObject(i);
                    TripEntity tripEnt = new TripEntity();

                    tripEnt.set_id(tripObj.getInt("id"));

                    tripEnt.setTime(tripObj.getString("date"));

                        CustomAddress from = new CustomAddress();
                        from.setStreets_name(tripObj.getString("street_from"));
                        from.setCity(tripObj.getString("city_from"));
                        from.setCoordenates(new LatLng(tripObj.getDouble("lat_from"), tripObj.getDouble("lng_from")));

                    tripEnt.setFrom(from);

                        CustomAddress to = new CustomAddress();
                        to.setStreets_name(tripObj.getString("street_to"));
                        to.setCity(tripObj.getString("city_to"));
                        to.setCoordenates(new LatLng(tripObj.getDouble("lat_to"), tripObj.getDouble("lng_to")));

                    tripEnt.setFrom(to);

                    tripEnt.setComents(tripObj.getString("coments"));

                    tripEnt.setKms((float) tripObj.getDouble("kms"));
                    tripEnt.setPrice((float) tripObj.getDouble("price"));

                    journeys.add(tripEnt);
                }





            } catch (JSONException e) {
                e.printStackTrace();
            }


            return journeys;
        }

        @Override
        protected void onPostExecute(ArrayList<TripEntity> tripEntities) {
            super.onPostExecute(tripEntities);


            TaxiDataBase db = new TaxiDataBase(getApplicationContext());
            if(!tripEntities.isEmpty()) {
                db.insertAllTrips(tripEntities);
            }

        }
    }

}
