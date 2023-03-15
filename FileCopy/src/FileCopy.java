/**
 * Alex Cao, (acao4@toromail.csudh.edu)
 *
 * 3. Write a Java program that takes two command line arguments, a source file and a target file. The
 * program will create the target file and copy the contents of the source file into it.
 *
 * Notes:
 * - If the Source File (first argument) does not exist, then the program will print an error
 * message and quit.
 * - If either the source or the target file is a directory, then the program will print an error and
 * quit.
 * - If the Target File (second argument) exists, then the program will not overwrite it. Print the
 * error message and quit.
 * - If any of the arguments are missing, then the program will print an error message and quit.
 * - The program must handle all types of files without any data loss (hint: use raw bytes when
 * reading / writing files)
 * - The program will automatically create any directories needed for the target file.
 *
 * The file used in this program is something I just made up called TestingFile.txt with total bytes of 114:
 *
 * This is some stuff we want to check
 *
 * kjfdfbksd
 * 1928379
 * 9348918212
 * jnldv
 * paul
 * steve
 * 102938
 * 3094320
 * Hamlet
 *
 */
//Can change to java.io.*; to reduce clutter of imports
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileCopy
{
    public static void CopyFile(String src_name, String o_name) throws IOException
    {
        File srcFile = new File(src_name); // Get File objects from Strings
        File oFile = new File(o_name);

        // Check source file exists, is a file, and is readable.
        if (!srcFile.exists())
            abortE("no such source file: " + src_name);
        if (!srcFile.isFile())
            abortE("can't copy directory: " + src_name);
        if (!srcFile.canRead())
            abortE("source file is unreadable: " + src_name);

        // If the target is a directory, use the source file name
        // as the destination file name
        if (oFile.isDirectory())
            oFile = new File(oFile, srcFile.getName());

        // If the target exists, make sure it is a writeable file
        // and ask before overwriting it. If the destination doesn't
        // exist, make sure the directory exists and is writeable.
        if (oFile.exists()) {
            if (!oFile.canWrite())
                abortE("destination file is unwriteable: " + o_name);
            // Ask whether to overwrite it
            System.out.print("\nOverwrite existing file " + oFile.getName() + "? (Y/N): ");
            System.out.flush();
            // Get the user's response.
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String UserInput = in.readLine();
            // Check the response. If not a Yes, abort the copy.
            if (!UserInput.equals("Y") && !UserInput.equals("y"))
                abortE("existing file was not overwritten.");
        }

        // If file doesn't exist, check if directory exists and is
        // writeable. If getParent() returns null, then the directory is
        // the current dir.
        else {
            String parent = oFile.getParent(); // The destination directory
            if (parent == null) // If none, use the current directory
                parent = System.getProperty("user.dir");
            File dir = new File(parent); // Convert it to a file.
            if (!dir.exists())
                abortE("destination directory doesn't exist: " + parent);
            if (dir.isFile())
                abortE("destination is not a directory: " + parent);
            if (!dir.canWrite())
                abortE("destination directory is unwriteable: " + parent);
        }

        // Copy file, a buffer bytes at a time.
        FileInputStream SRC = null; // Stream to read from source
        FileOutputStream OUT = null; // Stream to write to destination

        try
        {
            //Variables to Create input stream, Create output stream
            //hold file contents, and How many bytes in buffer
            SRC = new FileInputStream(srcFile);
            OUT = new FileOutputStream(oFile);
            byte[] buffer = new byte[4096];
            int readBytes;

            // Read a chunk of bytes into the buffer, then write them out,
            // looping until we reach the end of the file
            while ((readBytes = SRC.read(buffer)) != -1)
            {
                // Read/write until EOF
                OUT.write(buffer, 0, readBytes);
                System.out.println("\n"+ readBytes + " bytes copied from " + src_name + " to " + o_name + "\n");
            }
        }
        //Close the streams
        finally
        {
            if (SRC != null)
            {
                try {
                    SRC.close();
                }
                catch (IOException e) {
                    System.out.println("This is the exception: " + e);
                }
                if (OUT != null)
                {
                    try {
                        OUT.close();
                    }
                    catch (IOException e) {
                        System.out.println("This is the exception: " + e);
                    }
                }
            }
        }
    }
    
    // custom exception method
    private static void abortE(String msg) throws IOException {
        throw new IOException("FileCopy: " + msg);
    }
    
    public static void main(String[] args)
    {
        if (args.length != 2) {     // Check arguments
            System.err.println("Usage: java FileCopy <source> <destination>");
        }
        else {
            // Call copy() to do the copy; display any error messages
            try {
                CopyFile(args[0], args[1]);
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
