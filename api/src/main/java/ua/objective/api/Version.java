package ua.objective.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
public class Version {

    @RequestMapping("/version")
    public Map getVersion() {
        return Collections.singletonMap("version", null);
    }
}
