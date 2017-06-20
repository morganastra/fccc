# Funding Circle Coding Challenge - Primes

A command-line tool for printing multiplication tables of primes.

## Usage

Run with `lein run` or `lein run -- OPTIONS` in the project directory

## Options

    -n, --number N  10  Number of primes to use
    -t, --no-table      Don't print a multiplication table, just print N primes
    -h, --help

## Examples

Run with no arguments to simply print a multiplication table of the first 10 primes:

    $ lein run

    |  1 |  2 |  3 |   5 |   7 |  11 |  13 |  17 |  19 |  23 |  29 |
    |----+----+----+-----+-----+-----+-----+-----+-----+-----+-----|
    |  2 |  4 |  6 |  10 |  14 |  22 |  26 |  34 |  38 |  46 |  58 |
    |  3 |  6 |  9 |  15 |  21 |  33 |  39 |  51 |  57 |  69 |  87 |
    |  5 | 10 | 15 |  25 |  35 |  55 |  65 |  85 |  95 | 115 | 145 |
    |  7 | 14 | 21 |  35 |  49 |  77 |  91 | 119 | 133 | 161 | 203 |
    | 11 | 22 | 33 |  55 |  77 | 121 | 143 | 187 | 209 | 253 | 319 |
    | 13 | 26 | 39 |  65 |  91 | 143 | 169 | 221 | 247 | 299 | 377 |
    | 17 | 34 | 51 |  85 | 119 | 187 | 221 | 289 | 323 | 391 | 493 |
    | 19 | 38 | 57 |  95 | 133 | 209 | 247 | 323 | 361 | 437 | 551 |
    | 23 | 46 | 69 | 115 | 161 | 253 | 299 | 391 | 437 | 529 | 667 |
    | 29 | 58 | 87 | 145 | 203 | 319 | 377 | 493 | 551 | 667 | 841 |

To just print some primes (with no multiplication table), you can do something like:

    $ lein run -- -t -n 20
    (2 3 5 7 11 13 17 19 23 29 31 37 41 43 47 53 59 61 67 71)

The algorithm is quite fast and can generate hundreds of thousands of primes in a few seconds, so don't be afraid to try somewhat large numbers. To avoid cluttering your terminal's scrollback buffer, you will probably want to redirect the output to a file. For example:

    $ lein run -- -t -n 100000 > first-100k-primes.txt

## The Prime Generation Algorithm

This program uses a lazy prime sieve implementation inspired by the one presented in
Melissa O'Neill's paper [The Genuine Sieve of Eratosthenes](https://www.cs.hmc.edu/~oneill/papers/Sieve-JFP.pdf).

In a traditional prime sieve, one would choose a fixed upper bound M and eagerly eliminate the all multiples of each prime up to M. Since this prime generator is lazy, we do not have the luxury of eliminating all multiples of a given prime eagerly; we must instead eliminate the multiples of all primes we have found so far as we go.

We use a priority map `table` to represent the current state of multiples-elimation for all of the primes we have found thus far. The keys in `table` are the prime numbers themselves, and the values are the next multiple of the prime that has not yet been eliminated.

Following is an example of one iteration of the sieve algorith, with previous prime being 23. At this point, `table` would be the following

    {5 25
     3 27
     7 49
     ...
     23 529}

The next odd candidate would thus be 25, which we see is a composite. We eliminate
25, and adjust `table` to:

    {3 27
     5 35
     7 49
     ...
     23 529}

But then the next candidate is 27, also a composite, so we adjust `table` to:

    {3 33
     5 35
     7 49
     ...
     23 529}

And thus finally find our next candidate, 29, is less than the next composite 33,
so we deterime 29 to be prime. Finally, we add the newly found prime to `table`:

    {3 33
     5 35
     7 49
     ...
     23 529
     29 841}

Note that the first composite added to `table` for a prime `p` is `p`^2. This is because any composite less than `p`^2 will necessarily be a multiple of a prime less than `p`, and thus will already occur in the table as a multiple of its lesser factor.

### Performance in time and space

#### Size

The size of `table` scales linearly with the number of primes found (it contains exactly one composite for each prime found). We also store the primes a second time (in numerical order) in the sequence returned by `lazy-primes`, which also scales linearly, so our total space requirement for the prime generator is O(n).

The size of the multiplication table is O(n^2), so when we are printing a multiplication table the size of the table dominates.

#### Time

In order to find n primes we must run n iterations of `sieve`.

Each iteration of the sieve requires O(g(n)) `assoc`, `peek`, and arithmetic operations, where g(n) is the ["prime gap"](https://en.wikipedia.org/wiki/Prime_gap), or number of composites between one prime and next.

`assoc` and `peek` on the priority map are O(log(m)) operations where m is the number of items in the map, so each iteration is g*log(m) + g or O(g log(m)) operations.

The sum of g(n) over the first n primes is approximately log(log(n)), and m is approximately n, so our overall complexity to find the first n primes is O(n log(n) log(log(n))).

Computing a multiplication table of n rows and columns requires O(n^2) multiplications, so when we print a multiplication table, the complexity of computing it dominates the complexity of generating the primes and we just get O(n^2).

In practice, generating hundreds of thousands of primes using this algorithm takes only a few seconds on my computer. When printing large multiplication tables, the time spent in the pretty-printer far outweighs the time spent on the actual computations. If computing and storing extremely large multiplication tables quickly were important, there is significant room for optimizing the pretty-printing layout algorithm and reducing the constant factor on and/or parallelizing the O(n^2) multiplication table computations.
