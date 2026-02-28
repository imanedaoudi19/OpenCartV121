package utilities;

import java.io.IOException;

import org.testng.annotations.DataProvider;

public class DataProviders {

    // DataProvider 1
    @DataProvider(name = "LoginData")
    public String[][] getData() throws IOException {

        String path = ".\\testData\\Opencart_LoginData.xlsx"; // taking xl file from testData

        ExcelUtility xlutil = new ExcelUtility(path); // creating an object for XLUtility

        int totalrows = xlutil.getRowCount("Sheet1");
        int totalcols = xlutil.getCellCount("Sheet1", 1);

        String logindata[][] = new String[totalrows][totalcols]; // created two dimension array

        // Read the data from excel storing in two dimensional array
        for (int i = 1; i <= totalrows; i++) {   // i represents rows

            for (int j = 0; j < totalcols; j++) {  // j represents columns

                logindata[i - 1][j] = xlutil.getCellData("Sheet1", i, j);
            }
        }

        return logindata;  // returning two dimension array
    }
    
   //DataProvider 2
    
    //DataProvider 3

    
    //DataProvider 4

}