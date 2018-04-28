package com.example.mircea.moneymanager.Listeners;


import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.text.Layout;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.mircea.moneymanager.Activities.CreatePlanActivity;

public class HideKeyboardListener {

    private ConstraintLayout layout;
    private Context mContext;

    public HideKeyboardListener(ConstraintLayout layout, Context mContext){

        this.layout = layout;
        this.mContext = mContext;

        hideKeyboard(layout);
    }

    private void hideKeyboard(ConstraintLayout createPlanLayout) {

        final int childView = createPlanLayout.getChildCount();

        createPlanLayout.setOnTouchListener(new KeyboardCloser());

        for(int i = 0; i < childView; i++){

            View view = createPlanLayout.getChildAt(i);

            if(!(view instanceof EditText)){

                view.setOnTouchListener(new KeyboardCloser());
            }
        }
    }

    private void hideOnView(View v) {

        InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private class KeyboardCloser implements View.OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            hideOnView(view);
            return false;
        }
    }
}
