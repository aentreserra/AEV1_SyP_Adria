package es.florida.adria;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;

public class Simulador {

    private JFrame frame;
    private JTextField userInput;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Simulador window = new Simulador();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public Simulador() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 691, 502);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        userInput = new JTextField();
        userInput.setBounds(231, 124, 217, 20);
        frame.getContentPane().add(userInput);
        userInput.setColumns(10);

        JButton btnNewButton = new JButton("EJECUTAR");
        btnNewButton.setBounds(294, 155, 89, 23);
        frame.getContentPane().add(btnNewButton);

        JLabel lblNewLabel = new JLabel("Inserta cuantas proteínas quieres generar");
        lblNewLabel.setBounds(231, 101, 286, 14);
        frame.getContentPane().add(lblNewLabel);

        JTextArea results = new JTextArea();
        results.setBounds(10, 196, 655, 256);
        frame.getContentPane().add(results);

        btnNewButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String inputText = userInput.getText().trim();
                    int count = Integer.parseInt(inputText);

                    results.setText("Empezando simulaciones...\n");

                    int[] counts = {count, count, count, count};

                    // Ejecución en multiproceso
                    long multiProcessTime = runMultiProcess(counts);
                    results.append("Tiempo total en multiproceso: " + multiProcessTime / 1000.0 + " segundos\n");

                    // Ejecución en multihilo
                    long multiThreadTime = runMultiThread(counts);
                    results.append("Tiempo total en multihilo: " + multiThreadTime / 1000.0 + " segundos\n");

                } catch (Exception ex) {
                    results.setText("Error: " + ex.getMessage());
                }
            }
        });
    }

    private static long runMultiProcess(int[] counts) throws IOException, InterruptedException {
        long startTime = System.currentTimeMillis();

        for (int type = 1; type <= 4; type++) {
            for (int i = 0; i < counts[type - 1]; i++) {
                ProcessBuilder pb = new ProcessBuilder("java", "-jar", "E:\\Users\\adria\\eclipse-workspace\\AE01_SyP_AdriaEntreserra\\SimulacionMP.jar", String.valueOf(type));
                pb.start().waitFor();
            }
        }

        return System.currentTimeMillis() - startTime;
    }

    private static long runMultiThread(int[] counts) throws InterruptedException {
        long startTime = System.currentTimeMillis();

        Thread[] threads = new Thread[4];
        for (int type = 1; type <= 4; type++) {
            int finalType = type;
            threads[type - 1] = new Thread(() -> {
                for (int i = 0; i < counts[finalType - 1]; i++) {
                    new Thread(new SimulacionMT(finalType)).start();
                }
            });
            threads[type - 1].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        return System.currentTimeMillis() - startTime;
    }
}
