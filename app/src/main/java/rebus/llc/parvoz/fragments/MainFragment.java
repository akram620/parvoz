package rebus.llc.parvoz.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rebus.llc.parvoz.CallBackActivity;
import rebus.llc.parvoz.MainActivity;
import rebus.llc.parvoz.R;
import rebus.llc.parvoz.TicketForm;
import rebus.llc.parvoz.adapters.HurizonatlListAdapter;
import rebus.llc.parvoz.adapters.MyTicketAdapter;
import rebus.llc.parvoz.adapters.TicketAdapter;
import rebus.llc.parvoz.db.DBSample;
import rebus.llc.parvoz.models.HorizontalListModel;
import rebus.llc.parvoz.models.ListModels;
import rebus.llc.parvoz.models.MyTicketModel;
import rebus.llc.parvoz.models.SignInData;
import rebus.llc.parvoz.models.TicketModel;
import rebus.llc.parvoz.others.Encrypt;
import rebus.llc.parvoz.others.MyAsyncTask;
import rebus.llc.parvoz.others.Settings;

import static rebus.llc.parvoz.others.Utilities.getDate;
import static rebus.llc.parvoz.others.Utilities.getServerUrls;

public class MainFragment  extends Fragment  implements MyAsyncTask.ResponseCame, SwipeRefreshLayout.OnRefreshListener, HurizonatlListAdapter.ClickListener, MyTicketAdapter.ClickListener, TicketAdapter.ClickListener {

    public Context context;
    Settings settings;
    RecyclerView recyclerViewMyList;
    RecyclerView recyclerViewHorizontal;
    RecyclerView recyclerViewFlights;
    HurizonatlListAdapter hurizonatlListAdapter;
    MyTicketAdapter myTicketAdapter;
    TicketAdapter ticketAdapter;

    ArrayList<HorizontalListModel> horizontalListModels;
    ArrayList<MyTicketModel> myFlifhtListModels;
    ArrayList<TicketModel> ticketModelArrayList;
    private Encrypt md5Conver;

    int chosenHorizontalPozition = 0;
    CardView orderTicket;
    CardView callBack;
    CardView profile;
    CardView callMyTickets;
    MyAsyncTask myAsyncTask;
    ObjectMapper objectMapper;
    int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        View rootView = inflater.inflate(R.layout.main_fragment_layout, container, false);
        setRetainInstance(true);
        md5Conver = new Encrypt();

        swipeRefreshLayout = (SwipeRefreshLayout)  rootView.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        recyclerViewHorizontal = (RecyclerView) rootView.findViewById(R.id.recycler_view_horizontal);
        recyclerViewMyList = (RecyclerView) rootView.findViewById(R.id.recycler_view_active);
        recyclerViewFlights = (RecyclerView) rootView.findViewById(R.id.recycler_view_new);
        orderTicket = (CardView) rootView.findViewById(R.id.orderTicket);
        callBack = (CardView) rootView.findViewById(R.id.callBack);
        callMyTickets = (CardView) rootView.findViewById(R.id.callMyTickets);
        profile = (CardView) rootView.findViewById(R.id.profile);

        context = getContext();
        settings = new Settings(context);

        //setHorizontalList();


        orderTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent infoDialog = new Intent(getActivity(), TicketForm.class);
                infoDialog.putExtra("mode", "new");
                startActivityForResult(infoDialog, 1);
                getActivity().setTitle("Мои билеты");
            }
        });

        callBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent infoDialog = new Intent(getActivity(), CallBackActivity.class);
                startActivityForResult(infoDialog, 1);
            }
        });

        callMyTickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               MainActivity main = (MainActivity) getActivity();
               main.goToMyTickets();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent infoDialog = new Intent(getActivity(), ProfileActivity.class);
//                startActivityForResult(infoDialog, 1);
                callPhone_parvoz();
            }
        });

        getSpravochniki();


        return rootView;
    }

    public void getAktualnyeList(){
        if( myAsyncTask != null) return;

        String url = getServerUrls()+"aktualnye-rejsy";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("login", new Settings(context).getLogin()));
        params.add(new BasicNameValuePair("user_id", new Settings(context).getIdUser()));
        params.add(new BasicNameValuePair("token", new Settings(context).getToken()));

        myAsyncTask = new MyAsyncTask(context, params, url, "post");
        myAsyncTask.setResponseListener(this);
        myAsyncTask.execute();

    }

    public void getSpravochniki(){

        if( myAsyncTask != null) return;

        String url = getServerUrls()+"login";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("password",  md5Conver.md5(new Settings(context).getPassword())));
        params.add(new BasicNameValuePair("login", new Settings(context).getLogin()));

        myAsyncTask = new MyAsyncTask(context, params, url, "post");
        myAsyncTask.setResponseListener(this);
        myAsyncTask.execute();
    }

    public void getMyTicketsList(){

        if( myAsyncTask != null) return;

        String url = getServerUrls()+"zakazy";
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("login", new Settings(context).getLogin()));
        params.add(new BasicNameValuePair("user_id", new Settings(context).getIdUser()));
        params.add(new BasicNameValuePair("token", new Settings(context).getToken()));

        myAsyncTask = new MyAsyncTask(context, params, url, "get");
        myAsyncTask.setResponseListener(this);
        myAsyncTask.execute();

    }


    public void setHorizontalList(){

        horizontalListModels = DBSample.getHorizontalList(context);
        hurizonatlListAdapter = new HurizonatlListAdapter(horizontalListModels, context);
        hurizonatlListAdapter.setClickListener(this);
        recyclerViewHorizontal.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewHorizontal.setAdapter(hurizonatlListAdapter);

        if(horizontalListModels.size() > 0){
            setNewFlightList(horizontalListModels.get(0).getId_country());
        }

    }

    public void setMyFlightList(){

        myFlifhtListModels = DBSample.getMyTickets(context, getDate("yyyy-MM-dd"), null);
        myTicketAdapter = new MyTicketAdapter(myFlifhtListModels, context);
        myTicketAdapter.setClickListener(this);
        recyclerViewMyList.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewMyList.setAdapter(myTicketAdapter);
    }

    public void setNewFlightList(int id_city){

        ticketModelArrayList = DBSample.getNewTickets(context, id_city);

        ticketAdapter = new TicketAdapter(ticketModelArrayList, context);
        ticketAdapter.setClickListener(this);
        recyclerViewFlights.setLayoutManager(new LinearLayoutManager(context));

        recyclerViewFlights.setAdapter(ticketAdapter);

    }

    @Override
    public void itemHorizontalClicked(HorizontalListModel model, int position) {

        if(horizontalListModels.size() > 0) {
            horizontalListModels.get(chosenHorizontalPozition).setState(false);

            if (!horizontalListModels.get(position).isState()) {
                horizontalListModels.get(position).setState(true);
            } else {
                horizontalListModels.get(position).setState(false);
            }
            hurizonatlListAdapter.notifyItemChanged(chosenHorizontalPozition);
            hurizonatlListAdapter.notifyItemChanged(position);

            chosenHorizontalPozition = position;

            setNewFlightList(horizontalListModels.get(position).getId_country());
        }
    }

    @Override
    public void itemMyTicketClicked(MyTicketModel model, int position) {
        Intent infoDialog = new Intent(getActivity(), TicketForm.class);
        infoDialog.putExtra("mode", "view");
        infoDialog.putExtra("id_my_flight", model.getId());
        startActivityForResult(infoDialog, 1);
    }

    @Override
    public void responseCame(boolean res, String message, JSONObject jObj) {

        if(res){
            if(myAsyncTask.comandUrl.equals(getServerUrls()+"zakazy")){
                ListModels listModels = new ListModels();

                try {
                    objectMapper = new ObjectMapper();
                    listModels = objectMapper.readValue(jObj.toString(), ListModels.class);

                    DBSample.cleanMyFlights(context);

                    for(int i =0; i < listModels.getZakazy().size(); i++){
                        DBSample.addFlight(context, listModels.getZakazy().get(i));
                    }
                    setMyFlightList();
                    myAsyncTask = null;
                    getAktualnyeList();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("Connection", e.getMessage());
                }
            }else if(myAsyncTask.comandUrl.equals(getServerUrls()+"login")){
                try {
                    objectMapper = new ObjectMapper();
                    SignInData signInData = objectMapper.readValue(jObj.toString(), SignInData.class);
                    DBSample.addSpravochniki(context, signInData.getSpravochniki()); //HERE WE ADD OUR SPRAVOCHNIKI INTO DB
                    settings.saveToken(signInData.getToken());

                    myAsyncTask = null;
                    getMyTicketsList();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }else if(myAsyncTask.comandUrl.equals(getServerUrls()+"aktualnye-rejsy")){
                ListModels listModels = new ListModels();
                try {
                    objectMapper = new ObjectMapper();
                    listModels = objectMapper.readValue(jObj.toString(), ListModels.class);

                    DBSample.cleanFlights(context);

                    for(int i =0; i < listModels.getAktualnyeRejsy().size(); i++){
                        DBSample.addFActualnyeRejsi(context, listModels.getAktualnyeRejsy().get(i));
                    }
                    setHorizontalList();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("Connection", e.getMessage());
                }

                myAsyncTask = null;

                stopLoading();
            }


        }else {
            if(myAsyncTask.comandUrl.equals(getServerUrls()+"zakazy")){
                setMyFlightList();
                myAsyncTask = null;
                getAktualnyeList();
            }else if(myAsyncTask.comandUrl.equals(getServerUrls()+"login")){
                myAsyncTask = null;
                getMyTicketsList();
            }else if(myAsyncTask.comandUrl.equals(getServerUrls()+"aktualnye-rejsy")){
                myAsyncTask = null;
                setHorizontalList();
                stopLoading();
            }

        }
    }


    public void callPhone_parvoz(){
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            return;
        }else{
            Intent i = new Intent(Intent.ACTION_CALL);
            i.setData(Uri.parse("tel:+79691018008"));
            startActivity(i);
        }
    }

    @Override
    public void itemNewFlightClicked(TicketModel model, int position) {
        Intent infoDialog = new Intent(getActivity(), TicketForm.class);
        infoDialog.putExtra("mode", "new");
        infoDialog.putExtra("id_my_flight", model.getId());
        infoDialog.putExtra("new_flight","1");
        infoDialog.putExtra("gorod_vyleta_id", model.getGorod_vyleta_id());
        infoDialog.putExtra("gorod_prileta_id", model.getGorod_prileta_id());
        infoDialog.putExtra("data_vyleta", model.getData_vyleta());

        startActivityForResult(infoDialog, 1);
    }

    @Override
    public void onRefresh() {

        //Log.d("Connection", "OnRefresh");

        getSpravochniki();
//        new Handler().postDelayed(new Runnable() {
//            @Override public void run() {
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        }, 5000);
    }
    public void stopLoading(){
        swipeRefreshLayout.setRefreshing(false);
    }
}
