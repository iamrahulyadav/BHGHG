package com.kandara.medicalapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.kandara.medicalapp.Adapter.TestAdapter;
import com.kandara.medicalapp.Model.Test;
import com.kandara.medicalapp.R;
import com.kandara.medicalapp.Util.JsondataUtil;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestFragment extends Fragment {

    ListView testListView;

    ArrayList<Test> testArrayList;

    public TestFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        testListView = view.findViewById(R.id.testListView);

        testArrayList= JsondataUtil.getTestArrayList(getActivity().getApplicationContext());
        TestAdapter testAdapter=new TestAdapter(testArrayList, getActivity());
        testListView.setAdapter(testAdapter);

        return view;
    }

}
