package net.thumbtack.forums.api;

import net.thumbtack.forums.dto.response.SettingsDtoResponse;
import net.thumbtack.forums.errors.ForumException;
import net.thumbtack.forums.model.Session;
import net.thumbtack.forums.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/settings")
public class SettingsEndpoint {

    private SettingsService settingsService;

    @Autowired
    public SettingsEndpoint(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public SettingsDtoResponse getSettings(@CookieValue(value = "JAVASESSIONID", required = false) String cookie) throws ForumException {
        return settingsService.getForumSettings(new Session(cookie));
    }


}