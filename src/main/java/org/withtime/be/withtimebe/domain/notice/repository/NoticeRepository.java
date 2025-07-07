package org.withtime.be.withtimebe.domain.notice.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.withtime.be.withtimebe.domain.notice.entity.Notice;
import org.withtime.be.withtimebe.domain.notice.entity.enums.NoticeCategory;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

	@Query("""
		SELECT n FROM Notice n
		WHERE n.noticeCategory = :noticeCategory
			AND n.deletedAt IS NULL
		ORDER BY n.isPinned DESC, n.createdAt DESC
	""")
	Page<Notice> findNoticeListByNoticeCategory(
		@Param("noticeCategory") NoticeCategory noticeCategory,
		Pageable pageable
	);

	@Query("""
		SELECT n FROM Notice n
		WHERE n.noticeCategory = :noticeCategory
		  AND (
			n.title LIKE %:keyword%
			OR n.content LIKE %:keyword%
		  )
	      AND n.deletedAt IS NULL
		ORDER BY n.isPinned DESC, n.createdAt DESC
    """)
	Page<Notice> findNoticeListByNoticeCategoryAndKeyword(
		@Param("noticeCategory") NoticeCategory noticeCategory,
		@Param("keyword") String keyword,
		Pageable pageable
	);

	@Query("""
		SELECT n FROM Notice n
		WHERE n.noticeCategory = :noticeCategory
			AND n.deletedAt IS NOT NULL
		ORDER BY n.isPinned DESC, n.deletedAt ASC 
	""")
	Page<Notice> findTrashNoticeListByNoticeCategory(
		@Param("noticeCategory") NoticeCategory noticeCategory,
		Pageable pageable
	);

	Optional<Notice> findNoticeById(Long noticeId);
}
