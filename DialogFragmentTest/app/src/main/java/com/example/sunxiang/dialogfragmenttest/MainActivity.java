package com.example.sunxiang.dialogfragmenttest;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

  Button start;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    start = findViewById(R.id.start);

    start.setOnClickListener(new View.OnClickListener() {
      public DialogFragment dialogFragment;

      @Override
      public void onClick(final View v) {
        dialogFragment = new MyDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "nihao");
      }
    });
  }




}
