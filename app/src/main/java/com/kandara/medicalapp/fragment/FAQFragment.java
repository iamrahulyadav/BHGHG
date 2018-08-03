package com.kandara.medicalapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.kandara.medicalapp.Adapter.FAQItemAdapter;
import com.kandara.medicalapp.Adapter.OfflineBookStudyAdapter;
import com.kandara.medicalapp.Model.FAQItem;
import com.kandara.medicalapp.Model.Study;
import com.kandara.medicalapp.R;
import com.kandara.medicalapp.Util.JsondataUtil;
import com.orm.SugarRecord;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FAQFragment extends Fragment {



    ListView recyclerView;
    ArrayList<FAQItem> faqItemArrayList;

    public FAQFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_study, container, false);
        recyclerView = view.findViewById(R.id.study_listView);
        faqItemArrayList=new ArrayList<>();
        faqItemArrayList.add(new FAQItem("How do we revise a content?", "The content revision system is possible in only the flash card and MCQ System. In both flash card and MCQ screen you can see a ‘+’ icon. Clicking on that icon saves the content for revision, which you can find in the revision tab."));

        faqItemArrayList.add(new FAQItem("How to know more information of a content?", "The more information of a content can be viewed in Flash Card and MCQ System. In both flash card and MCQ screen you can see a flag icon. Clicking on that icon opens a dialog with more information about the content."));
        faqItemArrayList.add(new FAQItem("What is Offline Download?", "Offline Download enables you to save the content for offline use when you have no internet connection on your mobile. "));
        faqItemArrayList.add(new FAQItem("How many content can be downloaded for Offline Use?", "Only one chapter can be downloaded for offline use. If you try to download another chapter then previously downloaded chapter will be automatically deleted."));
        FAQItemAdapter faqItemAdapter=new FAQItemAdapter(faqItemArrayList, getActivity());
        recyclerView.setAdapter(faqItemAdapter);
        return view;
    }

}
