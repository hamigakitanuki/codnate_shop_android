package com.example.codnate_shop_android;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class Opening_pager_manager extends FragmentPagerAdapter {
    public Opening_pager_manager(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new Fragment_opening();
            case 1:
                return new Fragment_start1();
            case 2:
                return new Fragment_start2();
            case 3:
                return new Fragment_start3();
            default:
                return new Fragment_opening();
        }

    }

    @Override
    public int getCount() {
        return 4;
    }
}
