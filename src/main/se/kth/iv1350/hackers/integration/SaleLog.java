package se.kth.iv1350.hackers.integration;
import java.util.ArrayList;

import se.kth.iv1350.hackers.DTO.SaleDTO;

class SaleLog {
    private ArrayList<SaleDTO> saleLog = new ArrayList<SaleDTO>();
    /**
     * This is a package private constructor method
     */
    SaleLog(){

    }
   

    /**
     * Logs the current sale in the sale log
     * @param saleInformation SaleDTO containing information of sale.
     */
    public void logSale(SaleDTO saleInformation){
        saleLog.add(saleInformation);
    }

    public ArrayList<SaleDTO> getArrayList(){
        return saleLog;
    }
}