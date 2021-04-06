package rebus.llc.parvoz.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rebus.llc.parvoz.R;

public class InfoFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        View rootView = inflater.inflate(R.layout.info_fragment, container, false);

        return rootView;
    }

}
