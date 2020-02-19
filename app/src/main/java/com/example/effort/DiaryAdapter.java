package com.example.effort;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.CustomViewHoler>  {

    private ArrayList<DiaryData> mData;
    private Context context;

    public DiaryAdapter(ArrayList<DiaryData> mData, Context context) {
        this.mData=mData;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {//실제 연결된후 뷰홀더를 최초로 만들어냄
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_diary,parent,false);
        CustomViewHoler holer=new CustomViewHoler(view);

        return holer;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHoler holder, int position) {

        holder.tvtitle.setText(mData.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return (mData !=null? mData.size():0);
    }



    public class CustomViewHoler extends RecyclerView.ViewHolder {

        TextView tvtitle;



        public CustomViewHoler(@NonNull View itemView) {
            super(itemView);

            this.tvtitle=itemView.findViewById(R.id.tvtitl2);



        }
    }
}
