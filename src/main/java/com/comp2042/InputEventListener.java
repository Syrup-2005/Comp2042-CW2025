package com.comp2042;

public interface InputEventListener {

    DownData onDownEvent(MoveEvent event);

    ViewData onLeftEvent(MoveEvent event);

    ViewData onRightEvent(MoveEvent event);

    ViewData onRotateEvent(MoveEvent event);

    ViewData onZEvent(MoveEvent event);

    ViewData onXEvent(MoveEvent event);

    ViewData onSpaceEvent(MoveEvent event);

    void createNewGame();
}
