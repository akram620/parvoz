package rebus.llc.parvoz.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import rebus.llc.parvoz.R;
import rebus.llc.parvoz.adapters.NotifAdapter;
import rebus.llc.parvoz.db.DBSample;
import rebus.llc.parvoz.models.ObjavlenijaModel;
import rebus.llc.parvoz.others.Settings;

public class NotificationFragment extends Fragment implements NotifAdapter.ClickListener{

    RecyclerView recyclerViewMyList;
    Settings settings;
    public Context context;
    ArrayList<ObjavlenijaModel> objavlenijaModels;
    NotifAdapter notifAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        View rootView = inflater.inflate(R.layout.norification_fragment, container, false);
        setRetainInstance(true);
        recyclerViewMyList = (RecyclerView) rootView.findViewById(R.id.recycler_view_active);
        context = getContext();
        settings = new Settings(context);
        setNotifList();
        return rootView;
    }

    public void setNotifList(){

        objavlenijaModels = DBSample.getNotifList(context);
        notifAdapter = new NotifAdapter(objavlenijaModels, context);
        notifAdapter.setClickListener(this);
        recyclerViewMyList.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewMyList.setAdapter(notifAdapter);
    }

    @Override
    public void itemNotifClicked(ObjavlenijaModel model, int position) {

    }
}
