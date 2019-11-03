package server;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class Controller {

    private Timer timer;

    private Monitor monitor;

    /**
     *
     * @return rest api status
     */
    @RequestMapping("/")
    public String index() {
        return "REST SERVER RUNNING";
    }

    /**
     * Starts monitoring system if it hasn't started already.
     * @param url url of server to monitor
     * @param interval interval used between each status check
     * @return Returns a HTTP 200 if server has successfully started running or an HTTP 400 if any error occurred
     */
    @RequestMapping(value = "/start", method = POST)
    public ResponseEntity startServer(@RequestParam(name = "url") String url, @RequestParam int interval){

        try {
            if(interval < 1){
                interval = 5; // give it 5 seconds minimum
            }
            if(timer==null){
                timer = new Timer();
                monitor = new Monitor(url);
                // run monitoring process as a task
                timer.schedule(monitor,  0, interval * 1000);
                System.out.println("Monitoring started!");
                return new ResponseEntity(HttpStatus.OK);
            }
            return new ResponseEntity(HttpStatus.OK);
        }
        catch(Exception e){
            // catch all errors. though specific errors would be better to have at this point and handle them accordingly.
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * Stops monitoring
     * @param url url of the server monitored
     * @return returns HTTP 200 if stopped and HTTP 204 if the server never started
     */
    @RequestMapping(value = "/stop", method = POST)
    @ResponseBody
    public ResponseEntity stopServer(@RequestParam(name = "url") String url){
        if(timer != null && monitor != null && monitor.getUrl().equals(url)) {
            this.timer.cancel();
            this.timer.purge();
            System.out.println("Monitoring stopped...");
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * Returns a JSON response based on the monitoring server's start and end time.
     * @return json response object
     */
    @RequestMapping(value = "/overview", method = GET, produces = "application/json")
    @ResponseBody
    public HashMap<String, Object> getOverview(){
        if(monitor != null){
            Date start = monitor.getStartTime();
            Date end = monitor.getEndTime();
            HashMap<String, Object> rtn = new LinkedHashMap<>();
            rtn.put("startTime", start.toString());
            if(end != null) {
                rtn.put("endTime", end.toString());
            } else {
                rtn.put("endTime", null);
            }

            return rtn;
        }

        return new HashMap<>();
    }



}