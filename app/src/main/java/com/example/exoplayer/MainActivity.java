package com.example.exoplayer;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

  @BindView(R.id.btn_ui) Button btnUi;
  @BindView(R.id.btn_drm) Button btnDrm;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
  }

  @OnClick({ R.id.btn_ui, R.id.btn_drm })
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.btn_ui:
        startActivity(new Intent(this, UiCustomizeActivity.class));
        break;
      case R.id.btn_drm:
       startActivity(new Intent(this, DRMSampleActivity.class));
        break;
    }
  }
}
