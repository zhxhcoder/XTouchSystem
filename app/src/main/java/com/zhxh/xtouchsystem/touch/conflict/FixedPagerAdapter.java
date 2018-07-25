package com.zhxh.xtouchsystem.touch.conflict;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class FixedPagerAdapter extends FragmentStatePagerAdapter {
    private FragmentManager mFragmentManager;
    private WeakReference<Context> mWeakReference;
    private SparseArray<Fragment> registerFragments;
    private ArrayList<String> mFragments;
    private CViewPager pager;

    public FixedPagerAdapter(Context context, FragmentManager fm, CViewPager pager) {
        super(fm);
        this.mFragmentManager = fm;
        this.mWeakReference = new WeakReference<Context>(context);
        this.registerFragments = new SparseArray<Fragment>(2);
        this.pager = pager;
    }


    @Override
    public Fragment getItem(int position) {
        Log.v("yzy", "adapter-->getItem");
        if (mFragments != null && mFragments.size() > 0 && mWeakReference.get() != null) {
            return Fragment.instantiate(mWeakReference.get(), mFragments.get(position));
        }
        return null;
    }

    @Override
    public int getCount() {
        if (mFragments != null) {
            return mFragments.size();
        }
        return 0;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.v("yzy", "adapter-->instantiateItem");
        Object item = (Fragment) super.instantiateItem(container, position);
        if (item instanceof FirstFragment) {
            // parent
            ((FirstFragment) item).parent = pager;
        }
        registerFragments.put(position, (Fragment) item);
        return item;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        Fragment fragment = registerFragments.get(position);
        registerFragments.remove(position);
        if (fragment != null && fragment.isAdded()) {
            super.destroyItem(container, position, object);
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    public void setFragments(ArrayList<String> fragments) {
        this.mFragments = fragments;
    }
}
