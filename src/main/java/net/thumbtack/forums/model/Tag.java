package net.thumbtack.forums.model;

import java.util.Objects;

public class Tag {

    private int id;
    private String tag;

    public Tag() {
    }

    public Tag(int id, String tag) {
        this.id = id;
        this.tag = tag;
    }

    public Tag(String tag) {
        this(0, tag);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag)) return false;
        Tag tag1 = (Tag) o;
        return id == tag1.id &&
                Objects.equals(tag, tag1.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tag);
    }
}
