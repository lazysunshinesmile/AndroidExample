package com.example.recyclerviewtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class ContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        RecyclerView recyclerView = findViewById(R.id.contact_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        ConcatAdapter concatAdapter = new ConcatAdapter(new ConcatAdapter.Config.Builder().setIsolateViewTypes(true).setStableIdMode(ConcatAdapter.Config.StableIdMode.ISOLATED_STABLE_IDS).build(), new ConcatAdapter1(), new FinishedLineAdapter(), new ConcatAdapter2());
        recyclerView.setAdapter(concatAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlag = ItemTouchHelper.UP|ItemTouchHelper.DOWN;
                int swipeFlags = ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT;
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
    }
}