package net.thumbtack.forums.mappers.mapstruct;


import net.thumbtack.forums.dto.response.ForumInfoDtoResponse;
import net.thumbtack.forums.dto.response.MessageInfoDtoResponse;
import net.thumbtack.forums.dto.response.PostQuantityDtoResponse;

import net.thumbtack.forums.dto.response.PostRatingDtoResponse;
import net.thumbtack.forums.view.ForumInfo;
import net.thumbtack.forums.view.MessageInfo;
import net.thumbtack.forums.view.PostQuantityInfo;
import net.thumbtack.forums.view.PostRatingInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ResponseMapper {

    MessageInfoDtoResponse messageInfoToDtoResponse(MessageInfo info);

    ForumInfoDtoResponse forumInfoToDtoResponse(ForumInfo info);

    PostQuantityDtoResponse postQuantityInfoToDto(PostQuantityInfo info);

    PostRatingDtoResponse postRatingInfoToDto(PostRatingInfo info);


}
