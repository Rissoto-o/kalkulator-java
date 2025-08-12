package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(900, 900);
        frame.setTitle("--Calculator--");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(5, 5));
        frame.getContentPane().setBackground(Color.WHITE);

        JTextField tabelka = new JTextField();
        tabelka.setEditable(false);
        tabelka.setFont(new Font("Consolas", Font.BOLD, 72));
        tabelka.setHorizontalAlignment(JTextField.RIGHT);
        tabelka.setOpaque(false);
        frame.add(tabelka, BorderLayout.NORTH);

        JPanel panelPrzyciski = new JPanel();
        panelPrzyciski.setLayout(new GridLayout(5, 4, 5, 5));
        panelPrzyciski.setOpaque(false);

        String[] buttons = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", ".", "=", "+",
                "C", "⌫", "<3", "<3"
        };

        final boolean[] justCalculated = {false};

        for (String tekst : buttons) {
            JButton btn = new JButton(tekst);
            btn.setFont(new Font("Consolas", Font.BOLD, 36));

            btn.setOpaque(false);
            btn.setContentAreaFilled(false);
            btn.setBorderPainted(true);
            btn.setFocusPainted(false);

            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String current = tabelka.getText();

                    switch (tekst) {
                        case "C":
                            tabelka.setText("");
                            justCalculated[0] = false;
                            break;

                        case "⌫":
                            if (!current.isEmpty()) {
                                tabelka.setText(current.substring(0, current.length() - 1));
                            }
                            justCalculated[0] = false;
                            break;

                        case "=":
                            try {
                                double result = evaluateExpression(current);
                                tabelka.setText(removeTrailingZeros(result));
                                justCalculated[0] = true;
                            } catch (Exception ex) {
                                tabelka.setText("Error");
                                justCalculated[0] = true;
                            }
                            break;

                        case "<3":
                            JOptionPane.showMessageDialog(frame, "❤️ Sending you calculator love! ❤️");
                            break;

                        default:
                            if (justCalculated[0]) {
                                tabelka.setText(tekst);
                                justCalculated[0] = false;
                            } else {
                                tabelka.setText(current + tekst);
                            }
                            break;
                    }
                }
            });

            panelPrzyciski.add(btn);
        }

        frame.add(panelPrzyciski, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    // Simple math expression evaluator
    private static double evaluateExpression(String expression) {
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operations = new Stack<>();
        StringBuilder numBuffer = new StringBuilder();

        for (char ch : expression.toCharArray()) {
            if (Character.isDigit(ch) || ch == '.') {
                numBuffer.append(ch);
            } else if (isOperator(ch)) {
                if (numBuffer.length() > 0) {
                    numbers.push(Double.parseDouble(numBuffer.toString()));
                    numBuffer.setLength(0);
                }
                while (!operations.isEmpty() && precedence(operations.peek()) >= precedence(ch)) {
                    numbers.push(applyOperation(operations.pop(), numbers.pop(), numbers.pop()));
                }
                operations.push(ch);
            }
        }

        if (numBuffer.length() > 0) {
            numbers.push(Double.parseDouble(numBuffer.toString()));
        }

        while (!operations.isEmpty()) {
            numbers.push(applyOperation(operations.pop(), numbers.pop(), numbers.pop()));
        }

        return numbers.pop();
    }

    private static boolean isOperator(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/';
    }

    private static int precedence(char op) {
        if (op == '+' || op == '-') return 1;
        if (op == '*' || op == '/') return 2;
        return 0;
    }

    private static double applyOperation(char op, double b, double a) {
        switch (op) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/':
                if (b == 0) throw new ArithmeticException("Division by zero");
                return a / b;
        }
        return 0;
    }

    private static String removeTrailingZeros(double num) {
        if (num == (long) num)
            return String.format("%d", (long) num);
        else
            return String.format("%s", num);
    }
}
