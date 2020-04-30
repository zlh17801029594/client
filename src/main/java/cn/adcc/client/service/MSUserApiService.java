package cn.adcc.client.service;

import java.util.List;

public interface MSUserApiService {
    void onMSUserApi(List<Long> ids);

    void offMSUserApi(List<Long> ids);

    void delMSUserApi(List<Long> ids);
}
