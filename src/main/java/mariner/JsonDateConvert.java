// convert Json timestamp to csv string format
package mariner;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class JsonDateConvert {
    public String csvDate;
    
    public JsonDateConvert(String date){
        date = date;
        Calendar calendar = Calendar.getInstance();
        Long timeInMillis = Long.valueOf(date);
        calendar.setTimeInMillis(timeInMillis);
        csvDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss z").format(calendar.getTime());
        //System.out.println(csvDate);
    }
    public String getCSVDate(){
        return csvDate;
    }

}
