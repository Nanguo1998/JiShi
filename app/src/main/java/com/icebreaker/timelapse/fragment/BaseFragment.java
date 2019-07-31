package com.icebreaker.timelapse.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.icebreaker.timelapse.R;

/**
 * 基础的Fragment
 * @author Marhong
 * @time 2018/5/25 16:01
 */
public class BaseFragment extends Fragment {
    public FragmentManager mFragmentManager;
    public FragmentTransaction mFragmentTransaction;

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        return super.onCreateView(inflater, container, savedInstanceState);

    }

    public static BaseFragment newInstance(Context context, String tag) {
       BaseFragment mainbaseFragment = null;
        if (TextUtils.equals(tag, context.getString(R.string.str_record))) {
            mainbaseFragment = new RecordFragment();
        } else if (TextUtils.equals(tag, context.getString(R.string.str_honor))) {
            mainbaseFragment = new HonorFragment();
        } else if (TextUtils.equals(tag, context.getString(R.string.str_today))) {
            mainbaseFragment = new TodayFragment();
        }

        return mainbaseFragment;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        // TODO Auto-generated method stub
        super.onDetach();
    }
}
