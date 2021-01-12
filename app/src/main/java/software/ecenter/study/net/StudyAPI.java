package software.ecenter.study.net;


import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * 请求类
 * 1.所有请求都在onResume中执行
 */
public interface StudyAPI {

    /**
     * 请求示例写法
     */
    /*new RetroFactory(this, RetroFactory.getRetroFactory().update(paramMap))
            .handleResponse(new RetroFactory.ResponseListener<String>() {
		@Override
		public void onSuccess(String response) {
			//更新数据
			Update update= ParseUtil.parUpdate(response);
			if(update.getDatabody().getStatus().equals(ConstUtils.FORCEUPDATE)){//强制更新
				forceUdDiaog(update.getDatabody().getMsg(),update.getDatabody().getDownloadUrl());
			}else{//手动更新
				handUdDialog(update.getDatabody().getMsg(),update.getDatabody().getDownloadUrl());
			}
		}

		@Override
		public void onFail() {

		}
	});*/

    /**
     * Post示例
     */

    @FormUrlEncoded
    @POST("weixin/query")
//方法地址
    Call<String> demoPostField(@Field("name") String name, @Field("age") String age);//方法参数,@Field或@FieldMap

    @FormUrlEncoded
    @POST("business_sale.api?getEmployeeWorkDetail")
    Call<String> demoPostFieldMap(@FieldMap Map<String, String> map);


    /**
     * Get示例
     */
    @Headers("apikey: c4fea2e648f7b6aff142c9deec70c7d9")
    @GET("business_order.api")
//方法地址
    Call<String> demoGet(@Query("name") String name, @Query("keyword") String keyword,
                         @Query("page") String page, @Query("rows") String rows,
                         @Query("type") String type);//方法参数,@Query或@QueryMap


    //热更新补丁包下载
    @GET
    Call<ResponseBody> hotUpdate(@Url String fileUrl);

    /**
     * 上传文件示例
     */

    // 上传单个文件
    @Multipart
    @POST("")
    Call<ResponseBody> uploadFile(@Part("description") RequestBody description, @Part MultipartBody.Part file);

    // 上传多个文件
    @Multipart
    @POST("upload")
    Call<ResponseBody> uploadMultipleFiles(@Part("description") RequestBody description, @Part MultipartBody.Part file1,
                                           @Part MultipartBody.Part file2);

    //eg:上传文件写法
    // 创建文件的part (photo, video, ...)
    //文件一
/*	MultipartBody.Part body1 = RetroFactory.prepareFilePart("video", file1);
    //文件二
	MultipartBody.Part body2 = RetroFactory.prepareFilePart("thumbnail", file2);
	//文件描述
	RequestBody description = RetroFactory.createPartFromString("文件描述");
	new RetroFactory<ResponseBody>(this,
			//返回api类
			RetroFactory.getStringService()
			//返回Call
			.demoGet(RetroFactory.KEY,"宫保鸡丁"))
			//请求回调
			.handleResponse(new RetroFactory.ResponseListener<ResponseBody>() {//注意这里也要泛型

		@Override
		public void onSuccess(ResponseBody ResponseBody) {

		}

		@Override
		public void onFail() {

		}
	});*/


    /*干货集中营分类数据api(demo)*/
    @GET("data/福利/{count}/{page}")
//Path用于get方式中动态替换url占位符
    Call<String> ganhuoDataGet(@Path("count") int count, @Path("page") int page);

    /**
     *
     */

    //	获取手机验证码(登录)
    @FormUrlEncoded
    @POST("version2/platform/loginPhoneCode")
    Call<String> getVcode(@FieldMap Map<String, String> map);


    //登录
    @FormUrlEncoded
    @POST("version2/oauth/token")
    Call<String> login(@FieldMap Map<String, String> map);

    //新登录
    @POST("V3/version2/userInfos/login")
    Call<String> login(@Body RequestBody body);


    //注册
    @POST("V3/version2/userInfos/register")
    Call<String> register(@Body RequestBody body);

    //更新提示
    @FormUrlEncoded
    @POST("version2/versions/getNewVersion")
    Call<String> upDataVewsion(@FieldMap Map<String, Object> map);

    //检测手机号是否重复
    @FormUrlEncoded
    @POST("V3/version2/userInfos/checkPhone")
    Call<String> checkPhone(@FieldMap Map<String, String> map);

    //获取验证码
    @POST("V3/version2/userInfos/getCheckCode")
    Call<String> getCheckCode(@Body RequestBody body);

    //检查验证码、密码是否符合规范
    @POST("V3/version2/userInfos/checkCodePwd")
    Call<String> checkCodePwd(@Body RequestBody body);

    //忘记密码
    @POST("V3/version2/userInfos/forgetPassword")
    Call<String> forgetPassword(@Body RequestBody body);


    //首页我的书包
    @Deprecated
    @FormUrlEncoded
    @POST("version2/classroom/getUserBag")
    Call<String> homeGetUserBag(@FieldMap Map<String, String> map);

    //首页我的书包
    @POST("version2/classroom/getUserBag")
    Call<String> homeGetUserBag(@Body RequestBody body);

    //首页列表
    @POST("version2/classroom/classIndex")
    Call<String> home(@Body RequestBody body);

    //首页图书更多
    @POST("version2/classroom/classIndex_book")
    Call<String> homeBook(@Body RequestBody body);

    //首页课程更多
    @POST("version2/classroom/classIndex_curriculum")
    Call<String> homeCurriculum(@Body RequestBody body);

    //首页套系更多
    @POST("version2/classroom/classIndex_package")
    Call<String> homepackage(@Body RequestBody body);

    //搜索列表
    @POST("version2/classroom/search")
    Call<String> search(@Body RequestBody body);

    //搜索图书更多
    @POST("version2/classroom/search_book")
    Call<String> searchBook(@Body RequestBody body);

    //搜索资源更多
    @POST("version2/classroom/search_resource")
    Call<String> searchResource(@Body RequestBody body);

    //搜索答疑更多
    @POST("version2/classroom/search_question")
    Call<String> searchQuestion(@Body RequestBody body);

    //首页广告详情
    @FormUrlEncoded
    @POST("version2/classroom/getBannerDetail")
    Call<String> homeBannerDetai(@FieldMap Map<String, String> map);

    //首页图书详情
    @FormUrlEncoded
    @POST("version2/classroom/getBookDetail")
    Call<String> homeBookDetail(@FieldMap Map<String, String> map);

    //加入书包
    @FormUrlEncoded
    @POST("version2/classroom/addBag")
    Call<String> homeBookAddBag(@FieldMap Map<String, String> map);

    //首页课程详情
    @FormUrlEncoded
    @POST("v4/classroom/getCurriculumDetail")
    Call<String> homeCurriculumDetail(@FieldMap Map<String, String> map);

    //精品课程详情批次通知
//    @FormUrlEncoded
    @POST("v4/classroom/getNewBatchNotice")
    Call<String> getCurriculumSecondNofice(@Body RequestBody body);

    //首页套系详情
    @FormUrlEncoded
    @POST("version2/classroom/getPackageDetail")
    Call<String> homePackageDetail(@FieldMap Map<String, String> map);

    //首页章节资源列表
    @FormUrlEncoded
    @POST("version2/classroom/getChapterDetail")
    Call<String> homeChapterDetail(@FieldMap Map<String, String> map);

    //精品课程资源列表
    @FormUrlEncoded
    @POST("v4/classroom/getCurriculumChapterDetail")
    Call<String> homeCurriculumChapterDetail(@FieldMap Map<String, String> map);


    //绑定所属图书
    @POST("version2/classroom/bindSecurityCode")
    Call<String> bindSecurityCode(@Body RequestBody body);

//    //绑定所属图书
//    @FormUrlEncoded
//    @POST("classroom/checkSecurityCode")
//    Call<String> checkSecurityCode(@FieldMap Map<String, String> map);

    //绑定所属图书
    @POST("version2/classroom/checkSecurityCode")
    Call<String> checkSecurityCode(@Body RequestBody body);

    //新绑定所属图书
    @POST("version2/classroom/checkNewSecurityCode")
    Call<String> checkNewSecurityCode(@Body RequestBody body);

    //备选评论
    @POST("version2/classroom/getDefaultComments")
    Call<String> getDefaultComments();

    //讲座备选评论
    @POST("version2/classroom/getDefaultLectureResourceComments")
    Call<String> getDefaultLectureComments();

    //素质教育备选评论
    @POST("version2/classroom/getDefaultQualityResourceComments")
    Call<String> getDefaultQualityComments();

    //资源详情
    @FormUrlEncoded
    @POST("v4/classroom/getResourceDetail")
    Call<String> getResourceDetail(@FieldMap Map<String, String> map);

    //课后突破关联id
    @FormUrlEncoded
    @POST("version2/classroom/getAfterClassId")
    Call<String> getAfterClassId(@FieldMap Map<String, String> map);

    //作者详情
//    @FormUrlEncoded
    @POST("version2/classroom/getAuthorDetail")
    Call<String> getAuthDetail(@Body RequestBody body);

    //保存用户查看资源
    @FormUrlEncoded
    @POST("version2/classroom/saveUserCheckResource")
    Call<String> saveUserCheckResource(@FieldMap Map<String, String> map);

    //获得评论
    @POST("version2/classroom/getCommentList")
    Call<String> getCommentList(@Body RequestBody body);

    //点赞
    @POST("version2/classroom/submitResourceThumbUp")
    Call<String> submitResourceThumbUp(@Body RequestBody body);

    //去评论
    @POST("version2/classroom/submitResourceComment")
    Call<String> submitResourceComment(@Body RequestBody body);

    //收藏
    @FormUrlEncoded
    @POST("version2/classroom/submitResourceCollection")
    Call<String> submitResourceCollection(@FieldMap Map<String, String> map);

    //取消收藏
    @FormUrlEncoded
    @POST("version2/mine/removeCollection")
    Call<String> removeCollection(@FieldMap Map<String, String> map);


    //在线获取联系题目
    @POST("version2/classroom/getResourceExercise")
    Call<String> getResourceExercise(@Body RequestBody body);

    //在线练习提交
    @POST("version2/classroom/resourceExerciseSubmit")
    Call<String> resourceExerciseSubmit(@Body RequestBody body);

    //加入习题集
    @FormUrlEncoded
    @POST("version2/classroom/submitExercise")
    Call<String> submitExercise(@FieldMap Map<String, String> map);

    //加入习题集
    @POST("version2/classroom/submitExercise")
    Call<String> submitExercise(@Body RequestBody body);

    //获取资源获取信息
    @POST("v4/classroom/getBuyResource")
    Call<String> getBuyResource(@Body RequestBody body);

    //教师知识点获取
    @FormUrlEncoded
    @POST("version2/classroom/getKnowledgePoint")
    Call<String> getKnowledgePoint(@FieldMap Map<String, String> map);

    //教师上传资源
    @Multipart
    @POST("version2/classroom/uploadResource")
    Call<String> uploadResource(@PartMap() Map<String, String> map, @Part() List<MultipartBody.Part> parts);

    //新的教师上传资源
    @POST("version2/classroom/uploadResource")
    Call<String> uploadResource(@Body RequestBody body);

    //习题查看
    @FormUrlEncoded
    @POST("version2/classroom/showExercise")
    Call<String> showExercise(@FieldMap Map<String, String> map);

    //答疑模块 -------------------------------------------------
    //答疑获取图书信息
    @POST("version2/question/getQuestionBookList")
    Call<String> getQuestionBookList(@Body RequestBody body);

    //章获取接口
    @FormUrlEncoded
    @POST("version2/question/getQuestionChapterList")
    Call<String> getQuestionChapterList(@FieldMap Map<String, String> map);

    //获取精品课程接口
    @POST("version2/question/getCurriculums")
    Call<String> getCurriculums(@Body RequestBody body);

    //节获取接口
    @FormUrlEncoded
    @POST("version2/question/getQuestionSectionList")
    Call<String> getQuestionSectionList(@FieldMap Map<String, String> map);

    //题获取接口
    @FormUrlEncoded
    @POST("version2/question/getQuestionTopicList")
    Call<String> getQuestionTopicList(@FieldMap Map<String, String> map);

    //资源界面进入答疑
    @FormUrlEncoded
    @POST("version2/question/fromResource")
    Call<String> fromResource(@FieldMap Map<String, Integer> map);

    //答疑提交
    @Deprecated
    @Multipart
    @POST("version2/question/submitUserQuestion")
    Call<String> submitUserQuestion(@PartMap() Map<String, String> map, @Part() List<MultipartBody.Part> parts, @Part MultipartBody.Part file);

    //答疑提交
    @POST("version2/question/submitUserQuestion")
    Call<String> submitUserQuestionJson(@Body RequestBody requestBody);

    //答疑获取推荐问题
    @POST("version2/question/getRecommendList")
    Call<String> getRecommendList();

    //追问
    @POST("version2/mine/submitUserQuestion")
    Call<String> submitUserMineQuestionJson(@Body RequestBody requestBody);

    //活动 模块
    @FormUrlEncoded
    @POST("version2/activity/getActivityList")
    Call<String> getActivityList(@FieldMap Map<String, String> map);


    //活动详情
    @FormUrlEncoded
    @POST("version2/activity/getActivityDetail")
    Call<String> getActivityDetail(@FieldMap Map<String, String> map);

    //教师投稿
    @Multipart
    @POST("version2/activity/submitTeacher")
    Call<String> submitTeacher(@PartMap() Map<String, String> map, @Part() List<MultipartBody.Part> parts);

    //教师投稿
    @POST("version2/activity/submitTeacher")
    Call<String> submitTeacher(@Body() RequestBody requestBody);

    //易错题投稿
    @Multipart
    @POST("version2/activity/submitEasyQuestion")
    Call<String> submitEasyQuestion(@PartMap() Map<String, String> map, @Part() List<MultipartBody.Part> parts);
    //易错题投稿

    @POST("version2/activity/submitEasyQuestion")
    Call<String> submitEasyQuestion(@Body() RequestBody requestBody);

    //知识点获取
    @FormUrlEncoded
    @POST("version2/activity/getknowledgePoint")
    Call<String> getKnowledgePointActivity(@FieldMap Map<String, String> map);


    //作文投稿
    @Multipart
    @POST("version2/activity/submitComposition")
    Call<String> submitComposition(@PartMap() Map<String, String> map, @Part() List<MultipartBody.Part> parts);

    //作文投稿
    @POST("version2/activity/submitComposition")
    Call<String> submitComposition(@Body RequestBody requestBody);


    //我的模块 -----------------


    //我的-首页
    @FormUrlEncoded
    @POST("version2/mine/getUserMyIndex")
    Call<String> getUserMyIndex(@FieldMap Map<String, String> map);

    //个人信息获取
    @FormUrlEncoded
    @POST("version2/mine/getUserInfo")
    Call<String> getUserInfo(@FieldMap Map<String, String> map);

    //个人信息修改

    @Multipart
    @POST("version2/mine/updateUserInfo")
    Call<String> updateUserInfo(@PartMap() Map<String, String> map, @Part MultipartBody.Part headImage);

    //个人信息修改

    @POST("version2/mine/updateUserInfo")
    Call<String> updateUserInfo(@Body() RequestBody body);


    //教师资格认证
    @POST("version2/mine/qualityCertification")
    Call<String> qualityCertification(@Body() RequestBody body);

    //进入教师资格认证查看是否进行过认证
    @POST("version2/mine/teacherCheck")
    Call<String> teacherCheck();

    //系统设置
    @FormUrlEncoded
    @POST("version2/mine/getSettingList")
    Call<String> getSettingList(@FieldMap Map<String, String> map);

    //消息提示设置
    @FormUrlEncoded
    @POST("version2/mine/setSystemOption")
    Call<String> setSystemOption(@FieldMap Map<String, String> map);

    //我的消息
    @FormUrlEncoded
    @POST("version2/mine/getMessageList")
    Call<String> getMessageList(@FieldMap Map<String, String> map);

    //我的消息是否有未读消息
    @POST("version2/mine/isHaveNewMessage")
    Call<String> getHaveNewMessage();

    //批量标记为已经阅读
    @FormUrlEncoded
    @POST("version2/mine/userReadMark")
    Call<String> userReadMark(@FieldMap Map<String, String> map);

    //批量删除消息
    @FormUrlEncoded
    @POST("version2/mine/batchDeleteMessage")
    Call<String> userReadDel(@FieldMap Map<String, String> map);

    //消息详情
    @Deprecated
    @FormUrlEncoded
    @POST("version2/mine/messageDetail")
    Call<String> messageDetail(@FieldMap Map<String, String> map);

    //消息详情
    @POST("version2/mine/messageDetail")
    Call<String> messageDetail(@Body RequestBody body);


    //积分明细
    @FormUrlEncoded
    @POST("version2/mine/getIntegralDetail")
    Call<String> getIntegralDetail(@FieldMap Map<String, String> map);

    //等级和积分
    @POST("version2/mine/levelAndIntegral")
    Call<String> getLevelAndIntegral(@Body RequestBody body);

    //积分兑换规则（用来计算前端所需积分的显示）
    @FormUrlEncoded
    @POST("version2/mine/integralExchangeRules")
    Call<String> integralExchangeRules(@FieldMap Map<String, String> map);

    //积分兑换
    @POST("version2/mine/integralExchange")
    Call<String> integralExchange(@Body RequestBody body);


    //账户管理
    @FormUrlEncoded
    @POST("version2/mine/getWallet")
    Call<String> userAccountManagement(@FieldMap Map<String, String> map);

    //账户明细
    @FormUrlEncoded
    @POST("version2/mine/userAccountDetail")
    Call<String> userAccountDetail(@FieldMap Map<String, String> map);

    //充值
    @POST("version2/mine/accountRecharge")
    Call<String> accountRecharge1(@Body RequestBody body);

    //签到信息
    @FormUrlEncoded
    @POST("version2/mine/getSignInfo")
    Call<String> getSignInfo(@FieldMap Map<String, String> map);

    //进行签到
    @FormUrlEncoded
    @POST("version2/mine/userSign")
    Call<String> userSign(@FieldMap Map<String, String> map);

    //我的收藏
    @FormUrlEncoded
    @POST("version2/mine/getUserCollection")
    Call<String> getUserCollection(@FieldMap Map<String, String> map);


    //我的题集
    @FormUrlEncoded
    @POST("version2/mine/getExerciseList")
    Call<String> getExerciseList(@FieldMap Map<String, String> map);

    //题集删除
    @POST("version2/mine/deleteExercise")
    Call<String> deleteExercise(@Body RequestBody body);

    //我的提问
    @FormUrlEncoded
    @POST("version2/mine/getQuestionList")
    Call<String> getQuestionList(@FieldMap Map<String, String> map);

    //我的提问详情
    @FormUrlEncoded
    @POST("version2/mine/getQuestionDetail")
    Call<String> getQuestionDetail(@FieldMap Map<String, String> map);


    //我的上传
    @FormUrlEncoded
    @POST("version2/mine/getUserUpload")
    Call<String> getUserUpload(@FieldMap Map<String, String> map);

    //我的评论
    @FormUrlEncoded
    @POST("version2/mine/getUserComments")
    Call<String> getUserComments(@FieldMap Map<String, String> map);


    //删除评论
    @FormUrlEncoded
    @POST("version2/mine/deleteUserComment")
    Call<String> deleteUserComment(@FieldMap Map<String, String> map);

    //我的获取
    @FormUrlEncoded
    @POST("version2/mine/getUserPurchase")
    Call<String> getUserPurchase(@FieldMap Map<String, String> map);

    //帮助反馈
    @FormUrlEncoded
    @POST("version2/mine/getFeedbackList")
    Call<String> getFeedbackList(@FieldMap Map<String, String> map);

    //反馈提交
    @Deprecated
    @Multipart
    @POST("version2/mine/feedbackSubmit")
    Call<String> feedbackSubmit(@PartMap() Map<String, String> map, @Part() List<MultipartBody.Part> parts);

    //新的反馈提交
    @POST("version2/mine/feedbackSubmit")
    Call<String> feedbackSubmit(@Body RequestBody body);

    //提问评价
    @POST("version2/mine/submitQuestionScore")
    Call<String> submitQuestionScore(@Body RequestBody body);

    //反馈提交
    @Multipart
    @POST("version2/mine/submitUserQuestion")
    Call<String> addUserQuestion(@PartMap() Map<String, String> map, @Part() List<MultipartBody.Part> parts, @Part MultipartBody.Part file);

    //获取ossToken  type为上传模块，1：用户头像，2：用户投稿，3：用户答疑，4：用户反馈
    @POST("version2/ossToken/getOssToken/1")
    Call<String> getOssToken(@HeaderMap Map<String, String> headers);

    //获取ossToken
    @POST("version2/ossToken/getOssToken/2")
    Call<String> getOssTokenByTouGao(@HeaderMap Map<String, String> headers);

    //获取ossToken
    @POST("version2/ossToken/getOssToken/3")
    Call<String> getOssTokenByDaYi(@HeaderMap Map<String, String> headers);

    //获取ossToken
    @POST("version2/ossToken/getOssToken/4")
    Call<String> getOssTokenByFanKui(@HeaderMap Map<String, String> headers);

    //获取ossToken
    @POST("version2/ossToken/getOssToken/5")
    Call<String> getOssTokenByTeacher(@HeaderMap Map<String, String> headers);

    //获取ossToken
    @POST("version2/ossToken/getOssToken/6")
    Call<String> getOssTokenByPindu(@HeaderMap Map<String, String> headers);

    //微信
    @POST("v4/pay/getWXPaySign")
    Call<String> accountRecharge(@Body RequestBody body);

    //学习币获取
    @POST("v4/pay/coinPay")
    Call<String> coinPay(@Body RequestBody body);

    //积分获取
    @POST("v4/pay/bonusPay")
    Call<String> bonusPay(@Body RequestBody body);

    //答疑卷获取
    @POST("version2/pay/couponPay")
    Call<String> couponPay(@Body RequestBody body);

    //支付宝支付
    @POST("v4/pay/getAlipayOrder")
    Call<String> getAlipayOrder(@Body RequestBody body);

    //微信登录
    @POST("version2/userInfos/thirdPartyLogin")
    Call<String> thirdPartyLogin(@Body RequestBody body);

    //三方绑定
    @POST("version2/userInfos/bindThirdAccount")
    Call<String> bindThirdAccount(@Body RequestBody body);

    //解除三方绑定
    @POST("version2/userInfos/unbindThirdAccount")
    Call<String> unbindThirdAccount(@Body RequestBody body);

    //绑定手机号
    @POST("version2/userInfos/bindPhone")
    Call<String> bindPhone(@Body RequestBody body);

    // 获取手机号是否绑定第三方账号
    @POST("version2/userInfos/IsNotBindThirdAccount")
    Call<String> isNotBindThirdAccount(@Body RequestBody body);

    //二维码通过rid 获取真实Id
    @POST("version2/classroom/getRealId")
    Call<String> getRealId(@Body RequestBody body);

    //根据资源Id 获取图书
    @FormUrlEncoded
    @POST("version2/classroom/getBookInfoByResourceId")
    Call<String> getBookBuyResourceId(@FieldMap Map<String, String> map);

    //根据资源Id 获取课程
    @FormUrlEncoded
    @POST("version2/classroom/getCurriculumByResourceId")
    Call<String> getCurriculumByResourceId(@FieldMap Map<String, String> map);

    //退出登录
    @POST("version2/userInfos/logout")
    Call<String> logout();

    //下载记录 与是否更新
    @POST("version2/myDownloads")
    Call<String> myDownloads(@Body RequestBody body);

    //保存下载记录
    @POST("version2/myDownloads/save")
    Call<String> savemyDownloads(@Body RequestBody body);

    //删除下载记录
    @FormUrlEncoded
    @POST("version2/myDownloads/delete")
    Call<String> deletemyDownloads(@FieldMap Map<String, String> map);


    //获取教师是否激活
    @FormUrlEncoded
    @POST("version2/mine/getTeacherIsActivated")
    Call<String> getTeacherIsActivated(@FieldMap Map<String, String> map);

    //教师激活
    @FormUrlEncoded
    @POST("version2/mine/teacherActive")
    Call<String> teacherActive(@FieldMap Map<String, String> map);

    //获取省
    @FormUrlEncoded
    @POST("version2/schools/getProvinces")
    Call<String> getProvinces(@FieldMap Map<String, String> map);

    //获取市或区
    @FormUrlEncoded
    @POST("version2/schools/getCityOrArea")
    Call<String> getCityOrArea(@FieldMap Map<String, String> map);

    //获取学校
    @POST("version2/schools/getSchools")
    Call<String> getSchools(@Body RequestBody body);

    //根据年级获取作文单元
    @FormUrlEncoded
    @POST("version2/activity/getCompositionUnits")
    Call<String> getCompositionUnits(@FieldMap Map<String, String> map);


    //作文投稿
    @POST("version2/activity/submitComposition")
    Call<String> submitCompositionNew(@Body RequestBody body);


    //获取ossToken
    @POST("/version2/ossToken/getOssToken/2")
    Call<String> getOssTokenByDaYiNew(@HeaderMap Map<String, String> headers);

    //活动页列表
    @FormUrlEncoded
    @POST("version2/activity/getActivityList")
    Call<String> getActivityListNew(@FieldMap Map<String, String> map);


    //首页列表
    @POST("version2/classroom/classIndex")
    Call<String> classIndex(@Body RequestBody body);

    //图书更多
    @POST("version2/classroom/moreBook")
    Call<String> moreBook(@Body RequestBody body);

    //精品更多
    @POST("version2/classroom/classIndex_curriculum")
    Call<String> classIndex_curriculum(@Body RequestBody body);

    //素质更多
    @POST("version2/classroom/moreQualityEducation")
    Call<String> moreQualityEducation(@Body RequestBody body);

    //素质教育h5接口
    @FormUrlEncoded
    @POST("version2/classroom/viewQualityEducation")
    Call<String> viewQualityEducation(@FieldMap Map<String, String> map);


    //教师资源包列表
    @POST("version2/teacherResource/getList")
    Call<String> getteacherResourceList(@Body RequestBody body);


    //教师资源包详情
    @FormUrlEncoded
    @POST("version2/teacherResource/getPackageDetail")
    Call<String> getPackageDetail(@FieldMap Map<String, String> map);

    //获取目录下教师资源
    @FormUrlEncoded
    @POST("version2/teacherResource/getResourceByCategory")
    Call<String> getResourceByCategory(@FieldMap Map<String, String> map);

    //教师资源详情
    @FormUrlEncoded
    @POST("version2/teacherResource/getResourceDetail")
    Call<String> getTeacherResourceDetail(@FieldMap Map<String, String> map);

    //整本书 视频讲座章节列表
    @FormUrlEncoded
    @POST("version2/lecture/getDetail")
    Call<String> getlectureChapter(@FieldMap Map<String, Object> map);

    //整本书  课程列表
    @FormUrlEncoded
    @POST("version2/lecture/getCategoryDetail")
    Call<String> getlectureCurse(@FieldMap Map<String, Object> map);

    //整本书  课程详情
    @FormUrlEncoded
    @POST("version2/lecture/getResourceDetail")
    Call<String> getlectureResource(@FieldMap Map<String, Object> map);

    //教师资源点赞/取消点赞
    @POST("version2/teacherResource/up")
    Call<String> teacherResourceUp(@Body RequestBody body);

    //教师资源收藏/取消收藏
    @POST("version2/teacherResource/collect")
    Call<String> teacherResourcecollect(@Body RequestBody body);

    //教师资源收藏/取消收藏
    @POST("version2/classroom/getUserBag")
    Call<String> getUserBag(@Body RequestBody body);

    //积分兑换
    @POST("version2/redeemVoucher/exchange")
    Call<String> getIntegral(@Body RequestBody body);

    //我的收藏列表 新
    @FormUrlEncoded
    @POST("version2/mine/getUserCollection")
    Call<String> getUserCollectionNew(@FieldMap Map<String, String> map);

    //收藏列表  取消收藏 新
    @FormUrlEncoded
    @POST("version2/mine/removeCollection")
    Call<String> removeCollectionNew(@FieldMap Map<String, String> map);

    // 资源收藏/取消收藏 新
    @POST("version2/classroom/submitResourceCollection")
    Call<String> submitResourceCollectionNew(@Body RequestBody body);

    // 比赛列表
    @FormUrlEncoded
    @POST("version2/match/index")
    Call<String> getMatchList(@FieldMap Map<String, String> map);

    // 比赛详情
    @FormUrlEncoded
    @POST("version2/match/detail")
    Call<String> getMatchDetail(@FieldMap Map<String, String> map);

    // 比赛规则
    @POST("version2/match/getMatchRule")
    Call<String> getMatchGuiZhe();

    // 报名
    @FormUrlEncoded
    @POST("version2/match/enroll")
    Call<String> getMatchEnroll(@FieldMap Map<String, String> map);

    // 开始比赛
    @FormUrlEncoded
    @POST("version2/match/beginMatch")
    Call<String> getBeginMatch(@FieldMap Map<String, String> map);

    // 比赛排名
    @FormUrlEncoded
    @POST("version2/match/myRank")
    Call<String> getMatchRank(@FieldMap Map<String, String> map);

    // 状元榜
    @POST("version2/match/rankList")
    Call<String> getMatchRankList(@Body RequestBody body);

    // 获奖名单
    @FormUrlEncoded
    @POST("version2/match/prizeList")
    Call<String> getMatchPrizeList(@FieldMap Map<String, String> map);

    // 我的成绩
    @FormUrlEncoded
    @POST("version2/match/myResult")
    Call<String> getMyResult(@FieldMap Map<String, String> map);

    // 我的成绩-题目
    @POST("version2/match/myResultQuestion")
    Call<String> getMyResultQuestion(@Body RequestBody body);

    // 错题
    @POST("version2/match/wrongQuestionList")
    Call<String> getWrongQuestionList(@Body RequestBody body);

    // 分享
    @FormUrlEncoded
    @POST("version2/match/share")
    Call<String> getShare(@FieldMap Map<String, String> map);

    // 比赛提交答案
    @POST("version2/match/submit")
    Call<String> getMatchSubmit(@Body RequestBody body);

    // 学情报告列表
    @POST("version2/learningReport/getList")
    Call<String> getReportList();

    // 学情报告详情  已购买
    @POST("version2/learningReport/getDetail")
    Call<String> getReportBuy(@Body RequestBody body);

    // 学情报告详情  未购买
    @POST("version2/learningReport/getDetailNotBuy")
    Call<String> getReportNotBuy(@Body RequestBody body);

    // 学情报告购买信息
    @POST("version2/learningReport/getBuyLearningReport")
    Call<String> getBuyReport(@Body RequestBody body);

    // 学情报告  免费购买
    @FormUrlEncoded
    @POST("version2/learningReport/getFreeReport")
    Call<String> getBuyFree(@FieldMap Map<String, String> map);

    // 授权登录
    @POST("version2/userInfos/authorize")
    Call<String> getAuthorize(@Body RequestBody body);

    // 拼读获取题目
    @FormUrlEncoded
    @POST("version2/pinduExercise/getListByResource")
    Call<String> getPinduExercise(@FieldMap Map<String, String> map);

    // 拼读保存
    @POST("version2/pinduExercise/saveResult")
    Call<String> getPinduSave(@Body RequestBody body);

    // 拼读历史
    @POST("version2/pinduExercise/getUserHistory")
    Call<String> getPinduHis(@Body RequestBody body);

    // 拼读详情
    @FormUrlEncoded
    @POST("version2/pinduExercise/exerciseDetail")
    Call<String> getPinduDetail(@FieldMap Map<String, String> map);

    // 注销状态获取
    @FormUrlEncoded
    @POST("version2/userInfos/zhuxiaoState")
    Call<String> getZXState(@FieldMap Map<String, String> map);

    // 注销
    @FormUrlEncoded
    @POST("version2/userInfos/zhuxiao")
    Call<String> getZhuXiao(@FieldMap Map<String, String> map);

}
