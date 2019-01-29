/**
 * @author Jack Smith
 * 
 *          <summary> Methods for editing config.json file on-board.
 *          Opens base file (config.json), backs it up (configBackup.json)
 *          and (configBackup2.json), then writes over original 
 *          with changes. Requires json file to already be parsed 
 *          into a hashMap.
 *          </summary>
 * 
 */

 //Importing stuff
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class ConfigEdit {

    //Constant file paths used in test
    private static final String CONFIG_FILE = "config/config.json";

    private static final String CONFIG_FILE_BACKUP = "config/configBackup.json";

    private static final String CONFIG_FILE_BACKUP_BACKUP = "config/configBackup2.json";

    //Scanner object used for user input
    private static Scanner scan = new Scanner(System.in);

    //Stores preparsed hashMap and then stores changes
    private static HashMap<String, Object> mapChanges = new HashMap<String, Object>();

    //JSON Object used to transition hashMap for writing
    private static JSONObject mapFinal = new JSONObject();

    //File writer
    private static FileWriter fileW;


    /**
     * <summary> "Main function", call for intended purposes. </summary>
     */
    public static void test() {

        //declare variable used in if statement
        int status2 = -1;

        //Start call timer
        double copyFileTimer = System.nanoTime();

        //Run the function
        int status = copyFile(CONFIG_FILE, CONFIG_FILE_BACKUP, CONFIG_FILE_BACKUP_BACKUP);

        //End call timer
        double copyFileTimerEND = System.nanoTime();

        System.out.println("Copying files took: " + ((copyFileTimerEND - copyFileTimer) / 1000000) + " ms");

        //Start call timer
        double printMapTimer = System.nanoTime();

        //Run the function
        print(JSONConstants.getMap());

        //End call timer
        double printMapTimerEND = System.nanoTime();

        System.out.println("Printing map took: " + ((printMapTimerEND - printMapTimer) / 1000000) + " ms");

        //Start call timer
        double putMapTimer = System.nanoTime();

        //Run the function
        mapChanges = putHashMap(JSONConstants.getMap());

        //End call timer
        double putMapTimerEND = System.nanoTime();

        System.out.println("Editing map took: " + ((putMapTimerEND - putMapTimer) / 1000000) + " ms");

        //Start call timer
        double doSaveTimer = System.nanoTime();

        //Run the function
        boolean doSaveBool = doSave();
 
        //End call timer
        double doSaveTimerEND = System.nanoTime();
 
        System.out.println("Asking for save took: " + ((doSaveTimerEND - doSaveTimer) / 1000000) + " ms");

        if(doSaveBool) {
            //Start call timer
            double writeTimer = System.nanoTime();

            //Run the function
            status2 = writeToFile(CONFIG_FILE, mapChanges);
 
            //End call timer
            double writeTimerEND = System.nanoTime();
 
            System.out.println("Saving took: " + ((writeTimerEND - writeTimer) / 1000000) + " ms");
        }

        //What did we see?
        System.out.println("Copying files returned: " + status);
        System.out.println("Writing files returned: " + status2);

        scan.close();
    }

    /**
     * <summary> Method puts user input to filePath for use.
     * Only use when no specific filepath is given. </summary>
     *
     * @return filePath - returns the user input for a filepath
     */
    public static String enterFile() {

        //Ask for fileName to return to base code
        System.out.println("Enter the name and location of the file you would like to access.");
        String filePath = scan.next();

        return filePath;
    }

    /**
     * <summary> Copies backup file into second backup and then
     * original file into backup file. </summary>
     * 
     * @param FILEPATHORIGINAL - original file that is being copied.
     * @param FILEPATHBACKUP - backup file to be copied to in case of mistake.
     * @param FILEPATHBACKUP2 - extra backup precaution
     * 
     * @return int - returns status of completion
     */
    public static int copyFile(String FILEPATHORIGINAL, String FILEPATHBACKUP, String FILEPATHBACKUP2) {
        try {

            //Copies existing file as entered file path.
            Files.copy(Paths.get(FILEPATHBACKUP), Paths.get(FILEPATHBACKUP2), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get(FILEPATHORIGINAL), Paths.get(FILEPATHBACKUP), StandardCopyOption.REPLACE_EXISTING);

            

        } catch(FileNotFoundException fnfe) {

			// System did not find the file in the specified path
			// Check if file is defined correctly (spelling and path)
			System.err.println("One or more file(s) were not found at their specified paths");
            fnfe.printStackTrace();

			//FAIL
            return -1;
        } catch(IOException ioe) {

			// System was not able to perform I/O Operation(s)
			// Check if the file is marked as read only or is protected in any way
			// (encryption, etc.)
			System.err.println("IOException Occured");
			ioe.printStackTrace();

			//FAIL
			return -1;
        }

        //SUCCESS
        return 0;
    }

    /**
     * <summary> Gives user prompts to change the values in a HashMap. </summary>
     * 
     * @param map - Hashmap to be changed.
     * 
     * @return HashMap<String, Object> - returns a changed hashMap.
     */
    public static HashMap<String, Object> putHashMap(HashMap<String, Object> map) {
            
        //Loop that continues until user asks for termination.
        boolean endLoop = false;
        while(endLoop == false) {

            //Asks for input and then assigns to value
            System.out.println("\nWould you like to change a key?  [y/n]");
            String check = scan.nextLine();

            if (check.equals("y")) {
                //Asks user for inputs for key and value.
                System.out.println("\nEnter the key of the value you would like to change.");
                String key = scan.nextLine();

                System.out.println("\nEnter the value that you would like the key to be changed to.");
                String value = scan.nextLine();

                Object valf;

                //Casts the inputted value to be the same type as the existing hashMap.
                if(map.get(key) instanceof Integer) {
                    valf = Integer.parseInt(value);

                } else if(map.get(key) instanceof Double) {
                    valf = Double.parseDouble(value);

                } else if(map.get(key) instanceof Boolean) {
                    valf = Boolean.parseBoolean(value);

                } else if(map.get(key) instanceof Long) {
                    valf = Integer.parseInt(value);
                    
                } else {
                    valf = value;
                }


                //Checks if key is valid.
                if(map.containsKey(key)) {

                    //Asks user if they are okay with changes.
                    System.out.println("Are you sure you want to change: ");
                    System.out.println("KEY: [" + key + "] = " + map.get(key) + " of TYPE " + map.get(key).getClass());
                    System.out.println("To: ");
                    System.out.println("KEY: [" + key + "] = " + valf + " of TYPE " + valf.getClass());
                        
                    check = scan.nextLine();
                    if(check.equals("y")) {
                        //Saves if y
                        map.put(key, valf);
                    } 
                }else {
                        System.out.println("Key does not exist.");
                }

            } else {
                endLoop = true;
            }
        }
        //Clean up
        return map;
    }


    /**
     * <summary> Saves a json file to specified file path </summary>
     * 
     * @param FILEPATH - File to be saved over
     * @param HashMap - Hashmap to save over the file with
     * 
     * @return int - returns whether successful
     */
    public static int writeToFile(String FILEPATH, HashMap<String, Object> mapOriginal) {
        //Populates JSONObject used for writing file
        for(Map.Entry<String, Object> entry : mapOriginal.entrySet()) {
            mapFinal.put(entry.getKey(), entry.getValue());
        }

        try {
            //Assigns FileWriter object
            fileW = new FileWriter(FILEPATH);

            //Writes file and then flushes stream
            fileW.write(mapFinal.toJSONString());
            fileW.flush();

        } catch(FileNotFoundException fnfe) {

			// System did not find the file in the specified path
			// Check if file is defined correctly (spelling and path)
			System.err.println("The file " + FILEPATH + " was not found at its specified path");
            fnfe.printStackTrace();

			//FAIL
            return -1;
        } catch(IOException ioe) {

			// System was not able to perform I/O Operation(s)
			// Check if the file is marked as read only or is protected in any way
			// (encryption, etc.)
			System.err.println("An IOException Occured");
			ioe.printStackTrace();

			//FAIL
			return -1;
        }

        //SUCCESS
        return 0;
    }

    /**
     * <summary> Prints out a visual representation of a HashMap </summary>
     * 
     * @param map - Hash map to be printed out
     */
    public static void print(HashMap <String, Object> map) {

        //Prints out list of all keys and associated values
        System.out.println("\n======KEYS AND CONSTANTS======");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            System.out.println("KEY: [" + entry.getKey() + "] = " + entry.getValue() + " of TYPE " + map.get(entry.getKey()).getClass());
        }
    }

    /**
     * <summary> Asks user if they want to commit changes </summary>
     * 
     * @return boolean isSaved - Boolean value of 'y' / 'n'.
     */
    public static boolean doSave() {
        //Asks user for input.
        System.out.println("Would you like to commit changes? [y/n] ");
        String isSaved = scan.nextLine();

        //return true if y, else return false.
        if(isSaved.equals("y")) {
            return true;
        }
        return false;
    }
}