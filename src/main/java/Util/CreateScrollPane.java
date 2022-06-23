package Util;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;

public final class CreateScrollPane {

    final static ScrollPane scroller = new ScrollPane();
    public static ScrollPane createZoomPane(Group group) {
        final double SCALE_DELTA = 1.1;
        StackPane zoomPane = new StackPane();
        zoomPane.getChildren().add(group);

        final Group scrollContent = new Group(zoomPane);
        scroller.setContent(scrollContent);

        scroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scroller.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        scroller.viewportBoundsProperty().addListener((observable, oldValue, newValue) ->
                zoomPane.setPrefSize(newValue.getWidth(),newValue.getHeight()));

        scroller.setPrefViewportWidth(56);
        scroller.setPrefViewportHeight(56);

        zoomPane.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                //event.consume();
                if (event.getDeltaY() == 0) {
                    return;
                }
                double scaleFactor = (event.getDeltaY() > 0) ? SCALE_DELTA
                        : 1 / SCALE_DELTA;
                Point2D scrollOffset = figureScrollOffset(scrollContent, scroller);
                group.setScaleX(group.getScaleX() * scaleFactor);
                group.setScaleY(group.getScaleY() * scaleFactor);
                repositionScroller(scrollContent, scroller, scaleFactor, scrollOffset);
                event.consume();
            }
        });

        return scroller;
    }

    private static Point2D figureScrollOffset(Node scrollContent, ScrollPane scroller) {
        double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
        double hScrollProportion = (scroller.getHvalue() - scroller.getHmin()) / (scroller.getHmax() - scroller.getHmin());
        double scrollXOffset = hScrollProportion * Math.max(0, extraWidth);
        double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
        double vScrollProportion = (scroller.getVvalue() - scroller.getVmin()) / (scroller.getVmax() - scroller.getVmin());
        double scrollYOffset = vScrollProportion * Math.max(0, extraHeight);
        return new Point2D(scrollXOffset, scrollYOffset);
    }

    private static void repositionScroller(Node scrollContent, ScrollPane scroller, double scaleFactor, Point2D scrollOffset) {
        double scrollXOffset = scrollOffset.getX();
        double scrollYOffset = scrollOffset.getY();
        double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
        if (extraWidth > 0) {
            double halfWidth = scroller.getViewportBounds().getWidth() / 2;
            double newScrollXOffset = (scaleFactor - 1) * halfWidth + scaleFactor * scrollXOffset;
            scroller.setHvalue(scroller.getHmin() + newScrollXOffset * (scroller.getHmax() - scroller.getHmin()) / extraWidth);
        } else {
            scroller.setHvalue(scroller.getHmin());
        }

        double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
        if (extraHeight > 0) {
            double halfHeight = scroller.getViewportBounds().getHeight() / 2;
            double newScrollYOffset = (scaleFactor - 1) * halfHeight + scaleFactor * scrollYOffset;
            scroller.setVvalue(scroller.getVmin() + newScrollYOffset * (scroller.getVmax() - scroller.getVmin()) / extraHeight);
        } else {
            scroller.setHvalue(scroller.getHmin());
        }
    }

    public static ScrollPane getScroller() {
        return scroller;
    }
}
