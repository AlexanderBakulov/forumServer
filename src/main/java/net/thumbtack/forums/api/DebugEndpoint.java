package net.thumbtack.forums.api;

import net.thumbtack.forums.errors.ForumException;
import net.thumbtack.forums.service.DebugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/debug/")
public class DebugEndpoint {

    private DebugService debugService;

    @Autowired
    public DebugEndpoint(DebugService debugService) {
        this.debugService = debugService;
    }

    @PostMapping(value = "clear")
    public void clearServer() throws ForumException {
        debugService.clear();
    }

}
