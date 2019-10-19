package com.example.placememo_project;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.loopeer.itemtouchhelperextension.Extension;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.example.placememo_project.MainActivity.mainContext;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<String> items = new ArrayList<>();
    ArrayList<Integer> color = new ArrayList<>();
    Realm myRealm;
    private static final int VIEW_TYPE_A = 0;   // -- view 타입을 2개로 구분 (  메뉴 /  메모 )
//    private static final int VIEW_TYPE_B = 1;
//    int color;
    Context mcontext;
//    Realm myRealm;
//    String deletMessage;

    public RecyclerAdapter(Context context){
        this.mcontext = context;
        Realm.init(context);
        myRealm = Realm.getDefaultInstance();
    }
    @NonNull
    @Override
    public int getItemViewType(int position) {
            return VIEW_TYPE_A;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_nomal_item_main, viewGroup, false);
            return new ItemSwipeWithActionWidthViewHolder1(v);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof ItemSwipeWithActionWidthViewHolder1) {

            ((ItemSwipeWithActionWidthViewHolder1) viewHolder).textView.setText(items.get(position));
            ((ItemSwipeWithActionWidthViewHolder1) viewHolder).mViewContent1.setBackgroundColor(color.get(position));
            ((ItemSwipeWithActionWidthViewHolder1) viewHolder).mActionContainer1.setBackgroundColor(color.get(position));
            ((ItemSwipeWithActionWidthViewHolder1) viewHolder).mActionViewDelete1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(((ItemSwipeWithActionWidthViewHolder1) viewHolder).mViewContent1.getTranslationX() <=  -(((ItemSwipeWithActionWidthViewHolder1) viewHolder).mActionContainer1.getWidth())) {
                        remove(position);
                        ((ItemSwipeWithActionWidthViewHolder1) viewHolder).mViewContent1.setTranslationX(0f);

                    }
                }
            });
            ((ItemSwipeWithActionWidthViewHolder1) viewHolder).mActionViewEdit1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mcontext,"헤헿",Toast.LENGTH_LONG).show();

                }
            });




        }
    }



    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(String item,int color) {
        items.add(item);
        this.color.add(color);
        Log.d("===",color+"");
        notifyDataSetChanged();  // -- 업데이트
        ((MainActivity)mainContext).checkNoImage_nomal();
    }

    public void remove(int position){
        RealmResults<Data_nomal> data_nomals = myRealm.where(Data_nomal.class).equalTo("memo", items.get(position)).findAll();
        myRealm.beginTransaction();
        data_nomals.deleteAllFromRealm();
        myRealm.commitTransaction();
        myRealm.close();
        items.remove(position);
        color.remove(position);
        notifyDataSetChanged();  // -- 업데이트
        ((MainActivity)mainContext).checkNoImage_nomal();
    }



    public void clear(){
        items.clear();
        color.clear();
        notifyDataSetChanged();  // -- 업데이트
    }

    class ViewHolder1 extends RecyclerView.ViewHolder {
        TextView textView;
        View mViewContent1;
        View mActionContainer1;


        public ViewHolder1(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_list_item_memo);
            mViewContent1 = itemView.findViewById(R.id.view_list_nomal_content);
            mActionContainer1 = itemView.findViewById(R.id.view_list_memo_container);

        }


    }

    /*---------------------------------------------------------------------------------------------------------------------------*/
    class ItemSwipeWithActionWidthViewHolder1 extends ViewHolder1 implements Extension {
        View mActionViewDelete1;
        View mActionViewEdit1;



        public ItemSwipeWithActionWidthViewHolder1(View itemView) {
            super(itemView);
            mActionViewDelete1 = itemView.findViewById(R.id.view_list_memo_delete);
            mActionViewEdit1 = itemView.findViewById(R.id.view_list_memo_edit);
        }

        @Override
        public float getActionWidth() { return mActionContainer1.getWidth(); }
    }

    /*------------------------------------------------------------------------------------------------------------------*/

}
