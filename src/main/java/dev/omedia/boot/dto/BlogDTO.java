package dev.omedia.boot.dto;

import dev.omedia.boot.domain.Topic;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

@Getter
@Setter
@Schema(description = "blog data in database")
public class BlogDTO {
    @Schema(description = "blog id")
    private long id;

    @Schema(description = "blog author")
    @NotBlank(message = "author name should not be blank")
    private String author;

    @Schema(description = "blog name")
    @NotBlank(message = "blog name name should not be blank")
    private String name;

    @Schema(description = "blog content")
    @NotBlank(message = "blog content name should not be blank")
    private String content;

    @Schema(description = "blog publish date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate publishDate;

    @Schema(description = "blog last update date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate lastUpdateDate;

    @Schema(description = "topics that are brought up in blog")
    private Collection<Topic> topics;

    @Schema(description = "celebrities that are mentioned in blog")
    private Collection<String> celebrityFullNames;
}
