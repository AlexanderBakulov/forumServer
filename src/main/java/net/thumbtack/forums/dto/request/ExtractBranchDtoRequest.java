package net.thumbtack.forums.dto.request;

import net.thumbtack.forums.model.Tag;
import net.thumbtack.forums.model.enums.PostPriority;

import javax.validation.constraints.NotBlank;
import java.util.Set;

import static net.thumbtack.forums.Config.NOT_BLANK;

public class ExtractBranchDtoRequest {


    @NotBlank(message = NOT_BLANK)
    private String subject;

    private PostPriority priority;
    private Set<Tag> tags;

    public ExtractBranchDtoRequest() {
    }

    public ExtractBranchDtoRequest(String subject, PostPriority priority, Set<Tag> tags) {
        this.subject = subject;
        this.priority = priority;
        this.tags = tags;
    }

    public ExtractBranchDtoRequest(String subject, PostPriority priority) {
        this(subject, priority, null);
    }

    public ExtractBranchDtoRequest(String subject) {
        this(subject, PostPriority.NORMAL, null);
    }


    public String getSubject() {
        return subject;
    }

    public PostPriority getPriority() {
        return priority;
    }

    public Set<Tag> getTags() {
        return tags;
    }


}
