package com.example.utilities.Gallery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.utilities.R;
import com.example.utilities.Util_Class.Logger;

import org.checkerframework.checker.linear.qual.Linear;

import java.util.ArrayList;
import java.util.List;

/**
 * Used by : ImageDetailViewActivity
 */
public class ImageSlideAdapter extends PagerAdapter {

    private Context context;
    private List<ImageItem> images;
    private LayoutInflater inflater;

    //----- tools
    private boolean touched = false;

    //----- Related Zoom ----------------------------------------------
//    ImageView image_detailView;
//    private static final String TAG = "TAG_TOUCH";
//    @SuppressWarnings("unused")
//    private static final float MIN_ZOOM = 1f, MAX_ZOOM = 1f;
//
//    // These matrices will be used to scale points of the image
//    Matrix matrix = new Matrix();
//    Matrix savedMatrix = new Matrix();
//
//    // The 3 states (events) which the user is trying to perform
//    static final int NONE = 0;
//    static final int DRAG = 1;
//    static final int ZOOM = 2;
//    int mode = NONE;
//
//    // these PointF objects are used to record the point(s) the user is touching
//    PointF start = new PointF();
//    PointF mid = new PointF();
//    float oldDist = 1f;
//
//    private boolean firstTouch = false; // Double Tap
//    private long time = 0; // Used by : Double Tap
    //----- Related Zoom ----------------------------------------------


    ImageSlideAdapter(Context context, List<ImageItem> images) {
        this.context = context;
        this.images = images;
        inflater = LayoutInflater.from(context);
        //inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(@NonNull ViewGroup viewGroup, int position) {
        View itemView = inflater.inflate(R.layout.item_image_sliding_adapter, viewGroup, false);
        assert itemView != null;


        //ImageView image_detailView = itemView.findViewById(R.id.image_detailView);
        ZoomViewPagerImageView image_detailView = itemView.findViewById(R.id.image_detailView);
        final LinearLayout layout_preview = itemView.findViewById(R.id.layout_imgPreview);

        //Glide.with(context).load(images.get(position).getPath()).into(image_detailView); // placeholder()는 디폴트 이미지를 지정해줄 수 있다.
        Glide.with(context).load(images.get(position).getPath()).into(image_detailView);

        image_detailView.setOnClickListener(v -> {
            if (!touched) {
                layout_preview.setVisibility(View.VISIBLE);
                touched = true;
            } else {
                layout_preview.setVisibility(View.GONE);
                touched = false;
            }
        });
        //image_detailView.setOnTouchListener(this::onTouch);

        viewGroup.addView(itemView, 0); // 생성한 뷰를 컨테이너에 담아준다. 컨테이너 = 뷰페이저를 생성한 최외곽 레이아웃 개념
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

//    /**
//     * Listener of Related Zoom
//     */
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//
//        float scale;
//        image_detailView.setScaleType(ImageView.ScaleType.MATRIX);
//
//        dumpEvent(event);
//
//        // ImageView Pinch Zoom Event
//        switch (event.getAction() & MotionEvent.ACTION_MASK)
//        {
//            case MotionEvent.ACTION_DOWN:   // first finger down only
//
//                savedMatrix.set(matrix);
//                start.set(event.getX(), event.getY());
//                Log.d(TAG, "mode=DRAG"); // write to LogCat
//                mode = DRAG;
//                break;
//
//            case MotionEvent.ACTION_UP: // first finger lifted
//
//            case MotionEvent.ACTION_POINTER_UP: // second finger lifted
//
//                mode = NONE;
//                Log.d(TAG, "mode=NONE");
//                break;
//
//            case MotionEvent.ACTION_POINTER_DOWN: // first and second finger down
//
//                oldDist = spacing(event);
//                Log.d(TAG, "oldDist=" + oldDist);
//                if (oldDist > 5f) {
//                    savedMatrix.set(matrix);
//                    midPoint(mid, event);
//                    mode = ZOOM;
//                    Log.d(TAG, "mode=ZOOM");
//                }
//                break;
//
//            case MotionEvent.ACTION_MOVE:
//
//                if (mode == DRAG) {
//                    matrix.set(savedMatrix);
//                    matrix.postTranslate(event.getX() - start.x, event.getY() - start.y); // create the transformation in the matrix  of points
//                } else if (mode == ZOOM) {
//                    // pinch zooming
//                    float newDist = spacing(event);
//                    Log.d(TAG, "newDist=" + newDist);
//                    if (newDist > 5f) {
//                        matrix.set(savedMatrix);
//                        scale = newDist / oldDist; // setting the scaling of the
//                        // matrix...if scale > 1 means
//                        // zoom in...if scale < 1 means
//                        // zoom out
//                        matrix.postScale(scale, scale, mid.x, mid.y);
//                    }
//                }
//                break;
//            default: break;
//        } // switch
//
//
//        // ImageView Double Tap Listener
//        if(event.getAction() == MotionEvent.ACTION_DOWN) {
//            if (firstTouch && (System.currentTimeMillis() - time) <= 300) {
//                Log.i(TAG+"TAP", "timeDiff: "+(System.currentTimeMillis() - time));
//                firstTouch = false;
//
//                matrix.postScale(2, 2, mid.x, mid.y);
//            } else {
//                time = System.currentTimeMillis();
//                Log.i(TAG," time: "+time);
//                firstTouch = true;
//            }
//        }
//
//        image_detailView.setImageMatrix(matrix); // display the transformation on screen
//
//        return true; // indicate event was handled
//    }
//
//    /*
//     * --------------------------------------------------------------------------
//     * Method: spacing Parameters: MotionEvent Returns: float Description:
//     * checks the spacing between the two fingers on touch
//     * 터치 시 두 손가락 간격 확인
//     * ----------------------------------------------------
//     */
//    private float spacing(MotionEvent event) {
//        float x = event.getX(0) - event.getX(1);
//        float y = event.getY(0) - event.getY(1);
//        return (float) Math.sqrt(x * x + y * y);
//    }
//
//    /*
//     * --------------------------------------------------------------------------
//     * Method: midPoint Parameters: PointF object, MotionEvent Returns: void
//     * Description: calculates the midpoint between the two fingers
//     * 두 손가락 사이 중점 계산
//     * ------------------------------------------------------------
//     */
//    private void midPoint(PointF point, MotionEvent event) {
//        float x = event.getX(0) + event.getX(1);
//        float y = event.getY(0) + event.getY(1);
//        point.set(x / 2, y / 2);
//    }
//
//    /** Show an event in the LogCat view, for debugging */
//    private void dumpEvent(MotionEvent event) {
//        String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE","POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
//        StringBuilder sb = new StringBuilder();
//        int action = event.getAction();
//        int actionCode = action & MotionEvent.ACTION_MASK;
//        sb.append("event ACTION_").append(names[actionCode]);
//
//        if (actionCode == MotionEvent.ACTION_POINTER_DOWN || actionCode == MotionEvent.ACTION_POINTER_UP) {
//            sb.append("(pid ").append(action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
//            sb.append(")");
//        }
//
//        sb.append("[");
//        for (int i = 0; i < event.getPointerCount(); i++) {
//            sb.append("#").append(i);
//            sb.append("(pid ").append(event.getPointerId(i));
//            sb.append(")=").append((int) event.getX(i));
//            sb.append(",").append((int) event.getY(i));
//            if (i + 1 < event.getPointerCount())
//                sb.append(";");
//        }
//
//        sb.append("]");
//        Log.d("Touch Events ---------", sb.toString());
//    }
}
