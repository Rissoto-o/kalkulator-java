package org.example;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        panelPrzyciski.setLayout(new GridLayout(5, 4, 5, 5)); // 5 wierszy, 4 kolumny
        panelPrzyciski.setOpaque(false);

        String[] buttons = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", ".", "=", "+",
                "C", "delate", "<3", "<3"
        };

        ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
        final boolean[] justCalculated = {false}; // flaga, czy zaraz po "="

        for (String tekst : buttons) {
            JButton btn = new JButton(tekst);
            btn.setFont(new Font("Consolas", Font.BOLD, 36));

            btn.setOpaque(false);
            btn.setContentAreaFilled(false);
            btn.setBorderPainted(true);
            btn.setFocusPainted(false);

            if (tekst.equals("")) {
                btn.setEnabled(false);
            }

            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String current = tabelka.getText();

                    if (tekst.equals("C")) {
                        tabelka.setText("");
                        justCalculated[0] = false;
                    } else if (tekst.equals("⌫")) {
                        if (!current.isEmpty()) {
                            tabelka.setText(current.substring(0, current.length() - 1));
                        }
                    } else if (tekst.equals("=")) {
                        try {
                            Object result = engine.eval(current);
                            tabelka.setText(result.toString());
                            justCalculated[0] = true;
                        } catch (Exception ex) {
                            tabelka.setText("Error");
                            justCalculated[0] = true;
                        }
                    } else {
                        if (justCalculated[0]) {
                            tabelka.setText(tekst);
                            justCalculated[0] = false;
                        } else {
                            tabelka.setText(current + tekst);
                        }
                    }
                }
            });

            panelPrzyciski.add(btn);
        }

        frame.add(panelPrzyciski, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
