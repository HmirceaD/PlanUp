package com.example.mircea.moneymanager.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.mircea.moneymanager.Fragments.PlannerFragment;
import com.example.mircea.moneymanager.Fragments.SavingsFragment;
import com.example.mircea.moneymanager.Fragments.TransactionsFragment;
import com.example.mircea.moneymanager.R;

public class MainActivityPagerAdapter extends FragmentPagerAdapter{

    private Context mContext;

    //Add bundle later if i need to

    public MainActivityPagerAdapter(Context mContext, FragmentManager fragmentManager){
        super(fragmentManager);
        this.mContext = mContext;
    }

    @Override
    public Fragment getItem(int position) {

        if(position == 0){

            PlannerFragment plannerFragment = new PlannerFragment();
            return plannerFragment;
        }else if (position == 1){

            TransactionsFragment transactionsFragment = new TransactionsFragment();
            return transactionsFragment;
        }else{

            SavingsFragment savingsFragment = new SavingsFragment();
            return savingsFragment;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {

        if(position == 0){
            return mContext.getString(R.string.planner_fragment_title);
        }else if(position == 1){
            return mContext.getString(R.string.transaction_fragment_title);
        }else{
            return mContext.getString(R.string.savings_fragment_title);
        }
    }

    @Override
    public int getCount() { return 3;}
}
