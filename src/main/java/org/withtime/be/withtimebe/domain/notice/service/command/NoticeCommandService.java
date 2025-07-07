package org.withtime.be.withtimebe.domain.notice.service.command;

import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.domain.notice.dto.request.NoticeRequestDTO;
import org.withtime.be.withtimebe.domain.notice.entity.Notice;

public interface NoticeCommandService {
	Notice createNotice(NoticeRequestDTO.CreateNotice request, Member member);
	Notice updateNotice(NoticeRequestDTO.UpdateNotice request);
	void softDeleteNotice(Long noticeId);
}
