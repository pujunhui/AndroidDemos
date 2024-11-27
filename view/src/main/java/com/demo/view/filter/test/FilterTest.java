package com.demo.view.filter.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.demo.view.R;
import com.demo.view.arch.ViewCase;

import java.util.ArrayList;
import java.util.List;

public class FilterTest {

    private static Bitmap srcBitmap;
    private static ImageView dstImageView;
    private static Bitmap dstBitmap;

    @ViewCase(label = "FilterView", description = "过滤器")
    public static void testFilterView(Context context, ViewGroup parent) {
        View view = View.inflate(context, R.layout.test_filter, parent);

        srcBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.test);
        ImageView srcImageView = view.findViewById(R.id.src_img);
        srcImageView.setImageBitmap(srcBitmap);

        dstImageView = view.findViewById(R.id.dst_img);
        dstBitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), srcBitmap.getConfig());

        Spinner filterTypeSp = view.findViewById(R.id.filter_type_sp);
        Spinner filterItemSp = view.findViewById(R.id.filter_item_sp);


        List<BitmapBlurMaskFilter> blurMaskFilters = new ArrayList<>();
        blurMaskFilters.add(new BitmapBlurMaskFilter("NORMAL", 50, BlurMaskFilter.Blur.NORMAL));
        blurMaskFilters.add(new BitmapBlurMaskFilter("SOLID", 50, BlurMaskFilter.Blur.SOLID));
        blurMaskFilters.add(new BitmapBlurMaskFilter("OUTER", 50, BlurMaskFilter.Blur.OUTER));
        blurMaskFilters.add(new BitmapBlurMaskFilter("INNER", 50, BlurMaskFilter.Blur.INNER));
        final FilterAdapter blurMaskFilterAdapter = new FilterAdapter(context, android.R.layout.simple_list_item_1, android.R.id.text1, blurMaskFilters);

        List<BitmapMaskFilter> embossMaskFilters = new ArrayList<>();
        /**
         * Create an emboss maskfilter
         *
         * @param direction  指定光源的位置，长度为xxx的数组标量[x,y,z]
         * @param ambient    环境光的因子 （0~1），越接近0，环境光越暗
         * @param specular   镜面反射系数 越接近0，镜面反射越强
         * @param blurRadius 模糊半径 值越大，模糊效果越明显
         */
        embossMaskFilters.add(new BitmapMaskFilter("Default", new EmbossMaskFilter(new float[]{1, 1, 1}, 0.2f, 60, 80)));
        final FilterAdapter embossMaskFilterAdapter = new FilterAdapter(context, android.R.layout.simple_list_item_1, android.R.id.text1, embossMaskFilters);

        List<BitmapColorMatrixFilter> colorMatrixColorFilters = new ArrayList<>();
        colorMatrixColorFilters.add(new BitmapColorMatrixFilter("平移运算",
                // 平移运算---加法
                new float[]{
                        1, 0, 0, 0, 0,
                        0, 1, 0, 0, 100,
                        0, 0, 1, 0, 0,
                        0, 0, 0, 1, 0,
                }
        ));
        colorMatrixColorFilters.add(new BitmapColorMatrixFilter("底片效果",
                // 反相效果 -- 底片效果
                new float[]{
                        -1, 0, 0, 0, 255,
                        0, -1, 0, 0, 255,
                        0, 0, -1, 0, 255,
                        0, 0, 0, 1, 0,
                }
        ));
        colorMatrixColorFilters.add(new BitmapColorMatrixFilter("颜色增强",
                // 缩放运算---乘法 -- 颜色增强
                new float[]{
                        1.2f, 0, 0, 0, 0,
                        0, 1.2f, 0, 0, 0,
                        0, 0, 1.2f, 0, 0,
                        0, 0, 0, 1.2f, 0,
                }
        ));
        colorMatrixColorFilters.add(new BitmapColorMatrixFilter("黑白照片",
                // 黑白照片
                //是将我们的三通道变为单通道的灰度模式
                // 去色原理：只要把R G B 三通道的色彩信息设置成一样，那么图像就会变成灰色，
                // 同时为了保证图像亮度不变，同一个通道里的R+G+B =1
                new float[]{
                        0.213f, 0.715f, 0.072f, 0, 0,
                        0.213f, 0.715f, 0.072f, 0, 0,
                        0.213f, 0.715f, 0.072f, 0, 0,
                        0, 0, 0, 1, 0,
                }
        ));
        colorMatrixColorFilters.add(new BitmapColorMatrixFilter("发色效果",
                // 发色效果---（比如红色和绿色交换）
                new float[]{
                        1, 0, 0, 0, 0,
                        0, 0, 1, 0, 0,
                        0, 1, 0, 0, 0,
                        0, 0, 0, 0.5F, 0,
                }
        ));
        colorMatrixColorFilters.add(new BitmapColorMatrixFilter("复古效果",
                // 复古效果
                new float[]{
                        1 / 2f, 1 / 2f, 1 / 2f, 0, 0,
                        1 / 3f, 1 / 3f, 1 / 3f, 0, 0,
                        1 / 4f, 1 / 4f, 1 / 4f, 0, 0,
                        0, 0, 0, 1, 0,
                }
        ));
        colorMatrixColorFilters.add(new BitmapColorMatrixFilter("颜色通道过滤",
                // 颜色通道过滤
                //两个矩阵
                //本身颜色矩阵 A
                //过滤矩阵  c
                //a*c=out color
                new float[]{
                        1.3F, 0, 0, 0, 0,
                        0, 1.3F, 0, 0, 0,
                        0, 0, 1.3F, 0, 0,
                        0, 0, 0, 1, 0,
                }
        ));

        final FilterAdapter colorMatrixColorFilterAdapter = new FilterAdapter(context, android.R.layout.simple_list_item_1, android.R.id.text1, colorMatrixColorFilters);

        filterTypeSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        blurMaskFilterAdapter.applySpinner(filterItemSp);
                        return;
                    case 1:
                        embossMaskFilterAdapter.applySpinner(filterItemSp);
                        return;
                    case 2:
                        colorMatrixColorFilterAdapter.applySpinner(filterItemSp);
                        return;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        filterTypeSp.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, android.R.id.text1) {
            @Override
            public int getCount() {
                return 3;
            }

            @Nullable
            @Override
            public String getItem(int position) {
                switch (position) {
                    case 0:
                        return "BlurMaskFilter";
                    case 1:
                        return "EmbossMaskFilter";
                    case 2:
                        return "ColorMatrixColorFilter";
                    default:
                        return "";
                }
            }
        });
    }

    private static class FilterAdapter extends ArrayAdapter<String> implements AdapterView.OnItemSelectedListener {
        private final List<? extends BitmapFilter> filters;

        public FilterAdapter(@NonNull Context context, int resource, int textViewResourceId, List<? extends BitmapFilter> filters) {
            super(context, resource, textViewResourceId);
            this.filters = filters;
        }

        public void applySpinner(Spinner spinner) {
            spinner.setAdapter(this);
            spinner.setOnItemSelectedListener(this);
        }

        @Override
        public int getCount() {
            return filters.size();
        }

        @Nullable
        @Override
        public String getItem(int position) {
            return filters.get(position).getName();
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            BitmapFilter filter = filters.get(position);
            Canvas canvas = new Canvas(dstBitmap);
            canvas.drawColor(Color.WHITE);
            filter.drawBitmap(canvas, srcBitmap);
            dstImageView.setImageBitmap(dstBitmap);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private static abstract class BitmapFilter {
        private final String name;

        private BitmapFilter(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public abstract void drawBitmap(Canvas canvas, Bitmap bitmap);
    }

    private static class BitmapMaskFilter extends BitmapFilter {
        private final Paint paint;
        private final MaskFilter maskFilter;

        private BitmapMaskFilter(String name) {
            super(name);
            this.maskFilter = null;
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.RED);
        }

        private BitmapMaskFilter(String name, MaskFilter maskFilter) {
            super(name);
            this.maskFilter = maskFilter;
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.RED);
        }

        protected MaskFilter getMaskFilter() {
            return null;
        }

        @Override
        public final void drawBitmap(Canvas canvas, Bitmap bitmap) {
            if (maskFilter != null) {
                paint.setMaskFilter(maskFilter);
            } else {
                paint.setMaskFilter(getMaskFilter());
            }
            canvas.drawBitmap(bitmap, 0, 0, paint);
        }
    }

    private static class BitmapBlurMaskFilter extends BitmapMaskFilter {
        private final float radius;
        private final BlurMaskFilter.Blur style;

        private BitmapBlurMaskFilter(String name, float radius, BlurMaskFilter.Blur style) {
            super(name);
            this.style = style;
            this.radius = radius;
        }

        public final MaskFilter getMaskFilter() {
            /**
             * Create a blur maskfilter.
             *
             * @param radius 阴影的半径
             * @param style  NORMAL -- 整个图像都被模糊掉
             *               SOLID -- 图像边界外产生一层与Paint颜色一致阴影效果，不影响图像的本身
             *               OUTER -- 图像边界外产生一层阴影，并且将图像变成透明效果
             *               INNER -- 在图像内部边沿产生模糊效果
             * @return
             */
            return new BlurMaskFilter(radius, style);
        }
    }


    private static class BitmapColorFilter extends BitmapFilter {
        private final Paint paint;
        private final ColorFilter colorFilter;

        public BitmapColorFilter(String name) {
            super(name);
            this.colorFilter = null;
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.RED);
        }

        public BitmapColorFilter(String name, ColorFilter colorFilter) {
            super(name);
            this.colorFilter = colorFilter;
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.RED);
        }

        protected ColorFilter getColorFilter() {
            return null;
        }

        @Override
        public final void drawBitmap(Canvas canvas, Bitmap bitmap) {
            if (colorFilter != null) {
                paint.setColorFilter(colorFilter);
            } else {
                paint.setColorFilter(getColorFilter());
            }
            canvas.drawBitmap(bitmap, 0, 0, paint);
        }
    }

    private static class BitmapColorMatrixFilter extends BitmapColorFilter {
        private final float[] src;

        public BitmapColorMatrixFilter(String name, float[] src) {
            super(name);
            this.src = src;
        }

        public final ColorFilter getColorFilter() {
            return new ColorMatrixColorFilter(new ColorMatrix(src));
        }
    }
}
