package com.fuicuiedu.idedemo.easyshop_demo.main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fuicuiedu.idedemo.easyshop_demo.R;
import com.fuicuiedu.idedemo.easyshop_demo.model.CachePreferences;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoLoginFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO: 2016/11/23 0023 接入环信后，待操作
        if (CachePreferences.getUser().getName() != null){
            return inflater.inflate(R.layout.fragmen_login, container, false);
        }
        return inflater.inflate(R.layout.fragment_no_login, container, false);
    }

}
