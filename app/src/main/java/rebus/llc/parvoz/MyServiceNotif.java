package rebus.llc.parvoz;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import rebus.llc.parvoz.R;
import rebus.llc.parvoz.db.DBSample;
import rebus.llc.parvoz.fragments.MainFragment;
import rebus.llc.parvoz.models.ListModels;
import rebus.llc.parvoz.models.MyTicketModel;
import rebus.llc.parvoz.models.ObjavlenijaModel;
import rebus.llc.parvoz.others.Encrypt;
import rebus.llc.parvoz.others.MyAsyncTask;
import rebus.llc.parvoz.others.Settings;
import rebus.llc.parvoz.others.Utilities;

import static rebus.llc.parvoz.others.Utilities.getServerUrls;

public class MyServiceNotif  extends Service implements MyAsyncTask.ResponseCame{

    // This is the Notification Channel ID. More about this in the next section
    public static final String NOTIFICATION_CHANNEL_ID = "channel_id";
    //User visible Channel Name
    public static final String CHANNEL_NAME = "Notification Channel";
    // Importance applicable to all the notifications in this Channel
    int importance = NotificationManager.IMPORTANCE_DEFAULT;
    Settings settings;
    ObjectMapper objectMapper;
    private Encrypt md5Conver;

    MyAsyncTask myAsyncTask;
    Timer myTimer_local = null; // Создаем таймер

    private NotificationManagerCompat notificationManagerCompat;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onCreate() {

        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int  startId) {
        // Log.d(LOG_TAG, "onStartCommand");
        settings = new Settings(getApplicationContext());
        md5Conver = new Encrypt();
        myAsyncTask = null;
        notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());

        someTask();
        return START_STICKY;
    }

    void someTask(){
        if(myTimer_local == null) {

            myTimer_local = new Timer();

            myTimer_local.schedule(new TimerTask() { // Определяем задачу
                @Override
                public void run() {

                    if (MyAsyncTask.isNetworkAvailable(getApplication())) {
                        if(myAsyncTask == null) {
                            String url = getServerUrls()+"objavlenija";
                            List<NameValuePair> params = new ArrayList<NameValuePair>();
                            params.add(new BasicNameValuePair("login", new Settings(getApplicationContext()).getLogin()));
                            params.add(new BasicNameValuePair("user_id", new Settings(getApplicationContext()).getIdUser()));
                            params.add(new BasicNameValuePair("token", new Settings(getApplicationContext()).getToken()));

                            myAsyncTask = new MyAsyncTask(getApplicationContext(), params, url, "get");
                            myAsyncTask.setResponseListener(MyServiceNotif.this);
                            myAsyncTask.execute();

//                            String url2 = getServerUrls()+"login";
//                            List<NameValuePair> params2 = new ArrayList<NameValuePair>();
//                            params.add(new BasicNameValuePair("password",  md5Conver.md5(new Settings(getApplicationContext()).getPassword())));
//                            params.add(new BasicNameValuePair("login", new Settings(getApplicationContext()).getLogin()));
//
//                            myAsyncTask = new MyAsyncTask(getApplicationContext(), params2, url2, "post");
//                            myAsyncTask.setResponseListener(MyServiceNotif.this);
//                            myAsyncTask.execute();

                            Calendar calendar = Calendar.getInstance();
                            Calendar calendar_time = Calendar.getInstance();

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            SimpleDateFormat sdfT = new SimpleDateFormat("HH:mm");
                            String cuurent_d = sdf.format(calendar.getTime());

                            String cuurent_date_time = sdfT.format(calendar_time.getTime());

                            calendar_time.add(Calendar.HOUR, 6);
                            String cuurent_date = sdf.format(calendar_time.getTime());
                            String cuurent_date_time_2 = sdfT.format(calendar_time.getTime());
                            Log.d("cuurent_date_time_2", "cuurent_date_time_2 "+cuurent_date_time_2);

                            calendar.add(Calendar.DATE, 1);
                            String date_tomorrow = sdf.format(calendar.getTime());

                            String filter = " status_id = 'oplachen' AND vremja_vyleta < '"+cuurent_date_time_2+"' AND data_vyleta = '"+cuurent_date+" 00:00:00' ";

                            ArrayList<MyTicketModel> ticketsForTodayModels = DBSample.getMyTickets(getBaseContext(),null, filter);
                            Log.d("Services", "filter "+filter);

//                            ArrayList<MyTicketModel> tickets = DBSample.getMyTickets(getBaseContext(),null, null);
//
//                            for(int i = 0; i < tickets.size(); i++){
//                                Log.d("Services", "tickets time = "+tickets.get(i).getVremja_vyleta());
//                                Log.d("Services", "tickets date = "+tickets.get(i).getData_vyleta());
//                                Log.d("Services", "tickets id = "+tickets.get(i).getId());
//                                Log.d("Services", "tickets status = "+tickets.get(i).getStatus_id());
//                            }


                            for(int i = 0; i < ticketsForTodayModels.size(); i++){
                                if(!DBSample.getFlightNotifList(getApplicationContext(), ticketsForTodayModels.get(i).getId(), 2)){
                                    ObjavlenijaModel objavlenijaModel = new ObjavlenijaModel();
                                    objavlenijaModel.setId(i);
                                    objavlenijaModel.setCreated_at(cuurent_d+" "+cuurent_date_time);
                                    objavlenijaModel.setZagolovok("Регистрация начнется через 3 часа");
                                    objavlenijaModel.setOpisanie("Рейс: "+ticketsForTodayModels.get(i).getGorod_vyleta_name()+" - "+ticketsForTodayModels.get(i).getGorod_prileta_name());
                                    objavlenijaModel.setId_type(2);
                                    objavlenijaModel.setId_flight(ticketsForTodayModels.get(i).getId());
                                    sendOnChannel(objavlenijaModel);
                                    objavlenijaModel.setId(0);
                                    DBSample.addObjavlenie(getApplicationContext(), objavlenijaModel);
                                    Log.d("Services", "int i = 0; i < ticketsForTodayModels.si");
                                }
                                Log.d("Services", "ticketsForTodayModels > 0 ");
                            }

                            String filter_for_tommorow = "   vremja_vyleta < '"+cuurent_date_time+"' AND data_vyleta = '"+date_tomorrow+"' ";
                            ArrayList<MyTicketModel> ticketsForTomorowModels = DBSample.getMyTickets(getBaseContext(),null, filter_for_tommorow);
                            Log.d("Services", "filter_for_tommorow "+filter_for_tommorow);

                            for(int i = 0; i < ticketsForTomorowModels.size(); i++){

                                if(!DBSample.getFlightNotifList(getApplicationContext(), ticketsForTomorowModels.get(i).getId(), 1)){
                                    ObjavlenijaModel objavlenijaModel = new ObjavlenijaModel();
                                    objavlenijaModel.setId(i);
                                    objavlenijaModel.setCreated_at(cuurent_d+" "+cuurent_date_time);
                                    objavlenijaModel.setId_flight(ticketsForTomorowModels.get(i).getId());
                                    objavlenijaModel.setZagolovok("Вылет через 24 часов");
                                    objavlenijaModel.setOpisanie("Рейс: "+ticketsForTomorowModels.get(i).getGorod_vyleta_name()+" - "+ticketsForTomorowModels.get(i).getGorod_prileta_name());
                                    objavlenijaModel.setId_type(1);
                                    sendOnChannel(objavlenijaModel);
                                    objavlenijaModel.setId(0);
                                    DBSample.addObjavlenie(getApplicationContext(), objavlenijaModel);
                                    Log.d("Services", "if(!getFlightNotifList(getApplicationContext(), ticketsForTomorowMod");
                                }
                                Log.d("Services", "ticketsForTomorowModels > 0 ");
                            }
                        }
                        Log.d("Services", "myServices running-2");
                    }
                }


            }, 0L, 60L * 1000);

        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void responseCame(boolean res, String message, JSONObject jObj) {

        if(res) {
            ListModels objavlenijaModels = new ListModels();
            objectMapper = new ObjectMapper();

            try {
                objavlenijaModels = objectMapper.readValue(jObj.toString(), ListModels.class);
                for(int i = 0; i < objavlenijaModels.getObjavlenija().size(); i++){
                    sendOnChannel(objavlenijaModels.getObjavlenija().get(i));
                    objavlenijaModels.getObjavlenija().get(i).setReceived_date_time(Utilities.getDate("yyyy-MM-dd HH:mm:ss"));
                    DBSample.addObjavlenie(getApplicationContext(), objavlenijaModels.getObjavlenija().get(i));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        myAsyncTask = null;

    }

    public void sendOnChannel(ObjavlenijaModel model){
        Log.d("Services", "sendOnChannel(ObjavlenijaModel model)");

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("YOUR_CHANNEL_ID",
                    "YOUR_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
            mNotificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "YOUR_CHANNEL_ID")
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle(model.getZagolovok()) // title for notification
                .setContentText(model.getOpisanie())// message for notification
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(getApplicationContext(),  MainActivity.class);
        intent.putExtra("activity", "notification");

        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(model.getId(), mBuilder.build());


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//            Parvoz parvoz = new Parvoz();
//
//            parvoz.createNotification(model);
//
//            return;
//        }

//        Intent resultIntent = new Intent(this,  nazarkhudoev.shukhrat.parvoz.MainActivity.class);
//        resultIntent.putExtra("activity", "notification");
//        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        PendingIntent resultPedingIntent = PendingIntent.getActivity(this, Integer.valueOf(model.getId()), resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//
//        Notification notification = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
//                .setSmallIcon(R.drawable.ic_airplanemode)
//                .setContentTitle(model.getZagolovok())
//                .setContentText(model.getOpisanie())
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
//                .setContentIntent(resultPedingIntent)
//                .setSound(android.provider.Settings.System.DEFAULT_NOTIFICATION_URI)
//                .setVibrate(new long[]{1000,1000,1000,1000,1000})
//                .setAutoCancel(true)
//                .build();


//
//        notificationManagerCompat.notify(model.getId(), notification);
    }
}
