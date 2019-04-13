package com.example.utilities.Gallery;

/**
 * Used by : ImageDetailViewActivity, HorizontalAdapter
 */
public interface SynchronizeAdapter {
    void setPagerAdapter(int position); //
    void onClickTools(); // viewPager 클릭시 상단, 하단 view 나타남
}
