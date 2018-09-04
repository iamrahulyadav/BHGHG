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
        faqItemArrayList = new ArrayList<>();
        faqItemArrayList.add(new FAQItem("What is Smart PG? ",
                "PG preparation is indeed a stressful period. Study materials are voluminous and time scarce. The balance between work and academics is very difficult.\n" +
                        "\n" +
                        "Smart PG is the demand of technological era. It encompasses a complete syllabus including all past questions for PG preparation in the handy format. Smart PG includes student friendly features like instant search option, various interactive modes of study, online model tests, self test options and many more. \n" +
                        "\n" +
                        "Smart PG is the best available digital platform for PG preparation. When free, take your mobile and start reading, anytime, anywhere.\n"));
        faqItemArrayList.add(new FAQItem("What is “Read Book”? ",
                "Inside Read Book, you can read the book “PG Access”. The book can be read in flashcards mode and preview mode. "));
        faqItemArrayList.add(new FAQItem("What is Preview mode? ",
                "In preview mode, you can read book as if you are reading PDF file. "));
        faqItemArrayList.add(new FAQItem("What is Flashcards mode? ",
                "In flashcards mode, first the question is displayed. You have time to think the answer of the question. Once you have thought your answers, touch the sceen and the answer is displayed. "));

        faqItemArrayList.add(new FAQItem("How do we revise a content?",
                "The content revision system is possible in only the flash card and MCQ System. In both flash card and MCQ screen you can see a ‘+’ icon. Clicking on that icon saves the content for revision, which you can find in the revision tab.  You can remove the contents in the revision menu by clicking ‘-‘  icon. "));

        faqItemArrayList.add(new FAQItem("What is flag icon? ",
                "Flag icon are kept to increase the accuracy of our contents. There are three options -  Point out mistakes, Suggest additional contents or Suggest mnemonics. You can click any of them and write your feedbacks and send to us. We will improve our contents based on your feedbacks. "));

        faqItemArrayList.add(new FAQItem("Do I need internet connection to use the application? ",
                "Yes, Data are stored online. You need to be connected to internet to use the contents. However, for downloaded contents, you do not require internet connection. "));

        faqItemArrayList.add(new FAQItem("What is Offline Download?",
                "Offline Download enables you to save the content for offline use when you have no internet connection on your mobile. You can download only one chapter at a time. "));

        faqItemArrayList.add(new FAQItem("How many content can be downloaded for Offline Use?",
                "Only one chapter can be downloaded for offline use. If you try to download another chapter then previously downloaded chapter will be automatically deleted."));

        faqItemArrayList.add(new FAQItem("How to read next or previous questions? ",
                "You can use arrow bottoms in the right lower corner. You can also swipe screen left or right to go to previous or next questions respectively. "));

        faqItemArrayList.add(new FAQItem("What is online test? ",
                "It is a model online test exam based on various board exams. "));
        faqItemArrayList.add(new FAQItem("How to use Past Questions option? ",
                "You can read almost all past questions asked in various MD/MS entrance exams. You can apply any filters to practice questions. You can read by board or by year.  When you choose any subject or board, the number of the questions that has been filtered will be displayed in bottom of the page. You can click Start menu and start practicing MCQs. "));

        faqItemArrayList.add(new FAQItem("How can I find the explanation of the MCQ question? ",
                "There is a symbol of inverted exclamation sign within  circle,  clicking of which explanation of the questions will be displayed.  Not every questions are solved. The solution of all questions will be available in near future."));

        faqItemArrayList.add(new FAQItem("What additional features do I get, if I upgrade to Premium user? ",
                "1.\tFull content access. \n" +
                        "2.\tAbility to download the contents for offline view. \n" +
                        "3.\tSearch option. \n"));

        faqItemArrayList.add(new FAQItem("What are the steps to Upgrade my account to Premium user? ",
                "Copy the transection code and past it elsewhere. \n" +
                        "Go to Send Money inside Esewa and send  Rs 1000 to esewa account 9855065200. \n" +
                        "Inside send money menu, go to Remarks and send the message in following pattern – Transection code<space> Full Name of User <space>email id of the user. \n" +
                        "Activation for premium user may take up to 24 hours. \n"));

        faqItemArrayList.add(new FAQItem("Do I need to renew my premium user account? ",
                "Yes. Your premium account is valid only for one year. After one year, you need to renew your premium user account. "));


        FAQItemAdapter faqItemAdapter = new FAQItemAdapter(faqItemArrayList, getActivity());
        recyclerView.setAdapter(faqItemAdapter);
        return view;
    }

}
