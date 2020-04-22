package cn.adcc.client.service.impl;

import cn.adcc.client.DO.MSApply;
import cn.adcc.client.repository.MSApplyRepository;
import cn.adcc.client.service.MSApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MSApplyServiceImpl implements MSApplyService {
    @Autowired
    private MSApplyRepository msApplyRepository;

    @Override
    public List<MSApply> findMSApplies() {
        List<MSApply> msApplies = msApplyRepository.findMSAppliesByOrderByApplyTime();
        return msApplies;
    }

    @Override
    public List<MSApply> findMSAppliesByUsername(String username) {
        List<MSApply> msApplies = msApplyRepository.findMSAppliesByUsernameOrderByApplyTime(username);
        return msApplies;
    }
}
