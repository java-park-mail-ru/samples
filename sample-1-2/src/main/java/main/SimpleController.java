package main;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * Created by Solovyev on 16/09/2017.
 */
@RestController
public class SimpleController {
    private int it = 0;

    @RequestMapping(method = RequestMethod.GET, path = "info")
    public ResponseEntity<Pojo> info(HttpSession httpSession) {
        Integer userN = (Integer) httpSession.getAttribute("userN");
        if (userN == null) {
            userN = it++;
            httpSession.setAttribute("userN", userN);
        }
        return ResponseEntity.ok(new Pojo(userN));
    }

    @SuppressWarnings("unused")
    private static class Pojo {
        private Integer data;

        Pojo(@JsonProperty("data") Integer data) {
            this.data = data;
        }

        public Integer getData() {
            return data;
        }
    }


}
