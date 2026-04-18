package PortLib.extensions.net.neoforged.neoforge.client.event.ScreenEvent;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.neoforged.neoforge.client.event.ScreenEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.client.PortScreenEvent;

@Extension
public class PortScreenEventExtension {
    public static class MouseButtonPressed {
        public static class Post {
            public static class Result {
                @Diff
                public static PortScreenEvent.PortMouseButtonPressed.PortPost.PortResult wrap(@This ScreenEvent.MouseButtonPressed.Post.Result thiz) {
                    if (thiz == ScreenEvent.MouseButtonPressed.Post.Result.FORCE_HANDLED) {
                        return PortScreenEvent.PortMouseButtonPressed.PortPost.PortResult.FORCE_HANDLED;
                    } else if (thiz == ScreenEvent.MouseButtonPressed.Post.Result.FORCE_UNHANDLED) {
                        return PortScreenEvent.PortMouseButtonPressed.PortPost.PortResult.FORCE_UNHANDLED;
                    }
                    return PortScreenEvent.PortMouseButtonPressed.PortPost.PortResult.DEFAULT;
                }
            }
        }
    }

    public static class MouseButtonReleased {
        public static class Post {
            public static class Result {
                @Diff
                public static PortScreenEvent.PortMouseButtonReleased.PortPost.PortResult wrap(@This ScreenEvent.MouseButtonReleased.Post.Result thiz) {
                    if (thiz == ScreenEvent.MouseButtonReleased.Post.Result.FORCE_HANDLED) {
                        return PortScreenEvent.PortMouseButtonReleased.PortPost.PortResult.FORCE_HANDLED;
                    } else if (thiz == ScreenEvent.MouseButtonReleased.Post.Result.FORCE_UNHANDLED) {
                        return PortScreenEvent.PortMouseButtonReleased.PortPost.PortResult.FORCE_UNHANDLED;
                    }
                    return PortScreenEvent.PortMouseButtonReleased.PortPost.PortResult.DEFAULT;
                }
            }
        }
    }
}
