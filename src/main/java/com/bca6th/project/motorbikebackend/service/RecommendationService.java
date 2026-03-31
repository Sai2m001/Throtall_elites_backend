package com.bca6th.project.motorbikebackend.service;

import com.bca6th.project.motorbikebackend.dto.recommendation.RecommendationDto;
import com.bca6th.project.motorbikebackend.model.BikeView;
import com.bca6th.project.motorbikebackend.model.Product;
import com.bca6th.project.motorbikebackend.repository.BikeViewRepository;
import com.bca6th.project.motorbikebackend.repository.ProductRepository;
import com.bca6th.project.motorbikebackend.repository.TestRideRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationService {

    private final BikeViewRepository    bikeViewRepository;
    private final ProductRepository     productRepository;
    private final TestRideRepository    testRideRepository;

    private static final int LOOKBACK_DAYS = 30;
    public void recordView(Long bikeId, String viewerKey) {
        LocalDate today = LocalDate.now();
        if (bikeViewRepository.existsByBikeIdAndViewerKeyAndViewDate(bikeId, viewerKey, today)) {
            return;
        }
        try {
            bikeViewRepository.save(
                    BikeView.builder()
                            .bikeId(bikeId)
                            .viewerKey(viewerKey)
                            .viewDate(today)
                            .build()
            );
        } catch (Exception e) {
            log.debug("BikeView dedup race condition for bikeId={}, viewerKey={}: {}", bikeId, viewerKey, e.getMessage());
        }
    }

    public List<RecommendationDto> getMostViewed(int limit, Long excludeBikeId) {
        LocalDate since   = LocalDate.now().minusDays(LOOKBACK_DAYS);
        PageRequest top   = PageRequest.of(0, limit + 1);

        List<Object[]> rows = bikeViewRepository.findTopViewedBikeIdsSince(since, top);

        LinkedHashMap<Long, Long> scoredIds = new LinkedHashMap<>();
        for (Object[] row : rows) {
            Long bikeId = (Long) row[0];
            Long count  = (Long) row[1];
            if (excludeBikeId != null && bikeId.equals(excludeBikeId)) continue;
            scoredIds.put(bikeId, count);
            if (scoredIds.size() == limit) break;
        }

        return buildDtos(scoredIds, "views");
    }

    public List<RecommendationDto> getMostRequested(int limit, Long excludeBikeId) {
        List<Object[]> rows = testRideRepository.findTopRequestedBikeIds(
                PageRequest.of(0, limit + 1)
        );

        LinkedHashMap<Long, Long> scoredIds = new LinkedHashMap<>();
        for (Object[] row : rows) {
            Long bikeId = (Long) row[0];
            Long count  = (Long) row[1];
            if (excludeBikeId != null && bikeId.equals(excludeBikeId)) continue;
            scoredIds.put(bikeId, count);
            if (scoredIds.size() == limit) break;
        }

        return buildDtos(scoredIds, "test ride requests");
    }

    private List<RecommendationDto> buildDtos(LinkedHashMap<Long, Long> scoredIds, String label) {
        if (scoredIds.isEmpty()) return Collections.emptyList();

        Map<Long, Product> productMap = productRepository
                .findAllById(scoredIds.keySet())
                .stream()
                .filter(Product::getActive)
                .collect(Collectors.toMap(Product::getId, p -> p));

        List<RecommendationDto> result = new ArrayList<>();
        for (Map.Entry<Long, Long> entry : scoredIds.entrySet()) {
            Product p = productMap.get(entry.getKey());
            if (p == null) continue;

            String primaryImageUrl = p.getImages().stream()
                    .filter(img -> img.isPrimary())
                    .map(img -> img.getImageUrl())
                    .findFirst()
                    .or(() -> p.getImages().stream().map(img -> img.getImageUrl()).findFirst())
                    .orElse(null);

            result.add(RecommendationDto.builder()
                    .id(p.getId())
                    .name(p.getName())
                    .brand(p.getBrand())
                    .type(p.getType())
                    .price(p.getPrice())
                    .engineCapacityCc(p.getEngineCapacityCc())
                    .primaryImageUrl(primaryImageUrl)
                    .score(entry.getValue())
                    .scoreLabel(label)
                    .build());
        }
        return result;
    }
}