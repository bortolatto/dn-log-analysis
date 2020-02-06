package com.dn.service;

import com.dn.model.Rendering;
import com.dn.model.Summary;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SummaryService {
    private final List<Rendering> renderingList;
    private final Set<String> startRenderingUids;
    private final Set<String> getRenderingUids;

    public SummaryService(List<Rendering> renderingList,
                          Set<String> startRenderingUids,
                          Set<String> getRenderingUids) {
        this.renderingList = renderingList;
        this.startRenderingUids = startRenderingUids;
        this.getRenderingUids = getRenderingUids;
    }

    public List<Rendering> getDistinctRenderings() {
        return renderingList.stream().distinct().collect(Collectors.toList());
    }

    public Summary getSummary() {
        return new Summary(getTotalRenderings(), getDuplicateRenderings(), getOrphanRenderings());
    }

    private long getTotalRenderings() {
        return renderingList.stream().distinct().count();
    }

    private long getOrphanRenderings() {
        startRenderingUids.removeAll(getRenderingUids);
        return startRenderingUids.size();
    }

    private long getDuplicateRenderings() {
        Map<Rendering, Long> counted = renderingList.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        return counted.entrySet()
                .stream()
                .filter(x -> x.getValue() > 1)
                .count();
    }
}
