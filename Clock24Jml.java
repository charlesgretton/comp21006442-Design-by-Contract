// Compile for runtime checking (RAC):
//   openjml -rac Clock24Jml.java
// Run with assertions enabled:
//   java -ea Clock24Jml

public final class Clock24Jml {

    //@ public invariant 0 <= secondsSinceStart;

    /*@ spec_public @*/ private final long secondsSinceStart;

    /*@ private normal_behavior
      @   requires seconds >= 0;
      @   ensures secondsSinceStart == seconds;
      @   // NOTE: In constructors, do not use 'assignable' mentioning fields (implicit 'this').
      @   // If you prefer to be explicit, use:  assignable \everything;
      @*/
    private /*@ pure @*/ Clock24Jml(long seconds) {
        this.secondsSinceStart = seconds;
    }

    /*@ public normal_behavior
      @   requires seconds >= 0;
      @   ensures \result != null;
      @   ensures \result.secondsSinceStart == seconds;
      @   assignable \nothing;
      @*/
    public static /*@ pure @*/ Clock24Jml ofSecondsSinceStart(long seconds) {
        return new Clock24Jml(seconds);
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
      @   ensures \result.secondsSinceStart
      @               == secondsSinceStart + delta;
      @   assignable \nothing;
      @*/
    public /*@ pure @*/ Clock24Jml plusSeconds(long delta) {
        return new Clock24Jml(secondsSinceStart + delta);
    }
    
    // Here, I exclude \function{main} from static checking
    //@ skipesc
    public static void main(String[] args) {
      // Start at 10:30:00
      Clock24Jml t = Clock24Jml.ofSecondsSinceStart(10 * 3600L + 30 * 60L);

      // Add 1 hour and 40 minutes (100 minutes total)
      long delta = 100 * 60L;
      System.out.println("Start: " + t + " (minute=" + t.minute() + ")");

      Clock24Jml t2 = t.plusSeconds(delta);
      // Expected time: 10:30 + 1:40 = 12:10.  t2.minute() should be 10.
      System.out.println("End:   " + t2 + " (minute=" + t2.minute() + ")");

      // Example with assertions
      Clock24Jml t3 = Clock24Jml.ofSecondsSinceStart(23 * 3600L + 59 * 60L + 30L);
      Clock24Jml t4 = t3.plusSeconds(60L); // Change to 61 to see a "JML assertion is false" error
      System.out.println(t3 + " + 60s -> " + t4);
      //@ assert t4.second() == t3.second();
      //@ assert t4.minute() == (t3.minute() + 1) % 60;
      //@ assert t4.hour() == ((t3.hour() + ((t3.minute() + 1) / 60)) % 24);
    }
    
    // Here, exclude \function{toString} from static checking
    //@ skipesc
    @Override
    public String toString() {
        return String.format("%02d:%02d:%02d", hour(), minute(), second());
    }
}
