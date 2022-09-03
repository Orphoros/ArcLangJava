// Ask for the user's name and print it back to the screen

fName:text;
lName:text;

$() <- "What is your first name?";
$() -> fName; // This is how we ask for user input and store it in a variable
$() <- "What is your last name?";
$() -> lName;

$() <- "\nHello! So, your name is:";
$() <- fName;
$() <- lName;