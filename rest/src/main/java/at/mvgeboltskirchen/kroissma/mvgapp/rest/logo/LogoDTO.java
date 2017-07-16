package at.mvgeboltskirchen.kroissma.mvgapp.rest.logo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "LogoDTO", description = "A DTO with informations for a Logo")
public class LogoDTO {

    @ApiModelProperty(readOnly = true, name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(required = true, name = "The title of the news")
    private String title;

    @ApiModelProperty(name = "The image of the news")
    private byte[] imageBytes;

    public static LogoDTOBuilder builder() {
        return new LogoDTOBuilder();
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

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    @Override
    public String toString() {
        return "DetailedNewsDTO{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", image='" + imageBytes + '\'' +
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

        LogoDTO that = (LogoDTO) o;

        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }
        if (title != null ? !title.equals(that.title) : that.title != null) {
            return false;
        }
        return imageBytes != null ? imageBytes.equals(that.imageBytes) : that.imageBytes == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (imageBytes != null ? imageBytes.hashCode() : 0);
        return result;
    }

    public static final class LogoDTOBuilder {

        private Long id;
        private String title;
        private byte[] image;

        public LogoDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public LogoDTOBuilder title(String title) {
            this.title = title;
            return this;
        }

        public LogoDTOBuilder image(byte[] image) {
            this.image = image;
            return this;
        }

        public LogoDTO build() {
            LogoDTO logoDTO = new LogoDTO();
            logoDTO.setId(id);
            logoDTO.setTitle(title);
            logoDTO.setImageBytes(image);
            return logoDTO;
        }
    }
}
