package com.example.doanmonhoc.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.doanmonhoc.activity.HomepageFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new HomepageFragment();
            case 1:
                return new HomepageFragment();
            case 2:
                return new HomepageFragment();
            case 3:
                return new HomepageFragment();
            case 4:
                return new HomepageFragment();
            case 5:
                return new HomepageFragment();
            default:
                return new HomepageFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
