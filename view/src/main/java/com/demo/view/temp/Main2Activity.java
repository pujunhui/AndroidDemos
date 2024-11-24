package com.demo.view.temp;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.graphics.Path;
import android.os.Bundle;
import android.view.View;
import android.view.animation.PathInterpolator;
import android.widget.ImageView;

import com.demo.view.R;

public class Main2Activity extends AppCompatActivity {
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mImageView = findViewById(R.id.image);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Path path = new Path();
                path.arcTo(50, 50, 700, 700, 600, 600, false);
                ObjectAnimator animator = ObjectAnimator.ofFloat(v, View.X, View.Y, path);
                Path path1 = new Path();
                path1.lineTo(0.6f,0.9f);
                path1.lineTo(0.75f,0.2f);
                path1.lineTo(1f,1f);
                animator.setInterpolator(new PathInterpolator(path1));
                animator.setDuration(5000);
                animator.start();
            }
        });
    }
}
