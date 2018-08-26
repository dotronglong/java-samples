package com.dotronglong.sample.io;

import com.google.common.base.Stopwatch;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class TailTest {
    private Tail tail = new Tail();
    private static char[] chars;
    private static String filePath;
    private static boolean initOnce = true;
    private static int numberOfLines = 50;

    static {
        int min = 32;
        int max = 126;
        chars = new char[max - min + 2];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) (min + i);
        }
        chars[chars.length - 1] = 10;

        filePath = System.getProperty("user.dir") + "/test.log";
    }

    @BeforeClass
    public static void setUp() throws IOException {
        if (initOnce && (new File(filePath).exists())) {
            // Only create file one time
            System.out.println("File is created already");
            return;
        }

        // Create a file which has random data (and lines)
        long maxFileSize = 80 * 1024 * 1024; // in Mb
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false));
        Stopwatch stopwatch = Stopwatch.createStarted();

        long writtenBytes = 0;
        int maxLineLength = 200; // in characters
        int lineLength = 0;
        do {
            char c = randomChar();
            if (c != 10 && lineLength == maxLineLength) {
                lineLength = 0;
                c = 10;
            }
            writer.append(c);
            writtenBytes++;
            lineLength++;
        } while (writtenBytes < maxFileSize);

        writer.close();
        System.out.println("File is created in " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + " ms");
    }

    private static char randomChar() {
        int min = 0;
        int max = chars.length - 1;
        int range = max - min + 1;
        return chars[(int) ((Math.random() * range) + min)];
    }

    @Test
    public void testReadLines() throws IOException {
        testTailLines(numberOfLines);
        testTailUnix(numberOfLines);
    }

    private void testTailLines(int numberOfLines) throws IOException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        List<String> lines = tail.readLines(filePath, numberOfLines);
        System.out.println("tail.readLines finishes in " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + " ms");
        assertEquals(numberOfLines, lines.size());
    }

    private void testTailUnix(int numberOfLines) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("tail", "-n" + numberOfLines, filePath);
        try {
            Process process = builder.start();
            process.waitFor();
            System.out.println("tail finishes in " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + " ms");
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
