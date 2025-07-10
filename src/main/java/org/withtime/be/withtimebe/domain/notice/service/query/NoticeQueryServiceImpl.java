package org.withtime.be.withtimebe.domain.notice.service.query;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.domain.member.entity.enums.Role;
import org.withtime.be.withtimebe.domain.notice.dto.request.NoticeRequestDTO;
import org.withtime.be.withtimebe.domain.notice.entity.Notice;
import org.withtime.be.withtimebe.domain.notice.repository.NoticeRepository;
import org.withtime.be.withtimebe.global.error.code.AuthErrorCode;
import org.withtime.be.withtimebe.global.error.code.NoticeErrorCode;
import org.withtime.be.withtimebe.global.error.exception.AuthException;
import org.withtime.be.withtimebe.global.error.exception.NoticeException;

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

	@Override
	public Page<Notice> findTrashNoticeList(NoticeRequestDTO.FindNoticeList request) {
		return noticeRepository.findTrashNoticeListByNoticeCategory(
			request.noticeCategory(), request.pageable()
		);
	}

	@Override
	public Notice findNoticeDetail(NoticeRequestDTO.FindNoticeDetail request) {

		Notice notice = noticeRepository.findNoticeById(request.noticeId())
			.orElseThrow(() -> new NoticeException(NoticeErrorCode.NOTICE_NOT_FOUND));
		Member member = request.member();

		// 삭제된 게시글을 USER가 보려는 경우 처리
		if(notice.getDeletedAt() != null) {
			if(member == null || !member.getRole().equals(Role.ADMIN))
				throw new AuthException(NoticeErrorCode.DELETED_NOTICE_FORBIDDEN_ACCESS);
		}

		return notice;
	}
}
