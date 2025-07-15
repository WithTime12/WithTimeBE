package org.withtime.be.withtimebe.domain.faq.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.withtime.be.withtimebe.domain.faq.entity.Faq;
import org.withtime.be.withtimebe.domain.faq.entity.enums.FaqCategory;

public interface FaqRepository extends JpaRepository<Faq, Long> {

	@Query("""
		SELECT f FROM Faq f
		WHERE f.faqCategory = :faqCategory
			AND f.deletedAt IS NULL
		ORDER BY f.createdAt DESC
	""")
	Page<Faq> findFaqListByFaqCategory(
		@Param("faqCategory") FaqCategory faqCategory,
		Pageable pageable
	);

	@Query("""
		SELECT f FROM Faq f
		WHERE f.faqCategory = :faqCategory
		  AND (
			f.title LIKE %:keyword%
			OR f.content LIKE %:keyword%
		  )
	      AND f.deletedAt IS NULL
		ORDER BY f.createdAt DESC
    """)
	Page<Faq> findFaqListByFaqCategoryAndKeyword(
		@Param("faqCategory") FaqCategory faqCategory,
		@Param("keyword") String keyword,
		Pageable pageable
	);
}
