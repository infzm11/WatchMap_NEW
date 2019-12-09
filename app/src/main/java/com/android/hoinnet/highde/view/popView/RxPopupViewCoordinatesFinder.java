package com.android.hoinnet.highde.view.popView;

import android.graphics.Point;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

class RxPopupViewCoordinatesFinder {
    RxPopupViewCoordinatesFinder() {
    }

    static Point getCoordinates(TextView textView, RxPopupView rxPopupView) {
        Point point = new Point();
        RxCoordinates rxCoordinates = new RxCoordinates(rxPopupView.getAnchorView());
        RxCoordinates rxCoordinates2 = new RxCoordinates(rxPopupView.getRootView());
        textView.measure(-2, -2);
        switch (rxPopupView.getPosition()) {
            case 0:
                point = getPositionAbove(textView, rxPopupView, rxCoordinates, rxCoordinates2);
                break;
            case 1:
                point = getPositionBelow(textView, rxPopupView, rxCoordinates, rxCoordinates2);
                break;
            case 3:
                point = getPositionLeftTo(textView, rxPopupView, rxCoordinates, rxCoordinates2);
                break;
            case 4:
                point = getPositionRightTo(textView, rxPopupView, rxCoordinates, rxCoordinates2);
                break;
        }
        point.x += RxPopupViewTool.isRtl() ? -rxPopupView.getOffsetX() : rxPopupView.getOffsetX();
        point.y += rxPopupView.getOffsetY();
        point.x -= rxPopupView.getRootView().getPaddingLeft();
        point.y -= rxPopupView.getRootView().getPaddingTop();
        return point;
    }

    private static Point getPositionRightTo(TextView textView, RxPopupView rxPopupView, RxCoordinates rxCoordinates, RxCoordinates rxCoordinates2) {
        Point point = new Point();
        point.x = rxCoordinates.right;
        AdjustRightToOutOfBounds(textView, rxPopupView.getRootView(), point, rxCoordinates, rxCoordinates2);
        point.y = rxCoordinates.top + getYCenteringOffset(textView, rxPopupView);
        return point;
    }

    private static Point getPositionLeftTo(TextView textView, RxPopupView rxPopupView, RxCoordinates rxCoordinates, RxCoordinates rxCoordinates2) {
        Point point = new Point();
        point.x = rxCoordinates.left - textView.getMeasuredWidth();
        AdjustLeftToOutOfBounds(textView, rxPopupView.getRootView(), point, rxCoordinates, rxCoordinates2);
        point.y = rxCoordinates.top + getYCenteringOffset(textView, rxPopupView);
        return point;
    }

    private static Point getPositionBelow(TextView textView, RxPopupView rxPopupView, RxCoordinates rxCoordinates, RxCoordinates rxCoordinates2) {
        Point point = new Point();
        point.x = rxCoordinates.left + getXOffset(textView, rxPopupView);
        if (rxPopupView.alignedCenter()) {
            AdjustHorizontalCenteredOutOfBounds(textView, rxPopupView.getRootView(), point, rxCoordinates2);
        } else if (rxPopupView.alignedLeft()) {
            AdjustHorizontalLeftAlignmentOutOfBounds(textView, rxPopupView.getRootView(), point, rxCoordinates, rxCoordinates2);
        } else if (rxPopupView.alignedRight()) {
            AdjustHorizotalRightAlignmentOutOfBounds(textView, rxPopupView.getRootView(), point, rxCoordinates, rxCoordinates2);
        }
        point.y = rxCoordinates.bottom;
        return point;
    }

    private static Point getPositionAbove(TextView textView, RxPopupView rxPopupView, RxCoordinates rxCoordinates, RxCoordinates rxCoordinates2) {
        Point point = new Point();
        point.x = rxCoordinates.left + getXOffset(textView, rxPopupView);
        if (rxPopupView.alignedCenter()) {
            AdjustHorizontalCenteredOutOfBounds(textView, rxPopupView.getRootView(), point, rxCoordinates2);
        } else if (rxPopupView.alignedLeft()) {
            AdjustHorizontalLeftAlignmentOutOfBounds(textView, rxPopupView.getRootView(), point, rxCoordinates, rxCoordinates2);
        } else if (rxPopupView.alignedRight()) {
            AdjustHorizotalRightAlignmentOutOfBounds(textView, rxPopupView.getRootView(), point, rxCoordinates, rxCoordinates2);
        }
        point.y = rxCoordinates.top - textView.getMeasuredHeight();
        return point;
    }

    private static void AdjustRightToOutOfBounds(TextView textView, ViewGroup viewGroup, Point point, RxCoordinates rxCoordinates, RxCoordinates rxCoordinates2) {
        ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
        int paddingRight = (rxCoordinates2.right - viewGroup.getPaddingRight()) - rxCoordinates.right;
        if (point.x + textView.getMeasuredWidth() > rxCoordinates2.right - viewGroup.getPaddingRight()) {
            layoutParams.width = paddingRight;
            layoutParams.height = -2;
            textView.setLayoutParams(layoutParams);
            measureViewWithFixedWidth(textView, layoutParams.width);
        }
    }

    private static void AdjustLeftToOutOfBounds(TextView textView, ViewGroup viewGroup, Point point, RxCoordinates rxCoordinates, RxCoordinates rxCoordinates2) {
        ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
        int paddingLeft = rxCoordinates2.left + viewGroup.getPaddingLeft();
        if (point.x < paddingLeft) {
            point.x = paddingLeft;
            layoutParams.width = rxCoordinates.left - paddingLeft;
            layoutParams.height = -2;
            textView.setLayoutParams(layoutParams);
            measureViewWithFixedWidth(textView, layoutParams.width);
        }
    }

    private static void AdjustHorizotalRightAlignmentOutOfBounds(TextView textView, ViewGroup viewGroup, Point point, RxCoordinates rxCoordinates, RxCoordinates rxCoordinates2) {
        ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
        int paddingLeft = rxCoordinates2.left + viewGroup.getPaddingLeft();
        if (point.x < paddingLeft) {
            point.x = paddingLeft;
            layoutParams.width = rxCoordinates.right - paddingLeft;
            layoutParams.height = -2;
            textView.setLayoutParams(layoutParams);
            measureViewWithFixedWidth(textView, layoutParams.width);
        }
    }

    private static void AdjustHorizontalLeftAlignmentOutOfBounds(TextView textView, ViewGroup viewGroup, Point point, RxCoordinates rxCoordinates, RxCoordinates rxCoordinates2) {
        ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
        int paddingRight = rxCoordinates2.right - viewGroup.getPaddingRight();
        if (point.x + textView.getMeasuredWidth() > paddingRight) {
            layoutParams.width = paddingRight - rxCoordinates.left;
            layoutParams.height = -2;
            textView.setLayoutParams(layoutParams);
            measureViewWithFixedWidth(textView, layoutParams.width);
        }
    }

    private static void AdjustHorizontalCenteredOutOfBounds(TextView textView, ViewGroup viewGroup, Point point, RxCoordinates rxCoordinates) {
        ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
        int width = (viewGroup.getWidth() - viewGroup.getPaddingLeft()) - viewGroup.getPaddingRight();
        if (textView.getMeasuredWidth() > width) {
            point.x = rxCoordinates.left + viewGroup.getPaddingLeft();
            layoutParams.width = width;
            layoutParams.height = -2;
            textView.setLayoutParams(layoutParams);
            measureViewWithFixedWidth(textView, width);
        }
    }

    private static void measureViewWithFixedWidth(TextView textView, int i) {
        textView.measure(View.MeasureSpec.makeMeasureSpec(i, 1073741824), -2);
    }

    private static int getXOffset(View view, RxPopupView rxPopupView) {
        switch (rxPopupView.getAlign()) {
            case 0:
                return (rxPopupView.getAnchorView().getWidth() - view.getMeasuredWidth()) / 2;
            case 2:
                return rxPopupView.getAnchorView().getWidth() - view.getMeasuredWidth();
            default:
                return 0;
        }
    }

    private static int getYCenteringOffset(View view, RxPopupView rxPopupView) {
        return (rxPopupView.getAnchorView().getHeight() - view.getMeasuredHeight()) / 2;
    }
}
