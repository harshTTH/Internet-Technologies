package com.company;

import java.util.*;

public class Expression {

    Double answer = null;
    boolean valid = true;

    private String appendDig(char op, String exp, int i, List operators, List operands){
        String no ;
        if (operators.size() == 2) no = operators.remove(1) + "";
        else if (operators.size() == 1 && operands.isEmpty()) no = operators.remove(0) + "";
        else no = "";

        if (valid && no.equals("+") || no.equals("-") || no.equals("")) {
            no = no + op;

            int k = i + 1;
            while (k < exp.length()) {
                op = exp.charAt(k);
                if (op > 47 && op < 58 || (op == 46 && !no.contains(".")) || (op == 46 && no.equals("0"))) {
                    no = no + op;
                    k++;
                } else break;
            }

            if (no.equals("."))return "";
            return no;
        } else return "";
    }

    private double noBodmas(String exp) {
        List<Character> operators = new ArrayList<>();
        List<Double> operands = new ArrayList<>();

        for (int i = 0; valid && i < exp.length(); i++) {
            //System.out.println(answer);
            char op = exp.charAt(i);

            if (op == '+' || op == '-' || op == '*' || op == '/' || op == '^') {
                if (operators.isEmpty() || (operators.size() == 1 && !operands.isEmpty())) {
                    operators.add(op);
                } else {
                    valid = false;
                }
            } else {

                if ((op > 47 && op < 58) || op == 46) {
                    String no = appendDig(op, exp, i, operators , operands);

                    if(no.equals(""))valid = false;
                    else {
                        operands.add(Double.parseDouble(no));
                        i += no.length()-1;
                    }
                }

                if (operands.size() == 2 && operators.size() == 1) {
                    char oper = operators.remove(0);
                    Double op1 = operands.remove(0), op2 = operands.remove(0);
                    switch (oper) {
                        case '+':
                            answer = op1 + op2;
                            break;
                        case '-':
                            answer = op1 - op2;
                            break;
                        case '*':
                            answer = op1 * op2;
                            break;
                        case '/':
                            answer = op1 / op2;
                            break;
                        case '^':
                            answer = Math.pow(op1, op2);
                            break;

                    }
                    operands.add(answer);
                }
                else if (i + 1 == exp.length()) valid = false;
                else if(operands.size() >1 && operators.isEmpty())valid = false;
            }

        }
        return valid ? answer : Double.MAX_VALUE;
    }

    public static void main(String[] args) {
        String exp;
        do{
            System.out.println("Enter a Arithmetic Expression");
            Scanner in = new Scanner(System.in);
            exp = in.nextLine();

            Expression e = new Expression();
            Double ans = e.noBodmas(exp);
            if(ans != Double.MAX_VALUE)System.out.println("Answer is "+ ans);
            else System.out.println("Invalid Arithmetic Expression");
        }while(!exp.equals("exit"));
    }
}
