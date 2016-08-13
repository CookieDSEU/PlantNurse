package com.kot32.ksimplelibrary.widgets.view.kenburnsview.exception;

public class IncompatibleRatioException extends RuntimeException {

    public IncompatibleRatioException() {
        super("Can't perform Ken Burns effect on rects with distinct aspect ratios!");
    }
}
