package com.dn.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
public class Rendering {
    @EqualsAndHashCode.Exclude
    private final Document document;
    private final String uid;
    @EqualsAndHashCode.Exclude
    private List<XEvent> events = new ArrayList<>();
}
