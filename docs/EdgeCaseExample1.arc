myFunc:func;
a:num;

a <- 1;

$() <- @myFunc(a); // This will result in an error, as myFunc has not been initialized yet

myFunc <- (a:num):num => { // Since functions are variables, initializing the function later will not resolve myFunc on execution above
    () <- a + 10;
};