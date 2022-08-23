package com.dmall.vltava.util;

import tk.mybatis.mapper.version.NextVersion;
import tk.mybatis.mapper.version.VersionException;

import java.util.Date;


/**
 * @author Rob
 * @date Create in 11:33 AM 2020/3/12
 */
public class ExtendedNextVersion implements NextVersion {
    @Override
    public Object nextVersion(Object current) throws VersionException {
        if (current == null) {
            throw new VersionException("当前版本号为空!");
        }
        if (current instanceof Integer) {
            return (Integer) current + 1;
        } else if (current instanceof Long) {
            return (Long) current + 1L;
        } else if (current instanceof Date) {
            return new Date(System.currentTimeMillis());
        }
        else {
            throw new VersionException("扩展后的NextVersion 只支持 Integer 和 Long 和Date 类型的版本号，如果有需要请自行扩展!");
        }    }
}
