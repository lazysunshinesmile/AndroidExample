package com.sun.rxjava2examples.module.rxjava2.operators;

import androidx.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sun.rxjava2examples.R;
import com.sun.rxjava2examples.model.OperatorModel;

import java.util.List;

/**
 * Author: sun
 */

public abstract class OperatorsAdapter extends BaseQuickAdapter<OperatorModel, BaseViewHolder> {

    public OperatorsAdapter(@Nullable List<OperatorModel> data) {
        super(R.layout.layout_item_operator,data);
    }

    @Override
    protected void convert(final BaseViewHolder holder, OperatorModel item) {
        if (item != null){
            holder.setText(R.id.item_title,item.title)
                    .setText(R.id.item_des,item.des)
                    .itemView
                    .setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(holder.getAdapterPosition());
                }
            });
        }
    }

    public abstract void onItemClick(int position);
}
