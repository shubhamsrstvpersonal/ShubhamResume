package com.js.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "CHATDETAILS")
public class ChatDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private long id;

    @Column(name = "FROMID", length = 50)
    private String fromId;

    @Column(name = "TOID", length = 50)
    private String toId;

    @Column(name = "TIME", length = 100)
    private Date time;

    @Column(name = "MSG", length = 500)
    private String msg;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
