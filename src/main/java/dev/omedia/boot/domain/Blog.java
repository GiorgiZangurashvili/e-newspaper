package dev.omedia.boot.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.Collection;

@Document(indexName = "blogs")
@Getter
@Setter
public class Blog {

    @Id
    private long id;

    @NotBlank(message = "author name should not be blank")
    private String author;

    @NotBlank(message = "blog name should not be blank")
    private String name;

    @NotBlank(message = "blog content should not be blank")
    private String content;

    @Field(type = FieldType.Date)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate publishDate;

    @Field(type = FieldType.Date)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate lastUpdateDate;

    private Collection<Topic> topics;

    private Collection<String> celebrityFullNames;
}
