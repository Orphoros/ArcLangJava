index:num <- 0;

runner:func <- (ittr:num):nil => {
    rep (index < ittr) { // This will fail, because index is not part of the function's separate scope, and it is not passed as a parameter
        $() <- index + 1;
    }
};

@runner(10);