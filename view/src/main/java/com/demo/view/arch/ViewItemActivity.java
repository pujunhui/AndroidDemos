package com.demo.view.arch;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.demo.view.R;

public class ViewItemActivity extends AppCompatActivity {
    private ViewCaseInfo caseInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_item);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FrameLayout container = findViewById(R.id.container);

        caseInfo = getIntent().getParcelableExtra("caseInfo");
        if (caseInfo != null) {
            setTitle(caseInfo.getLabel());
            toolbar.setTitle(caseInfo.getLabel());
            toolbar.setSubtitle(caseInfo.getDescription());
            try {
                caseInfo.getMethod().invoke(null, this, container);
            } catch (Exception ignored) {
            }
        }
    }
}