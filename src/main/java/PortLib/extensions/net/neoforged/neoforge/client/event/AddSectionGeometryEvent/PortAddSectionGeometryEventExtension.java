package PortLib.extensions.net.neoforged.neoforge.client.event.AddSectionGeometryEvent;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.neoforged.neoforge.client.event.AddSectionGeometryEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.client.PortAddSectionGeometryEvent;

@Extension
public class PortAddSectionGeometryEventExtension {
    public static class AdditionalSectionRenderer {
        @Diff
        public static PortAddSectionGeometryEvent.PortAdditionalSectionRenderer wrap(@This AddSectionGeometryEvent.AdditionalSectionRenderer thiz) {
            return new PortAddSectionGeometryEvent.PortAdditionalSectionRenderer.Delegate(thiz);
        }
    }

    public static class SectionRenderingContext {
        @Diff
        public static PortAddSectionGeometryEvent.PortSectionRenderingContext wrap(@This AddSectionGeometryEvent.SectionRenderingContext thiz) {
            return new PortAddSectionGeometryEvent.PortSectionRenderingContext(thiz);
        }
    }
}
