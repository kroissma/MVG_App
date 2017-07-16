package at.mvgeboltskirchen.kroissma.mvgapp.server.entity;

import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.Size;

@Entity
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_news_id")
    @SequenceGenerator(name = "seq_news_id", sequenceName = "seq_news_id")
    private Long id;

    @Column(nullable = false, name = "published_at")
    private LocalDateTime publishedAt;

    @Column(nullable = false)
    @Size(max = 100)
    private String title;

    @Column(nullable = false, length = 10_000)
    private String text;

    @Column(nullable = true, length = 512)
    private String image;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "user_id", nullable = true)
//    private User creator;

    @ManyToMany(mappedBy = "readNews")
    private Set<User> users;

    public static NewsBuilder builder() {
        return new NewsBuilder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

//    public User getCreator() {
//        return creator;
//    }

//    public void setCreator(User creator) {
//        this.creator = creator;
//    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "News{" +
            "id=" + id +
            ", publishedAt=" + publishedAt +
            ", title='" + title + '\'' +
            ", text='" + text + '\'' +
            ", image='" + image + '\'' +
//            ", creator=" + creator +
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

        News news = (News) o;

        if (id != null ? !id.equals(news.id) : news.id != null) {
            return false;
        }
        if (publishedAt != null ? !publishedAt.equals(news.publishedAt)
            : news.publishedAt != null) {
            return false;
        }
        if (title != null ? !title.equals(news.title) : news.title != null) {
            return false;
        }
        if (image != null ? !image.equals(news.image) : news.image != null) {
            return false;
        }
//        if (creator != null ? !creator.equals(news.creator) : news.creator != null) {
//            return false;
//        }
        return text != null ? text.equals(news.text) : news.text == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (publishedAt != null ? publishedAt.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
//        result = 31 * result + (creator != null ? creator.hashCode() : 0);
        return result;
    }

    public static final class NewsBuilder {

        private Long id;
        private LocalDateTime publishedAt;
        private String title;
        private String text;
        private String image;
//        private User creator;


        private NewsBuilder() {
        }

        public NewsBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public NewsBuilder publishedAt(LocalDateTime publishedAt) {
            this.publishedAt = publishedAt;
            return this;
        }

        public NewsBuilder title(String title) {
            this.title = title;
            return this;
        }

        public NewsBuilder text(String text) {
            this.text = text;
            return this;
        }

        public NewsBuilder image(String image) {
            this.image = image;
            return this;
        }

        //       public NewsBuilder creator(User creator) {
        //           this.creator = creator;
        //           return this;
        //       }

        public News build() {
            News news = new News();
            news.setId(id);
            news.setPublishedAt(publishedAt);
            news.setTitle(title);
            news.setText(text);
            news.setImage(image);
//            news.setCreator(creator);
            return news;
        }
    }
}