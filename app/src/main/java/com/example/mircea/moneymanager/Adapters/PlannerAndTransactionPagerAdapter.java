package com.example.mircea.moneymanager.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.mircea.moneymanager.Fragments.PlannerFragment;
import com.example.mircea.moneymanager.Fragments.TransactionsFragment;
import com.example.mircea.moneymanager.R;

public class PlannerAndTransactionPagerAdapter extends FragmentPagerAdapter{

    private Context mContext;

    //Add bundle later if i need to

    public PlannerAndTransactionPagerAdapter(Context mContext, FragmentManager fragmentManager){
        super(fragmentManager);
        this.mContext = mContext;
    }

    @Override
    public Fragment getItem(int position) {

        if(position == 0){

            PlannerFragment plannerFragment = new PlannerFragment();
            return plannerFragment;
        }else{

            TransactionsFragment transactionsFragment = new TransactionsFragment();
            return transactionsFragment;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {

        if(position == 0){
            return mContext.getString(R.string.planner_fragment_title);
        }else if(position == 1){
            return mContext.getString(R.string.transaction_fragment_title);
        }else{
            return null;
        }
    }

    @Override
    public int getCount() { return 2;}
}
