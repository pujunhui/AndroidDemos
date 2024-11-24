package com.demo.view.test;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.demo.view.LetterSideBar;
import com.demo.view.R;
import com.demo.view.arch.ViewCase;

public class LetterSideBarTest {

    @ViewCase(label = "LetterSideBar", description = "字母索引")
    public static void testLetterSideBar(Context context, ViewGroup parent) {
        View view = View.inflate(context, R.layout.test_letter_side_bar, parent);
        LetterSideBar sideBar = view.findViewById(R.id.letter_side_bar);
        sideBar.setOnLetterTouchListener(new LetterSideBar.LetterTouchListener() {
            @Override
            public void touch(@Nullable CharSequence letter) {
                Log.d("PUJH", "" + letter);
            }
        });
    }
}
