import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

import myfileio.MyFileIO;

// TODO: Auto-generated Javadoc
/**
 * The Class GenWeights.
 */
public class GenWeights {
	
	/** Constant representing the number of ASCII characters. */
	private final int NUM_ASCII = 128;
	/** Constant representing the start of the printable range of ASCII characters */
    private final int ASCII_PRINT_MIN = 32;    
	/** Constant representing the start of the printable range of ASCII characters */
    private final int ASCII_PRINT_MAX = 126;    
    /** The input and output File handles. */
    private File inf,outf;
    
    /** The array for collecting the weights */
    private int[] weights = new int[NUM_ASCII]; 
    
    /** Instance the the MyFileIO package to access methods for handling files */
    private MyFileIO fio;
    
    /** The ignore chr 13. */
    private boolean ignoreChr13 = false;
    
    /** Instance of the HuffCompAlerts - used as an intermediary between this 
     *  class and the GUI
     */
    private HuffCompAlerts hca;
    
	/**
	 * Instantiates a new GenWeights object and connects it to the supplied
	 * HuffCompAlerts object (hca).
	 *
	 * @param hca the hca
	 */
	public GenWeights(HuffCompAlerts hca) {
		this.hca = hca;
		fio = new MyFileIO();

	}

	/**
	 * Initializes all elements of the weights array to 0
	 */
	void initWeights() {
		// TODO #1
		for (int i=0;i < weights.length;i++) {
			weights[i]=0;
		}
	}

	/**
	 * Generate character-based frequency weights. You will write this method,
	 * using the MyFileIO fio instance to create the File object, check, open 
	 * and close the file.
	 * 
	 * NOTE: All source text files are located in the data/ directory. You do NOT need to 
	 *       prepend "data/" to the infName - the GUI handles this for you.
	 * 
	 * You should check the file for the following issues (using the getFileStatus method
	 * in MyFileIO) and take the appropriate action if they occur.
	 * 
	 * a) if the filename is empty, raise a WARNING alert with an appropriate message and
	 *    return.
	 * b) if the file does not exist or is empty, raise a WARNING alert with an appropriate 
	 *    message and return.
	 * c) if the file exists and is not empty, but is not readable, raise a WARNING alert with 
	 *    an appropriate message and return.
	 * 
	 * Each of these errors should be differentiated and reported to the user via an alert 
	 * 
	 * Assuming that the requirements of a, b and c have all been met successfully, 
	 * initialize the weights and call the readInputFile() to read the file
	 * character by character.
	 *  
	 * Refer to the HuffAlerts and HuffCompAlerts Classes to understand the alert types
	 * 
	 * Once the input file has been fully processed, you should print the weights to the console.
	 *
	 * @param infName - the name of the text file to read
	 */
	void generateWeights(String infName) {
		// TODO #2: write this method and any helper methods
		if (errorCheck(infName)) {
			return;
		}
		initWeights();
		int EOF = -1;
		File file = fio.getFileHandle(infName);
		BufferedReader br = fio.openBufferedReader(file);
		
		try {
			int c;
			while ((c=  br.read())!=EOF) {
				weights[c & 0x7F]++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		weights[0]++;
		fio.closeFile(br);
		printWeights();
		return;

	}
	private boolean errorCheck(String filename) {
		int check = fio.getFileStatus(fio.getFileHandle(filename),true);
		if (check==MyFileIO.EMPTY_NAME ) {
			hca.issueAlert(HuffAlerts.INPUT, "Empty Name", "Empty File Name");
			return true;
		}
		else if (check==MyFileIO.FILE_DOES_NOT_EXIST) {
			hca.issueAlert(HuffAlerts.INPUT, "File Does not Exist", "File does not exist");
			return true;
		}
		else if (check==MyFileIO.READ_ZERO_LENGTH) {
			hca.issueAlert(HuffAlerts.INPUT, "Read zero Length", "File is empty");
			return true;
		}
		else if (check ==MyFileIO.NO_READ_ACCESS) {
			hca.issueAlert(HuffAlerts.INPUT, "No read access", "Cannot Read file");
			return true;
		}
		return false;
	}
	
	/**
	 * Prints the weights to the console. Non-printing characters (0-31, 127) 
	 * are indicated with [ ], printing characters are displayed to help with debug
	 * 
	 */
	void printWeights() {
		for (int i = 0; i < weights.length; i++) {
			if ((i < ASCII_PRINT_MIN) || (i > ASCII_PRINT_MAX))  
				System.out.println("i:"+i+" [ ] = "+weights[i]);
			else 
				System.out.println("i:"+i+" ("+(char)i+") = "+weights[i]);

				
		}
	}
	
	/**
	 * Write the character-based frequency data to the specified file, one index per line.
	 * Use the following format:
	 *   print the index and the frequency count separated by a comma.
	 *   ie, if weights[10]=421, this would write the following line:
	 *   10,421,
	 *   DO NOT PRINT THE ACTUAL CHARACTER - as this will cause problems when you
	 *   try to analyze the data with your favorite spreadsheet (YFSS)
	 *   
	 * Again, you will use the MyFileIO methods to create the File object, check, open and close
	 * the file. 
	 * 
	 * NOTE: all weights file will be written to the weights/ directory. outfName will already account 
	 * for this - you do NOT need to prepend "weights/" to outfName.
	 *   
	 * Assuming that there are no errors, raise an INFORMATION alert with an appropriate message
	 * to indicate to the user that the output file was created successfully. Make sure to refer to 
	 * the notes from class today for any more details. Again, the actual writing of the file might be
	 * best done in a separate helper method.
	 *   
	 * You must handle the following error conditions and take the appropriate actions,
	 * as in the generateWeights() method:
	 *   if outfName is blank, raise a OUTPUT alert with an appropriate message and return.
	 *   if the output file exists but is not writeable, raise an OUTPUT alert with an appropriate message
	 *   and return.
	 *   if the output file exists and is writeable, raise a CONFIRM alert with an appropriate
	 *   message to the user. if they cancel the operation, return; otherwise, continue.
	 *   otherwise, if there is no error (status == MyFileIO.FILE_OK), continue.
	 *   
	 * Refer to the HuffAlerts and HuffCompAlerts Classes to understand the alert types
	 * 
	 * @param outfName the name of the weights file (includes weights/ )
	 */
	 void saveWeightsToFile(String outfName) {
		// TODO #3: write this method (and any helper methods)
		
		 if (errorCheckWrite(outfName)){
			 return;
		 }
		 if (!hca.issueAlert(HuffAlerts.CONFIRM, "CONFIRM", "Confirm to write to file?")) {
			 return;
		 };
			
		BufferedWriter wr = fio.openBufferedWriter(fio.getFileHandle(outfName));
		String x= "";
		for (int i=0;i < weights.length;i++) {
			x += i + "," + weights[i] + ",\n";
		}
		try {
			wr.write(x);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fio.closeFile(wr);
	}
	 private boolean errorCheckWrite(String out) {
		 File file = fio.getFileHandle(out);
		 if (fio.getFileStatus(file, false)==MyFileIO.EMPTY_NAME) {
			 hca.issueAlert(HuffAlerts.OUTPUT, "Empty Name", "Empty File Name");
				return true;
		 }
		 else if (fio.getFileStatus(file, false)==MyFileIO.NO_WRITE_ACCESS) {
			 hca.issueAlert(HuffAlerts.OUTPUT, "No Write Access", "No Write Access");
				return true;
		 }
		 return false;
	 }
	
	/**
	 * Read input file and return weights - JUNIT USE ONLY!!! 
	 * This file forces the creation of the weights file for JUnit testing
	 * DO NOT CHANGE THIS METHOD
	 *
	 * @param infName the name of the text file to read
	 * @return the weights array
	 */
	int[] readInputFileAndReturnWeights(String infName) {
		System.out.println("Generating weights for: "+infName);
		generateWeights(infName);
		return weights;
	}	
}
