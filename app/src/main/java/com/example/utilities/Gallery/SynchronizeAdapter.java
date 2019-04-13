package com.example.utilities.Gallery;

/**
 * ImageDetailViewActivity 에서 사용하는
 * RecyclerView Item 와 ViewPager Item 의 동기화를 위한 Interface
 * Used by : ImageDetailViewActivity, HorizontalAdapter, ImageSlideAdapter, ZoomViewPagerImageView
 */
public interface SynchronizeAdapter {
    void setPagerAdapter(int position); // RecyclerView 에서 호출하며 RecyclerView Item 클릭시 ViewPager 와 동기화됨.
    void setVisibleTools(); // ViewPager 클릭시 상단, 하단 layout 나타남.
}
