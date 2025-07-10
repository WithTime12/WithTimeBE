package org.withtime.be.withtimebe.domain.notice.service.command;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.domain.notice.converter.NoticeConverter;
import org.withtime.be.withtimebe.domain.notice.dto.request.NoticeRequestDTO;
import org.withtime.be.withtimebe.domain.notice.entity.Notice;
import org.withtime.be.withtimebe.domain.notice.repository.NoticeRepository;
import org.withtime.be.withtimebe.global.error.code.NoticeErrorCode;
import org.withtime.be.withtimebe.global.error.exception.NoticeException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class NoticeCommandServiceImpl implements NoticeCommandService{

	private final NoticeRepository noticeRepository;

	@Override
	public Notice createNotice(NoticeRequestDTO.CreateNotice request, Member member) {
		Notice notice = NoticeConverter.toNoticeEntity(request, member);
		return noticeRepository.save(notice);
	}

	@Override
	public Notice updateNotice(NoticeRequestDTO.UpdateNotice request, Long noticeId) {
		Notice notice = noticeRepository.findNoticeById(noticeId)
			.orElseThrow(() -> new NoticeException(NoticeErrorCode.NOTICE_NOT_FOUND));

		notice.updateFields(request);

		return notice;
	}

	@Override
	public void softDeleteNotice(Long noticeId) {
		Notice notice = noticeRepository.findNoticeById(noticeId)
			.orElseThrow(() -> new NoticeException(NoticeErrorCode.NOTICE_NOT_FOUND));

		notice.updateDeletedAt(LocalDateTime.now());
	}

	@Override
	public Notice recoverDeletedNotice(Long noticeId) {
		Notice notice = noticeRepository.findNoticeById(noticeId)
			.orElseThrow(() -> new NoticeException(NoticeErrorCode.NOTICE_NOT_FOUND));

		notice.updateDeletedAt(null);

		return notice;
	}
}
