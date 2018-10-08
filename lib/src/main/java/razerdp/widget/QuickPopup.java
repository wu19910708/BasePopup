package razerdp.widget;

import android.animation.Animator;
import android.content.Context;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.QuickPopupConfig;

/**
 * Created by 大灯泡 on 2018/8/23.
 * <p>
 * 快速popup
 */
public class QuickPopup extends BasePopupWindow {

    private QuickPopupConfig mConfig;
    private View content;

    public QuickPopup(Context context, QuickPopupConfig config, View contentView, int w, int h) {
        super(context, w, h, false);
        mConfig = config;
        content = contentView;
        if (mConfig != null) {
            callInit(context, w, h);
        } else {
            throw new NullPointerException("QuickPopupConfig must be not null!");
        }
        applyConfigSetting();
    }

    protected void applyConfigSetting() {
        if (mConfig.getPopupBlurOption() != null) {
            setBlurOption(mConfig.getPopupBlurOption());
        } else {
            setBlurBackgroundEnable(mConfig.isBlurBackground(), mConfig.getOnBlurOptionInitListener());
        }

        setPopupFadeEnable(mConfig.isFadeEnable());

        applyClick();

        if (mConfig.getOffsetX() != 0 || mConfig.getOffsetRatioOfPopupWidth() != 0) {
            setOffsetX((int) (mConfig.getOffsetX() + getWidth() * mConfig.getOffsetRatioOfPopupWidth()));
        }

        if (mConfig.getOffsetY() != 0 || mConfig.getOffsetRatioOfPopupHeight() != 0) {
            setOffsetY((int) (mConfig.getOffsetY() + getHeight() * mConfig.getOffsetRatioOfPopupHeight()));
        }

        setAlignBackground(mConfig.isAlignBackground());
        setAutoLocatePopup(mConfig.isAutoLocated());
    }

    private void applyClick() {
        HashMap<Integer, Pair<WeakReference<View.OnClickListener>, Boolean>> eventsMap = mConfig.getListenersHolderMap();
        if (eventsMap == null || eventsMap.isEmpty()) return;
        for (Map.Entry<Integer, Pair<WeakReference<View.OnClickListener>, Boolean>> entry : eventsMap.entrySet()) {
            int viewId = entry.getKey();
            final Pair<WeakReference<View.OnClickListener>, Boolean> event = entry.getValue();
            View v = findViewById(viewId);
            if (v != null) {
                if (event.second) {
                    v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (event.first != null && event.first.get() != null) {
                                event.first.get().onClick(v);
                            }
                            dismiss();
                        }
                    });
                } else {
                    v.setOnClickListener(event.first == null ? null : event.first.get());
                }
            }
        }
    }


    @Override
    protected Animation onCreateShowAnimation() {
        return mConfig.getShowAnimation();
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return mConfig.getDismissAnimation();
    }

    @Override
    protected Animator onCreateDismissAnimator() {
        return mConfig.getDismissAnimator();
    }

    @Override
    protected Animator onCreateShowAnimator() {
        return mConfig.getShowAnimator();
    }

    @Override
    public View onCreateContentView() {
        return content;
    }
}