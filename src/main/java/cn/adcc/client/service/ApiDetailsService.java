package cn.adcc.client.service;

import cn.adcc.client.DTO.ApiDetailsDto;

public interface ApiDetailsService {
    ApiDetailsDto findById(Long id);
}
