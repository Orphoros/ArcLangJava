checkNum:func <- (a:num, b:num):num => {
    result:num;
    is (a > b) ->
        yes {
            result <- 1;
        }
        no {
            result <- 0;
        }
    () <- result;
};

is ( @checkNum(1,2) ) -> // Cannot do an if check on the result of the checkNum functions, at it does not return boolean
    yes {
        $() <- "Check passed";
    }
    no {
        $() <- "Check failed";
    }