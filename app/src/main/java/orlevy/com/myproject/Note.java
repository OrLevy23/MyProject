package orlevy.com.myproject;

import android.graphics.Color;

import java.util.Date;

/**
 * Created by Or.levy on 07/09/2016.
 */

public class Note {
    private String subject;
    private String note;
    private boolean star=false;
    private Color color;
    private Date date;
    private int id;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Date getDate() {
        return date;
    }

    public int getId() {
        return id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Note(int id,String subject, String note,boolean star) {
        this.subject = subject;
        this.note = note;
        this.id = id;
        this.star = star;
    }

    public boolean isStarred() {
        return star;
    }

    public void setStar() {
        this.star = !star;
    }

    public boolean isClone(Note another) {
        if (this.note.equals( another.note )&& this.subject.equals(another.subject )&& this.star == another.star) {
            return true;
        }
        return false;
    }
}
