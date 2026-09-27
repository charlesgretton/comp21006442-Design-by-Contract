// A clock with a comprehensive modular-arithmetic contract.
// Try bounded static checking with:
//   openjml -esc --progress --show-summary --timeout 30 ComprehensiveClockJml.java

public final class ComprehensiveClockJml {

    //@ public invariant 0 <= secondsSinceStart;

    /*@ spec_public @*/ private final long secondsSinceStart;

    /*@ private normal_behavior
      @   requires seconds >= 0;
      @   ensures secondsSinceStart == seconds;
      @*/
    private /*@ pure @*/ ComprehensiveClockJml(long seconds) {
        this.secondsSinceStart = seconds;
    }

    /*@ public normal_behavior
      @   requires seconds >= 0;
      @   ensures \result != null;
      @   ensures \result.secondsSinceStart == seconds;
      @   assignable \nothing;
      @*/
    public static /*@ pure @*/ ComprehensiveClockJml ofSecondsSinceStart(long seconds) {
        return new ComprehensiveClockJml(seconds);
    }

    /*@ public normal_behavior
      @   ensures 0 <= \result && \result <= 23;
      @   assignable \nothing;
      @*/
    public /*@ pure @*/ int hour() {
        return (int) ((secondsSinceStart / 3600L) % 24L);
    }

    /*@ public normal_behavior
      @   ensures 0 <= \result && \result <= 59;
      @   assignable \nothing;
      @*/
    public /*@ pure @*/ int minute() {
        return (int) ((secondsSinceStart / 60L) % 60L);
    }

    /*@ public normal_behavior
      @   ensures 0 <= \result && \result <= 59;
      @   assignable \nothing;
      @*/
    public /*@ pure @*/ int second() {
        return (int) (secondsSinceStart % 60L);
    }

    /*@ public normal_behavior
      @   requires delta >= 0;
      @   requires secondsSinceStart <= Long.MAX_VALUE - delta;
      @   ensures \result != null;
      @   ensures \result.secondsSinceStart == secondsSinceStart + delta;
      @   ensures (delta % 60L == 0L) ==> (
      @              \result.second() == this.second()
      @           && \result.minute() == (int) ((this.minute() + delta / 60L) % 60L)
      @        );
      @   assignable \nothing;
      @*/
    public /*@ pure @*/ ComprehensiveClockJml plusSeconds(long delta) {
        return new ComprehensiveClockJml(secondsSinceStart + delta);
    }
}