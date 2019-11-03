package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.TimerTask;

public class Monitor extends TimerTask {

    private String url;

    private String status;

    private Date startTime;

    private Date endTime;

    public Monitor (String url){
        this.url = url;
    }

    public String getUrl(){
        return url;
    }


    private void check() {
        try {
            // check api
            URL check_url = new URL(url);
            HttpURLConnection con = (HttpURLConnection) check_url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();
            status = content.toString();
            System.out.println("API status: " + status);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {

        if(startTime == null){
            startTime = new Date();
        }

        check();

    }

    @Override
    public boolean cancel(){

        endTime = new Date();
        return super.cancel();
    }

    public String getStatus(){
        return status;
    }

    public Date getStartTime(){
        return startTime;

    }

    public Date getEndTime(){
        return endTime;
    }
}

