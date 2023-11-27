package com.testhuamou.vltava.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.testhuamou.vltava.domain.base.CommonException;
import com.testhuamou.vltava.domain.git.GitVO;
import com.testhuamou.vltava.domain.jenkins.JenkinsBuildVO;
import com.testhuamou.vltava.service.JenkinsService;
import com.testhuamou.vltava.utils.HttpUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Rob
 * @date Create in 11:40 AM 2019/11/26
 */
@Service("JenkinsService")
public class JenkinsServiceImpl implements JenkinsService {
    private static OkHttpClient client = new OkHttpClient();

//    private final static String CONFIG_URL = "http://testdeploy.testhuamou.com/job/%s/config.xml%s";
//    private final static Pattern CONFIG_PATTERN = Pattern.compile("(?<=configName>)((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}");
    private final static String BASE_URL = "http://testdeploy.testhuamou.com/job/%s/%s/api/json";
    private final static String KEY_CAUSE = "hudson.model.CauseAction";
    private final static String KEY_BUILD = "hudson.plugins.git.com.testhuamou.vltava.util.BuildData";
    private final static String CACHE_KEY = "vltava_jenkins_info";
    private final static Integer NOT_FOUND = 404;


    @Override
    public void getLatestBuildInfo(JenkinsBuildVO jenkinsBuildVO) {
        JSONObject respObj = null;
        try {
            respObj = JSON.parseObject(get(jenkinsBuildVO, "lastSuccessfulBuild").body().string());
        } catch (IOException e) {
            e.printStackTrace();
            throw new CommonException(e.getMessage(), e.getStackTrace());
        }
        for (Object obj : respObj.getJSONArray("actions")) {
            JSONObject action = JSON.parseObject(JSON.toJSONString(obj));
            if (KEY_CAUSE.equals(action.getString("_class"))) {
                jenkinsBuildVO.setBuilder(action.getJSONArray("causes").getJSONObject(0).getString("userName"));
            }
            if (KEY_BUILD.equals(action.getString("_class"))) {
                jenkinsBuildVO.setGitVersion(action.getJSONObject("lastBuiltRevision").getString("SHA1"));
                String branchName = action.getJSONObject("lastBuiltRevision").getJSONArray("branch").getJSONObject(0).getString("name");
                jenkinsBuildVO.setBranch(branchName.substring(branchName.lastIndexOf("/") + 1, branchName.length()));
            }
        }
        jenkinsBuildVO.setBuildTime(respObj.getDate("timestamp"));
        jenkinsBuildVO.setLastSuccessfulBuild(respObj.getInteger("number"));
    }

    @Override
    public void saveCache(JenkinsBuildVO jenkinsBuildVO) {

    }

    @Override
    public JenkinsBuildVO getCache(Integer systemId) {
        return null;
    }

    @Override
    public Boolean projectExists(String jobName) {
        Response response = get(jobName, "");
        return !NOT_FOUND.equals(response.code());
    }

    private Response get(JenkinsBuildVO jenkinsBuildVO, String apiPath) {
        return get(jenkinsBuildVO.getJobName(), apiPath);
    }

    private Response get(String jobName, String apiPath) {
        return get(BASE_URL, jobName, apiPath);
    }

    private Response get(String inputUrl, String jobName, String apiPath){
        String url = String.format(inputUrl, jobName, apiPath);
        Request request = new Request.Builder().url(url).get().build();
        try {
            return client.newCall(request).execute();
        } catch (IOException e) {
            throw new CommonException(e.getMessage(), e.getStackTrace());
        }
    }


}
