// This Arc code prints the first 10 prime numbers

ct:num  <- 0;
n:num   <- 0;
i:num   <- 1;
j:num   <- 1;

rep (n < 10) {
    j <- 1;
    ct <- 0;

    rep (j <= i){
        is (i % j == 0) ->
         yes {
            ct <- ct + 1;
        }
        j <- j + 1;
    }

    is (ct == 2) ->
    yes {
        $() <- i;
        n <- n + 1;
    }

    i <- i + 1;
}