/**
 * Alex Cao, (acao4@toromail.csudh.edu)
 *
 * 4. Write a Java program that takes a single command line argument, a directory name. The program
 * will output information about files contained within the directory.
 *
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
public class DirectoryAnalyzer
{
    private static String formatFileSize(long size)
    {
        if (size < 1024)
        {
            return size + " bytes";
        }

        else if (size < 1024 * 1024)
        {
            return String.format("%.2f KBs", (double) size / 1024);
        }

        else if (size < 1024 * 1024 * 1024)
        {
            return String.format("%.2f MBs", (double) size / (1024 * 1024));
        }

        else {
            return String.format("%.2f GBs", (double) size / (1024 * 1024 * 1024));
        }
    }
    public static void main(String[] args)
    {
        // Check if directory name was provided as a command line argument
        if (args.length == 0)
        {
            System.out.println("Please provide a directory name.");
            return;
        }

        String directoryName = args[0];
        File directory = new File(directoryName);

        // Check if directory exists and is a directory
        if (!directory.exists())
        {
            System.out.println("Directory does not exist.");
            return;
        }

        else if (!directory.isDirectory())
        {
            System.out.println(directoryName + " is not a directory.");
            return;
        }

        // Initializing the accumulators
        int totalFiles = 0;
        int totalAlphaChars = 0;
        int totalNumericChars = 0;
        int totalSpaceChars = 0;
        long totalSize = 0;

        System.out.format("\n%-15s %-20s %-20s %-22s %-20s %n", "Size", "Alpha Chars", "Numeric Chars", "Spaces", "File Name");
        System.out.println();

        for (File file : directory.listFiles())
        {
            if (file.isFile())
            {
                totalFiles++;
                int alphaChars = 0;
                int numericChars = 0;
                int spaceChars = 0;
                long size = file.length();

                try (Scanner scnr = new Scanner(file))
                {

                    while (scnr.hasNextLine())
                    {
                        String temp = scnr.next();

                        for (int i = 0; i < temp.length(); i++)
                        {
                            char c = temp.charAt(i);

                            if (Character.isLetter(c))
                            {
                                alphaChars++;
                            }

                            else if (Character.isDigit(c))
                            {
                                numericChars++;
                            }

                            else if (Character.isWhitespace(c))
                            {
                                spaceChars++;
                            }
                        }
                    }

                }

                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }

                //Accumulators
                totalAlphaChars += alphaChars;
                totalNumericChars += numericChars;
                totalSpaceChars += spaceChars;
                totalSize += size;

                String fileSize = formatFileSize(size);

                System.out.format("%-15s %-20s %-20s %-22s %-20s %n", fileSize, alphaChars, numericChars, spaceChars, file.getName());
            }
        }

        String totalFileSize = formatFileSize(totalSize);

        System.out.println();
        System.out.format("%-15s %-20s %-22s %-20s %n", "Total Files:", "Total Alpha Chars:", "Total Numeric Chars:", "Total Space Chars:");
        System.out.format("%-15s %-20s %-22s %-20s %n", totalFiles, totalAlphaChars, totalNumericChars, totalSpaceChars);
        System.out.format("\n%-20s %-20s %n \n", "Total Size Disk:", totalFileSize);
    }
}