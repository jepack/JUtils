package com.jepack.jutils;

import android.os.Bundle;
import android.os.Handler;
import android.widget.SeekBar;

import com.jepack.jutils.base.BaseActivity;
import com.jepack.jutils.base.BaseFragment;
import com.jepack.jutils.progress.PaletteProgressFragment;
import com.jepack.lib.widget.PaletteProgressView;

import org.jetbrains.annotations.NotNull;

public class WelcomeActivity extends BaseActivity {

    @NotNull
    @Override
    public BaseFragment instanceHolderFragment(@NotNull BaseActivity activity) {
        return new PaletteProgressFragment();
    }

}
