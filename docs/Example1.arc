// Fibonacci numbers implemented in Arc, using 10 numbers

n1:num <- 0;  // This is how we can declare a variable and initialize it in one line.
n2:num <- 1;
n3:num;       // This is how we declare a variable without giving it a base value.
i:num <- 2;
count:num <- 10;

$() <- n1;    // This is how we print the value of an expression to the console.
$() <- n2;

rep (i<count) //we start looping from 2 because 0 and 1 are already printed. We loop 10 times
 {
  n3 <- n1 + n2;
  $()<- n3;
  n1 <- n2;
  n2 <- n3;
  i <- i + 1;
 }