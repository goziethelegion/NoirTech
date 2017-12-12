package ca.humber.gbmstats;
//redundant activity - previously to be developed by Bradey who dropped the course

/**
 * Created by Chigozie on 11/7/2017.
 */
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class tab2weather extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2weather, container, false);
        return rootView;
    }
}
