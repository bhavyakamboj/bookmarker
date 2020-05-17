package com.sivalabs.bookmarker.domain.model.email;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmailAttachment {
    private byte[] data;
    private String filename;
    private String mimeType;
    private boolean inline;
}
