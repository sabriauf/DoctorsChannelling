package lk.hemas.ayubo.model;

import java.io.Serializable;

public class Appointment implements Serializable {

    private SessionParent sessionParent;
    private Session session;
    private String source;
    private User user;

    public SessionParent getSessionParent() {
        return sessionParent;
    }

    public void setSessionParent(SessionParent sessionParent) {
        this.sessionParent = sessionParent;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
