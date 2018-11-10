package com.example.sunxiang.navigationviewtest;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

  private Toolbar mToolBar;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    //显示toolbar
    mToolBar = findViewById(R.id.toobar);
    setSupportActionBar(mToolBar);
    //设置Navigation 默认是返回键
    ActionBar actionBar = getSupportActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);

  }

  //为了让overflow中的选项显示图标，必须使用反射
  @Override
  public boolean onMenuOpened(int featureId, Menu menu) {
    showToast("kaishi");
    if (menu != null) {
      if (menu.getClass() == MenuBuilder.class) {
        try {
          Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
          m.setAccessible(true);
          m.invoke(menu, true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    return super.onMenuOpened(featureId, menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {


    switch (item.getItemId()) {
      case android.R.id.home:
        showToast("点击了返回键");
        break;
      case R.id.first_id:
        showToast("点击第一个menu");
        break;
      case R.id.second_id:
        showToast("点击第二个menu");
        break;
      case R.id.third_id:
        showToast("点击第三个menu");
        break;
    }
    return super.onOptionsItemSelected(item);

  }

  //设置menu
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater menuInflater = getMenuInflater();
    menuInflater.inflate(R.menu.all_menus, menu);
    return super.onCreateOptionsMenu(menu);
  }

  private void showToast(String msg) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
  }
}
