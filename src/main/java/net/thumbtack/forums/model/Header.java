package net.thumbtack.forums.model;

import net.thumbtack.forums.model.enums.PostPriority;

import java.util.Objects;
import java.util.Set;


public class Header {

    private int id;
    private Forum forum;
    private String subject;
    private PostPriority priority;
    private Set<Tag> tags;

    public Header() {
    }

    public Header(int id, Forum forum, String subject, PostPriority priority, Set<Tag> tags) {
        this.id = id;
        this.forum = forum;
        this.subject = subject;
        this.priority = priority;
        this.tags = tags;
    }

    public Header(Forum forum, String subject, PostPriority priority, Set<Tag> tags) {
        this(0, forum, subject, priority, tags);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Forum getForum() {
        return forum;
    }

    public void setForum(Forum forum) {
        this.forum = forum;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public PostPriority getPriority() {
        return priority;
    }

    public void setPriority(PostPriority priority) {
        this.priority = priority;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Header)) return false;
        Header header = (Header) o;
        return id == header.id &&
                Objects.equals(forum, header.forum) &&
                Objects.equals(subject, header.subject) &&
                priority == header.priority &&
                Objects.equals(tags, header.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, forum, subject, priority, tags);
    }
}

