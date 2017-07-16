package at.mvgeboltskirchen.kroissma.mvgapp.server.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.Size;

public class Logo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_logo_id")
    @SequenceGenerator(name = "seq_logo_id", sequenceName = "seq_logo_id")
    private Long id;

    @Column(nullable = false)
    @Size(max = 100)
    private String title;

    @Column(nullable = true, length = 512)
    private String imagePath;

    public static LogoBuilder builder() {
        return new LogoBuilder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return "News{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", imagePath='" + imagePath + '\'' +
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

        Logo news = (Logo) o;

        if (id != null ? !id.equals(news.id) : news.id != null) {
            return false;
        }
        if (title != null ? !title.equals(news.title) : news.title != null) {
            return false;
        }
        return imagePath != null ? imagePath.equals(news.imagePath) : news.imagePath == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (imagePath != null ? imagePath.hashCode() : 0);
        return result;
    }

    public static final class LogoBuilder {

        private Long id;
        private String title;
        private String imagePath;

        private LogoBuilder() {
        }

        public LogoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public LogoBuilder title(String title) {
            this.title = title;
            return this;
        }

        public LogoBuilder imagePath(String image) {
            this.imagePath = image;
            return this;
        }

        public Logo build() {
            Logo logo = new Logo();
            logo.setId(id);
            logo.setTitle(title);
            logo.setImagePath(imagePath);
            return logo;
        }
    }

}
