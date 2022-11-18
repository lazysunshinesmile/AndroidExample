package com.example.recyclerviewtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.ImageView;

public class ContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        RecyclerView recyclerView = findViewById(R.id.contact_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        ConcatAdapter concatAdapter = new ConcatAdapter(new ConcatAdapter.Config.Builder().setIsolateViewTypes(true).setStableIdMode(ConcatAdapter.Config.StableIdMode.NO_STABLE_IDS).build(), new ConcatAdapter1(), new FinishedLineAdapter(), new ConcatAdapter2());
        recyclerView.setAdapter(concatAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                return makeMovementFlags(dragFlag, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                concatAdapter.notifyItemMoved(viewHolder.getBindingAdapterPosition(), target.getBindingAdapterPosition());
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                concatAdapter.notifyItemRemoved(viewHolder.getBindingAdapterPosition());
            }

            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);


        Drawable d = getDrawable(R.drawable.copy);
//        Drawable copy = d.getConstantState().newDrawable();
//        Bitmap copy = getCopy(d);
        Bitmap bitmap = drawableToBitmap(d);
        Bitmap copy = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        ((ImageView) findViewById(R.id.image1)).setImageDrawable(d);
        ((ImageView) findViewById(R.id.image1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTodoFragment("xx");
            }
        });
//        ((ImageView)findViewById(R.id.image2)).setImageDrawable(d);
        /*((ImageView) findViewById(R.id.image1)).postDelayed(new Runnable() {
            @Override
            public void run() {
                ((BitmapDrawable) ((ImageView) findViewById(R.id.image1)).getDrawable()).getBitmap().recycle();
                Bitmap bitmap = drawableToBitmap(d);
                if(!bitmap.isRecycled()) {
                    ((ImageView) findViewById(R.id.image2)).setImageBitmap(bitmap);
                }
//                ((ImageView) findViewById(R.id.image2)).setImageDrawable(d);

            }
        }, 3000);*/

        findViewById(R.id.todo).setOnClickListener(v -> {
            startTodoFragment("view_todo_tab");
        });

        findViewById(R.id.note).setOnClickListener(v -> {
            startTodoFragment("view_notes_tab");
        });

        findViewById(R.id.document).setOnClickListener(v -> {
            startTodoFragment("view_documents");
        });

        findViewById(R.id.my).setOnClickListener(v -> {
            startTodoFragment("view_my_tab");
        });
    }

    private void startTodoFragment(String beanGuid) {
        //设置通知被点击的行为
        Intent clickTodoAlarmIntent = null;
        try {
            clickTodoAlarmIntent = createClickTodoAlarmIntent(beanGuid);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        startActivity(clickTodoAlarmIntent);
    }

    /**
     * Author：sunxiang 11133446
     * Date：2022.03.01 15:51
     * Description: 创建待办点击Intent
     *
     * @param todoId 待办id
     * @return Intent
     */
    public Intent createClickTodoAlarmIntent(String todoId) throws ClassNotFoundException {
        //设置通知被点击的行为
        Intent intentClick = new Intent();
        intentClick.setComponent(new ComponentName("com.android.notes","com.android.notes.Notes"));
//        intentClick.setAction("view");
//        Bundle bundle = new Bundle();
//        bundle.putString("todoId", todoId);
//        bundle.putInt("alarmNotification", 1);
//        intentClick.putExtras(bundle);
        intentClick.setData(Uri.parse("bbknotes://com.android.notes/notes/" + todoId));
        intentClick.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intentClick;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}