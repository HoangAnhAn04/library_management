package com.ntdev.library.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookCreateRequest {

    @NotBlank(message = "Title is mandatory")
    @Size(max = 255, message = "Title cannot exceed 255 characters")
    private String title;

    @NotBlank(message = "Author is mandatory")
    @Size(max = 255, message = "Author cannot exceed 255 characters")
    private String author;

    @NotBlank(message = "Genre is mandatory")
    private String genre;

    @NotBlank(message = "Cover URL is mandatory")
    private String coverUrl;

    @NotBlank(message = "Description is mandatory")
    private String description;

    @NotBlank(message = "ISBN is mandatory")
    private String isbn;

    @NotNull(message = "Total copies is mandatory")
    private Integer totalCopies;

    @NotNull(message = "Available copies is mandatory")
    private Integer availableCopies;

    @NotBlank(message = "Summary is mandatory")
    private String summary;
}
