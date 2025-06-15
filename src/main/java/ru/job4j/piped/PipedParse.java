package ru.job4j.piped;

import java.io.*;

public class PipedParse {
    public static void main(String[] args) throws IOException, InterruptedException {
        final PipedWriter writer = new PipedWriter();
        final PipedReader reader = new PipedReader(writer);
        Thread threadOne = new Thread(() -> {
            String expression = "2+2\n";
            try {
                writer.write(expression);
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        Thread threadTwo = new Thread(() -> {
            try {
                StringBuilder builder = new StringBuilder();
                int c;
                while ((c = reader.read()) != -1) {
                    if (c == '\n') {
                        String expression = builder.toString();
                        int index;
                        char operator;
                        int result = 0;
                        for (int i = 0; i < expression.length(); i++) {
                            if (expression.charAt(i) == '+'
                                    || expression.charAt(i) == '-'
                                    || expression.charAt(i) == '*'
                                    || expression.charAt(i) == '/') {
                                index = i;
                                int partOne = Integer.parseInt(expression.substring(0, index));
                                int partTwo = Integer.parseInt(expression.substring(index + 1));
                                operator = expression.charAt(index);
                                if (operator == '+') {
                                    result = partOne + partTwo;
                                } else if (operator == '-') {
                                    result = partOne - partTwo;
                                } else if (operator == '*') {
                                    result = partOne * partTwo;
                                } else if (operator == '/') {
                                    result = partOne / partTwo;
                                }
                                break;
                            }
                        }
                        System.out.println("Expression " + expression + "=" + result);
                        builder.delete(0, builder.length());
                    }
                    builder.append((char) c);
                }
                reader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        threadOne.start();
        threadTwo.start();
        threadOne.join();
        threadTwo.join();
    }
}
