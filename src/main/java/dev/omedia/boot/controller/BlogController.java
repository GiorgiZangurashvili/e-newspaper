package dev.omedia.boot.controller;

import dev.omedia.boot.dto.BlogDTO;
import dev.omedia.boot.service.BlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/blogs")
@RequiredArgsConstructor
public class BlogController {
    private final BlogService service;

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve blog with specified id",
            responses = {@ApiResponse(responseCode = "404", description = "Blog with specified id was not found"),
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved blog with specified id")})
    public ResponseEntity<BlogDTO> findById(
            @PathVariable(name = "id") long id
    ) {
        return ResponseEntity.of(
                Optional.of(service.findById(id))
        );
    }

    @GetMapping
    @Operation(summary = "Retrieve all blogs from database",
            responses = {@ApiResponse(responseCode = "204", description = "Not a single blog is stored in database"),
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved all blogs")})
    public ResponseEntity<Collection<BlogDTO>> findAll() {
        Collection<BlogDTO> all = service.findAll();
        if (!all.isEmpty()) {
            return ResponseEntity.of(Optional.of(all));
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(all);
        }
    }

    @GetMapping("/{word}/{celebrities}/{year}/{author}")
    @Operation(summary = "Retrieve all blogs from database",
            responses = {@ApiResponse(responseCode = "204", description = "Not a single blog is stored in database"),
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved all blogs")})
    public ResponseEntity<Collection<BlogDTO>> executeSearchQuery(
            @PathVariable(name = "word") String word,
            @PathVariable(name = "celebrities") Set<String> celebrities,
            @PathVariable(name = "year") int year,
            @PathVariable(name = "author") String author
    ) {
        Collection<BlogDTO> all = service.search(word, celebrities, year, author);
        if (!all.isEmpty()) {
            return ResponseEntity.of(Optional.of(all));
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(all);
        }
    }


    @PostMapping
    @Operation(summary = "Save a new blog to database",
            description = "Only can add blog's fields, not it's children",
            responses = {@ApiResponse(responseCode = "400", description = "Some validations failed"),
                    @ApiResponse(responseCode = "201", description = "Successfully created a new blog")})
    public ResponseEntity<BlogDTO> save(@Valid @RequestBody BlogDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.save(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update already existing blog",
            description = "Only can update unit's specific fields (only: name, content, topics and " +
                    "celebrityFullNames)",
            responses = {@ApiResponse(responseCode = "404", description = "Blog with specified id was not found"),
                    @ApiResponse(responseCode = "200", description = "Successfully updated blog with specified id"),
                    @ApiResponse(responseCode = "400", description = "Some validations failed")})
    public ResponseEntity<BlogDTO> update(
            @PathVariable(name = "id") long id,
            @Valid @RequestBody BlogDTO dto
    ) {
        return ResponseEntity.of(
                Optional.of(service.update(id, dto))
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes blog from database",
            responses = {@ApiResponse(responseCode = "404", description = "Blog with specified id was not found"),
                    @ApiResponse(responseCode = "204", description = "Blog was successfully deleted")})
    public void delete(@PathVariable(name = "id") long id) {
        service.deleteById(id);
    }
}
