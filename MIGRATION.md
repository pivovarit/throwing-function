# Migrating from 1.x to 2.0

2.0 reconciles the long-divergent `1.6.x` and `main` lines into a single,
coherent API. It contains breaking changes — all small and mechanical.

## Breaking changes

### 1. Exception wrapper renamed and repackaged

`com.pivovarit.function.exception.WrappedException` →
`com.pivovarit.function.CheckedException`.

The `unchecked(...)` adapters still throw a `RuntimeException` subclass; only the
type name and package changed. Update imports and any `catch` clauses:

```diff
- import com.pivovarit.function.exception.WrappedException;
+ import com.pivovarit.function.CheckedException;
...
- } catch (WrappedException e) {
+ } catch (CheckedException e) {
```

### 2. `ThrowingIntFunction` removed (no same-shape replacement)

1.x shipped `ThrowingIntFunction<R, E>` with SAM `R apply(int) throws E` — the
*primitive → object* (`IntFunction`) direction. It has been removed in 2.0.

2.0 instead ships the *object → primitive* (`To*Function`) family:
`ThrowingToIntFunction<T, E>` (`int applyAsInt(T)`), `ThrowingToLongFunction`,
and the new `ThrowingToDoubleFunction`. These are **not** drop-in replacements
for the old `ThrowingIntFunction` — the argument/result directions are reversed.

If you relied on `ThrowingIntFunction` (`int` → object), use a standard
`Function<Integer, R>` together with `unchecked()`/`sneaky()`, or box manually.

### 3. `lifted()` / `lift()` renamed to `optional()`

`ThrowingFunction.lifted(...)` (static) and `ThrowingFunction.lift()` (instance)
are both renamed to `optional()`, matching `ThrowingSupplier.optional(...)` and
`ThrowingBiFunction.optional(...)`.

```diff
- ThrowingFunction.lifted(URI::new)
+ ThrowingFunction.optional(URI::new)
```

## New in 2.0

- **`recover(throwing, handler)`** on every interface — produce a fallback value
  (or handle the side effect) instead of wrapping or rethrowing. For
  single-argument interfaces the handler receives the input and the exception;
  for two-argument interfaces it receives the exception.
- **`ThrowingToIntFunction`** and **`ThrowingToDoubleFunction`**, completing the
  `To{Int,Long,Double}Function` primitive trio.
- **`ThrowingIntSupplier`**, **`ThrowingLongSupplier`**, **`ThrowingDoubleSupplier`**,
  and **`ThrowingBooleanSupplier`** — primitive supplier variants, handy for I/O
  reads of primitives (e.g. `DataInputStream::readInt`).
