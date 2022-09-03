a:num;

add:func <- (n1:num, n2:num):num => {
    () <- n1 + n2;
};

getHugeNumber:func <- (multiplier:num):num => {
    maxInt:num <- 2147483649; // This will result in an error as the number overflows
    () <- multiplier * maxInt;
};

a <- 3;

$() <- @add(a, @getHugeNumber(4));