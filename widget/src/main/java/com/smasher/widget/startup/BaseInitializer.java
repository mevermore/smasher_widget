package com.smasher.widget.startup;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.startup.Initializer;

import java.util.List;

/**
 * ClassName:BaseInitializer
 * Package:com.smasher.widget.startup
 * Description:
 *
 * @date:2021/5/12 10:13
 */
public class BaseInitializer<T> implements Initializer<T> {

    @NonNull
    @Override
    public T create(@NonNull Context context) {
        return null;
    }

    @NonNull
    @Override
    public List<Class<? extends Initializer<?>>> dependencies() {
        return null;
    }
}
