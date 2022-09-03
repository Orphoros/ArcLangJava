// Test callback functionality with Arc using functions as parameters

// This is how we define a function with a parameter that expects a function with no parameters with a return type of an integer
divide:func <- (n1:num,n2:num,callBack:func[]#nil):num => {
    result:num;
    is (n2 == 0) ->
        yes {
             @callBack(); // This is how we execute a function
        }
        no {
            result <- n1 / n2;
        }
    () <- result; // This is how we return a value from a function
};

printer:func <- ():nil => { // This is how we make a function that does not return any value
    $() <- "Cannot divide by 0!";
};

$() <- @divide(6,3,printer); // This is how we print the result of a function once we execute it
$() <- @divide(6,0,printer);