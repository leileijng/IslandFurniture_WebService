package EntityManager;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class FeedbackEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Lob
    private String subject;
    @Lob
    private String name;
    @Lob
    private String email;
    @Lob
    private String message;
    
    public FeedbackEntity(){        
    }

public FeedbackEntity(String subject, String name, String email, String message){
        this.subject = subject;
        this.name = name;
        this.email = email;
        this.message = message;             
    }
            
         
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    

//    public MemberEntity getMember() {
//        return member;
//    }
//
//    public void setMember(MemberEntity member) {
//        this.member = member;
//    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}