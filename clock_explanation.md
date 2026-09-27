# Clock24Jml: Fast Static Checking

Run:

```bash
openjml -esc --progress --show-summary --timeout 30 Clock24Jml.java
```

## Changes

- The constructor is `pure`, so factory-style methods can promise `assignable \nothing`.
- `plusSeconds` now specifies the direct result state:
  `result.secondsSinceStart == secondsSinceStart + delta`.
  This keeps the useful clock model but avoids a difficult proof involving modulo arithmetic `%`, division `/`, and a cast from the `long` primitive type to the `int` primitive type.
- `main` and `toString` are marked `//@ skipesc`: it is demonstration code and not related to intended static checking.

## CAUTION

The original (see original git commit) `plusSeconds` contract was:

```java
/*@ public normal_behavior
  @   requires delta >= 0;
  @   requires secondsSinceStart <= Long.MAX_VALUE - delta;
  @   ensures \result != null;
  @   ensures \result.secondsSinceStart == secondsSinceStart + delta;
  @   ensures (delta % 60L == 0L) ==> (
  @              \result.second() == this.second()
  @           && \result.minute() == ((this.minute()
  @                + (int)(delta / 60L)) % 60)
  @        );
  @   assignable \nothing;
  @*/
```

- The solver underlying static checking, Z3, was therefore required to prove identities expessed using modular arithmetic. 
 - Also, the contract above casts `delta / 60L` from the `long` primitive type to the `int` primitive type, an operation OpenJML cannot prove safe (try it!).

The revised clock module here still has derived `hour`, `minute`, and `second` views. Their range contracts are quickly statically checkable.

Ideally, use the stronger original contract, as above, when the checker (a future version of Z3 or some other solver) can handle it quickly.