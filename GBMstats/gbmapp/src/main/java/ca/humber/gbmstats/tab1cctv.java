package ca.humber.gbmstats;
//GBMstats

/**
 * Created by Chigozie on 11/7/2017.
 */
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class tab1cctv extends Fragment{

    //The implementation for the CCTV camera would go here
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1cctv, container, false);
        return rootView;
    }

}
