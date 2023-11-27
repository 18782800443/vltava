package com.testhuamou.vltava.service;

import com.testhuamou.vltava.domain.mock.RegisterVO;

import java.util.List;

/**
 * @author Rob
 * @date Create in 3:07 下午 2020/8/21
 */
public interface AgentService {

    /**
     * 异步上传, 内部自动重试9次，服务器重启用
     * @param taskId mockId
     */
    void uploadDataAsync(Integer taskId, RegisterVO registerVO);

    /**
     * 更新数据到agent
     * @param taskId
     */
    void updateData(Integer taskId);

    /**
     * 更新状态到agent
     * @param taskId
     * @return 更新失败的
     */
    List<RegisterVO> updateDataAllGroup(Integer taskId);

    /**
     * 更新状态到agent
     * @param registerVOList, taskId
     * @return 更新失败的
     */
    List<RegisterVO> updateStatusAllGroup( Integer taskId);

    /**
     * 更新状态到agent
     * @param taskId mockId
     */

    void updateSuccessRegisterVoStatus(List<RegisterVO> registerVOList, Integer taskId);

    /**
     * 更新状态到agent
     * @param taskId mockId
     */
    void updateStatus(Integer taskId);

    /**
     * 健康检查
     */
    void healthCheck(String ip, String port);

    /**
     * 判断是否能更改状态
     * @param taskId
     * @return
     */
    Boolean canUpdate(Integer taskId);
}
