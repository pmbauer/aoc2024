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

### 3a and 3b

- [src/codes/bauer/aoc2024/03.clj](https://github.com/pmbauer/aoc2024/blob/main/src/codes/bauer/aoc2024/03.clj)

![](https://media.cleanshot.cloud/media/40959/9imKrro1YrToAMNxgSsQL4cJ1RJli1Bwazdmzolf.jpeg?Expires=1733274859&Signature=Xy-cYn03cYOMl4c3mG26-RyRZaFZHMwKITTjhCq2sP7H2SkIGvczoc9K-AQcRVKvDzJc9sZHcigLXi9WPZqecN7lA3aIgpHtwYVkBRL97LgNTY0mgVdVut2RJutiempPCNykRCq~czoBz37v74lnqyjRFNvHZSrvIEtbIG1Aovd2DCLizl1zqVGs~rb82wplzTTMLsvzjdv-6qXfiKshZDGprX6OpYDo35uSiY1fnsx5s8X9mx7q5wDx46sQpadXSR0ajHTfmtLhg64TKWvXhQqAAR1LDGk80XayW4FYfJuS1bc5Zg~fnsR5h7~4w3AFlYLPwyKYw0xD-loYm0BH4g__&Key-Pair-Id=K269JMAT9ZF4GZ)

![](https://media.cleanshot.cloud/media/40959/yJuC0Zoj2ac6ztwQs4BRHNui6xjK09B76NMyBiAx.jpeg?Expires=1733274814&Signature=Qmsv~my-hBDb5BMsfCReAdZgDCptBbsINaTqzi2tINrKH5blEbT-tf3o6d2~z2ulY4SGqDw-LP2jgiZHd4BSjouK89kUhR52ysJU3meR-zNUVL1zxmNTPbLCPuzrdgfoV0ad1FB17dbEUjsKxcqGpRPXUHroAYe~sEk-kgTLh1Z8J~rjRaavnn-Gy6TM9am4GD5lKlz3C9wr6ltyAmetndWFprR-lauI7cjSbxERKfdMMa8RINSNFlIK2TwLmX-breByk2q9h4eKcbgfT6Ae8xisMnR5VJ~D22D58HmW~8mX1SHLKPrsNxACo3aFNpo629j9QS-EG~V0RgPwlGZfjw__&Key-Pair-Id=K269JMAT9ZF4GZ)

### 4a and 4b

- [src/codes/bauer/aoc2024/04.clj](https://github.com/pmbauer/aoc2024/blob/main/src/codes/bauer/aoc2024/04.clj)

### 5a and 5b

- [src/codes/bauer/aoc2024/05.clj](https://github.com/pmbauer/aoc2024/blob/main/src/codes/bauer/aoc2024/05.clj)

### 6a and 6b

- [src/codes/bauer/aoc2024/06.clj](https://github.com/pmbauer/aoc2024/blob/main/src/codes/bauer/aoc2024/06.clj)

I did this with `lazy-seq` and `recur` because I wanted to needlessly recurse.  For fun.
Well, it did make me re-curse.
Clojure type error messages for destructured bindings are still awful and wrapping those errors in a `lazy-seq` makes debugging pretty gosh darn user hostile.  This keeps the rif-raf out. Never change, Clojure.

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
