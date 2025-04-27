package com.ntdev.library.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookUpdateRequest {

    @Size(max = 255, message = "Title cannot exceed 255 characters")
    private String title;

    @Size(max = 255, message = "Author cannot exceed 255 characters")
    private String author;

    private String genre;

    private String coverUrl;

    private String description;

    private String isbn;

    private Integer totalCopies;

    private String summary;
}
