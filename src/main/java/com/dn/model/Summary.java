package com.dn.model;

import lombok.Data;

@Data
public class Summary {
    private final long totalRenderings;
    private final long duplicateRenderings;
    private final long orphanRenderings;
}
