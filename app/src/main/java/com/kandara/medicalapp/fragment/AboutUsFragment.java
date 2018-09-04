package com.kandara.medicalapp.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.kandara.medicalapp.Adapter.OfflineBookStudyAdapter;
import com.kandara.medicalapp.Adapter.TeamMemberAdapter;
import com.kandara.medicalapp.Model.Study;
import com.kandara.medicalapp.Model.TeamMemberItem;
import com.kandara.medicalapp.R;
import com.kandara.medicalapp.Util.JsondataUtil;
import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutUsFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;


    public AboutUsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        viewPager = view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(getTeamFragment1(), "Team Member");
        adapter.addFragment(getTeamFragment2(), "Advisor");
        adapter.addFragment(getTeamFragment3(), "Developer");
        viewPager.setAdapter(adapter);
    }

    @NonNull
    private TeamFragment getTeamFragment1() {
        TeamFragment teamFragment1=new TeamFragment();
        ArrayList<TeamMemberItem> team1=new ArrayList<>();
        //teamMemberItemArrayList.add(new TeamMemberItem("Abinash Neupane", false, R.drawable.abinash, "Developer", "abinash.neupane123@gmail.com"));
        team1.add(new TeamMemberItem("Dr Prem Raj Sigdel ", false, R.drawable.prem, "MCh Resident, Urology ", "Department of Urology and Kidney Transplant \n" +
                "Institute of Medicine, TUTH\n" +
                "Phone No – 9855065200\n" +
                "Email – prem.sigdel7@gmail.com\n"));
        team1.add(new TeamMemberItem("Dr. Subash Bhattarai ", false, R.drawable.subash_bhattarai, "MD Paediatrics (IOM, TUTH) ", "Consultant Paediatrics \n" +
                "Chitwan Medical College\n"));
        team1.add(new TeamMemberItem("Dr Pratap Babu Bhandari ", false, R.drawable.pratap_babu, "MS Orthopedics, NAMS ", ""));
        team1.add(new TeamMemberItem("Dr Suman Phuyal ", false, R.drawable.suman_phuyal, "Mch Resident, Neurosurgery", "Department  of Neurosurgery \n" +
                "Institute of Medicine, TUTH\n"));
        team1.add(new TeamMemberItem("Dr Bikesh Rajbhandari ", false, R.drawable.bikesh_rajbhandari, "MS General Surgery ", "Institute of Medicine, TUTH"));
        team1.add(new TeamMemberItem("Dr Tulsi Bhattarai ", false, R.drawable.tulsi_bhattarai, "MD, Internal Medicine", ""));
        team1.add(new TeamMemberItem("Dr. Jeevan Thapa", false, R.drawable.jeevan_thapa, "MD Resident,", "School of Public Health and Community Medicine,\n" +
                "BPKIHS, Dharan\n"));
        team1.add(new TeamMemberItem("Dr Ashok Kharel ", false, R.drawable.ashok_kharel, "MS General Surgery ", "Institute of Medicine, TUTH "));
        team1.add(new TeamMemberItem("Dr. Kishor Upreti", false, R.drawable.kishor_upreti, "MBBS,MD Ped (NAMS)", "Consultant Pediatrician\n" +
                "Niko Children’s Hospital, Chitwan\n"));
        team1.add(new TeamMemberItem("Dr Ramesh Ghimire ", false, R.drawable.ramesh_ghimir, "MBBS, MD Radiodiagnosis", ""));
        team1.add(new TeamMemberItem("Dr Bijaya Kharel ", false, R.drawable.bijaya_kharel, "Consultant, ENT-HN Surgeon ", "Institute of Medicine, TUTH"));
        team1.add(new TeamMemberItem("Dr Ajay Pandey", false, R.drawable.ajay_pandey, "MBBS, CMS ", ""));
        team1.add(new TeamMemberItem("Dr. Bidhya Bhusal ", false, R.drawable.bidhya_bhusal, "Government medical officer ", "MBBS (NMC-TH) "));
        team1.add(new TeamMemberItem("Dr Prakash Nepali ", false, R.drawable.prakash_nepali, "Government medical officer ", "MBBS (CMCTH)"));
        team1.add(new TeamMemberItem("Dr Shushil Sigdel ", false, R.drawable.sushil_sigdel, "MBBS", "CMS"));
        team1.add(new TeamMemberItem("Dr Surendra Sigdel ", false, R.drawable.surendra_sigdel, "MBBS", "Chitwan Medical College"));
        team1.add(new TeamMemberItem("Saroj Poudel ", false, R.drawable.saroj_poudel, "MBBS", "Chitwan medical college "));
        team1.add(new TeamMemberItem("Sabin Rimal ", false, R.drawable.sabin_rimal, "MBBS", "Chitwan medical college "));
        team1.add(new TeamMemberItem("Pratikshya Baral ", false, R.drawable.pratikshya_baral, "MBBS", "Chitwan medical college "));
        team1.add(new TeamMemberItem("Sahil Rauniyar ", false, R.drawable.sahil_rauniyar, "MBBS", "Chitwan medical college "));
        team1.add(new TeamMemberItem("Sulav Subedi ", false, R.drawable.sulav_subedi, "MBBS", "Chitwan medical college "));
        teamFragment1.setTeamMemberItemArrayList(team1);
        return teamFragment1;
    }

    @NonNull
    private TeamFragment getTeamFragment2() {
        TeamFragment teamFragment1=new TeamFragment();
        ArrayList<TeamMemberItem> team1=new ArrayList<>();
        team1.add(new TeamMemberItem("Dr Bikal Ghimire", false, R.drawable.bikal_ghimire, "MCh, Gastrosurgery", "Assistant Professor\n" +
                "Department of General and GI Surgery\n" +
                "Institute of Medicine, TUTH\n"));
        team1.add(new TeamMemberItem("Dr Prakash Kafle", false, R.drawable.prakash_kafle, "Consultant Neurosurgeon", "MCh Neurosurgery\n" +
                "Nobel Medical College, Biratnagar \n"));
        team1.add(new TeamMemberItem("Dr Bipendra Rai ", false, R.drawable.bipendra_rai, "MCh, Urology", "Institute of Medicine, TUTH"));
        team1.add(new TeamMemberItem("Dr Krishna Manandhar ", false, R.drawable.krishna_manandhar, "MCh, Plastic Surgery", "Institute of Medicine, TUTH"));
        teamFragment1.setTeamMemberItemArrayList(team1);
        return teamFragment1;
    }



    @NonNull
    private TeamFragment getTeamFragment3() {
        TeamFragment teamFragment1=new TeamFragment();
        ArrayList<TeamMemberItem> team1=new ArrayList<>();
        team1.add(new TeamMemberItem("Abinash Neupane ", false, R.drawable.abinash, "Android Developer",
                "Phone No – 9816671050\n" +
                "Email – abinash.neupane123@gmail.com\n"));
        team1.add(new TeamMemberItem("Saurav Karki ", false, R.drawable.saurav, "Project Manager",
                "Phone No – 9804170690\n" +
                        "Email – karkisaurav22@gmail.com\n"));
        team1.add(new TeamMemberItem("Madhav Poudel ", false, R.drawable.madhav, "Backend Developer",
                        "Email – l3lackcurtains@gmail.com\n"));

        teamFragment1.setTeamMemberItemArrayList(team1);
        return teamFragment1;
    }



    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
