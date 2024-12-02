# aoc2024

## Problems

### 1a

[duckdb](https://duckdb.org/)

```sql
CREATE OR REPLACE TABLE locations AS SELECT * FROM '01a.csv';
WITH
    indexed_column0 AS (SELECT column0 AS loc, row_number() over (order by column0) as idx FROM locations),
    indexed_column1 AS (SELECT column1 AS loc, row_number() over (order by column1) as idx FROM locations),
    ordered_pairs AS (
        SELECT indexed_column0.loc AS column0, indexed_column1.loc AS column1, indexed_column1.idx AS idx
        FROM indexed_column0 JOIN indexed_column1
            ON (indexed_column0.idx = indexed_column1.idx)
            ORDER BY idx)
SELECT SUM(ABS(column0 - column1))
FROM ordered_pairs;
```

### 1b

```sql
WITH
    column0 AS (SELECT column0 AS loc FROM locations),
    column1 AS (SELECT column1 AS loc FROM locations),
    pairs AS (
        SELECT column0.loc AS loc
        FROM column0
                 JOIN column1
                      ON (column0.loc = column1.loc)),
    pair_counts AS (
        SELECT loc, COUNT(*) AS count
        FROM pairs
        GROUP BY loc)
SELECT SUM(loc*count)
FROM pair_counts;
```

### 2a and 2b

- [src/codes/bauer/aoc2024/02.clj](https://github.com/pmbauer/aoc2024/blob/main/src/codes/bauer/aoc2024/02.clj)

## Usage

Run the project's tests contained in [Rich Comment Forms](https://github.com/hyperfiddle/rcf):

    $ clojure -X:test

Run a REPL

    $ clj -M:env/dev:repl

Build a jar

    $ clj -T:build jar

## License

Copyright © 2024 Paul Bauer

Distributed under the Eclipse Public License version 1.0.
