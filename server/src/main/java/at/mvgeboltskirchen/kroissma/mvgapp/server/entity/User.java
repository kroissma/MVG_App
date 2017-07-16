package at.mvgeboltskirchen.kroissma.mvgapp.server.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;


@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_user_id")
    @SequenceGenerator(name = "seq_user_id", sequenceName = "seq_user_id")
    private Long id;

    @Column(nullable = false, length = 128, unique = true)
    private String username;

    @Column(nullable = false, length = 1028)
    private String password;

    @Column(nullable = false, name = "is_admin")
    private Boolean isAdmin;

    @Column(nullable = false, name = "is_locked")
    private Boolean isLocked;

//    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "creator")
//    private Set<News> createdNews = new HashSet<>();

    @Column(nullable = false, name = "failed_login_attempts")
    private Integer failedLoginAttempts = 0;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_read_news", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false),
        inverseJoinColumns = @JoinColumn(name = "news_id", referencedColumnName = "id", nullable = true))
    private Set<News> readNews = new HashSet<>();

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public Boolean getLocked() {
        return isLocked;
    }

    public void setLocked(Boolean locked) {
        isLocked = locked;
    }

//    public Set<News> getCreatedNews() {
//        return createdNews;
//    }

//    public void setCreatedNews( Set<News> createdNews) {
//        this.createdNews = createdNews;
//    }

    public Set<News> getReadNews() {
        return readNews;
    }

    public void setReadNews(Set<News> readNews) {
        this.readNews = readNews;
    }

    public Integer getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public void setFailedLoginAttempts(Integer failedLoginAttempts) {
        this.failedLoginAttempts = failedLoginAttempts;
    }

    @Override
    public String toString() {
        return "Client{" +
            "id=" + id +
            ", username='" + username + '\'' +
            ", password='" + password + '\'' +
            ", isAdmin='" + isAdmin + '\'' +
            ", isLocked='" + isLocked + '\'' +
            ", failedLoginAttempts=" + failedLoginAttempts +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;

        if (id != null ? !id.equals(user.id) : user.id != null) {
            return false;
        }
        if (username != null ? !username.equals(user.username) : user.username != null) {
            return false;
        }
        if (password != null ? !password.equals(user.password) : user.password != null) {
            return false;
        }
        if (isAdmin != null ? !isAdmin.equals(user.isAdmin) : user.isAdmin != null) {
            return false;
        }
        if (failedLoginAttempts != null ? !failedLoginAttempts.equals(user.failedLoginAttempts)
            : user.failedLoginAttempts != null) {
            return false;
        }
        return isLocked != null ? isLocked.equals(user.isLocked) : user.isLocked == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (isAdmin != null ? isAdmin.hashCode() : 0);
        result = 31 * result + (isLocked != null ? isLocked.hashCode() : 0);
        result = 31 * result + (failedLoginAttempts != null ? failedLoginAttempts.hashCode() : 0);
        return result;
    }

    public static final class UserBuilder {

        private Long id;
        private String username;
        private String password;
        private Boolean isAdmin;
        private Boolean isLocked;
        private Integer failedLoginAttempts = 0;
        //        private Set<News> createdNews = new HashSet<>();
        private Set<News> readNews = new HashSet<>();

        private UserBuilder() {
        }

        public UserBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder isAdmin(Boolean isAdmin) {
            this.isAdmin = isAdmin;
            return this;
        }

        public UserBuilder isLocked(Boolean isLocked) {
            this.isLocked = isLocked;
            return this;
        }

//        public UserBuilder createdNews(Set<News> createdNews) {
//            this.createdNews = createdNews;
//            return this;
        //       }

        public UserBuilder readNews(Set<News> readNews) {
            this.readNews = readNews;
            return this;
        }

        public UserBuilder failedLoginAttempts(Integer failedLoginAttempts) {
            this.failedLoginAttempts = failedLoginAttempts;
            return this;
        }

        public User build() {
            User user = new User();
            user.setUsername(username);
            user.setAdmin(isAdmin);
            user.setLocked(isLocked);
            user.setPassword(password);
//            user.setCreatedNews(createdNews);
            user.setReadNews(readNews);
            user.setFailedLoginAttempts(failedLoginAttempts);

            return user;
        }
    }
}
