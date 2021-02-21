package com.example.campusinformationplatform;

public class Status {
    public static final String SignUp_State = "Send_SignUp_Msg";// 客户端发送注册信息

    public static final String SignIn_State = "Send_SignIn_Msg";// 向服务器发送登录信息

    public static final String Release_State="Send_Release_Msg";// 向服务器发送用户想发布的信息

    public static final String GetInformtion_State="Send_GetInf_Msg";//向服务器请求用户发布的信息

    public static final String GetInformtionPic_State="Send_GetInfPic_Msg";//向服务器请求用户发布的信息图片

    public static final String GetInformtionDetails_State="Send_GetInfDetails_Msg";//向服务器请求用户发布的信息详情

    public static final String GetInformtionDetailsPic_State="Send_GetInfDetailsPic_Msg";//向服务器请求用户发布的信息详情

    public static final String SendMessage_State="Send_Message_Msg";//向服务器发送留言
    public static final String GetMessage_State="Send_GetMessage_Msg";//向服务器请求留言

    public static final String GetUserNumberOfInf="Send_GetUserNumberOfInf_Msg";


    public static final String Re_SignUp_Success = "SignUp_Success";// 注册成功
    public static final String Re_SignIn_Success = "SignIn_Success";// 登录成功

    public static final String Re_SignUp_UserName_Repeat_Err = "UserName_Repeat_Err";// 用户名存在注册失败

    public static final String Re_SignIn_User_Not_Exist_Err = "User_Not_Exist_Err";// 用户名存在登陆失败

    public static final String Re_SignIn_User_State_Locked_Err = "User_State_Locked_Err";// 用户被封禁

    public static final String Re_Message_Success = "Message_Success";// 留言成功

    public static final String Re_JDBC_Err = "JDBC_Err";// 数据库错误

    public static final String User_State_Activate="activate";//用户状态：正常
    public static final String User_State_Locked="locked";//用户状态：封禁



}
