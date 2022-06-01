package com.codingo.adapter;
import java.util.List;

import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

public class MainSliderAdopter extends SliderAdapter {
    List<Integer> banerList;
    public MainSliderAdopter(List<Integer> banerList) {
        this.banerList = banerList;
    }
    @Override
    public int getItemCount() {
        return banerList.size();
    }
    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder imageSlideViewHolder) {
  //      imageSlideViewHolder.bindImageSlide(banerList.get(position).toString());
        imageSlideViewHolder.bindImageSlide(banerList.get(position));

    }

}