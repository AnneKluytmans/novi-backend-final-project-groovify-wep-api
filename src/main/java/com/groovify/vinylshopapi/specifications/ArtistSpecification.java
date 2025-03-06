package com.groovify.vinylshopapi.specifications;

import com.groovify.vinylshopapi.models.Artist;
import com.groovify.vinylshopapi.utils.SpecificationUtils;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

public class ArtistSpecification {
    public static Specification<Artist> filterArtists(
            String country,
            String name,
            Integer minPopularity,
            Integer maxPopularity
    ) {
        return (Root<Artist> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            SpecificationUtils.addStringPredicate(predicates, cb, root.get("countryOfOrigin"), country, false);

            SpecificationUtils.addStringPredicate(predicates, cb, root.get("name"), name, false);

            if (minPopularity != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("popularity"), minPopularity));
            }

            if (maxPopularity != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("popularity"), maxPopularity));
            }

            return predicates.isEmpty() ? cb.conjunction() : cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
