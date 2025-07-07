package org.withtime.be.withtimebe.domain.notice.service.query;

import org.springframework.data.domain.Page;
import org.withtime.be.withtimebe.domain.notice.dto.request.NoticeRequestDTO;
import org.withtime.be.withtimebe.domain.notice.entity.Notice;

public interface NoticeQueryService {
	Page<Notice> findNoticeList(NoticeRequestDTO.FindNoticeList request);
	Page<Notice> findNoticeListByKeyword(NoticeRequestDTO.FindNoticeListByKeyword request);
	Page<Notice> findTrashNoticeList(NoticeRequestDTO.FindNoticeList request);
	Notice findNoticeDetail(NoticeRequestDTO.FindNoticeDetail request);
}
