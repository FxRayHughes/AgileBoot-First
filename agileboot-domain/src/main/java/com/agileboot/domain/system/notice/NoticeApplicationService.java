package com.agileboot.domain.system.notice;

import com.agileboot.common.core.page.PageDTO;
import com.agileboot.domain.common.command.BulkOperationCommand;
import com.agileboot.domain.system.notice.command.NoticeAddCommand;
import com.agileboot.domain.system.notice.command.NoticeUpdateCommand;
import com.agileboot.domain.system.notice.dto.NoticeDTO;
import com.agileboot.domain.system.notice.model.NoticeModel;
import com.agileboot.domain.system.notice.model.NoticeModelFactory;
import com.agileboot.domain.system.notice.query.NoticeQuery;
import com.agileboot.orm.system.entity.SysNoticeEntity;
import com.agileboot.orm.system.service.ISysNoticeService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author valarchie
 */
@Service
@RequiredArgsConstructor
public class NoticeApplicationService {

    @NonNull
    private ISysNoticeService noticeService;

    public PageDTO getNoticeList(NoticeQuery query) {
        Page<SysNoticeEntity> page = noticeService.getNoticeList(query.toPage(), query.toQueryWrapper());
        List<NoticeDTO> records = page.getRecords().stream().map(NoticeDTO::new).collect(Collectors.toList());
        return new PageDTO(records, page.getTotal());
    }


    public NoticeDTO getNoticeInfo(Long id) {
        SysNoticeEntity byId = noticeService.getById(id);
        return new NoticeDTO(byId);
    }


    public void addNotice(NoticeAddCommand addCommand) {
        NoticeModel noticeModel = NoticeModelFactory.loadFromAddCommand(addCommand, new NoticeModel());

        noticeModel.checkFields();

        noticeModel.insert();
    }


    public void updateNotice(NoticeUpdateCommand updateCommand) {
        NoticeModel noticeModel = NoticeModelFactory.loadFromDb(updateCommand.getNoticeId(), noticeService);
        noticeModel.loadUpdateCommand(updateCommand);

        noticeModel.checkFields();

        noticeModel.updateById();
    }

    public void deleteNotice(BulkOperationCommand<Integer> deleteCommand) {
        noticeService.removeBatchByIds(deleteCommand.getIds());
    }




}
