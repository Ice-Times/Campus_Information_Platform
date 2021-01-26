package com.example.campusinformationplatform;

public class Status {
    public static final String SignUp_State = "Send_SignUp_Msg";// 客户端发送注册信息

    public static final String SignIn_State = "Send_SignIn_Msg";// 向服务器发送登录信息

    public static final String Re_SignUp_Success = "SignUp_Success";// 注册成功
    public static final String Re_SignIn_Success = "SignIn_Success";// 登录成功

    public static final String Re_SignUp_UserName_Repeat_Err = "UserName_Repeat_Err";// 用户名存在注册失败

    public static final String Re_SignIn_User_Not_Exist_Err = "User_Not_Exist_Err";// 用户名存在登陆失败

    public static final String Re_SignIn_User_State_Locked_Err = "User_State_Locked_Err";// 用户被封禁

    public static final String User_State_Activate="activate";
    public static final String User_State_Locked="locked";

}
