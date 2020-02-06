package com.dn.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Document {
    private final int id;
    private final int pageNumber;
    private final String threadName;
}
