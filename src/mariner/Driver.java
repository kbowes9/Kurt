/*
 * Program to read in json, xml, csv files
 * and output csv file
 * Kurt Bowes
 */
package mariner;
import com.opencsv.CSVWriter;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.json.simple.JSONArray; //cannot locate javadoc
import org.json.simple.JSONObject; //cannot locate javadoc
import org.json.simple.parser.JSONParser; //cannot locate javadoc
import org.json.simple.parser.ParseException; //cannot locate javadoc
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Driver {
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws org.json.simple.parser.ParseException
     */
    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws IOException, ParseException {
        // create list for CSV output
        ArrayList<String> finalCSV = new ArrayList<String>();
        ArrayList<Record> objectCSV = new ArrayList<>();
        finalCSV.add("client-address,client-guid,request-time,service-guid,retries-request,packets-requested,packets-serviced,max-hole-size");
        //Record rec = new Record("client-address,client-guid,request-time,service-guid,retries-request,packets-requested,packets-serviced,max-hole-size");
        //JSON handling below:
        //https://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
        //https://stackoverflow.com/questions/42310468/class-cast-exception-
        //jsonarray-cannot-be-cast-to-org-json-simple-jsonobject
        JSONParser prs = new JSONParser();  
        JSONObject jsonObject = null; 
        String cliAdd, cliGui, reqTime, serGui, retReq, pakReq, pakSer, maxHole;
        
        try{
            JSONArray array =  (JSONArray) prs.parse(new FileReader("reports.json")); 
            for (int i=0; i<array.size(); i++){
                jsonObject = (JSONObject) array.get(i); //object from which to pull key values                
                cliAdd = jsonObject.get("client-address").toString();
                cliGui = jsonObject.get("client-guid").toString();
                JsonDateConvert convert = new JsonDateConvert(jsonObject.get("request-time").toString());
                reqTime = convert.getCSVDate(); //convert timestamp for CSV
                serGui = jsonObject.get("service-guid").toString();
                retReq = jsonObject.get("retries-request").toString();
                pakReq = jsonObject.get("packets-requested").toString();
                pakSer = jsonObject.get("packets-serviced").toString();
                maxHole = jsonObject.get("max-hole-size").toString();
                
                if (!pakSer.equals("0")) {//check for 0's
                    Record rec = new Record(cliAdd,cliGui,reqTime,serGui,retReq,pakReq,pakSer,maxHole);
                    objectCSV.add(rec);                    
                }                
        }
            
        }
        catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        
        //XML handling below:
        //root node - records
        //https://www.w3schools.com/xml/dom_nodes.asp
        //https://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
        DocumentBuilderFactory fct = DocumentBuilderFactory.newInstance();
        String cliAddXML = null, cliGuiXML = null, reqTimeXML = null, serGuiXML = null, retReqXML = null, pakReqXML = null, pakSerXML = null, maxHoleXML = null;
        try {
            DocumentBuilder bld = fct.newDocumentBuilder();
            Document doc = bld.parse("reports.xml");
            NodeList reps = doc.getElementsByTagName("report");
            for (int i=0; i<reps.getLength(); i++){
                Node n = reps.item(i);
                if (n.getNodeType()==Node.ELEMENT_NODE){
                    Element e = (Element)n;
                    NodeList kids = e.getChildNodes();
                    
                    for (int x=0; x<kids.getLength(); x++){
                        Node nn = kids.item(x);
                        if(nn.getNodeType()==Node.ELEMENT_NODE){
                            Element val = (Element)nn;                         
                            switch (val.getTagName().toString()) {
                                case "client-address":
                                  cliAddXML = val.getTextContent().toString();
                                  break;
                                case "client-guid":
                                  cliGuiXML = val.getTextContent().toString();
                                  break;
                                case "request-time":
                                  reqTimeXML = val.getTextContent().toString();
                                  break;
                                case "service-guid":
                                  serGuiXML = val.getTextContent().toString();
                                  break;
                                case "retries-request":
                                  retReqXML = val.getTextContent().toString();
                                  break;
                                case "packets-requested":
                                  pakReqXML = val.getTextContent().toString();
                                  break;
                                case "packets-serviced":
                                  pakSerXML = val.getTextContent().toString();
                                  break;
                                case "max-hole-size":
                                  maxHoleXML = val.getTextContent().toString();
                                  break;
                              }
                        }                        
                    }
                }
                
                if (!pakSerXML.equals("0")) {//check for 0's                    
                    Record rec = new Record(cliAddXML,cliGuiXML,reqTimeXML,serGuiXML,retReqXML
                            ,pakReqXML,pakSerXML,maxHoleXML);
                    objectCSV.add(rec);
                }                
            }  

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Driver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(Driver.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //handle CSV
        System.out.println();
        BufferedReader br = null;
        String line = "";
        String split = ",";
        br = new BufferedReader(new FileReader("reports.csv"));
        br.readLine();
        while ((line = br.readLine()) != null ){
            String [] csvArray = line.split(split);
            if (!csvArray[6].equals("0")) {//check for 0's
                Record rec = new Record(csvArray[0],csvArray[1],csvArray[2],
                        csvArray[3],csvArray[4],csvArray[5],csvArray[6],csvArray[7]);
                objectCSV.add(rec);
                }            
        }
        // sorting dates
        // https://stackoverflow.com/questions/43116496/sorting-csv-file-by-one-field
        Collections.sort(objectCSV, new Comparator<Record>(){
            public int compare(Record r1, Record r2){
                return r1.reqTim.compareTo(r2.reqTim);
            }
        });
        
        //convert Record object to list
        for (int i = 0; i < objectCSV.size(); i++) {
            finalCSV.add(objectCSV.get(i).toString());
        }
        //test print of finalCSV array
        /*
        System.out.println("list test:");
        for (int m=0; m<finalCSV.size(); m++){
                System.out.println("F > "+finalCSV.get(m));
            }
         */
        //write finalCSV arraylist to csv file
        //https://stackoverflow.com/questions/41738187/how-to-write-an-arraylist-as-a-csv

        CSVWriter writer = new CSVWriter(new FileWriter("finalReport.csv"),',') ;
        for (String i : finalCSV) {
            writer.writeNext(new String[]{i.toString()});           
        }
        writer.close();
        System.out.println("Summary:");
        System.out.println("Total no. of service-guid records:  " + finalCSV.size());
        
    } //end MAIN    
}
