package org.withtime.be.withtimebe.domain.notice.service.query;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.withtime.be.withtimebe.domain.notice.dto.request.NoticeRequestDTO;
import org.withtime.be.withtimebe.domain.notice.entity.Notice;
import org.withtime.be.withtimebe.domain.notice.repository.NoticeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeQueryServiceImpl implements NoticeQueryService {

	private final NoticeRepository noticeRepository;

	@Override
	public Page<Notice> findNoticeList(NoticeRequestDTO.FindNoticeList request) {
		return noticeRepository.findNoticeListByNoticeCategory(
			request.noticeCategory(), request.pageable());
	}

	@Override
	public Page<Notice> findNoticeListByKeyword(NoticeRequestDTO.FindNoticeListByKeyword request) {
		return noticeRepository.findNoticeListByNoticeCategoryAndKeyword(
			request.noticeCategory(), request.keyword(), request.pageable());
	}
}
