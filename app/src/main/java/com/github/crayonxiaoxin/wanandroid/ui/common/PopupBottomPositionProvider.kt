package com.github.crayonxiaoxin.wanandroid.ui.common

import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.PopupPositionProvider

class PopupBottomPositionProvider(var offset: IntOffset = IntOffset(0, 0)) : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        return IntOffset(
            0,
            anchorBounds.bottom - popupContentSize.height
        ) + offset
    }
}