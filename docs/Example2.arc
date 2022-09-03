//Check if a number is prime or not

//Number we want to check against:
number:num <- 7;


loop:num <- 2;
prime:num <- 1;

rep (loop < number) {
         is ((number % loop) == 0) -> // This is how we do an if statement with only a true block
             yes {
                prime <- 0;
             }
    loop <- loop + 1;
}

is (prime == 1) ->
    yes {
        $() <- "Your number is a prime number!";
    }
    no { // This is how we define an else block
        $() <- "Your number is not a prime number!";
    }