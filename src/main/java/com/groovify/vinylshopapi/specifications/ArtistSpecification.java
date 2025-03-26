package com.groovify.vinylshopapi.specifications;

import com.groovify.vinylshopapi.models.Artist;
import com.groovify.vinylshopapi.utils.SpecificationUtils;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ArtistSpecification {
    public static Specification<Artist> filterArtists(
            String country,
            String name,
            LocalDate debutDateAfter,
            LocalDate debutDateBefore,
            Integer minPopularity,
            Integer maxPopularity,
            Boolean isGroup
    ) {
        return (Root<Artist> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            SpecificationUtils.addStringPredicate(predicates, cb, root.get("countryOfOrigin"), country, false);

            SpecificationUtils.addStringPredicate(predicates, cb, root.get("name"), name, false);

            SpecificationUtils.addDatePredicates(predicates, cb, root.get("debutDate"), null, debutDateBefore, debutDateAfter);

            if (minPopularity != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("popularity"), minPopularity));
            }

            if (maxPopularity != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("popularity"), maxPopularity));
            }

            SpecificationUtils.addBooleanPredicate(predicates, cb, root.get("isGroup"), isGroup);

            return predicates.isEmpty() ? cb.conjunction() : cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
