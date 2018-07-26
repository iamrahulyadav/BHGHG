package com.kandara.medicalapp.Dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.kandara.medicalapp.Model.Comment;
import com.kandara.medicalapp.R;
import com.kandara.medicalapp.Util.JsondataUtil;
import com.kandara.medicalapp.fragment.CommentAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/30.
 */
public class DiscussionAddMcqDialog extends DialogFragment {

  public DiscussionAddMcqDialog() {
  }

  Handler handler;

  public void setHandler(Handler handler) {
    this.handler = handler;
  }

  Dialog mDialog;
  LinearLayout optionsLayout;
  Button btnDone;
  EditText optionField1, optionField2, optionField3,optionField4;

  String option1="";
  String option2="";
  String option3="";
  String option4="";

  public void setOption1(String option1) {
    this.option1 = option1;
  }

  public void setOption2(String option2) {
    this.option2 = option2;
  }

  public void setOption3(String option3) {
    this.option3 = option3;
  }

  public void setOption4(String option4) {
    this.option4 = option4;
  }

  @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
    if (mDialog == null) {
      mDialog = new Dialog(getActivity(), R.style.cart_dialog);
      mDialog.setContentView(R.layout.alert_addmcq);
      mDialog.setCanceledOnTouchOutside(false);
      mDialog.getWindow().setGravity(Gravity.CENTER);
      View view = mDialog.getWindow().getDecorView();
      optionsLayout=view.findViewById(R.id.optionLayout);

      optionField1=view.findViewById(R.id.optionField1);
      optionField2=view.findViewById(R.id.optionField2);
      optionField3=view.findViewById(R.id.optionField3);
      optionField4=view.findViewById(R.id.optionField4);

      optionField1.setText(option1);
      optionField2.setText(option2);
      optionField3.setText(option3);
      optionField4.setText(option4);

      btnDone=view.findViewById(R.id.btnDone);

      btnDone.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Message message=new Message();
          Bundle bundle=new Bundle();
          option1=optionField1.getText().toString();
          option2=optionField2.getText().toString();
          option3=optionField3.getText().toString();
          option4=optionField4.getText().toString();
          bundle.putString("option1", option1);
          bundle.putString("option2", option2);
          bundle.putString("option3", option3);
          bundle.putString("option4", option4);
          message.setData(bundle);
          handler.sendMessage(message);
          dismiss();
        }
      });
    }
    return mDialog;
  }

  @Override public void onResume() {
    super.onResume();
  }

  @Override public void onPause() {
    super.onPause();

  }


  @Override public void onDismiss(DialogInterface dialog) {
    super.onDismiss(dialog);
    mDialog = null;
    System.gc();
  }
}
