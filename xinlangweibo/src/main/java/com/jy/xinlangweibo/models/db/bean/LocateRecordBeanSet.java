package com.jy.xinlangweibo.models.db.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by JIANG on 2016/10/15.
 */
public class LocateRecordBeanSet<T> implements Serializable{

        public String recordType;
        public List<T> recordList;
}
