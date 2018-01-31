package com.jkkc.gridmember.common;

/**
 * Created by Guan on 2017/12/27.
 */

public class Config {

    public static final String GRIDMAN_URL = "http://123.56.65.127:8888";

    public static final String LOGIN_URL = "/gridMemberApp//Login/login.do";

    public static final String UPLOADFILE_URL = "/gridMemberApp/uploadFile.do";

    public static final String FIND_RETURN_VISIT_LIST = "/gridMemberApp/findReturnVisitList.do";

    public static final String ADD_RETURN_URL = "/gridMemberApp/addreturnVisitInfo.do";

    // 环信推送消息到APP（sosID），app点开消息提示的时候查后台接口
    public static final String PUSHSOSMSG_URL = "/gridMemberApp/pushSosMsg.do";

    //出动接口
    public static final String STARTOFF_URL = "/gridMemberApp/startoff.do";

    //拒绝出动接口
    public static final String REFUSESTARTOFF_URL = "/gridMemberApp/refuseStartoff.do";

    //到达现场
    public static final String ARRIVE_URL = "/gridMemberApp/arrive.do";

    //回访
    public static final String RETURN_VISIT_URL = "/gridMemberApp/returnVisitList.do";


}
