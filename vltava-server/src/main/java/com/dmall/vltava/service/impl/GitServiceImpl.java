package com.dmall.vltava.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dmall.vltava.cache.CacheHandler;
import com.dmall.vltava.domain.base.CommonException;
import com.dmall.vltava.domain.git.*;
import com.dmall.vltava.service.GitService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Rob
 * @date Create in 5:22 PM 2019/11/20
 */
@Service("GitService")
public class GitServiceImpl implements GitService {
    private static final Logger logger = LoggerFactory.getLogger(GitServiceImpl.class);

    private final static String TOKEN = "RMe2fdSoqPvdkj_8KTtY";
    private final static String BASE_URL = "http://gitlab.dmall.com/api/v4/";
    private final static String GIT_MAP_CACHE_KEY = "gitMap";
    private final static String HTML_TITLE = "<!DOCTYPE html>";
    private final static Pattern START_LINE_PATTERN = Pattern.compile("(?<=@@ -)\\d+(?<!,)");

    private static OkHttpClient client = new OkHttpClient();

    @Override
    public Map<String, Integer> getProjectMap() {
        Map<String, Integer> resultMap;
        Object cache = CacheHandler.get(GIT_MAP_CACHE_KEY);
        if (cache == null) {
            resultMap = new HashMap<>();
            Response response = get("project/");
            String resp = null;
            try {
                resp = response.body().string();
            } catch (IOException e) {
                throw new CommonException(e.getMessage(), e.getStackTrace().toString());
            }
            List<JSONObject> respList = JSON.parseArray(resp, JSONObject.class);
            for (JSONObject project : respList) {
                resultMap.put(project.getString("name"), project.getIntValue("id"));
            }
            CacheHandler.set(GIT_MAP_CACHE_KEY, resultMap);
        } else {
            resultMap = (Map<String, Integer>) cache;
        }
        return resultMap;
    }

    @Override
    public GitDiffVO getIncreasedCode(GitVO gitVO) {
        String path = String.format("project/%s/repository/compare?straight=true&from=%s&to=%s", gitVO.getGitId(), gitVO.getFromVersion(), gitVO.getToVersion());
        Response response = get(path);
        String resp = null;
        try {
            resp = response.body().string();
        } catch (IOException e) {
            throw new CommonException(e.getMessage(), e.getStackTrace().toString());
        }
        return this.diffConvert(resp);
    }

    @Override
    public List<GitCommitVO> getProjectCommits(GitVO gitVO) {
        String path = String.format("project/%s/repository/commits?ref_name=%s",gitVO.getGitId(),gitVO.getBranchName());
        Response response = get(path);
        String resp = null;
        try {
            resp = response.body().string();
        } catch (IOException e) {
            throw new CommonException(e.getMessage(), e.getStackTrace().toString());
        }
        return commitsConvert(resp);
    }

    @Override
    public Boolean getGitInfo(GitVO gitVO) {
        String path = "/search?scope=projects&search=" + gitVO.getGitName();
        Response response = get(path);
        String resp = null;
        try {
            resp = response.body().string();
            for (JSONObject obj: JSON.parseArray(resp, JSONObject.class)){
                if (obj.getString("name").equals(gitVO.getGitName())){
                    gitVO.setGitId(obj.getInteger("id"));
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            throw new CommonException(e.getMessage(), e.getStackTrace().toString());
        }
    }

    /**
     * 通用gitlab api http请求
     *
     * @param path api路径
     */
    private Response get(String path) {
        Request request = new Request.Builder().url(BASE_URL + path)
                .header("PRIVATE-TOKEN", TOKEN)
                .get().build();
        try {
            return client.newCall(request).execute();
        } catch (IOException e) {
            throw new CommonException(e.getMessage(), e.getStackTrace().toString());
        }

    }

    private List<GitCommitVO> commitsConvert(String respCommit){
        List<GitCommitVO> commitList = new ArrayList<>();
        for (JSONObject commit : JSON.parseArray(respCommit).toJavaList(JSONObject.class)) {
            GitCommitVO gitCommitVO = new GitCommitVO();
            gitCommitVO.setAuthor(commit.getString("author_name"));
            gitCommitVO.setCommitId(commit.getString("id"));
            gitCommitVO.setCommitTime(commit.getDate("created_at"));
            gitCommitVO.setMessage(commit.getString("message"));
            gitCommitVO.setTitle(commit.getString("title"));
            commitList.add(gitCommitVO);
        }
        return commitList;
    }

    private GitDiffVO diffConvert(String respDiff) {
        if (respDiff.startsWith(HTML_TITLE)) {
            throw new CommonException("gitlab异常，返回结果为html，请检查网络、环境");
        }
        JSONObject respObj = JSON.parseObject(respDiff);
        GitDiffVO result = new GitDiffVO();
        List<GitCommitVO> commitList = new ArrayList<>();
        List<GitDiffDetailVO> diffDetailList = new ArrayList<>();
        for (Object commitObj : respObj.getJSONArray("commits")) {
            JSONObject commit = JSON.parseObject(JSON.toJSONString(commitObj));
            GitCommitVO gitCommitVO = new GitCommitVO();
            gitCommitVO.setAuthor(commit.getString("author_name"));
            gitCommitVO.setCommitId(commit.getString("id"));
            gitCommitVO.setCommitTime(commit.getDate("created_at"));
            gitCommitVO.setMessage(commit.getString("message"));
            gitCommitVO.setTitle(commit.getString("title"));
            commitList.add(gitCommitVO);
        }
        for (Object diffObj : respObj.getJSONArray("diffs")) {
            JSONObject diff = JSON.parseObject(JSON.toJSONString(diffObj));
            GitDiffDetailVO gitDiffDetailVO = new GitDiffDetailVO();
            gitDiffDetailVO.setDeletedFile(diff.getBoolean("deleted_file"));
            gitDiffDetailVO.setNewFile(diff.getBoolean("new_file"));
            gitDiffDetailVO.setNewPath(diff.getString("new_path"));
            gitDiffDetailVO.setOldPath(diff.getString("old_path"));
            gitDiffDetailVO.setRenamedFile(diff.getBoolean("renamed_file"));
            gitDiffDetailVO.setCodeBlockList(this.codeBlockConvert(diff.getString("diff")));
            gitDiffDetailVO.setClassDefine(this.getClassDefine(diff.getString("diff")));
            diffDetailList.add(gitDiffDetailVO);
        }
        result.setCommitList(commitList);
        result.setDetailList(diffDetailList);
        return result;
    }

    private List<GitCodeBlock> codeBlockConvert(String diffStr) {
        List<GitCodeBlock> result = new ArrayList<>();
        List<String> codeBlock = new ArrayList<>();
        List<String> lineList = Arrays.asList(diffStr.split("\n"));
        // 去掉前两行无效str
        for (String line : lineList.subList(2, lineList.size())) {
            if (line.startsWith("@@")) {
                // 遇到@@表示开始新一段代码更改，下一行才是代码
                if (codeBlock.size() != 0) {
                    result.add(this.singleCodeBlockConvert(codeBlock));
                    codeBlock = new ArrayList<>();
                }
            }
            codeBlock.add(line);
        }
        result.add(this.singleCodeBlockConvert(codeBlock));
        return result;
    }

    private GitCodeBlock singleCodeBlockConvert(List<String> codeBlock) {
        GitCodeBlock cls = new GitCodeBlock();
        Integer begin = getBeginLine(codeBlock.get(0));
        cls.setBeginLine(begin);
        cls.setLineList(lineConvert(codeBlock, begin));
        return cls;
    }

    private List<GitLineVO> lineConvert(List<String> codeBlock, Integer begin) {
        List<GitLineVO> result = new ArrayList<>();
        // 跳过第一行@@定义
        Integer newLineCount = 0;
        Integer oldLineCount = 0;
        for (String line : codeBlock) {
            GitLineVO gitLineVO = new GitLineVO();
            gitLineVO.setLocate(line.startsWith("@@"));
            gitLineVO.setNewAdd(line.startsWith("+"));
            gitLineVO.setNewDelete(line.startsWith("-"));
            gitLineVO.setOrigin(line.startsWith(" "));
            gitLineVO.setCode(gitLineVO.getLocate() ? line : line.substring(1, line.length()));
            if (gitLineVO.getOrigin()){
                gitLineVO.setNewLineNum(begin + newLineCount);
                gitLineVO.setOldLineNum(begin + oldLineCount);
                newLineCount++;
                oldLineCount++;
            }
            if (gitLineVO.getNewAdd()) {
                gitLineVO.setNewLineNum(begin + newLineCount);
                newLineCount++;
            }
            if (gitLineVO.getNewDelete()) {
                gitLineVO.setOldLineNum(begin + oldLineCount);
                oldLineCount++;
            }
            result.add(gitLineVO);
        }
        return result;
    }

    private Integer getBeginLine(String line) {
        Matcher matcher = START_LINE_PATTERN.matcher(line);
        if (matcher.find()) {
            return Integer.valueOf(matcher.group(0));
        } else {
            throw new CommonException("无法获取diff变更其实行，请检查数据");
        }
    }

    private String getClassDefine(String diffStr) {
        for (String line : diffStr.split("\n")) {
            if (line.startsWith("@@")) {
                return line = line.substring(line.lastIndexOf("@") + 1, line.length());
            }
        }
        // 理论上不会
        throw new CommonException("缺少classDefine");
    }
}
