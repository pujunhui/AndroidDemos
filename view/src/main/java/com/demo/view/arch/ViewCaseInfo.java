package com.demo.view.arch;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.lang.reflect.Method;

public class ViewCaseInfo implements Parcelable {
    private final Method method;
    private final String label;
    private final String description;

    public ViewCaseInfo(Method method, String label, String description) {
        this.method = method;
        this.label = label;
        this.description = description;
    }

    protected ViewCaseInfo(Parcel in) throws Exception {
        String className = in.readString();
        Class<?> clazz = Class.forName(className);

        String methodName = in.readString();
        int parameterCount = in.readInt();
        Class<?>[] parameterClasses = new Class<?>[parameterCount];
        for (int i = 0; i < parameterCount; i++) {
            String parameterClassName = in.readString();
            parameterClasses[i] = Class.forName(parameterClassName);
        }
        method = clazz.getMethod(methodName, parameterClasses);

        label = in.readString();
        description = in.readString();
    }

    public static final Creator<ViewCaseInfo> CREATOR = new Creator<ViewCaseInfo>() {
        @Override
        public ViewCaseInfo createFromParcel(Parcel in) {
            try {
                return new ViewCaseInfo(in);
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        public ViewCaseInfo[] newArray(int size) {
            return new ViewCaseInfo[size];
        }
    };

    public Method getMethod() {
        return method;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        String className = method.getDeclaringClass().getCanonicalName();
        String methodName = method.getName();
        int parameterCount = method.getParameterCount();
        Class<?>[] parameterTypes = method.getParameterTypes();
        dest.writeString(className);
        dest.writeString(methodName);
        dest.writeInt(parameterCount);
        for (int i = 0; i < parameterCount; i++) {
            String parameterClassName = parameterTypes[i].getName();
            dest.writeString(parameterClassName);
        }
        dest.writeString(label);
        dest.writeString(description);
    }
}