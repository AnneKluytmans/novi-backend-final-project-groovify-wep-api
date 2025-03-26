package com.groovify.vinylshopapi.specifications;

import com.groovify.vinylshopapi.models.Artist;
import com.groovify.vinylshopapi.models.VinylRecord;
import com.groovify.vinylshopapi.models.VinylRecordStock;
import com.groovify.vinylshopapi.utils.SpecificationUtils;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VinylRecordSpecification {

    public static Specification<VinylRecord> filterVinylRecords(
            String title,
            String genre,
            String artist,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            LocalDate releasedAfter,
            LocalDate releasedBefore,
            Boolean isLimitedEdition,
            Boolean isAvailable
    ) {
        return (Root<VinylRecord> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            SpecificationUtils.addStringPredicate(predicates, cb, root.get("title"), title, false);

            SpecificationUtils.addStringPredicate(predicates, cb, root.get("genre"), genre, false);

            if (artist != null && !artist.isBlank()) {
                Join<VinylRecord, Artist> artistJoin = root.join("artist");
                SpecificationUtils.addStringPredicate(predicates, cb, artistJoin.get("name"), artist, false);
            }

            SpecificationUtils.addPricePredicates(predicates, cb, root.get("price"), minPrice, maxPrice);

            SpecificationUtils.addDatePredicates(predicates, cb, root.get("releaseDate"), null, releasedBefore, releasedAfter);

            SpecificationUtils.addBooleanPredicate(predicates, cb, root.get("isLimitedEdition"), isLimitedEdition);

            if (isAvailable != null) {
                Join<VinylRecord, VinylRecordStock> stockJoin = root.join("stock");
                if (isAvailable) {
                    predicates.add(cb.greaterThan(stockJoin.get("amountInStock"), 0));
                } else {
                    predicates.add(cb.equal(stockJoin.get("amountInStock"), 0));
                }
            }

            return predicates.isEmpty() ? cb.conjunction() : cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
