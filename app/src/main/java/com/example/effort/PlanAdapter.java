package com.example.effort;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.CustomViewHoler>  {

    ArrayList<String> unFilteredlist;
    ArrayList<String> filteredList;
    private ArrayList<AddPlanData> planlist;
    private Context context;

    public PlanAdapter(ArrayList<AddPlanData> planlist, Context context) {
        this.planlist = planlist;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {//실제 연결된후 뷰홀더를 최초로 만들어냄
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        CustomViewHoler holer=new CustomViewHoler(view);

        return holer;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHoler holder, int position) {

        holder.tvtitle.setText(planlist.get(position).getTitle());
        holder.tvcotent.setText(planlist.get(position).getContent());
        holder.day.setText(planlist.get(position).getDay());
    }

    @Override
    public int getItemCount() {
        return (planlist !=null? planlist.size():0);
    }



    public class CustomViewHoler extends RecyclerView.ViewHolder {

        TextView tvtitle;
        TextView tvcotent;
        TextView day;


        public CustomViewHoler(@NonNull View itemView) {
            super(itemView);

            this.tvtitle=itemView.findViewById(R.id.textView12);
            this.tvcotent=itemView.findViewById(R.id.textView13);
            this.day=itemView.findViewById(R.id.textView14);


        }
    }
}
