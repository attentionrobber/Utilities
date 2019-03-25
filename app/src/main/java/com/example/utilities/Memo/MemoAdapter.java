package com.example.utilities.Memo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.utilities.R;
import com.example.utilities.domain.Memo;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by KHS on 2017-02-14.
 */
public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.Holder> {

    List<Memo> datas;
    Context context;
    Intent intent;

    // 생성자
    public MemoAdapter(List<Memo> datas, Context context) {
        this.datas = datas;
        this.context = context;
        intent = new Intent(context, MemoViewActivity.class);
    }

    // 홀더(한 페이지)에 모양을 저장
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_item, parent, false); // context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) 와 같다.
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        // 1. 데이터를 행 단위로 꺼낸다.
        final Memo memo = datas.get(position); // 멤버변수가 아닌 지역변수를 참조할땐 상수로 가져와야함.

        // 2. 홀더에 데이터를 세팅한다.
        holder.textView_title.setText(memo.getTitle());
        holder.textView_content.setText(memo.getContent());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd kk:mm");
        String date_f = format.format(memo.getCurrentDate());
        holder.textView_time.setText(date_f);
        holder.imageUri = memo.getImgUri();
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView textView_title, textView_content, textView_time;
        String imageUri;
        int position;

        public Holder(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.cardView);
            textView_title = (TextView) itemView.findViewById(R.id.textView_title);
            textView_content = (TextView) itemView.findViewById(R.id.textView_content);
            textView_time = (TextView)itemView.findViewById(R.id.textView_time);

            cardView.setOnClickListener(clickListener);
        }

        private View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intent.putExtra("position", String.valueOf(datas.get(position)));
                intent.putExtra("position", position);
                intent.putExtra("title", textView_title.getText().toString());
                intent.putExtra("content", textView_content.getText().toString());
                intent.putExtra("imageUri", imageUri);

                context.startActivity(intent);
            }
        };
    }
}
