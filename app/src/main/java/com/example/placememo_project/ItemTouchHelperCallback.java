
package com.example.placememo_project;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.example.placememo_project.MainActivity.mainContext;

public class ItemTouchHelperCallback extends ItemTouchHelperExtension.Callback {
    Context context;
    public ItemTouchHelperCallback(Context context){
        this.context = context;
    }
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int swipeFlag = ItemTouchHelper.LEFT;
        return makeMovementFlags(0, swipeFlag);

    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder1) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        if(viewHolder instanceof LocationAdapter.ViewHolder1) {   //-- 저장된 알람 메뉴에 대한 스와이프 기능에 필요한 x 위치 설정
            LocationAdapter.ViewHolder1 holder1 = (LocationAdapter.ViewHolder1) viewHolder;
                holder1.mViewContent1.setTranslationX(dX);

        }else if (viewHolder instanceof  LocationAdapter.ViewHolder2){ //-- 저장된 알람 메모에 대한 스와이프 기능에 필요한 x 위치 설정
            LocationAdapter.ViewHolder2 holder2 = (LocationAdapter.ViewHolder2) viewHolder;
                holder2.mViewContent2.setTranslationX(dX);

        }




    }

    /*------------------------------------------------------------------------------------------------------------------------------------------*/
}