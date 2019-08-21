// interface for Record object
package mariner;

public class Record implements Comparable{
    public String cliAdd, cliGui, reqTim, serGui, retReq, pacReq, pacSer, maxHole;
    
        
    public Record(String cliAdd, String cliGui, String reqTim, String serGui, String retReq, 
            String pacReq, String pacSer, String maxHole) {

        this.cliAdd=cliAdd; 
        this.cliGui=cliGui; 
        this.reqTim=reqTim; 
        this.serGui=serGui;        
        this.retReq=retReq; 
        this.pacReq=pacReq;
        this.pacSer=pacSer;
        this.maxHole=maxHole;   
    }
    public Record(){
        //keep empty
    }  
    
    public String getCliAdd(){
        return cliAdd;
    }
    public String getCliGui(){
        return cliGui;
    }
    public String getReqTim(){
        return reqTim; //to be sorted
    }
    public String getSerGui(){
        return serGui;
    }
    public String getRetReq(){
        return retReq;
    }
    public String getPaqReq(){
        return pacReq;
    }
    public String getPacSer(){
        return pacSer;
    }
    public String getMaxhole(){
        return maxHole;
    } 
    
    public String toString(){
        return cliAdd+ "," + cliGui+ "," + reqTim+ "," + serGui+ "," + retReq+ ","
                + pacReq+ "," +  pacSer+ "," + maxHole;
    }    


    @Override
    public int compareTo(Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
