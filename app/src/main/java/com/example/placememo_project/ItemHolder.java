package com.example.placememo_project;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.example.placememo_project.databinding.ListItemMainBinding;

import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;
import com.xwray.groupie.Item;
import com.xwray.groupie.databinding.BindableItem;

import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmResults;

import static android.content.Context.POWER_SERVICE;
import static com.example.placememo_project.MainActivity.mainContext;
import static com.example.placememo_project.MainActivity.sort;


public class ItemHolder extends BindableItem<ListItemMainBinding> implements View.OnClickListener {
    Data_alam mItem;
    View mViewContent2;
    View mActionContainer2;
    ListItemMainBinding mainBinding;
    Context mContext;
    Realm myRealm;
    int position;

    ItemHolder(Data_alam item,Context context) {
        mItem = item;
        mContext = context;
        Realm.init(mContext);
        myRealm = Realm.getDefaultInstance();
    }

    @Override
    public void bind(@NonNull ListItemMainBinding viewBinding, int position) {
        viewBinding.getRoot().setTag(this);
        mainBinding = viewBinding;
        viewBinding.item.textListMainTitle.setText(mItem.getMemo());
        mViewContent2 = viewBinding.item.viewListMainContent;
        mActionContainer2 = viewBinding.viewListRepoActionContainer;
        mainBinding.item.alam.setOnClickListener(this);
        viewBinding.viewListRepoActionDelete.setOnClickListener(this);
        viewBinding.viewListRepoActionEdit.setOnClickListener(this);
        this.position = position;

        RealmResults<Data_alam> data_alams = myRealm.where(Data_alam.class).equalTo("isAlamOn", false).findAll();
        for(Data_alam data_alam : data_alams){
            if(mItem.getMemo().equals(data_alam.getMemo()))
            viewBinding.item.alam.setImageResource(R.drawable.baseline_notifications_off_white_48dp);
        }

    }



    @Override
    public int getLayout() {
        return R.layout.list_item_main;
    }


    public int getPosition() {
        return position;
    }

    @Override
    public void onClick(View view) {
        if (view == mainBinding.viewListRepoActionDelete) {
            if (mViewContent2.getTranslationX() == -mActionContainer2.getWidth()) {
                RealmResults<Data_alam> data_alams = myRealm.where(Data_alam.class).equalTo("memo", mItem.getMemo()).findAll();
                myRealm.beginTransaction();
                data_alams.deleteAllFromRealm();
                myRealm.commitTransaction();
                ((MainActivity) mainContext).remove();
                ((MainActivity) mainContext).ShowAlamUi(sort);
                mViewContent2.setTranslationX(0f);
            }
        } else if (view == mainBinding.viewListRepoActionEdit) {
            if (mViewContent2.getTranslationX() == -mActionContainer2.getWidth()) {
                ((MainActivity) mainContext).startEdit(mItem.getMemo(), mViewContent2);
                mViewContent2.setTranslationX(0f);
            }
        } else if ( view == mainBinding.item.alam){

            RealmResults<Data_alam> data_alams = myRealm.where(Data_alam.class).equalTo("memo", mItem.getMemo()).findAll();
            if(Objects.requireNonNull(data_alams.first()).getisAlamOn()){
                mainBinding.item.alam.setImageResource(R.drawable.baseline_notifications_off_white_48dp);
                myRealm.beginTransaction();
                Objects.requireNonNull(data_alams.first()).setAlamOn(false);
                myRealm.commitTransaction();
            }else{
                mainBinding.item.alam.setImageResource(R.drawable.baseline_notifications_active_white_48dp);
                myRealm.beginTransaction();
                Objects.requireNonNull(data_alams.first()).setAlamOn(true);
                myRealm.commitTransaction();
                ((MainActivity)mainContext).locationSerch(mContext);
            }
        }
    }

}