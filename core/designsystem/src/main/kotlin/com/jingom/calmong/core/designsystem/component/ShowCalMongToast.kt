package com.jingom.calmong.core.designsystem.component

import android.content.Context
import android.content.ContextWrapper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.jingom.calmong.core.designsystem.theme.CalMongTheme

/**
 * [CalMongToast]를 **시스템 Toast(커스텀 뷰)** 로 띄운다. **포그라운드 전용.**
 *
 * 우리 디자인을 진짜 `android.widget.Toast`에 실으려면 `Toast.setView`가 필요하다. 단:
 * - API 30(Android 11)부터 `Toast.setView`는 deprecated이며 향후 제거될 수 있다.
 * - targetSdk 30+ 앱이 **백그라운드**에서 커스텀 토스트를 띄우면 무시되고 텍스트 토스트로 대체된다.
 *
 * 따라서 이 함수는 앱이 화면에 떠 있을 때만 우리 디자인대로 보인다.
 *
 * 커스텀 뷰는 [CalMongToast] 컴포저블을 [ComposeView]로 감싸 그대로 재사용한다.
 * [ComposeView]는 ViewTree lifecycle/savedState/viewModelStore owner가 필요하므로
 * 컨텍스트에서 [ComponentActivity]를 찾아 연결한다. 액티비티 컨텍스트가 아니면
 * (예: applicationContext) 우리 디자인을 그릴 수 없어 **텍스트 Toast로 폴백**한다.
 *
 * @param message 표시할 메시지. 호출부에서 `stringResource(...)`로 만들어 넘긴다.
 * @param style 토스트 스타일(정보/에러/경고).
 * @param duration [Toast.LENGTH_SHORT] 또는 [Toast.LENGTH_LONG].
 */
@Suppress("DEPRECATION") // Toast 커스텀 뷰(setView)는 포그라운드 전용으로 의도적으로 사용
fun Context.showCalMongToast(
    message: String,
    style: CalMongToastStyle,
    duration: Int = Toast.LENGTH_SHORT,
) {
    val activity = findComponentActivity()
    if (activity == null) {
        // ViewTree owner가 없으면 ComposeView를 구성할 수 없다 — 텍스트 Toast로 폴백.
        Toast.makeText(this, message, duration).show()
        return
    }

    val composeView =
        ComposeView(this).apply {
            setViewTreeLifecycleOwner(activity)
            setViewTreeViewModelStoreOwner(activity)
            setViewTreeSavedStateRegistryOwner(activity)
            setContent {
                CalMongTheme {
                    CalMongToast(message = message, style = style)
                }
            }
        }

    Toast(this).apply {
        this.duration = duration
        view = composeView
        show()
    }
}

/** 컨텍스트 래퍼 체인을 따라 올라가며 [ComponentActivity]를 찾는다. 없으면 null. */
private fun Context.findComponentActivity(): ComponentActivity? {
    var context: Context? = this
    while (context is ContextWrapper) {
        if (context is ComponentActivity) return context
        context = context.baseContext
    }
    return null
}
