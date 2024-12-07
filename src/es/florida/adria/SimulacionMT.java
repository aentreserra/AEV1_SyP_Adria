package es.florida.adria;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SimulacionMT implements Runnable {
    private final int type;

    public SimulacionMT(int type) {
        this.type = type;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        double result = simulation(type);
        long endTime = System.currentTimeMillis();

        synchronized (SimulacionMT.class) {
            saveResult("MT", type, result, startTime, endTime);
        }
    }

    public static double simulation(int type) {
        double calc = 0.0;
        double simulationTime = Math.pow(5, type);
        long startTime = System.currentTimeMillis();
        long endTime = startTime + (long) simulationTime;

        while (System.currentTimeMillis() < endTime) {
            calc = Math.sin(Math.pow(Math.random(), 2));
        }

        return calc;
    }

    private void saveResult(String mode, int type, double result, long startTime, long endTime) {
        try {
        	long totalTimeMillis = endTime - startTime;
            double totalTimeSeconds = totalTimeMillis / 1000.0;
            
            String executionTimeFormatted = String.format("%.2f", totalTimeSeconds).replace(',', '_');
            
        	
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
            String fileName = "PROT_" + mode + "_" + type + "_n1_" + timestamp + ".sim";
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            
            writer.write(timestamp + "\n");
            writer.write(new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date()) + "\n");
            writer.write(executionTimeFormatted + "\n");
            writer.write(String.valueOf(result) + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
