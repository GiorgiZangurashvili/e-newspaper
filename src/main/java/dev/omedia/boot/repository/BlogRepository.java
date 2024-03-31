package dev.omedia.boot.repository;

import dev.omedia.boot.domain.Blog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends ElasticsearchRepository<Blog, Long> {
}
