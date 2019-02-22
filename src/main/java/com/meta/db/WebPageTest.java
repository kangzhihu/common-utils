package com.meta.db;

import com.alibaba.common.lang.Paginator;
import com.meta.db.PageList;

/**
* web分页查询简单示例
*/
public class WebPageTest{

public PageList queryByPage(
            PageQueryRequest pageQueryRequest) {
        Paginator paginator = new Paginator();
        paginator.setItemsPerPage(pageQueryRequest.getPageSize());
        paginator.setPage(pageQueryRequest.getPageNum());
        //查询总数
        int size = (int) cardAccountRefundOrderDAO.queryCount(pageQueryRequest.getDailycutDate(),
                pageQueryRequest.getCardNo(),
                pageQueryRequest.getCardName(),
                pageQueryRequest.getStatusList(), pageQueryRequest.getRefundStartDate(),
                pageQueryRequest.getRefundEndDate());
        paginator.setItems(size);

        PageList pageList = new PageList();
        pageList.setPaginator(paginator);

        List<XxxDO> xxxDOs = null;
        if (paginator.getBeginIndex() <= paginator.getItems()) {
            Integer startRow = new Integer(paginator.getOffset());
            xxxDOs = cardAccountRefundOrderDAO.queryCardAccountRefundOrder(
                    pageQueryRequest.getDailycutDate(),
                    pageQueryRequest.getCardNo(),
                    pageQueryRequest.getCardName(),
                    pageQueryRequest.getStatusList(), pageQueryRequest.getRefundStartDate(),
                    pageQueryRequest.getRefundEndDate(),
                    startRow,
                    pageQueryRequest.getPageSize());
        }
        pageList.addAll(xxxDOs);
        return pageList;
    }

}
