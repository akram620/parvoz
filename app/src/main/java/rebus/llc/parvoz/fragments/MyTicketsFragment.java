package rebus.llc.parvoz.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import rebus.llc.parvoz.R;
import rebus.llc.parvoz.TicketForm;
import rebus.llc.parvoz.adapters.MyTicketAdapter;
import rebus.llc.parvoz.db.DBSample;
import rebus.llc.parvoz.models.ListModels;
import rebus.llc.parvoz.models.MyTicketModel;
import rebus.llc.parvoz.others.MyAsyncTask;
import rebus.llc.parvoz.others.Settings;

import static rebus.llc.parvoz.db.DBSample.addFlight;
import static rebus.llc.parvoz.others.Utilities.getServerUrls;

public class MyTicketsFragment extends Fragment implements MyAsyncTask.ResponseCame, MyTicketAdapter.ClickListener {

    MyAsyncTask myAsyncTask;
    ObjectMapper objectMapper;
    RecyclerView recyclerViewMyList;
    Settings settings;
    public Context context;
    ArrayList<MyTicketModel> myFlifhtListModels;
    MyTicketAdapter myTicketAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        View rootView = inflater.inflate(R.layout.my_ticket_fragment, container, false);
        setRetainInstance(true);
        recyclerViewMyList = (RecyclerView) rootView.findViewById(R.id.recycler_view_active2);
        context = getContext();
        settings = new Settings(context);
        getMyTicketsList();
        return rootView;
    }


    public void getMyTicketsList() {

        if (myAsyncTask != null) return;

        String url = getServerUrls() + "zakazy";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("login", new Settings(context).getLogin()));
        params.add(new BasicNameValuePair("user_id", new Settings(context).getIdUser()));
        params.add(new BasicNameValuePair("token", new Settings(context).getToken()));

        myAsyncTask = new MyAsyncTask(context, params, url, "get");
        myAsyncTask.setResponseListener(this);
        myAsyncTask.execute();
    }


    public void setMyFlightList() {
        myFlifhtListModels = DBSample.getMyTickets(context, null, null);
        myTicketAdapter = new MyTicketAdapter(myFlifhtListModels, context);
        myTicketAdapter.setClickListener(this);
        recyclerViewMyList.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewMyList.setAdapter(myTicketAdapter);
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

        if (res) {
            if (myAsyncTask.comandUrl.equals(getServerUrls() + "zakazy")) {
                ListModels listModels = new ListModels();

                try {
                    objectMapper = new ObjectMapper();
                    listModels = objectMapper.readValue(jObj.toString(), ListModels.class);

                    for (int i = 0; i < listModels.getZakazy().size(); i++) {
                        addFlight(context, listModels.getZakazy().get(i));
                    }

                    setMyFlightList();
                    myAsyncTask = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else {
            if (myAsyncTask.comandUrl.equals(getServerUrls() + "zakazy")) {
                setMyFlightList();
                myAsyncTask = null;
            }

        }
    }

}
