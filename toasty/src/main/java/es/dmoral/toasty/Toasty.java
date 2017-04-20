package es.dmoral.toasty;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.CheckResult;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This file is part of Toasty.
 *
 * Toasty is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Toasty is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Toasty.  If not, see <http://www.gnu.org/licenses/>.
 */

@SuppressLint("InflateParams")
public class Toasty {
    private static final @ColorInt int DEFAULT_TEXT_COLOR = Color.parseColor("#FFFFFF");

    private static final @ColorInt int ERROR_COLOR = Color.parseColor("#D50000");
    private static final @ColorInt int INFO_COLOR = Color.parseColor("#3F51B5");
    private static final @ColorInt int SUCCESS_COLOR = Color.parseColor("#388E3C");
    private static final @ColorInt int WARNING_COLOR = Color.parseColor("#FFA900");

    private static final String TOAST_TYPEFACE = "sans-serif-condensed";

    private Toasty() {
    }

    public static Builder normal(Context context) {
        return new Builder(context)
                .withIcon(false)
                .textColor(DEFAULT_TEXT_COLOR)
                .shouldTint(false)
                .duration(Toast.LENGTH_SHORT);
    }

    public static Builder warning(Context context) {
        return new Builder(context)
                .withIcon(true)
                .icon(ToastyUtils.getDrawable(context, R.drawable.ic_error_outline_white_48dp))
                .textColor(DEFAULT_TEXT_COLOR)
                .shouldTint(true)
                .tintColor(WARNING_COLOR)
                .duration(Toast.LENGTH_SHORT);
    }

    public static Builder info(Context context) {
        Builder builder = new Builder(context);
        return builder
                .withIcon(true)
                .icon(ToastyUtils.getDrawable(context, R.drawable.ic_info_outline_white_48dp))
                .textColor(DEFAULT_TEXT_COLOR)
                .shouldTint(true)
                .tintColor(INFO_COLOR)
                .duration(Toast.LENGTH_SHORT);
    }

    public static Builder success(Context context) {
        Builder builder = new Builder(context);
        return builder
                .withIcon(true)
                .icon(ToastyUtils.getDrawable(context, R.drawable.ic_check_white_48dp))
                .textColor(DEFAULT_TEXT_COLOR)
                .shouldTint(true)
                .tintColor(SUCCESS_COLOR)
                .duration(Toast.LENGTH_SHORT);
    }

    public static Builder error(Context context) {
        Builder builder = new Builder(context);
        return builder
                .withIcon(true)
                .icon(ToastyUtils.getDrawable(context, R.drawable.ic_clear_white_48dp))
                .textColor(DEFAULT_TEXT_COLOR)
                .shouldTint(true)
                .tintColor(ERROR_COLOR)
                .duration(Toast.LENGTH_SHORT);
    }

    public static @CheckResult Toast custom(@NonNull Context context, @NonNull CharSequence message, Drawable icon,
                 @ColorInt int textColor, @ColorInt int tintColor, int duration,
                 boolean withIcon, boolean shouldTint) {
        final Toast currentToast = new Toast(context);
        final View toastLayout = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.toast_layout, null);
        final ImageView toastIcon = (ImageView) toastLayout.findViewById(R.id.toast_icon);
        final TextView toastTextView = (TextView) toastLayout.findViewById(R.id.toast_text);
        Drawable drawableFrame;

        if (shouldTint)
            drawableFrame = ToastyUtils.tint9PatchDrawableFrame(context, tintColor);
        else
            drawableFrame = ToastyUtils.getDrawable(context, R.drawable.toast_frame);
        ToastyUtils.setBackground(toastLayout, drawableFrame);

        if (withIcon) {
            if (icon == null)
                throw new IllegalArgumentException("Avoid passing 'icon' as null if 'withIcon' is set to true");
            ToastyUtils.setBackground(toastIcon, icon);
        } else
            toastIcon.setVisibility(View.GONE);

        toastTextView.setTextColor(textColor);
        toastTextView.setText(message);
        toastTextView.setTypeface(Typeface.create(TOAST_TYPEFACE, Typeface.NORMAL));

        currentToast.setView(toastLayout);
        currentToast.setDuration(duration);
        return currentToast;
    }

    public static class Builder {

        private Context mContext;
        private CharSequence mMessage;
        private int mDuration;
        private Drawable mIcon;
        private int mTextColor;
        private int mTintColor;
        private boolean mWithIcon;
        private boolean mShouldTint;


        public Builder(Context context) {
            mContext = context;
        }

        public Builder message(CharSequence message) {
            mMessage = message;
            return this;
        }

        public Builder duration(int duration) {
            mDuration = duration;
            return this;
        }

        public Builder icon(Drawable icon) {
            mIcon = icon;
            mWithIcon = true;
            return this;
        }

        public Builder withIcon(boolean withIcon) {
            mWithIcon = withIcon;
            return this;
        }

        public Builder textColor(int color) {
            mTextColor = color;
            return this;
        }

        public Builder tintColor(int color) {
            mTintColor = color;
            mShouldTint = true;
            return this;
        }

        public Builder shouldTint(boolean shouldTint) {
            mShouldTint = shouldTint;
            return this;
        }

        public Toast build() {
            return custom(mContext, mMessage, mIcon, mTextColor, mTintColor, mDuration, mWithIcon, mShouldTint);
        }

        public void show() {
            custom(mContext, mMessage, mIcon, mTextColor, mTintColor, mDuration, mWithIcon, mShouldTint).show();
        }
    }

}
