package org.withtime.be.withtimebe.domain.notice.service.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.domain.notice.dto.request.NoticeRequestDTO;
import org.withtime.be.withtimebe.domain.notice.entity.Notice;
import org.withtime.be.withtimebe.domain.notice.entity.enums.NoticeCategory;

public interface NoticeQueryService {
	Page<Notice> findNoticeList(Pageable pageable, NoticeCategory noticeCategory);
	Page<Notice> findNoticeListByKeyword(Pageable pageable, String keyword, NoticeCategory noticeCategory);
	Page<Notice> findTrashNoticeList(Pageable pageable, NoticeCategory noticeCategory);
	Notice findNoticeDetail(Long noticeId, Member member);
}
