package net.thumbtack.forums.dto.request;

import net.thumbtack.forums.model.enums.PostPriority;

import javax.validation.constraints.NotNull;

import static net.thumbtack.forums.Config.NOT_NULL;

public class ChangePriorityDtoRequest {

    @NotNull(message = NOT_NULL)
    private PostPriority priority;

    public ChangePriorityDtoRequest() {
    }

    public ChangePriorityDtoRequest(PostPriority priority) {
        this.priority = priority;
    }

    public PostPriority getPriority() {
        return priority;
    }

    public void setPriority(PostPriority priority) {
        this.priority = priority;
    }
}
