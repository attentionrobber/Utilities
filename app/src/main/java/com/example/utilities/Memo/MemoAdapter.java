package com.example.utilities.Memo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.utilities.R;
import com.example.utilities.domain.Memo;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Used by: MemoActivity
 * Created by KHS on 2017-02-14.
 */
public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.Holder> {

    private List<Memo> memos;
    private Context context;

    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm");

    MemoAdapter(List<Memo> memos, Context context) { // 생성자
        this.memos = memos;
        this.context = context;
    }

    // 홀더(한 페이지)에 모양을 저장
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_memo_cardview, parent, false); // context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) 와 같다.
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        final Memo memo = memos.get(position); // 데이터를 행 단위로 꺼낸다. 멤버변수가 아닌 지역변수를 참조할땐 상수로 가져와야함.

        if (memo.getTitle().equals("")) // 메모의 제목이 없으면 숨김.
            holder.textView_title.setVisibility(View.GONE);


        if (!memo.getImgUri().equals("")) { // 이미지가(uri) 있는 경우
            String content = memo.getContent(); // 메모 내용
            String[] split = memo.getImgUri().split("\n"); // 여러개의 이미지 uri 를 하나씩 나눔.

            holder.layout_memoImage.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(split[0])
                    .thumbnail(0.5f)// 50%의 비율로 로드
                    .override(150) // 강제 사이즈 제한
                    .dontAnimate()
                    .into(holder.iv_memo); // 첫번째로 추가한 이미지 보이기

            for (String splits : split) {
                content = content.replace(splits, "");
            }
            holder.textView_content.setText(content);

//            SpannableStringBuilder ssb = new SpannableStringBuilder(content);
//            for (int i=0; i<split.length; i++) {
//                int start = content.indexOf(split[i]); // uri text 의 시작 위치
//                int end = start + split[i].length(); // ri text 의 마지막 위치
//                ssb.setSpan(new ForegroundColorSpan(Color.BLUE), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // ssb 글씨 파란색으로 설정
//            }
        } else {
            holder.layout_memoImage.setVisibility(View.GONE);
            holder.textView_content.setText(memo.getContent()); // 메모 내용 세팅
        }

        // 홀더에 데이터를 세팅한다.
        holder.textView_title.setText(memos.get(position).getTitle()); // 메모 제목 세팅
        String date = df.format(memos.get(position).getCurrentDate()); // 메모 날짜 formatting
        holder.textView_time.setText(date); // 메모 날짜 세팅
        holder.imgUri = memos.get(position).getImgUri(); // 메모 이미지 가져오기
        //holder.position = position; // 메모 위치 가져오기
    }

    @Override
    public int getItemCount() {
        return memos.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView textView_title, textView_content, textView_time;
        ImageView iv_memo;
        LinearLayout layout_memoImage;
        String imgUri;
        //int position; // holder position

        Holder(View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            textView_title = itemView.findViewById(R.id.textView_title);
            textView_content = itemView.findViewById(R.id.textView_content);
            textView_time = itemView.findViewById(R.id.textView_time);
            iv_memo = itemView.findViewById(R.id.iv_memo);
            layout_memoImage = itemView.findViewById(R.id.layout_memoImage);

            cardView.setOnClickListener(clickListener);
        }

        private View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intent.putExtra("position", String.valueOf(memos.get(position)));
                //intent.putExtra("position", position);
                Intent intent = new Intent(context, MemoViewActivity.class);
                intent.putExtra("position", getAdapterPosition());
                context.startActivity(intent);
            }
        };
    }
}
