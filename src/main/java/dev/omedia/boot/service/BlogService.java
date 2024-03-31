package dev.omedia.boot.service;

import dev.omedia.boot.domain.Blog;
import dev.omedia.boot.dto.BlogDTO;
import dev.omedia.boot.exception.EntityNotFoundException;
import dev.omedia.boot.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class BlogService {
    private final ModelMapper mapper;
    private final BlogRepository repository;

    @Autowired
    private final ElasticsearchRestTemplate template;

    public Collection<BlogDTO> findAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .map(blog -> mapper.map(blog, BlogDTO.class))
                .collect(Collectors.toList());
    }

    public BlogDTO findById(long id) {
        Blog blog = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Blog with id was not found"));
        return mapper.map(blog, BlogDTO.class);
    }

    public List<BlogDTO> search(String word, Set<String> celebrities, int year, String author) {
        var builder = QueryBuilders.boolQuery();

        builder.must(QueryBuilders.termQuery("active", true));

        builder.should(QueryBuilders.matchQuery("name", word)
                .boost(3)
                .fuzziness("AUTO"));

        builder.should(QueryBuilders.matchQuery("celebrityFullNames", word)
                .boost(2)
                .fuzziness("AUTO"));

        builder.should(QueryBuilders.matchQuery("content", word)
                .boost(1)
                .fuzziness("AUTO"));

        builder.filter(QueryBuilders.termQuery("celebrityFullNames", celebrities));

        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);

        builder.filter(QueryBuilders.rangeQuery("publishDate")
                .gte(startDate)
                .lte(endDate));

        builder.filter(QueryBuilders.termQuery("author", author));

        var searchQueryBuilder = new NativeSearchQueryBuilder();
        var searchQuery = searchQueryBuilder.withQuery(builder).build();

        List<Blog> blogs = template.search(searchQuery, Blog.class)
                .stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());

        List<BlogDTO> dtos = blogs.stream()
                .map(blog -> mapper.map(blog, BlogDTO.class))
                .collect(Collectors.toList());

        return dtos;
    }

    public BlogDTO save(BlogDTO dto) {
        dto.setLastUpdateDate(dto.getPublishDate());
        Blog save = repository.save(mapper.map(dto, Blog.class));
        return mapper.map(save, BlogDTO.class);
    }

    public BlogDTO update(long id, BlogDTO dto) {
        Blog blog = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Blog with id was not found"));
        dto.setLastUpdateDate(LocalDate.now());
        dto.setPublishDate(blog.getPublishDate());
        dto.setAuthor(blog.getAuthor());
        dto.setId(id);

        Blog save = repository.save(mapper.map(dto, Blog.class));
        return mapper.map(save, BlogDTO.class);
    }

    public void deleteById(long id) {
        Blog blog = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Blog with id was not found"));
        repository.deleteById(id);
    }
}
