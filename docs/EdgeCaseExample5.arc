adder:func <- (n1:num, n2:num):num => {
    () <- n1 + n2;
};

checker:func <- (n1:num, n2:num):logic => {
    return:logic;
    is (n1 > n2) ->
        yes {
            return <- val;
        }
    () <- return;
};

printer:func <- (n1:num, n2:num, codeToRun:func[num, num]#logic):nil => {
    $() <- 4 + @codeToRun(n1,n2); // This will fail as we cannot add a boolean value to an integer
};

@printer(@adder(1,2), 3, checker);