package com.android.systemui.statusbar.charging;

import android.graphics.PointF;
import android.graphics.RuntimeShader;
import android.util.MathUtils;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0010\u000b\n\u0002\b\f\u0018\u0000 +2\u00020\u0001:\u0001+B\u0007\b\u0000¢\u0006\u0002\u0010\u0002R$\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR$\u0010\u000b\u001a\u00020\n2\u0006\u0010\u0003\u001a\u00020\n@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR$\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u0003\u001a\u00020\u0010@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015R$\u0010\u0016\u001a\u00020\n2\u0006\u0010\u0003\u001a\u00020\n@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\r\"\u0004\b\u0018\u0010\u000fR$\u0010\u0019\u001a\u00020\n2\u0006\u0010\u0003\u001a\u00020\n@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\r\"\u0004\b\u001b\u0010\u000fR$\u0010\u001c\u001a\u00020\n2\u0006\u0010\u0003\u001a\u00020\n@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\r\"\u0004\b\u001e\u0010\u000fR\u001a\u0010\u001f\u001a\u00020 X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b!\u0010\"\"\u0004\b#\u0010$R$\u0010%\u001a\u00020\n2\u0006\u0010\u0003\u001a\u00020\n@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b&\u0010\r\"\u0004\b'\u0010\u000fR$\u0010(\u001a\u00020\n2\u0006\u0010\u0003\u001a\u00020\n@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b)\u0010\r\"\u0004\b*\u0010\u000f¨\u0006,"}, mo64987d2 = {"Lcom/android/systemui/statusbar/charging/RippleShader;", "Landroid/graphics/RuntimeShader;", "()V", "value", "", "color", "getColor", "()I", "setColor", "(I)V", "", "distortionStrength", "getDistortionStrength", "()F", "setDistortionStrength", "(F)V", "Landroid/graphics/PointF;", "origin", "getOrigin", "()Landroid/graphics/PointF;", "setOrigin", "(Landroid/graphics/PointF;)V", "pixelDensity", "getPixelDensity", "setPixelDensity", "progress", "getProgress", "setProgress", "radius", "getRadius", "setRadius", "shouldFadeOutRipple", "", "getShouldFadeOutRipple", "()Z", "setShouldFadeOutRipple", "(Z)V", "sparkleStrength", "getSparkleStrength", "setSparkleStrength", "time", "getTime", "setTime", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: RippleShader.kt */
public final class RippleShader extends RuntimeShader {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final String SHADER = "uniform vec2 in_origin;\n                uniform float in_progress;\n                uniform float in_maxRadius;\n                uniform float in_time;\n                uniform float in_distort_radial;\n                uniform float in_distort_xy;\n                uniform float in_radius;\n                uniform float in_fadeSparkle;\n                uniform float in_fadeCircle;\n                uniform float in_fadeRing;\n                uniform float in_blur;\n                uniform float in_pixelDensity;\n                layout(color) uniform vec4 in_color;\n                uniform float in_sparkle_strength;float triangleNoise(vec2 n) {\n                    n  = fract(n * vec2(5.3987, 5.4421));\n                    n += dot(n.yx, n.xy + vec2(21.5351, 14.3137));\n                    float xy = n.x * n.y;\n                    return fract(xy * 95.4307) + fract(xy * 75.04961) - 1.0;\n                }\n                const float PI = 3.1415926535897932384626;\n\n                float threshold(float v, float l, float h) {\n                  return step(l, v) * (1.0 - step(h, v));\n                }\n\n                float sparkles(vec2 uv, float t) {\n                  float n = triangleNoise(uv);\n                  float s = 0.0;\n                  for (float i = 0; i < 4; i += 1) {\n                    float l = i * 0.01;\n                    float h = l + 0.1;\n                    float o = smoothstep(n - l, h, n);\n                    o *= abs(sin(PI * o * (t + 0.55 * i)));\n                    s += o;\n                  }\n                  return s;\n                }\n\n                float softCircle(vec2 uv, vec2 xy, float radius, float blur) {\n                  float blurHalf = blur * 0.5;\n                  float d = distance(uv, xy);\n                  return 1. - smoothstep(1. - blurHalf, 1. + blurHalf, d / radius);\n                }\n\n                float softRing(vec2 uv, vec2 xy, float radius, float blur) {\n                  float thickness_half = radius * 0.25;\n                  float circle_outer = softCircle(uv, xy, radius + thickness_half, blur);\n                  float circle_inner = softCircle(uv, xy, radius - thickness_half, blur);\n                  return circle_outer - circle_inner;\n                }\n\n                vec2 distort(vec2 p, vec2 origin, float time,\n                    float distort_amount_radial, float distort_amount_xy) {\n                    float2 distance = origin - p;\n                    float angle = atan(distance.y, distance.x);\n                    return p + vec2(sin(angle * 8 + time * 0.003 + 1.641),\n                                    cos(angle * 5 + 2.14 + time * 0.00412)) * distort_amount_radial\n                             + vec2(sin(p.x * 0.01 + time * 0.00215 + 0.8123),\n                                    cos(p.y * 0.01 + time * 0.005931)) * distort_amount_xy;\n                }vec4 main(vec2 p) {\n                    vec2 p_distorted = distort(p, in_origin, in_time, in_distort_radial,\n                        in_distort_xy);\n\n                    // Draw shapes\n                    float sparkleRing = softRing(p_distorted, in_origin, in_radius, in_blur);\n                    float sparkle = sparkles(p - mod(p, in_pixelDensity * 0.8), in_time * 0.00175)\n                        * sparkleRing * in_fadeSparkle;\n                    float circle = softCircle(p_distorted, in_origin, in_radius * 1.2, in_blur);\n                    float rippleAlpha = max(circle * in_fadeCircle,\n                        softRing(p_distorted, in_origin, in_radius, in_blur) * in_fadeRing) * 0.45;\n                    vec4 ripple = in_color * rippleAlpha;\n                    return mix(ripple, vec4(sparkle), sparkle * in_sparkle_strength);\n                }";
    private static final String SHADER_LIB = "float triangleNoise(vec2 n) {\n                    n  = fract(n * vec2(5.3987, 5.4421));\n                    n += dot(n.yx, n.xy + vec2(21.5351, 14.3137));\n                    float xy = n.x * n.y;\n                    return fract(xy * 95.4307) + fract(xy * 75.04961) - 1.0;\n                }\n                const float PI = 3.1415926535897932384626;\n\n                float threshold(float v, float l, float h) {\n                  return step(l, v) * (1.0 - step(h, v));\n                }\n\n                float sparkles(vec2 uv, float t) {\n                  float n = triangleNoise(uv);\n                  float s = 0.0;\n                  for (float i = 0; i < 4; i += 1) {\n                    float l = i * 0.01;\n                    float h = l + 0.1;\n                    float o = smoothstep(n - l, h, n);\n                    o *= abs(sin(PI * o * (t + 0.55 * i)));\n                    s += o;\n                  }\n                  return s;\n                }\n\n                float softCircle(vec2 uv, vec2 xy, float radius, float blur) {\n                  float blurHalf = blur * 0.5;\n                  float d = distance(uv, xy);\n                  return 1. - smoothstep(1. - blurHalf, 1. + blurHalf, d / radius);\n                }\n\n                float softRing(vec2 uv, vec2 xy, float radius, float blur) {\n                  float thickness_half = radius * 0.25;\n                  float circle_outer = softCircle(uv, xy, radius + thickness_half, blur);\n                  float circle_inner = softCircle(uv, xy, radius - thickness_half, blur);\n                  return circle_outer - circle_inner;\n                }\n\n                vec2 distort(vec2 p, vec2 origin, float time,\n                    float distort_amount_radial, float distort_amount_xy) {\n                    float2 distance = origin - p;\n                    float angle = atan(distance.y, distance.x);\n                    return p + vec2(sin(angle * 8 + time * 0.003 + 1.641),\n                                    cos(angle * 5 + 2.14 + time * 0.00412)) * distort_amount_radial\n                             + vec2(sin(p.x * 0.01 + time * 0.00215 + 0.8123),\n                                    cos(p.y * 0.01 + time * 0.005931)) * distort_amount_xy;\n                }";
    private static final String SHADER_MAIN = "vec4 main(vec2 p) {\n                    vec2 p_distorted = distort(p, in_origin, in_time, in_distort_radial,\n                        in_distort_xy);\n\n                    // Draw shapes\n                    float sparkleRing = softRing(p_distorted, in_origin, in_radius, in_blur);\n                    float sparkle = sparkles(p - mod(p, in_pixelDensity * 0.8), in_time * 0.00175)\n                        * sparkleRing * in_fadeSparkle;\n                    float circle = softCircle(p_distorted, in_origin, in_radius * 1.2, in_blur);\n                    float rippleAlpha = max(circle * in_fadeCircle,\n                        softRing(p_distorted, in_origin, in_radius, in_blur) * in_fadeRing) * 0.45;\n                    vec4 ripple = in_color * rippleAlpha;\n                    return mix(ripple, vec4(sparkle), sparkle * in_sparkle_strength);\n                }";
    private static final String SHADER_UNIFORMS = "uniform vec2 in_origin;\n                uniform float in_progress;\n                uniform float in_maxRadius;\n                uniform float in_time;\n                uniform float in_distort_radial;\n                uniform float in_distort_xy;\n                uniform float in_radius;\n                uniform float in_fadeSparkle;\n                uniform float in_fadeCircle;\n                uniform float in_fadeRing;\n                uniform float in_blur;\n                uniform float in_pixelDensity;\n                layout(color) uniform vec4 in_color;\n                uniform float in_sparkle_strength;";
    private int color = 16777215;
    private float distortionStrength;
    private PointF origin = new PointF();
    private float pixelDensity = 1.0f;
    private float progress;
    private float radius;
    private boolean shouldFadeOutRipple = true;
    private float sparkleStrength;
    private float time;

    @Metadata(mo64986d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0007\n\u0002\b\u0004\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J \u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\t2\u0006\u0010\f\u001a\u00020\tH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\r"}, mo64987d2 = {"Lcom/android/systemui/statusbar/charging/RippleShader$Companion;", "", "()V", "SHADER", "", "SHADER_LIB", "SHADER_MAIN", "SHADER_UNIFORMS", "subProgress", "", "start", "end", "progress", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: RippleShader.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        /* access modifiers changed from: private */
        public final float subProgress(float f, float f2, float f3) {
            float min = Math.min(f, f2);
            return (Math.min(Math.max(f3, min), Math.max(f, f2)) - f) / (f2 - f);
        }
    }

    public RippleShader() {
        super(SHADER);
    }

    public final float getRadius() {
        return this.radius;
    }

    public final void setRadius(float f) {
        this.radius = f;
        setFloatUniform("in_maxRadius", f);
    }

    public final PointF getOrigin() {
        return this.origin;
    }

    public final void setOrigin(PointF pointF) {
        Intrinsics.checkNotNullParameter(pointF, "value");
        this.origin = pointF;
        setFloatUniform("in_origin", pointF.x, pointF.y);
    }

    public final float getProgress() {
        return this.progress;
    }

    public final void setProgress(float f) {
        float f2;
        this.progress = f;
        setFloatUniform("in_progress", f);
        float f3 = (float) 1;
        float f4 = f3 - f;
        setFloatUniform("in_radius", (f3 - ((f4 * f4) * f4)) * this.radius);
        setFloatUniform("in_blur", MathUtils.lerp(1.25f, 0.5f, f));
        Companion companion = Companion;
        float f5 = 0.0f;
        float access$subProgress = companion.subProgress(0.0f, 0.1f, f);
        float access$subProgress2 = companion.subProgress(0.4f, 1.0f, f);
        if (this.shouldFadeOutRipple) {
            f5 = companion.subProgress(0.0f, 0.2f, f);
            f2 = companion.subProgress(0.3f, 1.0f, f);
        } else {
            f2 = 0.0f;
        }
        setFloatUniform("in_fadeSparkle", Math.min(access$subProgress, f3 - access$subProgress2));
        setFloatUniform("in_fadeCircle", f3 - f5);
        setFloatUniform("in_fadeRing", Math.min(access$subProgress, f3 - f2));
    }

    public final float getTime() {
        return this.time;
    }

    public final void setTime(float f) {
        this.time = f;
        setFloatUniform("in_time", f);
    }

    public final int getColor() {
        return this.color;
    }

    public final void setColor(int i) {
        this.color = i;
        setColorUniform("in_color", i);
    }

    public final float getSparkleStrength() {
        return this.sparkleStrength;
    }

    public final void setSparkleStrength(float f) {
        this.sparkleStrength = f;
        setFloatUniform("in_sparkle_strength", f);
    }

    public final float getDistortionStrength() {
        return this.distortionStrength;
    }

    public final void setDistortionStrength(float f) {
        this.distortionStrength = f;
        float f2 = (float) 75;
        setFloatUniform("in_distort_radial", this.progress * f2 * f);
        setFloatUniform("in_distort_xy", f2 * f);
    }

    public final float getPixelDensity() {
        return this.pixelDensity;
    }

    public final void setPixelDensity(float f) {
        this.pixelDensity = f;
        setFloatUniform("in_pixelDensity", f);
    }

    public final boolean getShouldFadeOutRipple() {
        return this.shouldFadeOutRipple;
    }

    public final void setShouldFadeOutRipple(boolean z) {
        this.shouldFadeOutRipple = z;
    }
}
