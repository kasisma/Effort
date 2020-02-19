package com.example.effort.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.effort.Post;
import com.example.effort.R;

import java.util.List;

public class PostAdepter extends RecyclerView.Adapter<PostAdepter.PostViewHolder> {

    public static final String TAG = "포스터 어뎁터";


    private List<Post> datas;

    public PostAdepter(List<Post> datas) {
        this.datas = datas;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {//뷰홀더를 만들어라
        return new PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post data=datas.get(position);//데이터 리스트의 데이터 값들을 가져옴
        holder.textView.setText(data.getTitle());//가져온 데이터 넣어줌
        holder.contents.setText(data.getContents());
    }

    @Override
    public int getItemCount() {//데이터를 가져온다
        return datas.size();//총길이
    }

    class PostViewHolder extends RecyclerView.ViewHolder{

        private TextView textView;
        private TextView contents;


        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

        textView=itemView.findViewById(R.id.title);
        contents=itemView.findViewById(R.id.item_contents   );


        }
    }
}
