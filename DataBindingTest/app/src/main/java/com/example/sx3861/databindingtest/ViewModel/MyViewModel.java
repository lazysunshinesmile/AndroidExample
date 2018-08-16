package com.example.sx3861.databindingtest.ViewModel;

import android.arch.lifecycle.ViewModel;
import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;

import com.example.sx3861.databindingtest.Model.User;
import com.example.sx3861.databindingtest.R;

public class MyViewModel extends ViewModel {

//    //在xml中如果没有设置app:adapter，那么在自动生成的binding文件中会使用默认的BindingRecyclerViewAdapter
//    public BindingRecyclerViewAdapter<User> adapter = new BindingRecyclerViewAdapter<>();
//
//    //在xml中如果没有设置app:viewHolder，那么在自动生成的binding文件中会使用默认的BindingRecyclerViewAdapter.ViewHolderFactory
//    public BindingRecyclerViewAdapter.ViewHolderFactory holder =  new BindingRecyclerViewAdapter.ViewHolderFactory() {
//        @Override
//        public RecyclerView.ViewHolder createViewHolder(ViewDataBinding binding) {
//            return new MyViewHolder(binding.getRoot());
//        }
//    };
//
//    public ObservableList<User> items = new ObservableArrayList<>();
//
//    public ItemBinding<User> itemBinding = ItemBinding.of(BR.item, R.layout.adapter_item);


}
