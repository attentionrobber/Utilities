package com.example.utilities.Memo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.utilities.R;
import com.example.utilities.domain.Memo;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Used by: MemoActivity
 * Created by KHS on 2017-02-14.
 */
public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.Holder> {

    private List<Memo> memoList;
    private Context context;
    private Intent intent;

    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm");

    MemoAdapter(List<Memo> memos, Context context) { // 생성자
        this.memoList = memos;
        this.context = context;
        intent = new Intent(context, MemoViewActivity.class);
    }

    // 홀더(한 페이지)에 모양을 저장
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_memo_cardview, parent, false); // context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) 와 같다.
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        final Memo memo = memoList.get(position); // 데이터를 행 단위로 꺼낸다. 멤버변수가 아닌 지역변수를 참조할땐 상수로 가져와야함.

        if (memo.getTitle().equals("")) // 메모의 제목이 없으면 숨김.
            holder.textView_title.setVisibility(View.GONE);

        if (!memo.getImgUri().equals("")) { // 이미지가(uri) 있는 경우
            // 내용은 uri 빼고 "이미지"로 넣기

            String context = memo.getContent(); // 메모 내용

            // imgUri 의 내용을 한줄씩 비교
            String[] split = memo.getImgUri().split("\n");
            for (int i=0; i<split.length; i++) {
                if (context.contains(split[i])) { // 메모 내용과 uri 가 같은 경우
                    SpannableStringBuilder ssb = new SpannableStringBuilder("이미지");
                    ssb.setSpan(new ForegroundColorSpan(Color.BLUE), 0, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    context = context.replace(split[i], ssb+""+i);
                    // TODO: ssb 제대로 나오도록 수정.
                }
            }
            holder.textView_content.setText(context); // 메모 내용 세팅
        } else
            holder.textView_content.setText(memo.getContent()); // 메모 내용 세팅

        // 홀더에 데이터를 세팅한다.
        holder.textView_title.setText(memo.getTitle()); // 메모 제목 세팅
        String date = df.format(memo.getCurrentDate());
        holder.textView_time.setText(date); // 메모 날짜 세팅
        holder.imgUri = memo.getImgUri(); // 메모 이미지 가져오기
        holder.position = position; // 메모 날짜 가져오기
    }

    @Override
    public int getItemCount() {
        return memoList.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView textView_title, textView_content, textView_time;
        String imgUri;
        int position; // holder position

        Holder(View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            textView_title = itemView.findViewById(R.id.textView_title);
            textView_content = itemView.findViewById(R.id.textView_content);
            textView_time = itemView.findViewById(R.id.textView_time);

            cardView.setOnClickListener(clickListener);
        }

        private View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intent.putExtra("position", String.valueOf(datas.get(position)));
                intent.putExtra("position", position);
                context.startActivity(intent);
            }
        };
    }
}
