package org.mesdag.portlib.event.level;

import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraftforge.event.level.NoteBlockEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHooks;

public abstract class PortNoteBlockEvent<E extends NoteBlockEvent> extends PortBlockEvent<E> {
    @Diff
    protected PortNoteBlockEvent(E e) {
        super(e);
    }

    public PortNote getNote() {
        return PortNote.wrap(e.getNote());
    }

    public PortOctave getOctave() {
        return PortOctave.wrap(e.getOctave());
    }

    public int getVanillaNoteId() {
        return e.getVanillaNoteId();
    }

    public void setNote(PortNote note, PortOctave octave) {
        e.setNote(note.unwrap(), octave.unwrap());
    }

    public static class Play extends PortNoteBlockEvent<NoteBlockEvent.Play> implements IPortCancellableEvent {
        @Diff
        public Play(NoteBlockEvent.Play e) {
            super(e);
        }

        public NoteBlockInstrument getInstrument() {
            return e.getInstrument();
        }

        public void setInstrument(NoteBlockInstrument instrument) {
            e.setInstrument(instrument);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class Change extends PortNoteBlockEvent<NoteBlockEvent.Change> implements IPortCancellableEvent {
        @Diff
        public Change(NoteBlockEvent.Change e) {
            super(e);
        }

        public PortNote getOldNote() {
            return PortNote.wrap(e.getOldNote());
        }

        public PortOctave getOldOctave() {
            return PortOctave.wrap(e.getOctave());
        }

        static {
            PortEventHooks.register();
        }
    }

    public enum PortNote {
        F_SHARP,
        G,
        G_SHARP,
        A,
        A_SHARP,
        B,
        C,
        C_SHARP,
        D,
        D_SHARP,
        E,
        F;

        @Diff
        public NoteBlockEvent.Note unwrap() {
            return switch (this) {
                case F_SHARP -> NoteBlockEvent.Note.F_SHARP;
                case G -> NoteBlockEvent.Note.G;
                case G_SHARP -> NoteBlockEvent.Note.G_SHARP;
                case A -> NoteBlockEvent.Note.A;
                case A_SHARP -> NoteBlockEvent.Note.A_SHARP;
                case B -> NoteBlockEvent.Note.B;
                case C -> NoteBlockEvent.Note.C;
                case C_SHARP -> NoteBlockEvent.Note.C_SHARP;
                case D -> NoteBlockEvent.Note.D;
                case D_SHARP -> NoteBlockEvent.Note.D_SHARP;
                case E -> NoteBlockEvent.Note.E;
                case F -> NoteBlockEvent.Note.F;
            };
        }

        @Diff
        public static PortNote wrap(NoteBlockEvent.Note note) {
            return switch (note) {
                case F_SHARP -> F_SHARP;
                case G -> G;
                case G_SHARP -> G_SHARP;
                case A -> A;
                case A_SHARP -> A_SHARP;
                case B -> B;
                case C -> C;
                case C_SHARP -> C_SHARP;
                case D -> D;
                case D_SHARP -> D_SHARP;
                case E -> E;
                case F -> F;
            };
        }
    }

    public enum PortOctave {
        LOW,
        MID,
        HIGH;

        @Diff
        public NoteBlockEvent.Octave unwrap() {
            return switch (this) {
                case LOW -> NoteBlockEvent.Octave.LOW;
                case MID -> NoteBlockEvent.Octave.MID;
                case HIGH -> NoteBlockEvent.Octave.HIGH;
            };
        }

        @Diff
        public static PortOctave wrap(NoteBlockEvent.Octave octave) {
            return switch (octave) {
                case LOW -> LOW;
                case MID -> MID;
                case HIGH -> HIGH;
            };
        }
    }
}
