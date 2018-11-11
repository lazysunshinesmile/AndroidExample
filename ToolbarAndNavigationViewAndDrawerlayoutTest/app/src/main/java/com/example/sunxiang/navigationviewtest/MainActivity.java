package com.example.sunxiang.navigationviewtest;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

  private Toolbar mToolBar;
  private NavigationView navigationView;
  private DrawerLayout drawerLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    //navigation 在toolbar下方
//    setContentView(R.layout.activity_main2);

    mToolBar = findViewById(R.id.toobar);
    navigationView = findViewById(R.id.navigation_view);
    drawerLayout = findViewById(R.id.drawerlayou);

    //显示toolbar
    setSupportActionBar(mToolBar);
    //设置Navigation 默认是返回键
    ActionBar actionBar = getSupportActionBar();

    //设置返回键显示
    actionBar.setHomeButtonEnabled(true);
    actionBar.setDisplayHomeAsUpEnabled(true);

    //设置左上角返回键图标，默认是返回
    actionBar.setHomeAsUpIndicator(R.mipmap.ic_launcher_round);

    //以下设置了左上角的功能图标，打开及关闭navigation。有如下语句，就可以注释onOptionsItemSelected中的case android.R.id.home:。
    ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, mToolBar, R.string.app_name,
        R.string.app_name);
    //该方法会自动和actionBar关联, 将开关的图片显示在了action上，如果不设置，也可以有抽屉的效果，不过是默认的图标
    drawerToggle.syncState();
    drawerLayout.addDrawerListener(drawerToggle);

    initListner();


  }

  private void initListner() {
    navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
          case R.id.first_id:
            showToast("navigationview first id");
            break;
          case R.id.second_id:
            showToast("navigationview second id");
            break;
          case R.id.third_id:
            showToast("navigationview third id");
            break;
        }
        return false;
      }
    });
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
//        if (!drawerLayout.isDrawerOpen(navigationView)){
//          drawerLayout.openDrawer(navigationView);
//        } else {
//          drawerLayout.closeDrawer(Gravity.LEFT);
//        }
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
