package net.thumbtack.forums.dto.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class EmptyResponse {

    public static ObjectNode respone = new ObjectMapper().createObjectNode();

}
