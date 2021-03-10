package com.example.campusinformationplatform;

public class Status {
    public static final String Buffer_State = "Send_Buffer_Msg";// 客户端发送缓存信息

    public static final String SignUp_State = "Send_SignUp_Msg";// 客户端发送注册信息

    public static final String SignIn_State = "Send_SignIn_Msg";// 向服务器发送登录信息

    public static final String Release_State="Send_Release_Msg";// 向服务器发送用户想发布的信息

    public static final String GetInformtion_State="Send_GetInf_Msg";//向服务器请求用户发布的信息

    public static final String GetInformtionPic_State="Send_GetInfPic_Msg";//向服务器请求用户发布的信息图片

    public static final String GetInformtionDetails_State="Send_GetInfDetails_Msg";//向服务器请求用户发布的信息详情

    public static final String GetInformtionDetailsPic_State="Send_GetInfDetailsPic_Msg";//向服务器请求用户发布的信息详情

    public static final String SendMessage_State="Send_Message_Msg";//向服务器发送留言
    public static final String GetMessage_State="Send_GetMessage_Msg";//向服务器请求留言

    public static final String SendUpdateRelease_State="Send_UpdateRelease_Msg";//向服务器发送更新

    public static final String SendForgetPassword_State="Send_ForgetPassword_Msg";//向服务器请求更改密码
    public static final String Re_ForgetPassword_Username_Exist_State="Username_Exist";//用户存在

    public static final String SendForgetPasswordUpdate_State="Send_ForgetPasswordUpdate_Msg";//向服务器更改密码
    public static final String Favorite_State = "Send_Favorite_Msg";// 客户端发送收藏信息

    public static final String SendUpdateReleaseinf_State="Send_UpdateReleaseinf_Msg";//向服务器发送编辑请求

    public static final String GetUserNumberOfInf="Send_GetUserNumberOfInf_Msg";//获取信息数量

    public static final String GetUserselfRelease="Send_GetUserselfRelease_Msg";//向服务器获取用户发布的信息
    public static final String GetUserselfMessage="Send_GetUserselfMessage_Msg";//向服务器获取用户发布的信息

    public static final String GetUserselfFavorite="Send_GetUserselfFavorite_Msg";//向服务器获取用户sc

    public static final String GetUserHeadImg="Send_GetUserHeadImg_Msg";//向服务器获取用户头像

    public static final String DeleteUserRelease="Send_DelUserselfRelease_Msg";//删除发布
    public static final String DeleteUserMessage="Send_DelUserselfMessage_Msg";//删除留言
    public static final String DeleteUserFavorite="Send_DelUserselfFavorite_Msg";//删除sc

    public static final String Re_SignUp_Success = "SignUp_Success";// 注册成功
    public static final String Re_SignIn_Success = "SignIn_Success";// 登录成功

    public static final String Re_Update_Success ="Update_Success";//更新成功
    public static final String Re_Update_Err ="Update_Err";//更新失败

    public static final String Re_SignUp_UserName_Repeat_Err = "UserName_Repeat_Err";// 用户名存在注册失败

    public static final String Re_SignIn_User_Not_Exist_Err = "User_Not_Exist_Err";// 用户名存在登陆失败

    public static final String Re_SignIn_User_State_Locked_Err = "User_State_Locked_Err";// 用户被封禁
    public static final String Re_InfisEnd = "InfisEnd";//信息已完全读完

    public static final String Re_Favorite_Exist = "UserFavorite_Exist";// 用户已收藏过
    public static final String Re_Favorite_Success = "Favorite_Success";// 收藏成功

    public static final String Re_Message_Success = "Message_Success";// 留言成功

    public static final String Re_JDBC_Err = "JDBC_Err";// 数据库错误

    public static final String User_State_Activate="activate";//用户状态：正常
    public static final String User_State_Locked="locked";//用户状态：封禁



}
