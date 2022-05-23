package com.app.fixirman.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;

import com.app.fixirman.R;
import com.app.fixirman.model.user.AdModel;
import com.app.fixirman.utils.AppConstants;
import com.app.fixirman.utils.AppUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class AdsSliderAdapter extends PagerAdapter {

    private List<AdModel> list;
    private FragmentActivity context;

    public AdsSliderAdapter(List<AdModel> list, FragmentActivity context) {

        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.vh_ad_slider_image, null);
        ImageView imageView = view.findViewById(R.id.ad);
        Glide.with(context).load(AppConstants.HOST_URL+list.get(position).getImage()).apply(
                new RequestOptions().error(R.drawable.ic_no_image)
                        .placeholder(AppUtils.getImageLoader(context)).dontAnimate()
                        .fitCenter())
                .placeholder(AppUtils.getImageLoader(context))
                .into(imageView);
        container.addView(view);
        return view;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object view) {
        container.removeView((View) view);
    }
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return object == view;
    }
}
