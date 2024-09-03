package com.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Calculator extends JFrame implements ActionListener {

    private JTextField display;
    private StringBuilder currentInput;
    private String lastOperator;
    private double result;

    public Calculator() {
        setTitle("Calculatrice iPhone");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        currentInput = new StringBuilder();
        lastOperator = "";
        result = 0;

        display = new JTextField();
        display.setFont(new Font("Arial", Font.PLAIN, 36));
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        display.setEditable(false);
        add(display, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        add(buttonPanel, BorderLayout.CENTER);

        String[] buttonLabels = {
                "C", "+/-", "%", "/",
                "7", "8", "9", "*",
                "4", "5", "6", "-",
                "1", "2", "3", "+",
                "0", "", ".", "="
        };

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);

        for (int i = 0; i < buttonLabels.length; i++) {
            JButton button = new JButton(buttonLabels[i]);
            button.setFont(new Font("Arial", Font.PLAIN, 24));
            button.addActionListener(this);
            button.setBorder(new RoundedBorder(30)); // Set rounded border

            if (buttonLabels[i].equals("0")) {
                gbc.gridx = 0;
                gbc.gridy = 4;
                gbc.gridwidth = 2;
                buttonPanel.add(button, gbc);
                gbc.gridwidth = 1;
            } else {
                gbc.gridx = i % 4;
                gbc.gridy = i / 4;
                if (gbc.gridy == 0 && gbc.gridx < 3) {
                    button.setBackground(Color.WHITE); // Style for the first three buttons in the first row
                } else if (gbc.gridx == 3) {
                    button.setBackground(Color.ORANGE); // Style for the first column on the right
                }
                buttonPanel.add(button, gbc);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (Character.isDigit(command.charAt(0)) || command.equals(".")) {
            currentInput.append(command);
            display.setText(currentInput.toString());
        } else if (command.equals("=")) {
            calculate(Double.parseDouble(currentInput.toString()));
            lastOperator = "=";
            display.setText(String.valueOf(result));
            currentInput.setLength(0);
        } else if (command.equals("C")) {
            currentInput.setLength(0);
            result = 0;
            lastOperator = "";
            display.setText("");
        } else if (command.equals("+/-")) {
            if (currentInput.length() > 0) {
                double value = Double.parseDouble(currentInput.toString());
                value = -value;
                currentInput.setLength(0);
                currentInput.append(value);
                display.setText(currentInput.toString());
            }
        } else if (command.equals("%")) {
            if (currentInput.length() > 0) {
                double value = Double.parseDouble(currentInput.toString());
                if (lastOperator.isEmpty() || lastOperator.equals("=")) {
                    // If no operator or equals, simply divide by 100
                    value = value / 100;
                    currentInput.setLength(0);
                    currentInput.append(value);
                    display.setText(currentInput.toString());
                } else {
                    // If an operator is already selected, calculate as a percentage of the current result
                    value = (result * value) / 100;
                    display.setText(String.valueOf(value));
                    currentInput.setLength(0);
                    currentInput.append(value);
                }
            }
        } else {
            if (currentInput.length() == 0 && lastOperator.equals("=")) {
                currentInput.append(result);
            }
            if (lastOperator.isEmpty() || lastOperator.equals("=")) {
                result = Double.parseDouble(currentInput.toString());
            } else {
                calculate(Double.parseDouble(currentInput.toString()));
            }
            lastOperator = command;
            currentInput.setLength(0);
            display.setText(String.valueOf(result));
        }
    }

    private void calculate(double num) {
        switch (lastOperator) {
            case "+" -> result += num;
            case "-" -> result -= num;
            case "*" -> result *= num;
            case "/" -> {
                if (num != 0) {
                    result /= num;
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur : Division par zéro");
                }
            }
            case "=" -> result = num;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Calculator calculator = new Calculator();
            calculator.setVisible(true);
        });
    }
}