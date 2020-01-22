package Config;

public class Config {
    //This will be in %APPDATA%\Roaming\NetBeans\8.0\config\GF_4.0\domain1\config for windows 8 
    //C:\Program Files\glassfish-4.1\glassfish\domains\domain1\config for windows 7
    public static String logFilePath = ".\\IslandFurnitureSystemLog.txt";
    
    //Volume >= to the value below will be redirected to collection area for the checkout process
    //The deduction of qty from inventory control will also not happen until picker completes the picking
    public static Integer minVolumeForCollectionAreaItems = 360000;
}
