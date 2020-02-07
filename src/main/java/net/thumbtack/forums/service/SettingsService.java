package net.thumbtack.forums.service;

import net.thumbtack.forums.dto.response.SettingsDtoResponse;
import net.thumbtack.forums.errors.ForumException;
import net.thumbtack.forums.model.Session;

public interface SettingsService {

    SettingsDtoResponse getForumSettings(Session session) throws ForumException;

}
