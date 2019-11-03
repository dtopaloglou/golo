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

    @RequestMapping("/")
    public String index() {
        return "REST SERVER RUNNING";
    }

    @RequestMapping(value = "/start", method = POST)
    public ResponseEntity startServer(@RequestParam(name = "url") String url, @RequestParam int interval){
        if(interval < 1){
            interval = 5;
        }
        System.out.println("URL: " + url + " @ interval: " + interval + " seconds.") ;
        try {
            timer = new Timer();
            monitor = new Monitor(url);
            timer.schedule(monitor, interval * 1000);
            return new ResponseEntity(HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }



    @RequestMapping(value = "/stop", method = POST)
    @ResponseBody
    public ResponseEntity stopServer(@RequestParam(name = "url") String url){
        if(timer != null && monitor != null && monitor.getUrl().equals(url)) {
            this.timer.cancel();
            this.timer.purge();
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

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