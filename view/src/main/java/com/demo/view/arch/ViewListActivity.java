package com.demo.view.arch;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.animator.PathInterpolatorTest;
import com.demo.view.R;
import com.demo.view.drawable.test.GalleryHorizontalScrollViewTest;
import com.demo.view.drawable.test.WaveBitmapDrawableTest;
import com.demo.view.filter.test.FilterViewTest;
import com.demo.view.layout.test.CustomLayoutTest;
import com.demo.view.layout.test.FlowLayoutTest;
import com.demo.view.test.ColorTrackTextViewTest;
import com.demo.view.test.LetterSideBarTest;
import com.demo.view.test.PageTransformerTest;
import com.demo.view.xfermode.test.CircleImageViewTest;
import com.demo.view.xfermode.test.ScratchCardTest;
import com.demo.view.xfermode.test.XfermodeViewTest;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class ViewListActivity extends AppCompatActivity {

    private final List<ViewCaseInfo> testCases = getViewCaseInfos(
            FlowLayoutTest.class,
            ColorTrackTextViewTest.class,
            GalleryHorizontalScrollViewTest.class,
            LetterSideBarTest.class,
            WaveBitmapDrawableTest.class,
            PageTransformerTest.class,
            CircleImageViewTest.class,
            XfermodeViewTest.class,
            ScratchCardTest.class,
            FilterViewTest.class,
            PathInterpolatorTest.class,
            CustomLayoutTest.class
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView testList = findViewById(R.id.view_list);
        testList.setLayoutManager(new LinearLayoutManager(this));
        testList.setAdapter(new TestAdapter(getLayoutInflater(), testCases));

//        Paint paint = new Paint();
//        ColorMatrix cm = new ColorMatrix();
//        cm.setSaturation(0);
//        paint.setColorFilter(new ColorMatrixColorFilter(cm));
//        getWindow().getDecorView().setLayerType(View.LAYER_TYPE_HARDWARE, paint);
    }

    private List<ViewCaseInfo> getViewCaseInfos(Class<?>... classes) {
        List<ViewCaseInfo> testCases = new ArrayList<>();
        for (Class<?> clazz : classes) {
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                ViewCaseInfo testCase = getViewCaseInfo(method);
                if (testCase != null) {
                    testCases.add(testCase);
                }
            }
        }
        return testCases;
    }

    private ViewCaseInfo getViewCaseInfo(Method method) {
        ViewCase annotation = method.getAnnotation(ViewCase.class);
        if (annotation == null) {
            return null;
        }
        int modifiers = method.getModifiers();
        if (!Modifier.isStatic(modifiers)) {
            return null;
        }
        return new ViewCaseInfo(method, annotation.label(), annotation.description());
    }

    private class TestAdapter extends RecyclerView.Adapter<ViewHolder> {
        private final LayoutInflater inflater;
        private final List<ViewCaseInfo> testCases;

        private TestAdapter(LayoutInflater inflater, List<ViewCaseInfo> testCases) {
            this.inflater = inflater;
            this.testCases = testCases;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = inflater.inflate(android.R.layout.simple_list_item_2, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ViewCaseInfo info = testCases.get(position);
            holder.bind(info);
        }

        @Override
        public int getItemCount() {
            return testCases.size();
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView labelTv;
        private final TextView descriptionTv;
        private ViewCaseInfo caseInfo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            labelTv = itemView.findViewById(android.R.id.text1);
            descriptionTv = itemView.findViewById(android.R.id.text2);
            itemView.setOnClickListener(this);
        }

        public void bind(ViewCaseInfo info) {
            labelTv.setText(info.getLabel());
            descriptionTv.setText(info.getDescription());
            caseInfo = info;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ViewListActivity.this, ViewItemActivity.class);
            intent.putExtra("caseInfo", caseInfo);
            startActivity(intent);
        }
    }
}
